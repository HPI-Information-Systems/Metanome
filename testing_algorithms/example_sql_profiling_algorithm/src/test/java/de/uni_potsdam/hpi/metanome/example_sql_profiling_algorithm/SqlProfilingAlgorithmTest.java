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

package de.uni_potsdam.hpi.metanome.example_sql_profiling_algorithm;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.metanome.algorithm_integration.configuration.ConfigurationSpecificationSqlIterator;
import de.metanome.algorithm_integration.input.SqlInputGenerator;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link de.uni_potsdam.hpi.metanome.example_sql_profiling_algorithm.SqlProfilingAlgorithm}
 *
 * @author Jakob Zwiener
 */
public class SqlProfilingAlgorithmTest {

  protected SqlProfilingAlgorithm algorithm;

  @Before
  public void setUp() throws Exception {
    algorithm = new SqlProfilingAlgorithm();
  }

  /**
   * Test method for {@link SqlProfilingAlgorithm#getConfigurationRequirements()}
   */
  @Test
  public void testGetConfigurationRequirements() {
    // Execute functionality
    List<ConfigurationSpecification>
        actualConfigurationRequirements =
        algorithm.getConfigurationRequirements();

    // Check result
    assertThat(actualConfigurationRequirements.get(0),
               instanceOf(ConfigurationSpecificationSqlIterator.class));
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.example_sql_profiling_algorithm.SqlProfilingAlgorithm#setSqlInputConfigurationValue(String,
   * de.metanome.algorithm_integration.input.SqlInputGenerator...)}
   */
  @Test
  public void testSetSqlInputConfigurationValue() throws AlgorithmConfigurationException {
    // Setup
    // Expected values
    SqlInputGenerator expectedInputGenerator = mock(SqlInputGenerator.class);

    // Execute functionality
    algorithm.setSqlInputConfigurationValue(SqlProfilingAlgorithm.SQL_IDENTIFIER,
                                            expectedInputGenerator);

    // Check result
    assertSame(expectedInputGenerator, algorithm.inputGenerator);
  }

  /**
   * TODO docs
   */
  @Test
  public void testExecute() throws AlgorithmConfigurationException {
    // Setup
    SqlInputGenerator inputGenerator = mock(SqlInputGenerator.class);
    algorithm.setSqlInputConfigurationValue(SqlProfilingAlgorithm.SQL_IDENTIFIER, inputGenerator);
    // Expected values
    // TODO add asserts
    // the systems property should be output from an sql connection

  }
}
