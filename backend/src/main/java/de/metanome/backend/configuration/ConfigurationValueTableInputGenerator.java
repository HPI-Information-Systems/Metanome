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

package de.metanome.backend.configuration;

import de.metanome.algorithm_integration.Algorithm;
import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.algorithm_types.TableInputParameterAlgorithm;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementTableInput;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingTableInput;
import de.metanome.algorithm_integration.configuration.ConfigurationValue;
import de.metanome.algorithm_integration.input.TableInputGenerator;
import de.metanome.backend.input.sql.DefaultTableInputGenerator;

import java.util.Set;

/**
 * Represents {@link de.metanome.algorithm_integration.input.TableInputGenerator} configuration
 * values for {@link Algorithm}s.
 *
 * @author Jakob Zwiener
 */
public class ConfigurationValueTableInputGenerator implements ConfigurationValue {

  protected final String identifier;
  protected final TableInputGenerator[] values;

  /**
   * Constructs a {@link ConfigurationValueRelationalInputGenerator} using the specification's
   * identifier and the {@link de.metanome.algorithm_integration.input.RelationalInputGenerator}values.
   *
   * @param identifier the configuration value's identifier
   * @param values     the values
   */
  public ConfigurationValueTableInputGenerator(String identifier,
                                               TableInputGenerator... values) {
    this.identifier = identifier;
    this.values = values;
  }

  /**
   * Constructs a {@link de.metanome.backend.configuration.ConfigurationValueRelationalInputGenerator}
   * using a {@link de.metanome.algorithm_integration.configuration.ConfigurationRequirementTableInput}.
   *
   * @param requirement the requirement to generate the {@link de.metanome.algorithm_integration.input.TableInputGenerator}s
   */
  public ConfigurationValueTableInputGenerator(ConfigurationRequirementTableInput requirement)
      throws AlgorithmConfigurationException {
    this.identifier = requirement.getIdentifier();
    this.values = convertToValues(requirement);
  }

  /**
   * Converts a {@link de.metanome.algorithm_integration.configuration.ConfigurationRequirementTableInput}
   * to a {@link de.metanome.algorithm_integration.input.TableInputGenerator}.
   *
   * @param requirement the
   * @return the created table input generator
   */
  protected TableInputGenerator[] convertToValues(ConfigurationRequirementTableInput requirement)
      throws AlgorithmConfigurationException {

    ConfigurationSettingTableInput[] settings = requirement.getSettings();
    TableInputGenerator[] newValues = new TableInputGenerator[settings.length];

    for (int i = 0; i < settings.length; i++) {
      newValues[i] = new DefaultTableInputGenerator(settings[i]);
    }

    return newValues;
  }

  @Override
  public void triggerSetValue(Algorithm algorithm, Set<Class<?>> algorithmInterfaces)
      throws AlgorithmConfigurationException {

    if (!algorithmInterfaces.contains(TableInputParameterAlgorithm.class)) {
      throw new AlgorithmConfigurationException(
          "Algorithm does not accept table input configuration values.");
    }

    TableInputParameterAlgorithm tableInputParameterAlgorithm =
        (TableInputParameterAlgorithm) algorithm;
    tableInputParameterAlgorithm.setTableInputConfigurationValue(identifier, values);
  }

}
