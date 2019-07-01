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

import de.metanome.algorithm_integration.Algorithm;
import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.algorithm_types.DatabaseConnectionParameterAlgorithm;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementDatabaseConnection;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingDatabaseConnection;
import de.metanome.algorithm_integration.input.DatabaseConnectionGenerator;
import de.metanome.backend.input.database.DefaultDatabaseConnectionGenerator;
import de.metanome.backend.results_db.AlgorithmType;

import java.io.FileNotFoundException;
import java.util.Set;

/**
 * Represents database connection configuration values for {@link Algorithm}s.
 *
 * @author Jakob Zwiener
 */
public class ConfigurationValueDatabaseConnectionGenerator
  extends
  ConfigurationValue<DatabaseConnectionGenerator, ConfigurationRequirementDatabaseConnection> {

  protected ConfigurationValueDatabaseConnectionGenerator() {
  }

  public ConfigurationValueDatabaseConnectionGenerator(String identifier,
                                                       DatabaseConnectionGenerator... values) {
    super(identifier, values);
  }

  public ConfigurationValueDatabaseConnectionGenerator(
    ConfigurationRequirementDatabaseConnection requirement)
    throws AlgorithmConfigurationException, FileNotFoundException {
    super(requirement);
  }

  @Override
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
    if (!algorithmInterfaces.contains(AlgorithmType.DB_CONNECTION.getAlgorithmClass())) {
      throw new AlgorithmConfigurationException(
        "Algorithm does not accept database connection input configuration values.");
    }

    DatabaseConnectionParameterAlgorithm
      databaseConnectionParameterAlgorithm = (DatabaseConnectionParameterAlgorithm) algorithm;
    databaseConnectionParameterAlgorithm.setDatabaseConnectionGeneratorConfigurationValue(
      this.identifier, this.values);
  }
}
