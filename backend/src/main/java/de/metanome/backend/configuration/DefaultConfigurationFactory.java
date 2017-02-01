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
package de.metanome.backend.configuration;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.configuration.*;
import de.metanome.backend.input.DefaultRelationalInputGeneratorInitializer;

import java.io.FileNotFoundException;

/**
 * Converts given {@link de.metanome.algorithm_integration.configuration.ConfigurationRequirement}s
 * to the {@link de.metanome.algorithm_integration.configuration.ConfigurationValue}s.
 *
 * @author Jakob Zwiener
 */
public class DefaultConfigurationFactory implements ConfigurationFactory {

  /**
   * Builds a {@link de.metanome.backend.configuration.ConfigurationValueBoolean} from a@{link
   * ConfigurationRequirementBoolean}.
   *
   * @param requirement the requirement to build
   * @return the value corresponding to the requirement
   */
  @Override
  public ConfigurationValueBoolean build(ConfigurationRequirementBoolean requirement)
    throws AlgorithmConfigurationException, FileNotFoundException {
    return new ConfigurationValueBoolean(requirement);
  }

  /**
   * Builds a {@link de.metanome.backend.configuration.ConfigurationValueInteger} from a@{link
   * ConfigurationRequirementInteger}.
   *
   * @param requirement the requirement to build
   * @return the value corresponding to the requirement
   */
  @Override
  public ConfigurationValueInteger build(ConfigurationRequirementInteger requirement)
    throws AlgorithmConfigurationException, FileNotFoundException {
    return new ConfigurationValueInteger(requirement);
  }

  /**
   * Builds a {@link de.metanome.backend.configuration.ConfigurationValueListBox} from a@{link
   * ConfigurationRequirementListBox}.
   *
   * @param requirement the requirement to build
   * @return the value corresponding to the requirement
   */
  @Override
  public ConfigurationValueListBox build(ConfigurationRequirementListBox requirement)
    throws AlgorithmConfigurationException, FileNotFoundException {
    return new ConfigurationValueListBox(requirement);
  }

  /**
   * Builds a {@link de.metanome.backend.configuration.ConfigurationValueCheckBox}
   * from a@{link ConfigurationRequirementCheckBox}.
   *
   * @param requirement the requirement to build
   * @return the value corresponding to the requirement
   * @throws de.metanome.algorithm_integration.AlgorithmConfigurationException the {@link de.metanome.algorithm_integration.input.RelationalInputGenerator}
   *                                                                           cannot be initialized
   */
  @Override
  public ConfigurationValueCheckBox build(ConfigurationRequirementCheckBox requirement)
          throws AlgorithmConfigurationException, FileNotFoundException {
    return new ConfigurationValueCheckBox(requirement);
  }

  /**
   * Builds a {@link de.metanome.backend.configuration.ConfigurationValueRelationalInputGenerator}
   * from a@{link ConfigurationRequirementRelationalInput}.
   *
   * @param requirement the requirement to build
   * @return the value corresponding to the requirement
   * @throws de.metanome.algorithm_integration.AlgorithmConfigurationException the {@link de.metanome.algorithm_integration.input.RelationalInputGenerator}
   *                                                                           cannot be initialized
   */
  @Override
  public ConfigurationValueRelationalInputGenerator build(
    ConfigurationRequirementRelationalInput requirement)
    throws AlgorithmConfigurationException {
    DefaultRelationalInputGeneratorInitializer
      inputGeneratorInitializer =
      new DefaultRelationalInputGeneratorInitializer(requirement);
    return inputGeneratorInitializer.getConfigurationValue();
  }

  /**
   * Builds a {@link de.metanome.backend.configuration.ConfigurationValueDatabaseConnectionGenerator}
   * from a@{link ConfigurationRequirementDatabaseConnection}.
   *
   * @param requirement the requirement to build
   * @return the value corresponding to the requirement
   * @throws de.metanome.algorithm_integration.AlgorithmConfigurationException when the {@link
   *                                                                           de.metanome.algorithm_integration.input.DatabaseConnectionGenerator}
   *                                                                           cannot be initialized
   */
  @Override
  public ConfigurationValueDatabaseConnectionGenerator build(
    ConfigurationRequirementDatabaseConnection requirement)
    throws AlgorithmConfigurationException, FileNotFoundException {
    return new ConfigurationValueDatabaseConnectionGenerator(requirement);
  }

  /**
   * Builds a {@link de.metanome.backend.configuration.ConfigurationValueFileInputGenerator} from
   * a@{link ConfigurationRequirementFileInput}.
   *
   * @param requirement the requirement to build
   * @return the value corresponding to the requirement
   * @throws AlgorithmConfigurationException if the underlying file cannot be found
   */
  @Override
  public ConfigurationValueFileInputGenerator build(ConfigurationRequirementFileInput requirement)
    throws AlgorithmConfigurationException, FileNotFoundException {
    return new ConfigurationValueFileInputGenerator(requirement);
  }

  /**
   * Builds a {@link de.metanome.backend.configuration.ConfigurationValueString} from a@{link
   * ConfigurationRequirementString}.
   *
   * @param requirement the requirement to build
   * @return the value corresponding to the requirement
   */
  @Override
  public ConfigurationValueString build(ConfigurationRequirementString requirement)
    throws AlgorithmConfigurationException, FileNotFoundException {
    return new ConfigurationValueString(requirement);
  }

  /**
   * Builds a {@link de.metanome.backend.configuration.ConfigurationValueTableInputGenerator} from
   * a@{link ConfigurationRequirementTableInput}.
   *
   * @param requirement the requirement to build
   * @return the value corresponding to the requirement
   * @throws AlgorithmConfigurationException when the statement fetching the table data cannot be
   *                                         executed
   */
  @Override
  public ConfigurationValueTableInputGenerator build(ConfigurationRequirementTableInput requirement)
    throws AlgorithmConfigurationException, FileNotFoundException {
    return new ConfigurationValueTableInputGenerator(requirement);
  }

}
