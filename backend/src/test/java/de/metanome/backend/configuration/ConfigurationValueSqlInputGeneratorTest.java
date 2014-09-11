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

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.algorithm_types.SqlInputParameterAlgorithm;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementDatabaseConnection;
import de.metanome.algorithm_integration.input.DatabaseConnectionGenerator;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link de.metanome.backend.configuration.ConfigurationValueSqlInputGenerator}
 *
 * @author Jakob Zwiener
 */
public class ConfigurationValueSqlInputGeneratorTest {

  /**
   * Test method for {@link de.metanome.backend.configuration.ConfigurationValueSqlInputGenerator#triggerSetValue(de.metanome.algorithm_integration.Algorithm,
   * java.util.Set)} <p/> Parameters should be set on the algorithm through triggerSetValue. This is
   * the last call in a double dispatch call to determine the parameters type.
   */
  @Test
  public void testTriggerSetValue() throws AlgorithmConfigurationException {
    // Setup
    SqlInputParameterAlgorithm algorithm = mock(SqlInputParameterAlgorithm.class);
    Set<Class<?>> interfaces = new HashSet<>();
    interfaces.add(SqlInputParameterAlgorithm.class);
    // Expected values
    String expectedIdentifier = "configId1";
    DatabaseConnectionGenerator[]
        expectedConfigurationValue =
        {mock(DatabaseConnectionGenerator.class), mock(DatabaseConnectionGenerator.class)};

    // Execute functionality
    ConfigurationValueSqlInputGenerator configValue = new ConfigurationValueSqlInputGenerator(
        new ConfigurationRequirementDatabaseConnection(expectedIdentifier).getIdentifier(),
        expectedConfigurationValue);
    configValue.triggerSetValue(algorithm, interfaces);

    // Check result
    verify(algorithm).setSqlInputConfigurationValue(expectedIdentifier, expectedConfigurationValue);
  }
}