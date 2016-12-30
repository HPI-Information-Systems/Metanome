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

import de.metanome.algorithm_integration.AlgorithmConfigurationException;

import java.io.FileNotFoundException;

/**
 * Converts {@link ConfigurationRequirement} to {@link de.metanome.algorithm_integration.configuration.ConfigurationValue}s.
 *
 * @author Jakob Zwiener
 */
public interface ConfigurationFactory {

  /**
   * Builds a {@link de.metanome.algorithm_integration.configuration.ConfigurationValue} from
   * a@{link ConfigurationRequirementBoolean}.
   *
   * @param requirement the requirement to build
   * @return the value corresponding to the requirement
   * @throws de.metanome.algorithm_integration.AlgorithmConfigurationException if the {@link de.metanome.algorithm_integration.configuration.ConfigurationValue}
   *                                                                           cannot be correctly
   *                                                                           build from the requirement
   */
  ConfigurationValue build(ConfigurationRequirementBoolean requirement)
    throws AlgorithmConfigurationException, FileNotFoundException;

  /**
   * Builds a {@link de.metanome.algorithm_integration.configuration.ConfigurationValue} from
   * a@{link ConfigurationRequirementRelationalInput}.
   *
   * @param requirement the requirement to build
   * @return the value corresponding to the requirement
   * @throws de.metanome.algorithm_integration.AlgorithmConfigurationException if the {@link de.metanome.algorithm_integration.configuration.ConfigurationValue}
   *                                                                           cannot be correctly
   *                                                                           build from the requirement
   */
  ConfigurationValue build(ConfigurationRequirementRelationalInput requirement)
    throws AlgorithmConfigurationException;

  /**
   * Builds a {@link de.metanome.algorithm_integration.configuration.ConfigurationValue} from
   * a@{link ConfigurationRequirementDatabaseConnection}.
   *
   * @param requirement the requirement to build
   * @return the value corresponding to the requirement
   * @throws de.metanome.algorithm_integration.AlgorithmConfigurationException if the {@link de.metanome.algorithm_integration.configuration.ConfigurationValue}
   *                                                                           cannot be correctly
   *                                                                           build from the requirement
   */
  ConfigurationValue build(ConfigurationRequirementDatabaseConnection requirement)
    throws AlgorithmConfigurationException, FileNotFoundException;

  /**
   * Builds a {@link de.metanome.algorithm_integration.configuration.ConfigurationValue} from
   * a@{link ConfigurationRequirementFileInput}.
   *
   * @param requirement the requirement to build
   * @return the value corresponding to the requirement
   * @throws de.metanome.algorithm_integration.AlgorithmConfigurationException if the {@link de.metanome.algorithm_integration.configuration.ConfigurationValue}
   *                                                                           cannot be correctly
   *                                                                           build from the requirement
   */
  ConfigurationValue build(ConfigurationRequirementFileInput requirement)
    throws AlgorithmConfigurationException, FileNotFoundException;

  /**
   * Builds a {@link de.metanome.algorithm_integration.configuration.ConfigurationValue} from
   * a@{link ConfigurationRequirementInteger}.
   *
   * @param requirement the requirement to build
   * @return the value corresponding to the requirement
   * @throws de.metanome.algorithm_integration.AlgorithmConfigurationException if the {@link de.metanome.algorithm_integration.configuration.ConfigurationValue}
   *                                                                           cannot be correctly
   *                                                                           build from the requirement
   */
  ConfigurationValue build(ConfigurationRequirementInteger requirement)
    throws AlgorithmConfigurationException, FileNotFoundException;

  /**
   * Builds a {@link de.metanome.algorithm_integration.configuration.ConfigurationValue} from
   * a@{link ConfigurationRequirementListBox}.
   *
   * @param requirement the requirement to build
   * @return the value corresponding to the requirement
   * @throws de.metanome.algorithm_integration.AlgorithmConfigurationException if the {@link de.metanome.algorithm_integration.configuration.ConfigurationValue}
   *                                                                           cannot be correctly
   *                                                                           build from the requirement
   */
  ConfigurationValue build(ConfigurationRequirementListBox requirement)
    throws AlgorithmConfigurationException, FileNotFoundException;

  /**
   * Builds a {@link de.metanome.algorithm_integration.configuration.ConfigurationValue} from
   * a@{link ConfigurationRequirementCheckBox}.
   *
   * @param requirement the requirement to build
   * @return the value corresponding to the requirement
   * @throws de.metanome.algorithm_integration.AlgorithmConfigurationException if the {@link de.metanome.algorithm_integration.configuration.ConfigurationValue}
   *                                                                           cannot be correctly
   *                                                                           build from the requirement
   */
  ConfigurationValue build(ConfigurationRequirementCheckBox requirement)
          throws AlgorithmConfigurationException, FileNotFoundException;

  /**
   * Builds a {@link de.metanome.algorithm_integration.configuration.ConfigurationValue} from
   * a@{link ConfigurationRequirementString}.
   *
   * @param requirement the requirement to build
   * @return the value corresponding to the requirement
   * @throws de.metanome.algorithm_integration.AlgorithmConfigurationException if the {@link de.metanome.algorithm_integration.configuration.ConfigurationValue}
   *                                                                           cannot be correctly
   *                                                                           build from the requirement
   */

  ConfigurationValue build(ConfigurationRequirementString requirement)
    throws AlgorithmConfigurationException, FileNotFoundException;

  /**
   * Builds a {@link de.metanome.algorithm_integration.configuration.ConfigurationValue} from
   * a@{link ConfigurationRequirementTableInput}.
   *
   * @param requirement the requirement to build
   * @return the value corresponding to the requirement
   * @throws de.metanome.algorithm_integration.AlgorithmConfigurationException if the {@link de.metanome.algorithm_integration.configuration.ConfigurationValue}
   *                                                                           cannot be correctly
   *                                                                           build from the requirement
   */
  ConfigurationValue build(ConfigurationRequirementTableInput requirement)
    throws AlgorithmConfigurationException, FileNotFoundException;
}
