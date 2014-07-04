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
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.ListBoxParameterAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingListBox;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationListBox;

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

  public ConfigurationValueListBox(
      ConfigurationSpecificationListBox specification) {
    this.identifier = specification.getIdentifier();
    this.selectedValues = new String[specification.getSettings().length];
    int i = 0;
    for (ConfigurationSettingListBox setting : specification.getSettings()) {
      this.selectedValues[i] = setting.selectedValue;
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
