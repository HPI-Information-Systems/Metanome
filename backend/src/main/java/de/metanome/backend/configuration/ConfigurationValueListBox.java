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

package de.metanome.backend.configuration;

import de.metanome.algorithm_integration.Algorithm;
import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.algorithm_types.ListBoxParameterAlgorithm;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementListBox;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingListBox;
import de.metanome.algorithm_integration.configuration.ConfigurationValue;

import java.util.Set;


public class ConfigurationValueListBox implements ConfigurationValue {

  protected final String identifier;
  protected final String[] selectedValues;

  /**
   * Constructs a ConfigurationValueListBox using the specification's identifier and the list of
   * string values.
   *
   * @param identifier     the configuration value enum identifier
   * @param selectedValues the configuration value string values
   */
  public ConfigurationValueListBox(String identifier, String... selectedValues) {
    this.identifier = identifier;
    this.selectedValues = selectedValues;
  }

  /**
   * Constructs a {@link de.metanome.backend.configuration.ConfigurationValueListBox} using a {@link
   * de.metanome.algorithm_integration.configuration.ConfigurationRequirementListBox}.
   *
   * @param requirement the requirement to generate the list box values
   */
  public ConfigurationValueListBox(
      ConfigurationRequirementListBox requirement) {
    this.identifier = requirement.getIdentifier();
    ConfigurationSettingListBox[] settings = requirement.getSettings();
    this.selectedValues = new String[settings.length];
    int i = 0;
    for (ConfigurationSettingListBox setting : settings) {
      this.selectedValues[i] = setting.getValue();
      i++;
    }
  }

  @Override
  public void triggerSetValue(Algorithm algorithm, Set<Class<?>> algorithmInterfaces)
      throws AlgorithmConfigurationException {
    if (!algorithmInterfaces.contains(ListBoxParameterAlgorithm.class)) {
      throw new AlgorithmConfigurationException(
          "Algorithm does not accept arraylist configuration values.");
    }

    ListBoxParameterAlgorithm listBoxParameterAlgorithm = (ListBoxParameterAlgorithm) algorithm;
    listBoxParameterAlgorithm.setListBoxConfigurationValue(identifier, selectedValues);
  }
}
