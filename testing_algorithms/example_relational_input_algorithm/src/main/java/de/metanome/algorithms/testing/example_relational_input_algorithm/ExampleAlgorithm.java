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
package de.metanome.algorithms.testing.example_relational_input_algorithm;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.AlgorithmExecutionException;
import de.metanome.algorithm_integration.algorithm_types.RelationalInputParameterAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.TableInputParameterAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.UniqueColumnCombinationsAlgorithm;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementRelationalInput;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementTableInput;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.algorithm_integration.input.TableInputGenerator;
import de.metanome.algorithm_integration.result_receiver.UniqueColumnCombinationResultReceiver;

import java.util.ArrayList;

/**
 * A testing algorithm that requests a {@link de.metanome.algorithm_integration.input.TableInputGenerator}
 * and {@link de.metanome.algorithm_integration.input.RelationalInputGenerator}. An exception is
 * thrown if they are not set upon calling {@link ExampleAlgorithm#execute()}.
 *
 * @author Jakob Zwiener
 */
public class ExampleAlgorithm implements UniqueColumnCombinationsAlgorithm,
                                         RelationalInputParameterAlgorithm,
                                         TableInputParameterAlgorithm {


  public static final String RELATIONAL_INPUT_IDENTIFIER = "relationalInput";
  public static final String TABLE_INPUT_IDENTIFIER = "tableInput";

  protected UniqueColumnCombinationResultReceiver resultReceiver;
  protected RelationalInputGenerator relationalInputGenerator;
  protected TableInputGenerator tableInputGenerator;

  @Override
  public ArrayList<ConfigurationRequirement<?>> getConfigurationRequirements() {
    ArrayList<ConfigurationRequirement<?>> configurationRequirements = new ArrayList<>();

    ConfigurationRequirementRelationalInput
        requirementRelationalInput =
        new ConfigurationRequirementRelationalInput(RELATIONAL_INPUT_IDENTIFIER);
    ConfigurationRequirementTableInput
        requirementTableInput =
        new ConfigurationRequirementTableInput(TABLE_INPUT_IDENTIFIER);

    configurationRequirements.add(requirementRelationalInput);
    configurationRequirements.add(requirementTableInput);

    return configurationRequirements;
  }

  /**
   * Algorithm fails if the relational input has not been configured.
   */
  @Override
  public void execute() throws AlgorithmExecutionException {
    if (relationalInputGenerator == null) {
      throw new AlgorithmConfigurationException("The relational input has not been configured.");
    }
  }

  @Override
  public void setTableInputConfigurationValue(String identifier, TableInputGenerator... values)
      throws AlgorithmConfigurationException {
    tableInputGenerator = values[0];
  }

  @Override
  public void setRelationalInputConfigurationValue(String identifier,
                                                   RelationalInputGenerator... values)
      throws AlgorithmConfigurationException {
    relationalInputGenerator = values[0];
    try {
      relationalInputGenerator.generateNewCopy();
    } catch (InputGenerationException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void setResultReceiver(UniqueColumnCombinationResultReceiver resultReceiver) {
    this.resultReceiver = resultReceiver;
  }


  @Override
  public String getAuthors() {
    return "Metanome Team";
  }

  @Override
  public String getDescription() {
    return "Example algorithm, which requires a relational input.";
  }
}
