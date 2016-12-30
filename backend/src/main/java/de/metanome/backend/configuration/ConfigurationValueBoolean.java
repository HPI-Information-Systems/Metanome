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
import de.metanome.algorithm_integration.algorithm_types.BooleanParameterAlgorithm;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementBoolean;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingBoolean;

import java.io.FileNotFoundException;
import java.util.Set;

/**
 * Represents boolean configuration values for {@link Algorithm}s.
 */
public class ConfigurationValueBoolean
  extends ConfigurationValue<Boolean, ConfigurationRequirementBoolean> {

  protected ConfigurationValueBoolean() {
  }

  public ConfigurationValueBoolean(String identifier, Boolean... values) {
    super(identifier, values);
  }

  public ConfigurationValueBoolean(ConfigurationRequirementBoolean requirement)
    throws AlgorithmConfigurationException, FileNotFoundException {
    super(requirement);
  }

  @Override
  protected Boolean[] convertToValues(ConfigurationRequirementBoolean requirement)
    throws AlgorithmConfigurationException {
    ConfigurationSettingBoolean[] settings = requirement.getSettings();
    Boolean[] configValues = new Boolean[settings.length];
    int i = 0;
    for (ConfigurationSettingBoolean setting : settings) {
      configValues[i] = setting.getValue();
      i++;
    }
    return configValues;
  }

  @Override
  public void triggerSetValue(Algorithm algorithm, Set<Class<?>> algorithmInterfaces)
    throws AlgorithmConfigurationException {
    if (!algorithmInterfaces.contains(BooleanParameterAlgorithm.class)) {
      throw new AlgorithmConfigurationException(
        "Algorithm does not accept boolean configuration values.");
    }

    BooleanParameterAlgorithm booleanParameterAlgorithm = (BooleanParameterAlgorithm) algorithm;
    booleanParameterAlgorithm.setBooleanConfigurationValue(this.identifier, this.values);
  }
}
