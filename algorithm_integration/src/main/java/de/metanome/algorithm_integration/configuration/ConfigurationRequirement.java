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
package de.metanome.algorithm_integration.configuration;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.metanome.algorithm_integration.Algorithm;
import de.metanome.algorithm_integration.AlgorithmConfigurationException;

import javax.xml.bind.annotation.XmlTransient;
import java.io.FileNotFoundException;
import java.io.Serializable;

/**
 * Represents a configuration parameter an {@link Algorithm} needs to be properly configured. The
 * ConfigurationRequirement is then used to construct a configuration value that is sent to the
 * {@link Algorithm} for configuration. Only type concrete ConfigurationRequirement subclasses
 * should be used to specify configuration parameters.
 *
 * @param <T> the setting type of the requirement
 * @author Jakob Zwiener
 */
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = ConfigurationRequirementBoolean.class, name = "ConfigurationRequirementBoolean"),
  @JsonSubTypes.Type(value = ConfigurationRequirementDatabaseConnection.class, name = "ConfigurationRequirementDatabaseConnection"),
  @JsonSubTypes.Type(value = ConfigurationRequirementFileInput.class, name = "ConfigurationRequirementFileInput"),
  @JsonSubTypes.Type(value = ConfigurationRequirementInteger.class, name = "ConfigurationRequirementInteger"),
  @JsonSubTypes.Type(value = ConfigurationRequirementListBox.class, name = "ConfigurationRequirementListBox"),
  @JsonSubTypes.Type(value = ConfigurationRequirementCheckBox.class, name = "ConfigurationRequirementCheckBox"),
  @JsonSubTypes.Type(value = ConfigurationRequirementRelationalInput.class, name = "ConfigurationRequirementRelationalInput"),
  @JsonSubTypes.Type(value = ConfigurationRequirementString.class, name = "ConfigurationRequirementString"),
  @JsonSubTypes.Type(value = ConfigurationRequirementTableInput.class, name = "ConfigurationRequirementTableInput")
})
public abstract class ConfigurationRequirement<T extends ConfigurationSetting> implements Serializable {

  public static final int ARBITRARY_NUMBER_OF_VALUES = -1;
  private static final long serialVersionUID = -821916342930792349L;

  protected String identifier;
  protected boolean required;
  protected int numberOfSettings;
  protected int minNumberOfSettings;
  protected int maxNumberOfSettings;

  public T[] settings;

  /**
   * Exists for serialization.
   */
  public ConfigurationRequirement() {
  }

  /**
   * Construct a configuration specification. A string identifier is stored to identify
   * configuration parameter. The identifier should be unique among all parameters of one algorithm.
   * The number of requested values defaults to 1.
   *
   * @param identifier the specification's identifier
   */
  public ConfigurationRequirement(String identifier) {
    this(identifier, 1, 1);
  }

  /**
   * Construct a configuration specification. A string identifier is stored to identify
   * configuration parameter. The identifier should be unique among all parameters of one algorithm.
   * The number of requested values is set. Use ARBITRARY_NUMBER_OF_VALUES to request arbitrary
   * number of values.
   *
   * @param identifier       the specification's identifier
   * @param numberOfSettings the number of settings expected
   */
  public ConfigurationRequirement(String identifier, int numberOfSettings) {
    this(identifier, numberOfSettings, numberOfSettings);
  }

  /**
   * Construct a configuration specification. A string identifier is stored to identify
   * configuration parameter. The identifier should be unique among all parameters of one algorithm.
   * The min and max number of requested values is set.
   *
   * @param identifier          the specification's identifier
   * @param minNumberOfSettings the minimum number of settings expected
   * @param maxNumberOfSettings the maximum number of settings expected (included)
   */
  public ConfigurationRequirement(String identifier, int minNumberOfSettings,
                                  int maxNumberOfSettings) {
    this.identifier = identifier;
    this.minNumberOfSettings = minNumberOfSettings;
    this.maxNumberOfSettings = maxNumberOfSettings;
    this.numberOfSettings = maxNumberOfSettings;
    this.required = true;
  }

  /**
   * @return identifier
   */
  public String getIdentifier() {
    return identifier;
  }

  /**
   * @return true, if a fix number of settings is required, false, otherwise
   */
  public Boolean isFixNumberOfSettings() {
    return this.maxNumberOfSettings == this.minNumberOfSettings;
  }

  /**
   * @return min number of settings
   */
  public int getMinNumberOfSettings() {
    return this.minNumberOfSettings;
  }

  /**
   * @return max number of settings
   */
  public int getMaxNumberOfSettings() {
    return this.maxNumberOfSettings;
  }

  /**
   * @return the number of settings
   */
  public int getNumberOfSettings() {
    return numberOfSettings;
  }

  /**
   * @return the specification's settings
   */
  public T[] getSettings() {
    return settings;
  }

  /**
   * Exists only for serialization!
   *
   * @param settings the settings
   */
  public void setSettings(T... settings) {
    this.settings = settings;
  }

  /**
   * @return true, if the requirement is required, i.e. it is not optional
   */
  public boolean isRequired() {
    return required;
  }

  /**
   * If there is a fix number of settings and required is true, all settings has to be set. If a
   * range of settings is given and required is true, the minimum number of settings has to be set.
   *
   * @param required true, if the requirement is not optional
   */
  public void setRequired(boolean required) {
    this.required = required;
  }

  /**
   * If a setting is set, the number of given settings has to match the expected number.
   *
   * @param number number of set settings
   * @throws de.metanome.algorithm_integration.AlgorithmConfigurationException if the given number
   *                                                                           of settings does not
   *                                                                           match the expected
   *                                                                           number.
   */
  @XmlTransient
  protected void checkNumberOfSettings(int number) throws AlgorithmConfigurationException {
    if (this.required && this.numberOfSettings != ARBITRARY_NUMBER_OF_VALUES
      && number != this.numberOfSettings &&
      (number < this.minNumberOfSettings || number > this.maxNumberOfSettings)) {
      throw new AlgorithmConfigurationException(
        "The number of settings does not match the expected number!");
    }
  }

  /**
   * Sets the actual settings on the requirement if the number of settings is correct.
   *
   * @param settings the settings
   * @throws de.metanome.algorithm_integration.AlgorithmConfigurationException if the number of
   *                                                                           settings does not
   *                                                                           match the expected
   *                                                                           number of settings
   */
  @XmlTransient
  public final void checkAndSetSettings(T... settings)
    throws AlgorithmConfigurationException {
    checkNumberOfSettings(settings.length);
    this.settings = settings;
  }

  /**
   * Builds the corresponding {@link de.metanome.algorithm_integration.configuration.ConfigurationValue}
   * from the {@link de.metanome.algorithm_integration.configuration.ConfigurationRequirement} using
   * the {@link de.metanome.algorithm_integration.configuration.ConfigurationFactory}.
   *
   * @param factory the {@link de.metanome.algorithm_integration.configuration.ConfigurationFactory}
   *                used for conversion
   * @return the corresponding {@link de.metanome.algorithm_integration.configuration.ConfigurationValue}
   * @throws AlgorithmConfigurationException thrown if the conversion is not successful
   */
  @XmlTransient
  public abstract ConfigurationValue build(ConfigurationFactory factory)
          throws AlgorithmConfigurationException, FileNotFoundException;

}
