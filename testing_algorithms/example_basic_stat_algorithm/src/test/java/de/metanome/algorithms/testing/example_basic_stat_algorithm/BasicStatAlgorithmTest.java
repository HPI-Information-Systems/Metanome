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
package de.metanome.algorithms.testing.example_basic_stat_algorithm;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.AlgorithmExecutionException;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementFileInput;
import de.metanome.algorithm_integration.input.FileInputGenerator;
import de.metanome.algorithm_integration.result_receiver.BasicStatisticsResultReceiver;
import de.metanome.algorithm_integration.results.BasicStatistic;
import de.metanome.algorithm_integration.results.basic_statistic_values.BasicStatisticValueDouble;
import de.metanome.algorithm_integration.results.basic_statistic_values.BasicStatisticValueInteger;
import de.metanome.algorithm_integration.results.basic_statistic_values.BasicStatisticValueIntegerList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.isA;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link de.metanome.algorithms.testing.example_basic_stat_algorithm.BasicStatAlgorithm}
 *
 * @author Jakob Zwiener
 */
public class BasicStatAlgorithmTest {

  protected BasicStatAlgorithm algorithm;

  @Before
  public void setUp() throws Exception {
    algorithm = new BasicStatAlgorithm();
  }

  @After
  public void tearDown() throws Exception {
  }

  /**
   * Test method for {@link BasicStatAlgorithm#getConfigurationRequirements()} <p/> The algorithm
   * should request a file input file.
   */
  @Test
  public void testGetConfigurationRequirements() {
    // Execute functionality
    List<ConfigurationRequirement<?>> actualConfiguration = algorithm.getConfigurationRequirements();

    // Check result
    assertThat(actualConfiguration, hasItem(isA(ConfigurationRequirementFileInput.class)));
  }

  /**
   * Test method for {@link BasicStatAlgorithm#execute()} <p/> Execution should fail if no {@link
   * de.metanome.algorithm_integration.result_receiver.BasicStatisticsResultReceiver} has been set
   * on the algorithm.
   */
  @Test
  public void testExecuteNoResultReceiver() throws AlgorithmConfigurationException {
    // Setup
    FileInputGenerator[] inputs = new FileInputGenerator[BasicStatAlgorithm.NUMBER_OF_INPUT_FILES];
    for (int i = 0; i < BasicStatAlgorithm.NUMBER_OF_INPUT_FILES; i++) {
      inputs[i] = mock(FileInputGenerator.class);
    }
    algorithm.setFileInputConfigurationValue(BasicStatAlgorithm.INPUT_FILE_IDENTIFIER, inputs);

    // Execute functionality
    // Check result
    try {
      algorithm.execute();
      fail("No exception was thrown.");
    } catch (AlgorithmExecutionException e) {
      // Intentionally left blank
    }
  }

  /**
   * Test method for {@link BasicStatAlgorithm#execute()} <p/> Execution should fail if no {@link
   * de.metanome.algorithm_integration.input.FileInputGenerator} has been set on the algorithm.
   */
  @Test
  public void testExecuteNoInputs() throws AlgorithmConfigurationException {
    // Setup
    algorithm.setResultReceiver(mock(BasicStatisticsResultReceiver.class));

    // Execute functionality
    // Check result
    try {
      algorithm.execute();
      fail("No exception was thrown.");
    } catch (AlgorithmExecutionException e) {
      // Intentionally left blank
    }
  }

  /**
   * Test method for {@link BasicStatAlgorithm#execute()} <p/> When properly configured the
   * algorithm should send a statistic to the result receiver. The statistic should contain the last
   * input's file name as value.
   */
  @Test
  public void testExecute() throws AlgorithmExecutionException {
    // Setup
    BasicStatisticsResultReceiver resultReceiver = mock(BasicStatisticsResultReceiver.class);
    algorithm.setResultReceiver(resultReceiver);
    FileInputGenerator[] inputs = new FileInputGenerator[BasicStatAlgorithm.NUMBER_OF_INPUT_FILES];
    for (int i = 0; i < BasicStatAlgorithm.NUMBER_OF_INPUT_FILES; i++) {
      inputs[i] = mock(FileInputGenerator.class);
    }
    algorithm.setFileInputConfigurationValue(BasicStatAlgorithm.INPUT_FILE_IDENTIFIER, inputs);
    // Expected values
    String expectedFileName = new File("some file name").getAbsolutePath();
    when(inputs[4].getInputFile()).thenReturn(new File(expectedFileName));

    // Execute functionality
    algorithm.execute();

    BasicStatistic result1 = new BasicStatistic(BasicStatAlgorithm.COLUMN_IDENTIFIER_1);
    result1.addStatistic(BasicStatAlgorithm.STATISTIC_NAME_MIN, new BasicStatisticValueInteger(BasicStatAlgorithm.STATISTIC_VALUE_MIN));
    result1.addStatistic(BasicStatAlgorithm.STATISTIC_NAME_MAX, new BasicStatisticValueInteger(BasicStatAlgorithm.STATISTIC_VALUE_MAX));
    result1.addStatistic(BasicStatAlgorithm.STATISTIC_NAME_AVG, new BasicStatisticValueDouble(BasicStatAlgorithm.STATISTIC_VALUE_AVG));
    List<Integer> list = new ArrayList<>();
    list.add(4);
    list.add(2);
    list.add(1);
    result1.addStatistic("DISTINCT", new BasicStatisticValueIntegerList(list));

    BasicStatistic result2 = new BasicStatistic(BasicStatAlgorithm.COLUMN_IDENTIFIER_2);
    result2.addStatistic(BasicStatAlgorithm.STATISTIC_NAME_MIN, new BasicStatisticValueInteger(BasicStatAlgorithm.STATISTIC_VALUE_MIN));
    result2.addStatistic(BasicStatAlgorithm.STATISTIC_NAME_AVG, new BasicStatisticValueDouble(BasicStatAlgorithm.STATISTIC_VALUE_AVG));

    // Check result
    verify(resultReceiver).receiveResult(result1);
    verify(resultReceiver).receiveResult(result2);
  }

  /**
   * Test method for {@link de.metanome.algorithms.testing.example_basic_stat_algorithm.BasicStatAlgorithm#setFileInputConfigurationValue(String,
   * de.metanome.algorithm_integration.input.FileInputGenerator...)} <p/> No input should be
   * settable with the wrong identifier.
   */
  @Test
  public void testSetConfigurationValueFileInputGeneratorWrongIdentifier() {
    // Execute functionality
    // Check result
    try {
      algorithm
          .setFileInputConfigurationValue("some other identifier", mock(FileInputGenerator.class));
      fail("No exception was thrown.");
    } catch (AlgorithmConfigurationException e) {
      // Intentionally left blank
    }
  }

  /**
   * Test method for {@link de.metanome.algorithms.testing.example_basic_stat_algorithm.BasicStatAlgorithm#setFileInputConfigurationValue(String,
   * de.metanome.algorithm_integration.input.FileInputGenerator...)} <p/> 5 {@link
   * de.metanome.algorithm_integration.input.FileInputGenerator}s should be set on the algorithm.
   */
  @Test
  public void testSetConfigurationValueFileInputGeneratorWrongNumber() {
    // Execute functionality
    // Check result
    try {
      algorithm.setFileInputConfigurationValue(BasicStatAlgorithm.INPUT_FILE_IDENTIFIER,
                                               mock(FileInputGenerator.class));
      fail("No exception was thrown.");
    } catch (AlgorithmConfigurationException e) {
      // Intentionally left blank
    }
  }

}
