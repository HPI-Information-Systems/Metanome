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

package de.uni_potsdam.hpi.metanome.algorithm_integration.configuration;

/**
 * Concrete {@link de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification}
 * for {@link de.uni_potsdam.hpi.metanome.algorithm_integration.input.RelationalInput}s.
 *
 * @author Jakob Zwiener
 * @see ConfigurationSpecification
 */
public class ConfigurationSpecificationRelationalInput extends ConfigurationSpecification {

  ConfigurationSettingRelationalInput[] settings;

  /**
   * Exists for GWT serialization.
   */
  public ConfigurationSpecificationRelationalInput() {
  }

  /**
   * Construct a {@link de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationRelationalInput},
   * requesting 1 value.
   *
   * @param identifier the specification's identifier
   */
  public ConfigurationSpecificationRelationalInput(String identifier) {
    super(identifier);
  }

  /**
   * Constructs a {@link de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationRelationalInput},
   * potentially requesting several values.
   *
   * @param identifier     the specification's identifier
   * @param numberOfValues the number of values expected
   */
  public ConfigurationSpecificationRelationalInput(
      String identifier, int numberOfValues) {
    super(identifier, numberOfValues);
  }


  @Override
  public ConfigurationSettingRelationalInput[] getSettings() {
    return settings;
  }

  /**
   * Sets the actual values on the specification if the number of settings is correct.
   *
   * @param values the values
   * @return true if the correct number of settings have been set
   */
  public boolean setSettings(ConfigurationSettingRelationalInput... values) {
    if (values.length != getNumberOfValues()) {
      return false;
    }

    settings = values;

    return true;
  }
}
