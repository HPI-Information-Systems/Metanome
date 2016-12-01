/**
 * Copyright 2014-2016 by Metanome Project
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
package de.metanome.algorithms.testing.example_cucc_algorithm;

import de.metanome.algorithm_integration.*;
import de.metanome.algorithm_integration.algorithm_types.ConditionalUniqueColumnCombinationAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.FileInputParameterAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.StringParameterAlgorithm;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementFileInput;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementString;
import de.metanome.algorithm_integration.input.FileInputGenerator;
import de.metanome.algorithm_integration.result_receiver.ColumnNameMismatchException;
import de.metanome.algorithm_integration.result_receiver.ConditionalUniqueColumnCombinationResultReceiver;
import de.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.metanome.algorithm_integration.results.ConditionalUniqueColumnCombination;

import java.util.ArrayList;

/**
 * @author Jens Ehrlich
 */
public class ExampleAlgorithm implements ConditionalUniqueColumnCombinationAlgorithm,
                                         StringParameterAlgorithm, FileInputParameterAlgorithm {

  protected String path1, path2 = null;
  protected ConditionalUniqueColumnCombinationResultReceiver resultReceiver;

  @Override
  public ArrayList<ConfigurationRequirement<?>> getConfigurationRequirements() {
    ArrayList<ConfigurationRequirement<?>> configurationRequirement = new ArrayList<>();

    configurationRequirement.add(new ConfigurationRequirementString(
        "pathToInputFile", 2));
    configurationRequirement.add(new ConfigurationRequirementFileInput(
        "input file", 2));

    return configurationRequirement;
  }

  @Override
  public void execute() throws AlgorithmExecutionException {
    if ((path1 != null) && (path2 != null)) {
      System.out.println("CUCC Algorithm executing");
      try {
        resultReceiver.receiveResult(new ConditionalUniqueColumnCombination(
            new ColumnCombination(new ColumnIdentifier("WDC_planets.csv", "Name")),
            new ColumnConditionValue(new ColumnIdentifier("WDC_planetz.csv", "Planet"), "hello world")));

      } catch (CouldNotReceiveResultException | ColumnNameMismatchException e) {
        e.printStackTrace();
        throw e;
      }
    }
  }

  @Override
  public void setStringConfigurationValue(String identifier, String... values)
      throws AlgorithmConfigurationException {
    if ((identifier.equals("pathToInputFile")) && (values.length == 2)) {
      path1 = values[0];
      path2 = values[1];
    } else {
      throw new AlgorithmConfigurationException("Incorrect identifier or value list length.");
    }
  }

  @Override
  public void setFileInputConfigurationValue(String identifier, FileInputGenerator... values)
      throws AlgorithmConfigurationException {
    if (identifier.equals("input file")) {
      // Input file is not being set on algorithm.
    }
  }

  @Override
  public void setResultReceiver(ConditionalUniqueColumnCombinationResultReceiver resultReceiver) {
    this.resultReceiver = resultReceiver;
  }

  @Override
  public String getAuthors() {
    return "Metanome Team";
  }

  @Override
  public String getDescription() {
    return "Example algorithm for conditional unique column combinations.";
  }
}
