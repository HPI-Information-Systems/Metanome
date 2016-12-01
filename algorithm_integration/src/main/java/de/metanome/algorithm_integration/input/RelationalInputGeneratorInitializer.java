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
package de.metanome.algorithm_integration.input;


import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingFileInput;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingTableInput;

/**
 * Initializes {@link de.metanome.algorithm_integration.input.RelationalInputGenerator}s that are
 * based on files or database tables.
 *
 * @author Tanja Bergmann
 */
public interface RelationalInputGeneratorInitializer {

  /**
   * Initialize {@link de.metanome.algorithm_integration.input.RelationalInputGenerator} from a
   * {@link de.metanome.algorithm_integration.configuration.ConfigurationSettingFileInput}.
   *
   * @param setting the setting used to initialize the input
   * @throws AlgorithmConfigurationException if the input cannot be initialized
   */
  public void initialize(ConfigurationSettingFileInput setting)
    throws AlgorithmConfigurationException;

  /**
   * Initialize {@link de.metanome.algorithm_integration.input.RelationalInputGenerator} from a
   * {@link de.metanome.algorithm_integration.configuration.ConfigurationSettingTableInput}.
   *
   * @param setting the setting used to initialize the input
   * @throws AlgorithmConfigurationException if the input cannot be initialized
   */
  public void initialize(ConfigurationSettingTableInput setting)
    throws AlgorithmConfigurationException;

}
