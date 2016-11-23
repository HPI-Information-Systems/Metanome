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
package de.metanome.algorithms.testing.example_indirect_interfaces_algorithm;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.AlgorithmExecutionException;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementFileInput;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.algorithm_integration.result_receiver.UniqueColumnCombinationResultReceiver;
import de.metanome.algorithm_integration.results.UniqueColumnCombination;

import java.util.ArrayList;

/**
 * A metanome algorithm that does not implement the metanome interfaces directly.
 *
 * @author Jakob Zwiener
 */
public class IndirectInterfacesAlgorithm extends AlgorithmSuperclass {

  protected RelationalInputGenerator inputGenerator = null;
  protected UniqueColumnCombinationResultReceiver resultReceiver = null;

  @Override
  public void setRelationalInputConfigurationValue(String identifier,
                                                   RelationalInputGenerator... values)
      throws AlgorithmConfigurationException {
    this.inputGenerator = values[0];
  }

  @Override
  public void setResultReceiver(UniqueColumnCombinationResultReceiver resultReceiver) {
    this.resultReceiver = resultReceiver;
  }

  @Override
  public ArrayList<ConfigurationRequirement<?>> getConfigurationRequirements() {
    ArrayList<ConfigurationRequirement<?>> configurations = new ArrayList<>();

    configurations.add(new ConfigurationRequirementFileInput("identifier"));

    return configurations;
  }

  @Override
  public void execute() throws AlgorithmExecutionException {
    if ((inputGenerator != null) && (resultReceiver != null)) {
      resultReceiver
          .receiveResult(new UniqueColumnCombination(new ColumnIdentifier("WDC_planets.csv", "Name")));
    }
  }

  @Override
  public String getAuthors() {
    return "Metanome Team";
  }

  @Override
  public String getDescription() {
    return "Example algorithm that does not implement the Metanome interfaces directly.";
  }
}
