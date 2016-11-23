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
package de.metanome.algorithms.testing.example_od_algorithm;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.AlgorithmExecutionException;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementString;
import de.metanome.algorithm_integration.result_receiver.OrderDependencyResultReceiver;
import de.metanome.algorithm_integration.results.OrderDependency;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Test for {@link de.metanome.algorithms.testing.example_od_algorithm}
 */
public class ExampleAlgorithmTest {

  protected ExampleAlgorithm algorithm;
  protected String pathIdentifier;

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
    algorithm = new ExampleAlgorithm();
    pathIdentifier = "pathToFile";
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception {

  }

  /**
   * Test method for {@link ExampleAlgorithm#execute()} <p/> When the algorithm is started after
   * configuration a result should be received.
   */
  @Test
  public void testExecute() throws AlgorithmExecutionException {
    // Setup
    final OrderDependencyResultReceiver resultReceiver = mock(OrderDependencyResultReceiver.class);
    final String configurationValue = "someFilePath";
    this.algorithm.setStringConfigurationValue(pathIdentifier, configurationValue);

    // Execute functionality
    this.algorithm.setResultReceiver(resultReceiver);
    this.algorithm.execute();

    // Check result
    verify(resultReceiver).receiveResult(isA(OrderDependency.class));
  }

  /**
   * Test method for {@link ExampleAlgorithm#getConfigurationRequirements()} <p/> The algorithm
   * should return one configuration specification of string type
   */
  @Test
  public void testGetConfigurationRequirements() {
    // Execute functionality
    final List<ConfigurationRequirement<?>> actualConfigurationRequirements =
        this.algorithm.getConfigurationRequirements();

    // Check result
    assertEquals(1, actualConfigurationRequirements.size());
    assertThat(actualConfigurationRequirements.get(0),
               instanceOf(ConfigurationRequirementString.class));
  }

  /**
   * Test method for {@link ExampleAlgorithm#setStringConfigurationValue(String, String...)} <p/>
   * The algorithm should store the path when it is supplied through setConfigurationValue.
   */
  @Test
  public void testSetConfigurationValue() throws AlgorithmConfigurationException {
    // Setup
    // Expected values
    final String expectedConfigurationValue = "something";

    // Execute functionality
    this.algorithm.setStringConfigurationValue(pathIdentifier, expectedConfigurationValue);

    // Check result
    assertEquals(expectedConfigurationValue, this.algorithm.fileName);
  }
}
