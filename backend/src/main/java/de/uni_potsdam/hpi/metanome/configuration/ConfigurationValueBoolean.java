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
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.BooleanParameterAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingBoolean;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationBoolean;

import java.util.Set;

/**
 * Represents boolean configuration values for {@link Algorithm}s.
 */
public class ConfigurationValueBoolean implements ConfigurationValue {

  protected final String identifier;
  protected final boolean[] values;

  /**
   * Constructs a ConfigurationValueBoolean using the given identifier and the boolean values.
   */
  public ConfigurationValueBoolean(String identifier, boolean... values) {
    this.identifier = identifier;
    this.values = values;
  }

  /**
   * Constructs a ConfigurationValueBoolean using the specification's identifier and its boolean
   * values.
   */
  public ConfigurationValueBoolean(ConfigurationSpecificationBoolean spec) {
    this.identifier = spec.getIdentifier();
    this.values = new boolean[spec.getSettings().length];
    int i = 0;
    for (ConfigurationSettingBoolean setting : spec.getSettings()) {
      this.values[i] = setting.value;
      i++;
    }
  }

  @Override
  public void triggerSetValue(Algorithm algorithm, Set<Class<?>> algorithmInterfaces)
      throws AlgorithmConfigurationException {
    if (!algorithmInterfaces.contains(BooleanParameterAlgorithm.class)) {
      throw new AlgorithmConfigurationException(
          "Algorithm does not accept boolean configuration values.");
    }

    BooleanParameterAlgorithm booleanParameterAlgorithm = (BooleanParameterAlgorithm) algorithm;
    booleanParameterAlgorithm.setBooleanConfigurationValue(identifier, values);
  }
}
