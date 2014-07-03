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

package de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types;

import de.uni_potsdam.hpi.metanome.algorithm_integration.Algorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.RelationalInputGenerator;

/**
 * An {@link Algorithm} that takes {@link RelationalInputGenerator} configuration values.
 *
 * @author Jakob Zwiener
 */
public interface RelationalInputParameterAlgorithm extends Algorithm {

  /**
   * Sets a RelationalInputGenerator configuration value on the algorithm.
   *
   * @param identifier the value's identifier
   * @param values     the configuration values
   * @throws de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException if the
   *                                                                                           algorithm
   *                                                                                           cannot
   *                                                                                           be correctly
   *                                                                                           configured
   *                                                                                           using
   *                                                                                           the received
   *                                                                                           configuration
   *                                                                                           values
   */
  void setRelationalInputConfigurationValue(String identifier, RelationalInputGenerator... values)
      throws AlgorithmConfigurationException;

}
