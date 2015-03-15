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
import de.metanome.algorithm_integration.algorithm_types.DatabaseConnectionParameterAlgorithm;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementDatabaseConnection;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingDatabaseConnection;
import de.metanome.algorithm_integration.configuration.ConfigurationValue;
import de.metanome.algorithm_integration.input.DatabaseConnectionGenerator;
import de.metanome.backend.input.database.DefaultDatabaseConnectionGenerator;

import java.util.Set;

/**
 * Represents database input configuration values for {@link Algorithm}s.
 *
 * @author Jakob Zwiener
 */
public class ConfigurationValueDatabaseConnectionGenerator implements ConfigurationValue {

  protected final String identifier;
  protected final DatabaseConnectionGenerator[] values;

  /**
   * Constructs a ConfigurationValueDatabaseConnectionGenerator using the specification's identifier
   * and a DatabaseConnectionGenerator as value.
   */
  public ConfigurationValueDatabaseConnectionGenerator(String identifier,
                                                       DatabaseConnectionGenerator... values) {
    this.identifier = identifier;
    this.values = values;
  }

  /**
   * Constructs a {@link de.metanome.backend.configuration.ConfigurationValueDatabaseConnectionGenerator}
   * using a {@link ConfigurationRequirementDatabaseConnection}.
   *
   * @param requirement the requirement to generate the {@link de.metanome.algorithm_integration.input.DatabaseConnectionGenerator}s
   * @throws AlgorithmConfigurationException thrown if the requirement cannot be converted to
   *                                         values
   */
  public ConfigurationValueDatabaseConnectionGenerator(
      ConfigurationRequirementDatabaseConnection requirement)
      throws AlgorithmConfigurationException {
    this.identifier = requirement.getIdentifier();
    this.values = convertToValues(requirement);
  }

  /**
   * Converts a {@link de.metanome.algorithm_integration.configuration.ConfigurationRequirementDatabaseConnection}
   * to a {@link de.metanome.algorithm_integration.input.DatabaseConnectionGenerator}.
   *
   * @param requirement the database connection requirement
   * @return the created database connection generator
   */
  protected DatabaseConnectionGenerator[] convertToValues(
      ConfigurationRequirementDatabaseConnection requirement)
      throws AlgorithmConfigurationException {
    ConfigurationSettingDatabaseConnection[] settings = requirement.getSettings();
    DatabaseConnectionGenerator[] newValues = new DatabaseConnectionGenerator[settings.length];

    for (int i = 0; i < settings.length; i++) {
      newValues[i] = new DefaultDatabaseConnectionGenerator(settings[i]);
    }

    return newValues;
  }

  @Override
  public void triggerSetValue(Algorithm algorithm, Set<Class<?>> algorithmInterfaces)
      throws AlgorithmConfigurationException {
    if (!algorithmInterfaces.contains(DatabaseConnectionParameterAlgorithm.class)) {
      throw new AlgorithmConfigurationException(
          "Algorithm does not accept database connection input configuration values.");
    }

    DatabaseConnectionParameterAlgorithm
        databaseConnectionParameterAlgorithm = (DatabaseConnectionParameterAlgorithm) algorithm;
    databaseConnectionParameterAlgorithm.setDatabaseConnectionGeneratorConfigurationValue(
        identifier, values);
  }
}
