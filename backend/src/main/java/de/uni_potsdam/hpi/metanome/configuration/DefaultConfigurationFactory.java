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

package de.uni_potsdam.hpi.metanome.configuration;

import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationFactory;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationBoolean;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationCsvFile;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationInteger;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationListBox;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationSqlIterator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationString;

/**
 * TODO docs
 *
 * @author Jakob Zwiener
 */
public class DefaultConfigurationFactory extends ConfigurationFactory {

  /**
   * TODO docs
   */
  public ConfigurationValueBoolean build(ConfigurationSpecificationBoolean specification) {
    return new ConfigurationValueBoolean(specification);
  }

  /**
   * TODO docs
   */
  public ConfigurationValueFileInputGenerator build(
      ConfigurationSpecificationCsvFile specification) {
    return new ConfigurationValueFileInputGenerator(specification);
  }

  /**
   * TODO docs
   */
  public ConfigurationValueInteger build(ConfigurationSpecificationInteger specification) {
    return new ConfigurationValueInteger(specification);
  }

  /**
   * TODO docs
   */
  public ConfigurationValueListBox build(ConfigurationSpecificationListBox specification) {
    return new ConfigurationValueListBox(specification);
  }

  /**
   * TODO docs
   */
  public ConfigurationValueRelationalInputGenerator build(
      ConfigurationSpecificationCsvFile specification) {
    return new ConfigurationValueRelationalInputGenerator(specification);
  }

  /**
   * TODO docs
   */
  public ConfigurationValueRelationalInputGenerator build(
      ConfigurationSpecificationSqlIterator specification) {
    return new ConfigurationValueRelationalInputGenerator(specification);
  }

  // TODO add ConfigurationValueDatabaseConnection

  /**
   * TODO docs
   */
  public ConfigurationValueString build(ConfigurationSpecificationString specification) {
    return new ConfigurationValueString(specification);
  }

}
