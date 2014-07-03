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
 * Concrete {@link ConfigurationSpecification} sql iterator.
 *
 * @author Jakob Zwiener
 * @see ConfigurationSpecification
 */
public class ConfigurationSpecificationSqlIterator extends ConfigurationSpecification {

  private static final long serialVersionUID = 6601202469601881851L;

  private ConfigurationSettingSqlIterator[] settings;

  /**
   * Exists for GWT serialization.
   */
  public ConfigurationSpecificationSqlIterator() {
  }

  /**
   * Construct a {@link ConfigurationSpecificationSqlIterator}, requesting 1 value.
   *
   * @param identifier the specification's identifier
   */
  public ConfigurationSpecificationSqlIterator(String identifier) {
    super(identifier);
  }

  /**
   * Construcats a {@link ConfigurationSpecificationSqlIterator}, potentially requesting several
   * values.
   *
   * @param identifier     the specification's identifier
   * @param numberOfValues the number of values expected
   */
  public ConfigurationSpecificationSqlIterator(String identifier,
                                               int numberOfValues) {

    super(identifier, numberOfValues);
  }

  @Override
  public ConfigurationSettingSqlIterator[] getSettings() {
    return settings;
  }

  public void setValues(ConfigurationSettingSqlIterator[] values) {
    this.settings = values;
  }
}
