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

import com.fasterxml.jackson.annotation.JsonTypeName;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;

import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

/**
 * Concrete {@link ConfigurationRequirement} for list box of strings.
 *
 * @author Tanja Bergmann
 * @see ConfigurationRequirement
 */
@JsonTypeName("ConfigurationRequirementListBox")
public class ConfigurationRequirementListBox extends ConfigurationRequirementDefaultValue<String> {

  private ConfigurationSettingListBox[] settings;
  private List<String> values;

  /**
   * Exists for serialization.
   */
  public ConfigurationRequirementListBox() {
  }

  /**
   * Construct a ConfigurationSpecificationListBox, requesting 1 values.
   *
   * @param identifier the specification's identifier
   * @param values     the values, which should be displayed in the list box
   */
  public ConfigurationRequirementListBox(String identifier, List<String> values) {
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
  public ConfigurationRequirementListBox(String identifier, List<String> values,
                                         int numberOfSettings) {
    super(identifier, numberOfSettings);
    this.values = values;
  }

  /**
   * Constructs a {@link ConfigurationRequirementListBox}, requesting several values.
   *
   * @param identifier         the specification's identifier
   * @param values           the values, which should be displayed in the list box
   * @param minNumberOfSetting the min number of settings expected
   * @param maxNumberOfSetting the max number of settings expected
   */
  public ConfigurationRequirementListBox(String identifier,
                                         List<String> values,
                                         int minNumberOfSetting,
                                         int maxNumberOfSetting) {
    super(identifier, minNumberOfSetting, maxNumberOfSetting);
    this.values = values;
  }

  @Override
  public ConfigurationSettingListBox[] getSettings() {
    return this.settings;
  }
  /**
   * Exists only for serialization!
   * @param settings the settings
   */
  public void setSettings(ConfigurationSettingListBox... settings) {
    this.settings = settings;
  }

  /**
   * @return the values, which should be displayed in the list box
   */
  public List<String> getValues() {
    return this.values;
  }

  public void setValues(List<String> values) { this.values = values; }

  /**
   * Sets the actual settings on the requirement if the number of settings is correct.
   *
   * @param settings the settings
   * @throws de.metanome.algorithm_integration.AlgorithmConfigurationException if the number of
   * settings does not match the expected number of settings
   */
  @XmlTransient
  public void checkAndSetSettings(ConfigurationSettingListBox... settings)
      throws AlgorithmConfigurationException {
    checkNumberOfSettings(settings.length);
    this.settings = settings;
    applyDefaultValues();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @XmlTransient
  @GwtIncompatible("ConfigurationValues cannot be build on client side.")
  public ConfigurationValue build(ConfigurationFactory factory)
      throws AlgorithmConfigurationException {
    return factory.build(this);
  }

  /**
   * Sets the default values on the settings, if both are set.
   */
  @XmlTransient
  @Override
  public void applyDefaultValues() {
    if (this.defaultValues == null || this.settings == null ||
        this.defaultValues.length != this.settings.length)
      return;

    for (int i = 0; i < this.settings.length; i++) {
      this.settings[i].setSelectedValue(this.defaultValues[i]);
    }
  }

}
