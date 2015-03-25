package de.metanome.backend.result_postprocessing.result_analyzer;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.AlgorithmExecutionException;
import de.metanome.algorithm_integration.algorithm_types.RelationalInputParameterAlgorithm;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementRelationalInput;
import de.metanome.algorithm_integration.configuration.ConfigurationSetting;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingFileInput;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.algorithm_integration.results.FunctionalDependency;
import de.metanome.algorithm_integration.results.InclusionDependency;
import de.metanome.algorithm_integration.results.JsonConverter;
import de.metanome.algorithm_integration.results.Result;
import de.metanome.algorithm_integration.results.UniqueColumnCombination;
import de.metanome.backend.configuration.ConfigurationValueRelationalInputGenerator;
import de.metanome.backend.input.DefaultRelationalInputGeneratorInitializer;
import de.metanome.backend.result_postprocessing.result_analyzer.functional_dependencies.FDResult;
import de.metanome.backend.result_postprocessing.result_analyzer.helper.PathUnifier;
import de.metanome.backend.results_db.Execution;
import de.metanome.backend.results_db.FileInput;
import de.metanome.backend.results_db.Input;
import de.metanome.backend.results_db.ResultType;

import org.apache.commons.lang3.ClassUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Provides an interface which allows to call all result analyzer in the same way.
 *
 * Created by Alexander Spivak on 05.11.2014.
 */
public abstract class ResultAnalyzer implements RelationalInputParameterAlgorithm {

  //<editor-fold desc="Constants">

  protected static final String BASE_RESULT_DIR   = PathUnifier.combinePaths("results", "rankings");
  private static String INPUT_IDENTIFIER = "input_identifier";

  //</editor-fold>

  //<editor-fold desc="Attributes">

  protected List<RelationalInputGenerator> relationalInputGenerators;
  protected Execution execution;

  //</editor-fold>

  //<editor-fold desc="Execution analysis methods">

  /**
   * Loads the results of a algorithm run from hard disk
   *
   * @param execution Execution containing the algorithm results file path
   * @return Returns the results of the executed algorithm
   */
  protected List<Result> extractResults(Execution execution){
    List<Result> results = new ArrayList<>();

    // Get path to the results file
    List<de.metanome.backend.results_db.Result> executionResults = new ArrayList<>(execution.getResults());
    String filePath = executionResults.get(0).getFileName();
    ResultType resultType = executionResults.get(0).getType();

    JsonConverter<FunctionalDependency> fdConverter = new JsonConverter<>();
    JsonConverter<InclusionDependency> indConverter = new JsonConverter<>();
    JsonConverter<UniqueColumnCombination> uccConverter = new JsonConverter<>();
    try {
      try (BufferedReader input = new BufferedReader(new FileReader(new File(filePath)))) {
        String line;
        while ((line = input.readLine()) != null) {
          if(resultType == ResultType.FD){
            results.add(fdConverter.fromJsonString(line, FunctionalDependency.class));
          }
          if(resultType == ResultType.IND){
            results.add(indConverter.fromJsonString(line, InclusionDependency.class));
          }
          if(resultType == ResultType.UCC){
            results.add(uccConverter.fromJsonString(line, UniqueColumnCombination.class));
          }
        }
      }
    }
    catch (IOException ex){
      ex.printStackTrace();
    }

    // Load results file
    return results;
  }

