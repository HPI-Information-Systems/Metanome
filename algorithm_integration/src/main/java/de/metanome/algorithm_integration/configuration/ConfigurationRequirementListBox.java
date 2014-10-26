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

package de.metanome.algorithm_integration.configuration;


import com.google.common.annotations.GwtIncompatible;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;

import java.util.ArrayList;

/**
 * Concrete {@link ConfigurationRequirement} for list box of strings.
 *
 * @author Tanja Bergmann
 * @see ConfigurationRequirement
 */
public class ConfigurationRequirementListBox extends ConfigurationRequirement {

  private ConfigurationSettingListBox[] settings;
  private ArrayList<String> values;

  /**
   * Exists for GWT serialization.
   */
  public ConfigurationRequirementListBox() {
  }

  /**
   * Construct a ConfigurationSpecificationListBox, requesting 1 values.
   *
   * @param identifier the specification's identifier
   * @param values     the values, which should be displayed in the list box
   */
  public ConfigurationRequirementListBox(String identifier, ArrayList<String> values) {
    super(identifier);
    this.values = values;
  }

  /**
   * Constructs a {@link ConfigurationRequirementListBox}, potentially requesting several values.
   *
   * @param identifier       the specification's identifier
   * @param values           the values, which should be displayed in the list box
   * @param numberOfSettings the number of settings expected
   */
  public ConfigurationRequirementListBox(String identifier, ArrayList<String> values,
                                         int numberOfSettings) {
    super(identifier, numberOfSettings);
    this.values = values;
  }

  @Override
  public ConfigurationSettingListBox[] getSettings() {
    return this.settings;
  }

  /**
   * Sets the actual settings on the requirement if the number of settings is correct.
   *
   * @param settings the settings
   * @throws de.metanome.algorithm_integration.AlgorithmConfigurationException if the number of
   * settings does not match the expected number of settings
   */
  public void setSettings(ConfigurationSettingListBox... settings)
      throws AlgorithmConfigurationException {
    checkNumberOfSettings(settings.length);
    this.settings = settings;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @GwtIncompatible("ConfigurationValues cannot be build on client side.")
  public ConfigurationValue build(ConfigurationFactory factory)
      throws AlgorithmConfigurationException {
    return factory.build(this);
  }

  /**
   * @return the values, which should be displayed in the list box
   */
  public ArrayList<String> getValues() {
    return this.values;
  }
}
