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
import de.metanome.algorithm_integration.algorithm_types.StringParameterAlgorithm;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementString;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingString;

import java.io.FileNotFoundException;
import java.util.Set;

/**
 * Represents string configuration values for {@link Algorithm}s.
 *
 * @author Jakob Zwiener
 */
public class ConfigurationValueString
  extends ConfigurationValue<String, ConfigurationRequirementString> {

  protected ConfigurationValueString() {
  }

  public ConfigurationValueString(String identifier, String... values) {
    super(identifier, values);
  }

  public ConfigurationValueString(ConfigurationRequirementString requirement)
    throws AlgorithmConfigurationException, FileNotFoundException {
    super(requirement);
  }

  @Override
  protected String[] convertToValues(ConfigurationRequirementString requirement)
    throws AlgorithmConfigurationException {
    ConfigurationSettingString[] settings = requirement.getSettings();
    String[] configValues = new String[settings.length];
    int i = 0;
    for (ConfigurationSettingString setting : settings) {
      configValues[i] = setting.value;
      i++;
    }
    return configValues;
  }

  @Override
  public void triggerSetValue(Algorithm algorithm, Set<Class<?>> algorithmInterfaces)
    throws AlgorithmConfigurationException {
    if (!algorithmInterfaces.contains(StringParameterAlgorithm.class)) {
      throw new AlgorithmConfigurationException(
        "Algorithm does not accept string configuration values.");
    }

    StringParameterAlgorithm stringParameterAlgorithm = (StringParameterAlgorithm) algorithm;
    stringParameterAlgorithm.setStringConfigurationValue(this.identifier, this.values);
  }
}
