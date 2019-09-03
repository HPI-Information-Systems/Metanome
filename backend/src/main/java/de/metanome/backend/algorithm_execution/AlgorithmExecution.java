/**
 * Copyright 2015-2016 by Metanome Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.metanome.backend.algorithm_execution;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.algorithm_execution.FileGenerator;
import de.metanome.algorithm_integration.configuration.ConfigurationValue;
import de.metanome.algorithm_integration.input.*;
import de.metanome.algorithm_integration.results.JsonConverter;
import de.metanome.backend.constants.Constants;
import de.metanome.backend.helper.*;
import de.metanome.backend.input.file.DefaultFileInputGenerator;
import de.metanome.backend.resources.AlgorithmResource;
import de.metanome.backend.result_receiver.ResultCache;
import de.metanome.backend.result_receiver.ResultCounter;
import de.metanome.backend.result_receiver.ResultPrinter;
import de.metanome.backend.result_receiver.ResultReceiver;
import de.metanome.backend.results_db.ExecutionSetting;
import de.metanome.backend.results_db.FileInput;
import de.metanome.backend.results_db.HibernateUtil;
import de.metanome.backend.results_db.Input;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Takes care of executing algorithms with specified settings in a designated/separate process
 */
public class AlgorithmExecution {

  /**
   * Extract the column names from the input to forward them later on to the result receiver.
   *
   * @param inputs the inputs
   * @return a list of column names
   * @throws AlgorithmConfigurationException if the input could not be converted into an input generator
   * @throws InputGenerationException if no relational input could be generated
   */
  protected static List<ColumnIdentifier> extractColumnNames(List<Input> inputs) throws AlgorithmConfigurationException, InputGenerationException{
    List<RelationalInputGenerator> inputGenerators = new ArrayList<>();
    for (Input input : inputs) {
      if (input instanceof FileInput) {
        File currFile = new File(input.getName());
        if (currFile.isFile()) {
          inputGenerators.add(InputToGeneratorConverter.convertInput(input));
        } else if (currFile.isDirectory()) {
          File[] filesInDirectory = currFile.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String name) {
              for (String fileEnding : Constants.ACCEPTED_FILE_ENDINGS_ARRAY) {
                if (name.endsWith(fileEnding)) {
                  return true;
                }
              }
              return false;
            }
          });
          for (File file : filesInDirectory) {
            try {
              inputGenerators.add(new DefaultFileInputGenerator(file, InputToGeneratorConverter.convertInputToSetting((FileInput) input)));
            } catch (FileNotFoundException e) {
              e.printStackTrace();
            }
          }
        }
      } else {
        RelationalInputGenerator relInpGen = InputToGeneratorConverter.convertInput(input);
        if (relInpGen != null) {
          inputGenerators.add(relInpGen);
        }
      }
    }

    // if there is no input generator we can not extract any column names
    // there is probably no input generator, because a database connection was used
    if (inputGenerators.isEmpty()) {
      return null;
    }

    List<ColumnIdentifier> columnNames = new ArrayList<>();
    for (RelationalInputGenerator inputGenerator : inputGenerators) {
      RelationalInput input = inputGenerator.generateNewCopy();

      String tableName = input.relationName();
      for (String columnName : input.columnNames()) {
        columnNames.add(new ColumnIdentifier(tableName, columnName));
      }
    }

    return columnNames;
  }

  /**
   * Builds an {@link de.metanome.backend.algorithm_execution.AlgorithmExecutor} with the given
   * execution settings.
   *
   * @param executionSetting the execution setting
   * @param acceptedColumns the column identifiers of the accepted columns
   * @return an {@link de.metanome.backend.algorithm_execution.AlgorithmExecutor}
   * @throws java.io.FileNotFoundException        when the result files cannot be opened
   * @throws java.io.UnsupportedEncodingException when the temp files cannot be opened
   */
  protected static AlgorithmExecutor buildExecutor(ExecutionSetting executionSetting, List<ColumnIdentifier> acceptedColumns)
    throws FileNotFoundException, UnsupportedEncodingException {
    FileGenerator fileGenerator = new TempFileGenerator();
    String identifier = executionSetting.getExecutionIdentifier();

    ResultReceiver resultReceiver;
    if (executionSetting.getCacheResults()) {
      resultReceiver = new ResultCache(identifier, acceptedColumns);
    } else if (executionSetting.getCountResults()) {
      resultReceiver = new ResultCounter(identifier);
    } else {
      resultReceiver = new ResultPrinter(identifier, acceptedColumns);
    }

    AlgorithmExecutor executor =
      new AlgorithmExecutor(resultReceiver, fileGenerator);
    executor.setResultPathPrefix(resultReceiver.getOutputFilePathPrefix());
    return executor;
  }

  /**
   * Generates a list of ConfigurationValues from an List of ConfigurationValue json-strings
   *
   * @param parameterValuesJson List of parameter values in json format
   * @return a list of all configuration values
   */
  public static List<ConfigurationValue> parseConfigurationValues(List<String> parameterValuesJson) {
    JsonConverter<ConfigurationValue> jsonConverter = new JsonConverter<>();
    jsonConverter.addMixIn(FileInputGenerator.class, FileInputGeneratorMixIn.class);
    jsonConverter.addMixIn(TableInputGenerator.class, TableInputGeneratorMixIn.class);
    jsonConverter.addMixIn(DatabaseConnectionGenerator.class, DatabaseConnectionGeneratorMixIn.class);
    jsonConverter.addMixIn(RelationalInputGenerator.class, RelationalInputGeneratorMixIn.class);
    jsonConverter.addMixIn(ConfigurationValue.class, ConfigurationValueMixIn.class);

    List<ConfigurationValue> parameterValues = new ArrayList<>();
    for (String json : parameterValuesJson) {
      try {
        parameterValues.add(jsonConverter.fromJsonString(json, ConfigurationValue.class));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return parameterValues;
  }

  /**
   * Generates a list of Inputs from an List of Input json-strings
   *
   * @param inputsJson inputs in json format
   * @return a list of inputs
   */
  public static List<Input> parseInputs(List<String> inputsJson) {
    JsonConverter<Input> jsonConverterInput = new JsonConverter<>();
    List<Input> inputs = new ArrayList<>();

    for (String json : inputsJson) {
      try {
        inputs.add(jsonConverterInput.fromJsonString(json, Input.class));
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    }

    return inputs;
  }

  /**
   * Uses Algorithm and Execution Identifier (parsed from args[]) to load instances of Algorithm and
   * ExecutionSetting from the database, which are then used to execute the specified Algorithm with
   * the specified setting in the designated process
   *
   * @param args the program parameters
   */
  public static void main(String args[]) {
    Long algorithmId = Long.valueOf(args[0]);
    String executionIdentifier = args[1];

    // Get the algorithm object
    AlgorithmResource algorithmResource = new AlgorithmResource();
    de.metanome.backend.results_db.Algorithm algorithm = algorithmResource.get(algorithmId);

    // Get the execution setting from hibernate
    Session session = HibernateUtil.getSessionFactory().openSession();
    Criteria cr2 = session.createCriteria(ExecutionSetting.class);
    cr2.add(Restrictions.eq("executionIdentifier", executionIdentifier));
    ExecutionSetting executionSetting = (ExecutionSetting) cr2.list().get(0);

    // Parse the parameters
    List<ConfigurationValue> parameters = parseConfigurationValues(executionSetting.getParameterValuesJson());
    List<Input> inputs = parseInputs(executionSetting.getInputsJson());

    session.close();

    try {
      // Extract column names from the inputs
      List<ColumnIdentifier> columnNames = extractColumnNames(inputs);

      // Get the algorithm executor
      AlgorithmExecutor executor = buildExecutor(executionSetting, columnNames);
      executor
        .executeAlgorithm(algorithm, parameters, inputs, executionIdentifier,
          executionSetting);

      executor.close();
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
    System.exit(0);
  }


}
