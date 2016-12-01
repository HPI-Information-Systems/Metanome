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
package de.metanome.algorithm_integration;

import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;

import java.util.ArrayList;

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
  ArrayList<ConfigurationRequirement<?>> getConfigurationRequirements();

  /**
   * Starts the execution of the algorithm.
   *
   * @throws AlgorithmExecutionException if the algorithm's execution fails
   */
  void execute() throws AlgorithmExecutionException;

  /**
   * @return a string containing the authors of the algorithm
   */
  public String getAuthors();

  /**
   * @return a string containing the description of the algorithm
   */
  public String getDescription();

}
