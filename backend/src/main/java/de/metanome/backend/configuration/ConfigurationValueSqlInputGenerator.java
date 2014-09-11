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
import de.metanome.algorithm_integration.algorithm_types.SqlInputParameterAlgorithm;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementDatabaseConnection;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingDatabaseConnection;
import de.metanome.algorithm_integration.configuration.ConfigurationValue;
import de.metanome.algorithm_integration.input.DatabaseConnectionGenerator;
import de.metanome.backend.input.sql.SqlIteratorGenerator;

import java.util.Set;

/**
 * Represents sql input configuration values for {@link Algorithm}s.
 *
 * @author Jakob Zwiener
 */
public class ConfigurationValueSqlInputGenerator implements ConfigurationValue {

  protected final String identifier;
  protected final DatabaseConnectionGenerator[] values;

  /**
   * Constructs a ConfigurationValueSqlInputGenerator using the specification's identifier and a
   * SqlInputGenerator as value.
   */
  public ConfigurationValueSqlInputGenerator(String identifier,
                                             DatabaseConnectionGenerator... values) {
    this.identifier = identifier;
    this.values = values;
  }

  public ConfigurationValueSqlInputGenerator(
      ConfigurationRequirementDatabaseConnection configurationRequirement)
      throws AlgorithmConfigurationException {
    this.identifier = configurationRequirement.getIdentifier();
    this.values = convertToValues(configurationRequirement);
  }

  /**
   * Converts a {@link de.metanome.algorithm_integration.configuration.ConfigurationRequirementDatabaseConnection}
   * to a {@link de.metanome.algorithm_integration.input.DatabaseConnectionGenerator}.
   *
   * @param configurationRequirement the sql iterator specification
   * @return the created sql input generator
   */
  protected DatabaseConnectionGenerator[] convertToValues(
      ConfigurationRequirementDatabaseConnection configurationRequirement)
      throws AlgorithmConfigurationException {
    ConfigurationSettingDatabaseConnection[] settings = configurationRequirement.getSettings();
    DatabaseConnectionGenerator[] newValues = new DatabaseConnectionGenerator[settings.length];

    for (int i = 0; i < settings.length; i++) {
      newValues[i] = new SqlIteratorGenerator(settings[i]);
    }

    return newValues;
  }

  @Override
  public void triggerSetValue(Algorithm algorithm, Set<Class<?>> algorithmInterfaces)
      throws AlgorithmConfigurationException {
    if (!algorithmInterfaces.contains(SqlInputParameterAlgorithm.class)) {
      throw new AlgorithmConfigurationException(
          "Algorithm does not accept sql input configuration values.");
    }

    SqlInputParameterAlgorithm sqlInputParameterAlgorithm = (SqlInputParameterAlgorithm) algorithm;
    sqlInputParameterAlgorithm.setSqlInputConfigurationValue(identifier, values);
  }
}
