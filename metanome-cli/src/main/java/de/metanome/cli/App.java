package de.metanome.cli;

import static java.util.stream.Collectors.toList;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.google.common.base.Preconditions;
import de.metanome.algorithm_integration.Algorithm;
import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.algorithm_types.*;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementDatabaseConnection;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingDatabaseConnection;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingFileInput;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingTableInput;
import de.metanome.algorithm_integration.configuration.DbSystem;
import de.metanome.algorithm_integration.input.DatabaseConnectionGenerator;
import de.metanome.algorithm_integration.input.FileInputGenerator;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.algorithm_integration.input.TableInputGenerator;
import de.metanome.algorithm_integration.result_receiver.*;
import de.metanome.algorithm_integration.results.Result;
import de.metanome.backend.input.database.DefaultDatabaseConnectionGenerator;
import de.metanome.backend.input.database.DefaultTableInputGenerator;
import de.metanome.backend.input.file.DefaultFileInputGenerator;
import de.metanome.backend.result_receiver.ResultCache;
import de.metanome.backend.result_receiver.ResultPrinter;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import lombok.ToString;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * App to run Metanome algorithms from the command line.
 */
public class App {

  private static final Logger LOG = LoggerFactory.getLogger(App.class);

  private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


  public static void main(String[] args) {
    final Parameters parameters = parseParameters(args);
    LOG.trace(parameters.toString());
    run(parameters);
  }

  private static Parameters parseParameters(String[] args) {
    final Parameters parameters = new Parameters();
    final JCommander jCommander = new JCommander(parameters);
    try {
      jCommander.parse(args);
    } catch (ParameterException e) {
      LOG.error("Could not parse command line args: {}", e.getMessage());
      StringBuilder sb = new StringBuilder();
      jCommander.usage(sb);
      LOG.info(sb.toString());
      System.exit(1);
    }
    return parameters;
  }

  private static void run(Parameters parameters) {
    LOG.info("Running {}", parameters.algorithmClassName);
    LOG.info("* in:            {}", parameters.inputDatasets);
    LOG.info("* out:           {}", parameters.output);
    LOG.info("* configuration: {}", parameters.algorithmConfigurationValues);

    LOG.info("Initializing algorithm.");
    OmniscientResultReceiver resultReceiver = createResultReceiver(parameters);
    Algorithm algorithm = configureAlgorithm(parameters, resultReceiver);

    TempFileGenerator tempFileGenerator = setUpTempFileGenerator(parameters, algorithm);

    final long startTimeMillis = System.currentTimeMillis();
    LOG.debug("Execution started at {}", DATE_FORMAT.format(new Date(startTimeMillis)));
    long elapsedMillis;
    boolean isExecutionSuccess = false;
    try {
      algorithm.execute();
      isExecutionSuccess = true;
    } catch (Exception e) {
      LOG.error("Algorithm crashed.", e);
    } finally {
      if (tempFileGenerator != null) {
        tempFileGenerator.cleanUp();
      }

      long endTimeMillis = System.currentTimeMillis();
      elapsedMillis = endTimeMillis - startTimeMillis;
      LOG.debug("Execution completed at {}", DATE_FORMAT.format(new Date(endTimeMillis)));
      LOG.info("Elapsed time: {} ({} ms).", formatDuration(elapsedMillis), elapsedMillis);
    }

    // Handle "file:exec-id" formats properly.
    ResultCache resultCache;
    switch (parameters.output.split(":")[0]) {
      case "print":
        resultCache = (ResultCache) resultReceiver;
        LOG.info("Results:");
        for (Result result : resultCache.fetchNewResults()) {
          LOG.info(result.toString());
        }
        break;
      default:
        LOG.warn("Unknown output mode \"{}\". Defaulting to \"file\"", parameters.output);
      case "file!":
      case "file":
      case "none":
        try {
          if (resultReceiver instanceof Closeable) {
            ((Closeable) resultReceiver).close();
          }
        } catch (IOException e) {
          LOG.error("Storing the result failed.", e);
          System.exit(4);
        }
        break;
    }

    System.exit(isExecutionSuccess ? 0 : 23);
  }

  private static OmniscientResultReceiver createResultReceiver(Parameters parameters) {
    String executionId;
    if (parameters.output.equalsIgnoreCase("none")) {
      try {
        return new DiscardingResultReceiver();
      } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
      }
    }

