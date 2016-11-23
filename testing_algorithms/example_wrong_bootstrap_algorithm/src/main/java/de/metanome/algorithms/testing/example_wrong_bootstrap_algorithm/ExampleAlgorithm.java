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
package de.metanome.algorithms.testing.example_wrong_bootstrap_algorithm;

import de.metanome.algorithm_integration.AlgorithmExecutionException;
import de.metanome.algorithm_integration.algorithm_types.UniqueColumnCombinationsAlgorithm;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.result_receiver.UniqueColumnCombinationResultReceiver;

import java.util.ArrayList;

public class ExampleAlgorithm implements UniqueColumnCombinationsAlgorithm {

  @Override
  public ArrayList<ConfigurationRequirement<?>> getConfigurationRequirements() {
    return new ArrayList<>();
  }

  @Override
  public void execute() throws AlgorithmExecutionException {

  }

  @Override
  public String getAuthors() {
    return "";
  }

  @Override
  public String getDescription() {
    return "";
  }

  @Override
  public void setResultReceiver(UniqueColumnCombinationResultReceiver resultReceiver) {

  }

}
