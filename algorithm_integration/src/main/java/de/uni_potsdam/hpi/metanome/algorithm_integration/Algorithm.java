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

package de.uni_potsdam.hpi.metanome.algorithm_integration;

import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification;

import java.util.List;

/**
 * An algorithm should supply the configuration requirements, should initiate double dispatch with
 * incoming configurations and receive the different configuration types. Subclassing of this class
 * will not lead to a functioning algorithm, as only more concrete algorithms can be started.
 */
public interface Algorithm {

  /**
   * Algorithms should supply a list of needed configuration parameters.
   *
   * @return a list of ConfigurationSpecifications
   */
  List<ConfigurationSpecification> getConfigurationRequirements();

  /**
   * Starts the execution of the algorithm.
   *
   * @throws de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmExecutionException if the algorithm's
   *                                                                                       execution
   *                                                                                       fails
   */
  void execute() throws AlgorithmExecutionException;

}
