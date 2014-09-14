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


import de.metanome.algorithm_integration.AlgorithmConfigurationException;

/**
 * Concrete {@link ConfigurationRequirement} for integers.
 *
 * @author Tanja Bergmann
 * @see ConfigurationRequirement
 */
public class ConfigurationRequirementInteger extends ConfigurationRequirement {

  private ConfigurationSettingInteger[] settings;

  /**
   * Exists for GWT serialization.
   */
  public ConfigurationRequirementInteger() {
  }

  /**
   * Construct a ConfigurationSpecificationInteger, requesting 1 value.
   *
   * @param identifier the specification's identifier
   */
  public ConfigurationRequirementInteger(String identifier) {
    super(identifier);
  }


  /**
   * Constructs a {@link ConfigurationRequirementInteger}, potentially requesting several values.
   *
   * @param identifier     the specification's identifier
   * @param numberOfValues the number of values expected
   */
  public ConfigurationRequirementInteger(String identifier,
                                         int numberOfValues) {

    super(identifier, numberOfValues);
  }

  @Override
  public ConfigurationSettingInteger[] getSettings() {
    return this.settings;
  }

  public void setSettings(ConfigurationSettingInteger... settings) {
    this.settings = settings;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ConfigurationValue build(ConfigurationFactory factory)
      throws AlgorithmConfigurationException {
    return factory.build(this);
  }
}
