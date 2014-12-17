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
import com.google.gwt.user.client.rpc.IsSerializable;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import de.metanome.algorithm_integration.Algorithm;
import de.metanome.algorithm_integration.AlgorithmConfigurationException;


/**
 * Represents a configuration parameter an {@link Algorithm} needs to be properly configured. The
 * ConfigurationSpecification is then used to construct a configuration value that is sent to the
 * {@link Algorithm} for configuration. Only type concrete ConfigurationSpecification subclasses
 * should be used to specify configuration parameters.
 *
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
                  @JsonSubTypes.Type(value = ConfigurationRequirementRelationalInput.class, name = "ConfigurationRequirementRelationalInput"),
                  @JsonSubTypes.Type(value = ConfigurationRequirementString.class, name = "ConfigurationRequirementString"),
                  @JsonSubTypes.Type(value = ConfigurationRequirementTableInput.class, name = "ConfigurationRequirementTableInput")
              })
public abstract class ConfigurationRequirement implements IsSerializable {

  public static final int ARBITRARY_NUMBER_OF_VALUES = -1;
  protected String identifier;
  /**
   * would be good to make this final, but then it would not be serialized and thus be reset to 1 in
   * frontend
   */
  protected int numberOfSettings;

  /**
   * Exists for GWT serialization.
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
    this(identifier, 1);
  }

  /**
   * Construct a configuration specification. A string identifier is stored to identify
   * configuration parameter. The identifier should be unique among all parameters of one algorithm.
   * The number of requested values is set. Use ARBITRARY_NUMBER_OF_VALUES to request arbitrary
   * number of values.
   *
   * @param identifier     the specification's identifier
   * @param numberOfSettings the number of settings expected
   */
  public ConfigurationRequirement(String identifier, int numberOfSettings) {
    this.identifier = identifier;
    this.numberOfSettings = numberOfSettings;
  }

  /**
   * @return identifier
   */
  public String getIdentifier() {
    return identifier;
  }

  /**
   * @return number of settings
   */
  public int getNumberOfSettings() {
    return numberOfSettings;
  }

  /**
   * @return the specification's settings
   */
  public abstract Object[] getSettings();

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
  @GwtIncompatible("ConfigurationValues cannot be build on client side.")
  public abstract ConfigurationValue build(ConfigurationFactory factory)
      throws AlgorithmConfigurationException;

  /**
   * If a setting is set, the number of given settings has to match the expected number.
   *
   * @throws AlgorithmConfigurationException if the given number of settings does not match the expected number.
   */
  protected void checkNumberOfSettings(int number) throws AlgorithmConfigurationException {
    if (this.numberOfSettings != ConfigurationRequirement.ARBITRARY_NUMBER_OF_VALUES &&
        number != this.numberOfSettings) {
      throw new AlgorithmConfigurationException("The number of settings does not match the expected number!");
    }
  }

}
