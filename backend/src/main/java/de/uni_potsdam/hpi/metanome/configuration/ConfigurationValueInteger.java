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
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.IntegerParameterAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingInteger;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationInteger;

import java.util.Set;


public class ConfigurationValueInteger implements ConfigurationValue {

  protected final String identifier;
  protected final int[] values;

  /**
   * Constructs a ConfigurationValueInteger using the specification's identifier and the integer
   * value.
   *
   * @param identifier the configuration value integer identifier
   * @param values     the configuration value integer values
   */
  public ConfigurationValueInteger(String identifier, int... values) {
    this.identifier = identifier;
    this.values = values;
  }

  public ConfigurationValueInteger(ConfigurationSpecificationInteger specification) {
    this.identifier = specification.getIdentifier();
    this.values = new int[specification.getSettings().length];
    int i = 0;
    for (ConfigurationSettingInteger setting : specification.getSettings()) {
      this.values[i] = setting.value;
      i++;
    }
  }

  @Override
  public void triggerSetValue(Algorithm algorithm, Set<Class<?>> algorithmInterfaces)
      throws AlgorithmConfigurationException {
    if (!algorithmInterfaces.contains(IntegerParameterAlgorithm.class)) {
      throw new AlgorithmConfigurationException(
          "Algorithm does not accept integer configuration values.");
    }

    IntegerParameterAlgorithm integerParameterAlgorithm = (IntegerParameterAlgorithm) algorithm;
    integerParameterAlgorithm.setIntegerConfigurationValue(identifier, values);
  }
}
