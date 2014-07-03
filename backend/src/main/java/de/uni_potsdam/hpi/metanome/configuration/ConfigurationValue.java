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

package de.uni_potsdam.hpi.metanome.configuration;

import de.uni_potsdam.hpi.metanome.algorithm_integration.Algorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;

import java.util.Set;

/**
 * Represents configuration values for {@link Algorithm}s. Sets it's own value on an {@link
 * Algorithm} through double dispatch (second call).
 */
public interface ConfigurationValue {

  /**
   * Sets the configuration value on the algorithm. The type of the configuration value is resolved
   * in the implementations.
   *
   * @param algorithm           the algorithm to set the value on
   * @param algorithmInterfaces the interfaces the algorithm implements
   */
  void triggerSetValue(Algorithm algorithm, Set<Class<?>> algorithmInterfaces)
      throws AlgorithmConfigurationException;

}
