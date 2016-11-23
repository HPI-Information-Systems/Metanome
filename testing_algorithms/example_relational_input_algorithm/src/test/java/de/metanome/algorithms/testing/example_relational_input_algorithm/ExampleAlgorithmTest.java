/**
 * Copyright 2015-2016 by Metanome Project
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
package de.metanome.algorithms.testing.example_relational_input_algorithm;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.AlgorithmExecutionException;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementRelationalInput;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementTableInput;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.algorithm_integration.input.TableInputGenerator;
import de.metanome.algorithm_integration.result_receiver.UniqueColumnCombinationResultReceiver;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link ExampleAlgorithm}
 *
 * @author Jakob Zwiener
 */
public class ExampleAlgorithmTest {

  private ExampleAlgorithm algorithm;

  @Before
  public void setUp() {
    algorithm = new ExampleAlgorithm();
  }

  /**
   * Test method for {@link ExampleAlgorithm#getConfigurationRequirements()}
   *
   * The algorithm should require a {@link de.metanome.algorithm_integration.configuration.ConfigurationRequirementRelationalInput}
   * and a {@link de.metanome.algorithm_integration.configuration.ConfigurationRequirementTableInput}.
   */
  @Test
  public void testGetConfigurationRequirements() {
    // Execute functionality
    List<ConfigurationRequirement<?>> actualRequirements = algorithm.getConfigurationRequirements();

    // Check result
    assertThat(actualRequirements, IsIterableContainingInAnyOrder.containsInAnyOrder(instanceOf(
        ConfigurationRequirementRelationalInput.class), instanceOf(
        ConfigurationRequirementTableInput.class)));
  }

  /**
   * Test method for {@link ExampleAlgorithm#execute()}
   *
   * The algorithm should throw an {@link de.metanome.algorithm_integration.AlgorithmExecutionException}
   * if the relational input has not been set.
   */
  @Test
  public void testExecuteMissingRelationalInput() throws AlgorithmExecutionException {
    // Execute functionality
    // Check result
    try {
      algorithm.execute();
      fail("An exception should have been thrown.");
    } catch (AlgorithmExecutionException e) {
      // Intentionally left blank
    }

    algorithm.setRelationalInputConfigurationValue("some identifier",
                                                   mock(RelationalInputGenerator.class));
    // No exception should be thrown
    algorithm.execute();
  }

  /**
   * Test method for {@link ExampleAlgorithm#setTableInputConfigurationValue(String,
   * de.metanome.algorithm_integration.input.TableInputGenerator...)}
   *
   * The first {@link de.metanome.algorithm_integration.input.TableInputGenerator} in the
   * setTableInputGenerator method call should be stored.
   */
  @Test
  public void testSetTableInputConfigurationValue() throws AlgorithmConfigurationException {
    // Expected values
    TableInputGenerator expectedTableInputGenerator = mock(TableInputGenerator.class);

    // Execute functionality
    algorithm.setTableInputConfigurationValue("some identifier", expectedTableInputGenerator,
                                              mock(TableInputGenerator.class));
    TableInputGenerator actualTableInputGenerator = algorithm.tableInputGenerator;

    // Check result
    assertEquals(expectedTableInputGenerator, actualTableInputGenerator);
  }

  /**
   * Test method for {@link ExampleAlgorithm#setRelationalInputConfigurationValue(String,
   * de.metanome.algorithm_integration.input.RelationalInputGenerator...)}
   *
   * The first {@link de.metanome.algorithm_integration.input.RelationalInputGenerator} in the
   * setRelationalInputGenerator method call should be stored.
   */
  @Test
  public void testSetRelationalInputConfigurationValue() throws AlgorithmConfigurationException {
    // Expected values
    RelationalInputGenerator
        expectedRelationalInputGenerator =
        mock(RelationalInputGenerator.class);

    // Execute functionality
    algorithm
        .setRelationalInputConfigurationValue("some identifier", expectedRelationalInputGenerator,
                                              mock(RelationalInputGenerator.class));
    RelationalInputGenerator actualRelationalInputGenerator = algorithm.relationalInputGenerator;

    // Check result
    assertEquals(expectedRelationalInputGenerator, actualRelationalInputGenerator);
  }

  /**
   * Test method for {@link ExampleAlgorithm#setResultReceiver(de.metanome.algorithm_integration.result_receiver.UniqueColumnCombinationResultReceiver)}
   *
   * The result receiver should be stored.
   */
  @Test
  public void testSetResultReceiver() {
    // Expected values
    UniqueColumnCombinationResultReceiver
        expectedResultReceiver =
        mock(UniqueColumnCombinationResultReceiver.class);

    // Execute functionality
    algorithm.setResultReceiver(expectedResultReceiver);
    UniqueColumnCombinationResultReceiver actualResultReceiver = algorithm.resultReceiver;

    // Check result
    assertEquals(expectedResultReceiver, actualResultReceiver);
  }
}