    boolean isCaching;
    if (parameters.output.startsWith("file:")) {
      executionId = parameters.output.substring("file:".length());
      isCaching = true;
    } else if (parameters.output.startsWith("file!:")) {
      executionId = parameters.output.substring("file!:".length());
      isCaching = false;
    } else if (parameters.output.equalsIgnoreCase("file!")) {
      Calendar calendar = GregorianCalendar.getInstance();
      executionId = String.format("%04d-%02d-%02d_%02d-%02d-%02d",
          calendar.get(Calendar.YEAR),
          calendar.get(Calendar.MONTH) + 1,
          calendar.get(Calendar.DATE),
          calendar.get(Calendar.HOUR_OF_DAY),
          calendar.get(Calendar.MINUTE),
          calendar.get(Calendar.SECOND)
      );
      isCaching = false;
    } else {
      Calendar calendar = GregorianCalendar.getInstance();
      executionId = String.format("%04d-%02d-%02d_%02d-%02d-%02d",
          calendar.get(Calendar.YEAR),
          calendar.get(Calendar.MONTH) + 1,
          calendar.get(Calendar.DATE),
          calendar.get(Calendar.HOUR_OF_DAY),
          calendar.get(Calendar.MINUTE),
          calendar.get(Calendar.SECOND)
      );
      isCaching = true;
    }
    try {
      return isCaching ?
          new ResultCache(executionId, null) :
          new ResultPrinter(executionId, null);
    } catch (FileNotFoundException e) {
      throw new RuntimeException("Unexpected exception.", e);
    }
  }

  private static String formatDuration(long millis) {
    if (millis < 0) {
      return "-:--:--.---";
    }
    long ms = millis % 1000;
    millis /= 1000;
    long s = millis % 60;
    millis /= 60;
    long m = millis % 60;
    millis /= 60;
    long h = millis % 60;
    return String.format("%d:%02d:%02d.%03d", h, m, s, ms);
  }

  /**
   * Instantiate and configure an {@link Algorithm} instance according to the {@link Parameters}.
   *
   * @param parameters tell which {@link Algorithm} to instantiate and provides its properties.
   * @param resultReceiver that should be used by the {@link Algorithm} to store results
   * @return the configured {@link Algorithm} instance
   */
  private static Algorithm configureAlgorithm(Parameters parameters,
      OmniscientResultReceiver resultReceiver) {
    try {
      final Algorithm algorithm = createAlgorithm(parameters.algorithmClassName);
      loadMiscConfigurations(parameters, algorithm);
      setUpInputGenerators(parameters, algorithm);
      configureResultReceiver(algorithm, resultReceiver);
      return algorithm;

    } catch (Exception e) {
      LOG.error("Could not initialize algorithm.", e);
      System.exit(3);
      return null;
    }
  }

  private static Algorithm createAlgorithm(String algorithmClassName)
      throws ClassNotFoundException, IllegalAccessException, InstantiationException {
    final Class<?> algorithmClass = Class.forName(algorithmClassName);
    return (Algorithm) algorithmClass.newInstance();
  }

  private static void loadMiscConfigurations(Parameters parameters, Algorithm algorithm) throws AlgorithmConfigurationException {
    final List<Pair<String, String>> values = new ArrayList<>();
    for (String algorithmConfigurationValue : parameters.algorithmConfigurationValues) {
      int colonPos = algorithmConfigurationValue.indexOf(':');
      final String key = algorithmConfigurationValue.substring(0, colonPos);
      final String value = algorithmConfigurationValue.substring(colonPos + 1);
      values.add(Pair.of(key, value));
    }
    AlgorithmInitializer.forAlgorithm(algorithm, values).apply();
  }


  private static void setUpInputGenerators(Parameters parameters, Algorithm algorithm) throws AlgorithmConfigurationException {
    if (parameters.pgpassPath != null) {
      // We assume that we are given table inputs.
      ConfigurationSettingDatabaseConnection databaseSettings = loadConfigurationSettingDatabaseConnection(
          parameters.pgpassPath, parameters.dbType
      );
      if (algorithm instanceof RelationalInputParameterAlgorithm) {
        List<RelationalInputGenerator> inputGenerators = new LinkedList<>();
        for (int i = 0; i < parameters.inputDatasets.size(); i++) {
          inputGenerators.addAll(createTableInputGenerators(parameters, i, databaseSettings));
        }
        ((RelationalInputParameterAlgorithm) algorithm).setRelationalInputConfigurationValue(
            parameters.inputDatasetKey,
            inputGenerators.toArray(new RelationalInputGenerator[inputGenerators.size()])
        );

      } else if (algorithm instanceof TableInputParameterAlgorithm) {
        List<TableInputGenerator> inputGenerators = new LinkedList<>();
        for (int i = 0; i < parameters.inputDatasets.size(); i++) {
          inputGenerators.addAll(createTableInputGenerators(parameters, i, databaseSettings));
        }
        ((TableInputParameterAlgorithm) algorithm).setTableInputConfigurationValue(
            parameters.inputDatasetKey,
            inputGenerators.toArray(new TableInputGenerator[inputGenerators.size()])
        );
      } else {
        LOG.error("Algorithm does not implement a supported input method (relational/tables).");
        System.exit(5);
        return;
      }

      if (algorithm instanceof DatabaseConnectionParameterAlgorithm) {
        final List<ConfigurationRequirement<?>> db = algorithm.getConfigurationRequirements()
            .stream()
            .filter(cr -> cr.getClass() == ConfigurationRequirementDatabaseConnection.class)
            .collect(toList());

        if (db.isEmpty()) {
          LOG.debug("DatabaseConnection not specified");
        } else {
          Preconditions.checkState(db.size() == 1, "More than one DB conf requirement");
          final DatabaseConnectionGenerator generator = createDatabaseConnectionGenerator(
              databaseSettings);
          ((DatabaseConnectionParameterAlgorithm) algorithm)
              .setDatabaseConnectionGeneratorConfigurationValue(db.get(0).getIdentifier(),
                  new DatabaseConnectionGenerator[]{generator});
        }
      }

    } else {
      // We assume that we are given file inputs.
      if (algorithm instanceof RelationalInputParameterAlgorithm) {
        List<RelationalInputGenerator> inputGenerators = new LinkedList<>();
        for (int i = 0; i < parameters.inputDatasets.size(); i++) {
          inputGenerators
              .addAll(createFileInputGenerators(parameters, i, RelationalInputGenerator.class));
        }
        ((RelationalInputParameterAlgorithm) algorithm).setRelationalInputConfigurationValue(
            parameters.inputDatasetKey,
            inputGenerators.toArray(new RelationalInputGenerator[inputGenerators.size()])
        );

      } else {
        boolean isAnyInput = false;
        if (algorithm instanceof FileInputParameterAlgorithm) {
          List<FileInputGenerator> inputGenerators = new LinkedList<>();
          for (int i = 0; i < parameters.inputDatasets.size(); i++) {
            inputGenerators
                .addAll(createFileInputGenerators(parameters, i, FileInputGenerator.class));
          }
          ((FileInputParameterAlgorithm) algorithm).setFileInputConfigurationValue(
              parameters.inputDatasetKey,
              inputGenerators.toArray(new FileInputGenerator[inputGenerators.size()])
          );
          isAnyInput = true;
        }

        if (algorithm instanceof HdfsInputParameterAlgorithm) {
          List<HdfsInputGenerator> inputGenerators = new LinkedList<>();
          for (int i = 0; i < parameters.inputDatasets.size(); i++) {
            inputGenerators
                .addAll(createFileInputGenerators(parameters, i, HdfsInputGenerator.class));
          }
          ((HdfsInputParameterAlgorithm) algorithm).setHdfsInputConfigurationValue(
              parameters.inputDatasetKey,
              inputGenerators.toArray(new HdfsInputGenerator[inputGenerators.size()])
          );
          isAnyInput = true;

        }

        if (!isAnyInput) {
          LOG.error("Algorithm does not implement a supported input method (relational/files).");
          System.exit(5);
          return;
        }
      }
    }
  }


  /**
   * Create a {@link DefaultFileInputGenerator}s.
   *
   * @param parameters defines how to configure the {@link DefaultFileInputGenerator}
   * @param parameterIndex index of the dataset parameter to create the {@link DefaultFileInputGenerator}s for
   * @param cls create {@link RelationalInputGenerator}s must be a subclass
   * @return the {@link DefaultFileInputGenerator}s
   */
  private static <T extends RelationalInputGenerator> Collection<T> createFileInputGenerators(
      Parameters parameters, int parameterIndex, Class<T> cls
  ) throws AlgorithmConfigurationException {
    final String parameter = parameters.inputDatasets.get(parameterIndex);
    if (parameter.startsWith("load:")) {
      try {
        return Files.lines(Paths.get(parameter.substring("load:".length())))
            .map(path -> {
              try {
                return createFileInputGenerator(parameters, path, cls);
              } catch (AlgorithmConfigurationException e) {
                throw new RuntimeException("Could not create input generator.", e);
              }
            })
            .filter(Objects::nonNull)
            .collect(toList());
      } catch (IOException e) {
        throw new UncheckedIOException("Could not load input specification file.", e);
      } catch (RuntimeException e) {
        if (e.getCause() != null && (e.getCause() instanceof AlgorithmConfigurationException)) {
          throw (AlgorithmConfigurationException) e.getCause();
        } else {
          throw e;
        }
      }
    } else {
      T inputGenerator = createFileInputGenerator(parameters, parameter, cls);
      return inputGenerator == null ? Collections.emptyList()
          : Collections.singleton(inputGenerator);
    }
  }

  @SuppressWarnings("unchecked")
  private static <T extends RelationalInputGenerator> T createFileInputGenerator(
      Parameters parameters, String path, Class<T> cls
  ) throws AlgorithmConfigurationException {
    ConfigurationSettingFileInput setting = new ConfigurationSettingFileInput(
        path,
        true,
        toChar(parameters.inputFileSeparator),
        toChar(parameters.inputFileQuotechar),
        toChar(parameters.inputFileEscape),
        parameters.inputFileStrictQuotes,
        parameters.inputFileIgnoreLeadingWhiteSpace,
        parameters.inputFileSkipLines,
        parameters.inputFileHasHeader,
        parameters.inputFileSkipDifferingLines,
        parameters.inputFileNullString
    );
    RelationalInputGenerator generator;
    if (path.startsWith("hdfs://")) {
      generator = new HdfsInputGenerator(setting);
    } else {
      generator = new DefaultFileInputGenerator(setting);
    }
    if (!cls.isAssignableFrom(generator.getClass())) {
      return null;
    }
    return (T) generator;
  }

  private static ConfigurationSettingDatabaseConnection loadConfigurationSettingDatabaseConnection(
      String pgpassPath, String dbType) throws AlgorithmConfigurationException {
    try {
      String firstLine = Files.lines(new File(pgpassPath).toPath()).findFirst().orElseThrow(
          () -> new AlgorithmConfigurationException("Could not load PGPass file.")
      );
      int colonPos1 = firstLine.indexOf(':');
      int colonPos2 = firstLine.indexOf(':', colonPos1 + 1);
      int colonPos3 = firstLine.indexOf(':', colonPos2 + 1);
      int colonPos4 = firstLine.indexOf(':', colonPos3 + 1);
      if (colonPos4 == -1) {
        throw new IllegalArgumentException("Cannot parse PGPass file.");
      }
      String host = firstLine.substring(0, colonPos1);
      String port = firstLine.substring(colonPos1 + 1, colonPos2);
      String dbName = firstLine.substring(colonPos2 + 1, colonPos3);
      String user = firstLine.substring(colonPos3 + 1, colonPos4);
      String password = firstLine.substring(colonPos4 + 1);

      // TODO: Consider special JDBC URL formats, such as Oracle Thin.
      String jdbcUrl = String.format("jdbc:%s://%s:%s/%s", dbType, host, port, dbName);
      DbSystem dbSystem = DbSystem.PostgreSQL;
      if ("postgres".equalsIgnoreCase(dbType)) {
        dbSystem = DbSystem.PostgreSQL;
      } else if ("mysql".equalsIgnoreCase(dbType)) {
        dbSystem = DbSystem.MySQL;
      } else {
        // TODO: Consider other DB types. But it does not seem that this is a crucial piece of information for Metanome.
      }
      return new ConfigurationSettingDatabaseConnection(
          jdbcUrl, user, password, dbSystem
      );
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  /**
   * Create a {@link DefaultTableInputGenerator}s.
   *
   * @param parameters defines how to configure the {@link DefaultTableInputGenerator}
   * @param parameterIndex index of the dataset parameter to create the {@link DefaultTableInputGenerator}s for
   * @return the {@link DefaultFileInputGenerator}s
   */
  private static Collection<DefaultTableInputGenerator> createTableInputGenerators(
      Parameters parameters,
      int parameterIndex,
      ConfigurationSettingDatabaseConnection databaseSettings)
      throws AlgorithmConfigurationException {
    final String parameter = parameters.inputDatasets.get(parameterIndex);
    if (parameter.startsWith("load:")) {
      try {
        return Files.lines(Paths.get(parameter.substring("load:".length())))
            .map(table -> {
              try {
                return createTableInputGenerator(databaseSettings, table);
              } catch (AlgorithmConfigurationException e) {
                throw new RuntimeException("Could not create input generator.", e);
              }
            })
            .collect(toList());
      } catch (IOException e) {
        throw new UncheckedIOException("Could not load input specification file.", e);
      } catch (RuntimeException e) {
        if (e.getCause() != null && (e.getCause() instanceof AlgorithmConfigurationException)) {
          throw (AlgorithmConfigurationException) e.getCause();
        } else {
          throw e;
        }
      }
    } else {
      return Collections.singleton(
          createTableInputGenerator(databaseSettings, parameter)
      );
    }
  }

  private static DefaultTableInputGenerator createTableInputGenerator(
      ConfigurationSettingDatabaseConnection configurationSettingDatabaseConnection, String table)
      throws AlgorithmConfigurationException {
    return new DefaultTableInputGenerator(new ConfigurationSettingTableInput(
        table, configurationSettingDatabaseConnection
    ));
  }

  private static DefaultDatabaseConnectionGenerator createDatabaseConnectionGenerator(
      ConfigurationSettingDatabaseConnection configurationSettingDatabaseConnection)
      throws AlgorithmConfigurationException {
    return new DefaultDatabaseConnectionGenerator(new ConfigurationSettingDatabaseConnection(
        configurationSettingDatabaseConnection.getDbUrl(),
        configurationSettingDatabaseConnection.getUsername(),
        configurationSettingDatabaseConnection.getPassword(),
        configurationSettingDatabaseConnection.getSystem()
    ));
  }

  private static char toChar(String string) {
    if (string == null || string.isEmpty()) {
      return '\0';
    } else if (string.length() == 1) {
      return string.charAt(0);
    }
    switch (string) {
      case "none":
        return '\0';
      case "\\t":
      case "tab":
        return '\t';
      case "' '":
      case "\" \"":
      case "space":
        return ' ';
      case "semicolon":
        return ';';
      case "comma":
        return ',';
      case "|":
      case "pipe":
        return '|';
      case "double":
        return '"';
      case "single":
        return '\'';
      default:
        throw new IllegalArgumentException(
            String.format("Illegal character specification: %s", string));
    }
  }

  public static TempFileGenerator setUpTempFileGenerator(final Parameters parameters,
      final Algorithm algorithm) {
    if (algorithm instanceof TempFileAlgorithm) {
      final TempFileGenerator generator = new TempFileGenerator(
          algorithm.getClass().getSimpleName(),
          parameters.tempFileDirectory,
          parameters.clearTempFiles,
          parameters.clearTempFilesByPrefix);
      ((TempFileAlgorithm) algorithm).setTempFileGenerator(generator);
      return generator;
    }

    return null;
  }

  public static void configureResultReceiver(Algorithm algorithm,
      OmniscientResultReceiver resultReceiver) {
    boolean isAnyResultReceiverConfigured = false;

    if (algorithm instanceof BasicStatisticsAlgorithm) {
      ((BasicStatisticsAlgorithm) algorithm).setResultReceiver(resultReceiver);
      isAnyResultReceiverConfigured = true;
    }

    if (algorithm instanceof FunctionalDependencyAlgorithm) {
      ((FunctionalDependencyAlgorithm) algorithm).setResultReceiver(resultReceiver);
      isAnyResultReceiverConfigured = true;
    }

    if (algorithm instanceof MatchingDependencyAlgorithm) {
      ((MatchingDependencyAlgorithm) algorithm).setResultReceiver(resultReceiver);
      isAnyResultReceiverConfigured = true;
    }

    if (algorithm instanceof ConditionalFunctionalDependencyAlgorithm) {
      ((ConditionalFunctionalDependencyAlgorithm) algorithm).setResultReceiver(resultReceiver);
      isAnyResultReceiverConfigured = true;
    }

    if (algorithm instanceof RelaxedFunctionalDependencyAlgorithm) {
      ((RelaxedFunctionalDependencyAlgorithm) algorithm).setResultReceiver(resultReceiver);
      isAnyResultReceiverConfigured = true;
    }

    if (algorithm instanceof ConditionalInclusionDependencyAlgorithm) {
      ((ConditionalInclusionDependencyAlgorithm) algorithm).setResultReceiver(resultReceiver);
      isAnyResultReceiverConfigured = true;
    }

    if (algorithm instanceof RelaxedInclusionDependencyAlgorithm) {
      ((RelaxedInclusionDependencyAlgorithm) algorithm).setResultReceiver(resultReceiver);
      isAnyResultReceiverConfigured = true;
    }

    if (algorithm instanceof InclusionDependencyAlgorithm) {
      ((InclusionDependencyAlgorithm) algorithm).setResultReceiver(resultReceiver);
      isAnyResultReceiverConfigured = true;
    }

    if (algorithm instanceof UniqueColumnCombinationsAlgorithm) {
      ((UniqueColumnCombinationsAlgorithm) algorithm).setResultReceiver(resultReceiver);
      isAnyResultReceiverConfigured = true;
    }

    if (algorithm instanceof ConditionalUniqueColumnCombinationAlgorithm) {
      ((ConditionalUniqueColumnCombinationAlgorithm) algorithm).setResultReceiver(resultReceiver);
      isAnyResultReceiverConfigured = true;
    }

    if (algorithm instanceof RelaxedUniqueColumnCombinationAlgorithm) {
      ((RelaxedUniqueColumnCombinationAlgorithm) algorithm).setResultReceiver(resultReceiver);
      isAnyResultReceiverConfigured = true;
    }

    if (algorithm instanceof OrderDependencyAlgorithm) {
      ((OrderDependencyAlgorithm) algorithm).setResultReceiver(resultReceiver);
      isAnyResultReceiverConfigured = true;
    }

    if (algorithm instanceof MultivaluedDependencyAlgorithm) {
      ((MultivaluedDependencyAlgorithm) algorithm).setResultReceiver(resultReceiver);
      isAnyResultReceiverConfigured = true;
    }

    if (algorithm instanceof DenialConstraintAlgorithm) {
      ((DenialConstraintAlgorithm) algorithm).setResultReceiver(resultReceiver);
      isAnyResultReceiverConfigured = true;
    }

    if (!isAnyResultReceiverConfigured) {
      LOG.error("Could not configure any result receiver.");
    }
  }

  /**
   * Parameters for the Metanome CLI {@link App}.
   */
  @ToString
  public static class Parameters {

    @Parameter(names = {
        "--algorithm-config"}, description = "algorithm configuration parameters (<name>:<value>)", variableArity = true)
    public List<String> algorithmConfigurationValues = new ArrayList<>();

    @Parameter(names = {"-a",
        "--algorithm"}, description = "name of the Metanome algorithm class", required = true)
    public String algorithmClassName;

    @Parameter(names = {"--file-key", "--input-key",
        "--table-key"}, description = "configuration key for the input files/tables", required = true)
    public String inputDatasetKey;

    @Parameter(names = {"--files", "--inputs",
        "--tables"}, description = "input file/tables to be analyzed and/or files list input files/tables (prefixed with 'load:')", required = true, variableArity = true)
    public List<String> inputDatasets = new ArrayList<>();

    @Parameter(names = "--db-connection", description = "a PGPASS file that specifies the database connection; if given, the inputs are treated as database tables", required = false)
    public String pgpassPath = null;

    @Parameter(names = "--db-type", description = "the type of database as it would appear in a JDBC URL", required = false)
    public String dbType = null;

    @Parameter(names = "--separator", description = "separates fields in the input file")
    public String inputFileSeparator = ";";

    @Parameter(names = "--quote", description = "delimits fields in the input file")
    public String inputFileQuotechar = "\"";

    @Parameter(names = "--escape", description = "escapes special characters")
    public String inputFileEscape = "\0";

    @Parameter(names = "--skip", description = "numbers of lines to skip")
    public int inputFileSkipLines = 0;

    @Parameter(names = "--strict-quotes", description = "enforce strict quotes")
    public boolean inputFileStrictQuotes = false;

    @Parameter(names = "--ignore-leading-spaces", description = "ignore leading white spaces in each field")
    public boolean inputFileIgnoreLeadingWhiteSpace = false;

    @Parameter(names = "--header", description = "first row is a header")
    public boolean inputFileHasHeader = false;

    @Parameter(names = "--skip-differing-lines", description = "skip lines with incorrect number of fields")
    public boolean inputFileSkipDifferingLines = false;

    @Parameter(names = "--null", description = "representation of NULLs")
    public String inputFileNullString = "";

    @Parameter(names = "--temp", description = "directory for temporary files")
    public String tempFileDirectory;

    @Parameter(names = "--clearTempFiles", description = "clear temporary files")
    public boolean clearTempFiles = true;

    @Parameter(names = "--clearTempFilesByPrefix", description = "if additional files in the temp directory with same prefix should be removed")
    public boolean clearTempFilesByPrefix = false;

    @Parameter(names = {"-o",
        "--output"}, description = "how to output results (none/print/file[:run-ID])")
    public String output = "file";
  }
}
