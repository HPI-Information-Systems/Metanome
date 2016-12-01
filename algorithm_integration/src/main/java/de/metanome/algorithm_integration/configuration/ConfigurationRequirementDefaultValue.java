/**
 * Copyright 2015-2016 by Metanome Project
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


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.metanome.algorithm_integration.AlgorithmConfigurationException;

import javax.xml.bind.annotation.XmlTransient;

/**
 * Handles default values for configuration requirements.
 *
 * @param <T> the type of the default values
 * @param <S> the setting type of the requirement
 */
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = ConfigurationRequirementBoolean.class, name = "ConfigurationRequirementBoolean"),
  @JsonSubTypes.Type(value = ConfigurationRequirementInteger.class, name = "ConfigurationRequirementInteger"),
  @JsonSubTypes.Type(value = ConfigurationRequirementListBox.class, name = "ConfigurationRequirementListBox"),
  @JsonSubTypes.Type(value = ConfigurationRequirementString.class, name = "ConfigurationRequirementString"),
  @JsonSubTypes.Type(value = ConfigurationRequirementCheckBox.class, name = "ConfigurationRequirementCheckBox"),
})
public abstract class ConfigurationRequirementDefaultValue<T, S extends ConfigurationSettingPrimitive<T>>
  extends ConfigurationRequirement<S> {

  private static final long serialVersionUID = -9157771032477423545L;
  public T[] defaultValues;

  public ConfigurationRequirementDefaultValue() {
  }

  public ConfigurationRequirementDefaultValue(String identifier) {
    super(identifier);
  }

  public ConfigurationRequirementDefaultValue(String identifier, int numberOfSettings) {
    super(identifier, numberOfSettings);
  }

  public ConfigurationRequirementDefaultValue(String identifier, int minNumberOfSetting,
                                              int maxNumberOfSetting) {
    super(identifier, minNumberOfSetting, maxNumberOfSetting);
  }

  /**
   * Checks if the number of default values match with the number of settings. If it matches, the
   * default values are set.
   *
   * @param values the default values to set
   * @throws AlgorithmConfigurationException if the number of default values does not match the
   *                                         number of settings
   */
  @XmlTransient
  public final void checkAndSetDefaultValues(T... values) throws AlgorithmConfigurationException {
    try {
      checkNumberOfSettings(values.length);
    } catch (AlgorithmConfigurationException e) {
      throw new AlgorithmConfigurationException(
        "The number of default values does not match the number of settings.");
    }

    this.defaultValues = values;
    applyDefaultValues();
  }

  /**
   * @param index the index of the setting
   * @return the default value of the specific setting
   */
  @XmlTransient
  public T getDefaultValue(int index) {
    if (defaultValues != null && defaultValues.length > index) {
      return this.defaultValues[index];
    }
    return null;
  }

  /**
   * Sets the default values on the settings, if both are set.
   */
  @XmlTransient
  public void applyDefaultValues() {
    if (this.defaultValues == null || this.settings == null ||
      this.defaultValues.length != this.settings.length) {
      return;
    }

    for (int i = 0; i < this.settings.length; i++) {
      this.settings[i].setValue(this.defaultValues[i]);
    }
  }

  /**
   * Exists only for serialization!
   *
   * @return the default values
   */
  public T[] getDefaultValues() {
    return defaultValues;
  }

  /**
   * Exists only for serialization!
   *
   * @param defaultValues the default values
   */
  public void setDefaultValues(T[] defaultValues) {
    this.defaultValues = defaultValues;
  }

  /**
   * {@inheritDoc} Exists for serialization!
   */
  @Override
  public S[] getSettings() {
    return settings;
  }

  /**
   * {@inheritDoc} Exists for serialization!
   */
  @Override
  public final void setSettings(S... settings) {
    this.settings = settings;
  }

}