  /**
   * Creates the input generators for inputs used during the algorithms execution
   *
   * @param execution Provides inputs of the algorithm run
   * @throws AlgorithmConfigurationException Thrown if the inputs cannot be created for some reason
   */
  protected void extractInputs(Execution execution) throws AlgorithmConfigurationException {
    for(Input input : execution.getInputs()){
      // TODO adopt for other input types and multiple inputs
      FileInput fileInput = (FileInput)input;

      // Create appropriate settings for the input
      List<ConfigurationSetting> settings = new ArrayList<>();

      ConfigurationSetting setting = new ConfigurationSettingFileInput(fileInput.getFileName(), true,
                                                                       fileInput.getSeparatorAsChar(), fileInput.getQuoteCharAsChar(), fileInput.getEscapeCharAsChar(), fileInput.isStrictQuotes(),
                                                                       fileInput.isIgnoreLeadingWhiteSpace(), fileInput.getSkipLines(), fileInput.isHasHeader(), fileInput.isSkipDifferingLines(), fileInput.getNullValue());
      settings.add(setting);

      ConfigurationRequirementRelationalInput configurationRequirementRelationalInput = new ConfigurationRequirementRelationalInput(INPUT_IDENTIFIER);
      configurationRequirementRelationalInput.checkAndSetSettings(
          settings.toArray(new ConfigurationSettingFileInput[settings.size()]));

      // Create an initializer for relational inputs
      DefaultRelationalInputGeneratorInitializer
          inputGeneratorInitializer = new DefaultRelationalInputGeneratorInitializer(configurationRequirementRelationalInput);
      ConfigurationValueRelationalInputGenerator
          configurationValueRelationalInputGenerator = inputGeneratorInitializer.getConfigurationValue();

      // Trigger the configuration value generation (writes the result into the according class attribute)
      configurationValueRelationalInputGenerator.triggerSetValue(this, getInterfaces());

    }
  }

  //</editor-fold>

  //<editor-fold desc="Relational input algorithm methods">

  private Set<Class<?>> getInterfaces() {
    return new HashSet<>(ClassUtils.getAllInterfaces(this.getClass()));
  }

  @Override
  public void setRelationalInputConfigurationValue(String identifier,
                                                   RelationalInputGenerator... relationalInputGenerators)
      throws AlgorithmConfigurationException {
    if (identifier.equals(INPUT_IDENTIFIER)) {
      if(this.relationalInputGenerators == null) {
        this.relationalInputGenerators = new ArrayList<>(Arrays.asList(relationalInputGenerators));
      }
      else{
        this.relationalInputGenerators.addAll(new ArrayList<>(Arrays.asList(relationalInputGenerators)));
      }
    }

  }

  @Override
  public ArrayList<ConfigurationRequirement> getConfigurationRequirements() {
    ArrayList<ConfigurationRequirement> requiredConfig = new ArrayList<>();

    requiredConfig.add(new ConfigurationRequirementRelationalInput(INPUT_IDENTIFIER));

    return requiredConfig;
  }

  @Override
  public void execute() throws AlgorithmExecutionException {
    throw new AlgorithmExecutionException("Method not implemented: Call analyzeResults instead!");
  }

  //</editor-fold>

  //<editor-fold desc="Results analysis methods">

  /**
   * Implements an analysis of results based on provided execution
   *
   * @param execution Execution containing all informations about an algorithm run
   * @param useRowData Is it allowed to access row data or only table header data?
   */
  public void analyzeResults(Execution execution, boolean useRowData){

    this.execution = execution;

    // Extract the algorithm type
    List<Result> results = extractResults(this.execution);

    try {
      extractInputs(this.execution);
    }
    catch (Exception ex){
      ex.printStackTrace();
    }

    // Decide which data analysis to perform
    if(useRowData) {
      analyzeResultsWithTupleData(results);
      // TODO that is not more needed but for UCC analyzers it is here
      //analyzeResults(results);
    }
    else{
      analyzeResultsWithoutTupleData(results);
    }

    printResultsToFile(useRowData);
  }

  /**
   * Implements an analysis of results based on input data headers and
   * some filtering/ordering operations on the result set according to analysis results.
   *
   * @param oldResults Provides access to the results created by the algorithm
   */
  protected abstract void analyzeResultsWithoutTupleData(List<Result> oldResults);

  /**
   * Implements an analysis of results based on input data rows and
   * some filtering/ordering operations on the result set according to analysis results.
   *
   * @param oldResults Provides access to the results created by the algorithm
   */
  protected abstract void analyzeResultsWithTupleData(List<Result> oldResults);

  /**
   * Prints the results of postprocessing to file
   *
   * @param useRowData Determines whether the data-dependent rankings will be printed
   */
  protected abstract void printResultsToFile(boolean useRowData);

  /**
   * Implements an analysis of results based on input data and
   * some filtering/ordering operations on the result set according to analysis results.
   *
   * @param oldResults Provides access to the results created by the algorithm
   */
  protected abstract void analyzeResults(List<Result> oldResults);

  //</editor-fold>


}
