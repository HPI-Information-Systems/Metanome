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

/**
 * Concrete {@link ConfigurationRequirement} sql iterator.
 *
 * @author Jakob Zwiener
 * @see ConfigurationRequirement
 */
public class ConfigurationRequirementSqlIterator extends ConfigurationRequirement {

  private static final long serialVersionUID = 6601202469601881851L;

  private ConfigurationSettingSqlIterator[] settings;

  /**
   * Exists for GWT serialization.
   */
  public ConfigurationRequirementSqlIterator() {
  }

  /**
   * Construct a {@link ConfigurationRequirementSqlIterator}, requesting 1 value.
   *
   * @param identifier the specification's identifier
   */
  public ConfigurationRequirementSqlIterator(String identifier) {
    super(identifier);
  }

  /**
   * Construcats a {@link ConfigurationRequirementSqlIterator}, potentially requesting several
   * values.
   *
   * @param identifier     the specification's identifier
   * @param numberOfValues the number of values expected
   */
  public ConfigurationRequirementSqlIterator(String identifier,
                                             int numberOfValues) {

    super(identifier, numberOfValues);
  }

  @Override
  public ConfigurationSettingSqlIterator[] getSettings() {
    return settings;
  }

  public void setSettings(ConfigurationSettingSqlIterator... settings) {
    this.settings = settings;
  }
}
