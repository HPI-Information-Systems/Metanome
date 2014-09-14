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
 * Concrete {@link ConfigurationRequirement} for table inputs.
 *
 * @see ConfigurationRequirement
 */
public class ConfigurationRequirementTableInput extends ConfigurationRequirement {

  private static final long serialVersionUID = 8842139128248338302L;

  private ConfigurationSettingTableInput[] settings;

  /**
   * Exists for GWT serialization.
   */
  public ConfigurationRequirementTableInput() {
  }

  /**
   * Constructs a {@link ConfigurationRequirementFileInput}, requesting 1 value.
   *
   * @param identifier the specification's identifier
   */
  public ConfigurationRequirementTableInput(String identifier) {
    super(identifier);
  }

  /**
   * Constructs a {@link ConfigurationRequirementFileInput}, potentially requesting several values.
   *
   * @param identifier     the specification's identifier
   * @param numberOfValues the number of values expected
   */
  public ConfigurationRequirementTableInput(String identifier,
                                           int numberOfValues) {

    super(identifier, numberOfValues);
  }

  @Override
  public ConfigurationSettingTableInput[] getSettings() {
    return this.settings;
  }

  public void setSettings(ConfigurationSettingTableInput... configurationSettings) {
    this.settings = configurationSettings;
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
