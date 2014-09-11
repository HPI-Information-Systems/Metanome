/*
 * Copyright 2014 by the Metanome project
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
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.algorithm_integration.input.TableInputGenerator;
import de.metanome.algorithm_integration.result_receiver.UniqueColumnCombinationResultReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * A testing algorithm that requests a {@link de.metanome.algorithm_integration.input.TableInputGenerator}
 * and {@link de.metanome.algorithm_integration.input.RelationalInputGenerator}. An exception is
 * thrown if they are not set upon calling {@link RelationalInputAlgorithm#execute()}.
 *
 * @author Jakob Zwiener
 */
public class RelationalInputAlgorithm implements UniqueColumnCombinationsAlgorithm,
                                                 RelationalInputParameterAlgorithm,
                                                 TableInputParameterAlgorithm {


  public static final String RELATIONAL_INPUT_IDENTIFIER = "relationalInput";
  public static final String TABLE_INPUT_IDENTIFIER = "tableInput";

  protected UniqueColumnCombinationResultReceiver resultReceiver;
  protected RelationalInputGenerator relationalInputGenerator;
  protected TableInputGenerator tableInputGenerator;

  @Override
  public List<ConfigurationRequirement> getConfigurationRequirements() {
    List<ConfigurationRequirement> configurationRequirements = new ArrayList<>();

    configurationRequirements.add(new ConfigurationRequirementRelationalInput(
        RELATIONAL_INPUT_IDENTIFIER));
    configurationRequirements.add(new ConfigurationRequirementTableInput(TABLE_INPUT_IDENTIFIER));

    return configurationRequirements;
  }

  @Override
  public void execute() throws AlgorithmExecutionException {
    if ((relationalInputGenerator == null) || (tableInputGenerator == null)) {
      throw new AlgorithmConfigurationException("Some inputs have not been configured.");
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
  }

  @Override
  public void setResultReceiver(UniqueColumnCombinationResultReceiver resultReceiver) {
    this.resultReceiver = resultReceiver;
  }
}
