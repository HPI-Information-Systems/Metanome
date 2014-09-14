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
 * Concrete {@link ConfigurationRequirement} for {@link de.metanome.algorithm_integration.input.RelationalInput}s.
 *
 * @author Jakob Zwiener
 * @see ConfigurationRequirement
 */
public class ConfigurationRequirementRelationalInput extends ConfigurationRequirement {

  ConfigurationSettingRelationalInput[] settings;

  /**
   * Exists for GWT serialization.
   */
  public ConfigurationRequirementRelationalInput() {
  }

  /**
   * Construct a {@link ConfigurationRequirementRelationalInput}, requesting 1 value.
   *
   * @param identifier the specification's identifier
   */
  public ConfigurationRequirementRelationalInput(String identifier) {
    super(identifier);
  }

  /**
   * Constructs a {@link ConfigurationRequirementRelationalInput}, potentially requesting several
   * values.
   *
   * @param identifier     the specification's identifier
   * @param numberOfValues the number of values expected
   */
  public ConfigurationRequirementRelationalInput(
      String identifier, int numberOfValues) {
    super(identifier, numberOfValues);
  }


  @Override
  public ConfigurationSettingRelationalInput[] getSettings() {
    return settings;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ConfigurationValue build(ConfigurationFactory factory)
      throws AlgorithmConfigurationException {
    return factory.build(this);
  }

  /**
   * Sets the actual values on the specification if the number of settings is correct.
   *
   * @param values the values
   * @return true if the correct number of settings have been set
   */
  public boolean setSettings(ConfigurationSettingRelationalInput... values) {
    // FIXME an Exception should be thrown
    if (values.length != getNumberOfValues()) {
      return false;
    }

    settings = values;

    return true;
  }
}
