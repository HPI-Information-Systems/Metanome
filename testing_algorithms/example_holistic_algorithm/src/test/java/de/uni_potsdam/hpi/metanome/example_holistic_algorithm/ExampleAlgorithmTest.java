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

package de.uni_potsdam.hpi.metanome.example_holistic_algorithm;

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationString;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.FunctionalDependencyResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.UniqueColumnCombinationResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.FunctionalDependency;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.UniqueColumnCombination;

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

public class ExampleAlgorithmTest {

  protected ExampleAlgorithm algorithm;
  protected String pathIdentifier;

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
    algorithm = new ExampleAlgorithm();
    pathIdentifier = "pathToOutputFile";
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception {

  }

  /**
   * Test method for {@link ExampleAlgorithm#getConfigurationRequirements()} <p/> The algorithm
   * should return one configuration specification of string type
   */
  @Test
  public void testGetConfigurationRequirements() {
    // Execute functionality
    List<ConfigurationSpecification>
        actualConfigurationRequirements =
        this.algorithm.getConfigurationRequirements();

    // Check result
    assertEquals(1, actualConfigurationRequirements.size());
    assertThat(actualConfigurationRequirements.get(0),
               instanceOf(ConfigurationSpecificationString.class));
  }

  /**
   * Test method for {@link ExampleAlgorithm#setStringConfigurationValue(String, String...)} <p/>
   * The algorithm should store the path when it is supplied through setConfigurationValue.
   */
  @Test
  public void testSetConfigurationValue() throws AlgorithmConfigurationException {
    // Setup
    // Expected values
    String expectedConfigurationValue = "test";

    // Execute functionality
    this.algorithm.setStringConfigurationValue(pathIdentifier, expectedConfigurationValue);

    // Check result
    assertEquals(expectedConfigurationValue, this.algorithm.path);
  }

  /**
   * Test method for {@link ExampleAlgorithm#execute()} <p/> When the algorithm is started after
   * configuration a result should be received.
   */
  @Test
  public void testExecute() throws CouldNotReceiveResultException, AlgorithmConfigurationException {
    // Setup
    FunctionalDependencyResultReceiver
        fdResultReceiver =
        mock(FunctionalDependencyResultReceiver.class);
    UniqueColumnCombinationResultReceiver
        uccResultReceiver =
        mock(UniqueColumnCombinationResultReceiver.class);
    this.algorithm.setStringConfigurationValue(pathIdentifier, "something");

    // Execute functionality
    this.algorithm.setResultReceiver(fdResultReceiver);
    this.algorithm.setResultReceiver(uccResultReceiver);
    this.algorithm.execute();

    // Check result
    verify(fdResultReceiver).receiveResult(isA(FunctionalDependency.class));
    verify(uccResultReceiver).receiveResult(isA(UniqueColumnCombination.class));
  }
}
