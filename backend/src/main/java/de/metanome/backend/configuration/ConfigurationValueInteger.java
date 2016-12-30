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
package de.metanome.backend.configuration;

import de.metanome.algorithm_integration.Algorithm;
import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.algorithm_types.IntegerParameterAlgorithm;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementInteger;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingInteger;

import java.io.FileNotFoundException;
import java.util.Set;

/**
 * Represents integer configuration values for {@link Algorithm}s.
 *
 * @author Jakob Zwiener
 */
public class ConfigurationValueInteger
  extends ConfigurationValue<Integer, ConfigurationRequirementInteger> {

  protected ConfigurationValueInteger() {
  }

  public ConfigurationValueInteger(String identifier, Integer... values) {
    super(identifier, values);
  }

  public ConfigurationValueInteger(ConfigurationRequirementInteger requirement)
    throws AlgorithmConfigurationException, FileNotFoundException {
    super(requirement);
  }

  @Override
  protected Integer[] convertToValues(ConfigurationRequirementInteger requirement)
    throws AlgorithmConfigurationException {
    ConfigurationSettingInteger[] settings = requirement.getSettings();
    Integer[] configValues = new Integer[settings.length];
    int i = 0;
    for (ConfigurationSettingInteger setting : settings) {
      configValues[i] = setting.value;
      i++;
    }
    return configValues;
  }

  @Override
  public void triggerSetValue(Algorithm algorithm, Set<Class<?>> algorithmInterfaces)
    throws AlgorithmConfigurationException {
    if (!algorithmInterfaces.contains(IntegerParameterAlgorithm.class)) {
      throw new AlgorithmConfigurationException(
        "Algorithm does not accept integer configuration values.");
    }

    IntegerParameterAlgorithm integerParameterAlgorithm = (IntegerParameterAlgorithm) algorithm;
    integerParameterAlgorithm.setIntegerConfigurationValue(this.identifier, this.values);
  }
}
