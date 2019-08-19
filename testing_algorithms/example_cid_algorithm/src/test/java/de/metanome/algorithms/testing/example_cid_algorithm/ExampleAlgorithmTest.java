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
package de.metanome.algorithms.testing.example_cid_algorithm;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.AlgorithmExecutionException;
import de.metanome.algorithm_integration.algorithm_execution.FileGenerator;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementString;
import de.metanome.algorithm_integration.input.FileInputGenerator;
import de.metanome.algorithm_integration.result_receiver.ConditionalInclusionDependencyResultReceiver;
import de.metanome.algorithm_integration.results.ConditionalInclusionDependency;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test for {@link de.metanome.algorithms.testing.example_cid_algorithm.ExampleAlgorithm}
 *
 * @author Joana Bergsiek
 */
public class ExampleAlgorithmTest {

  protected ExampleAlgorithm algorithm;
  protected String fileInputIdentifier = ExampleAlgorithm.CSV_FILE_IDENTIFIER;

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
    algorithm = new ExampleAlgorithm();
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
    List<ConfigurationRequirement<?>>
        actualConfigurationRequirements =
        this.algorithm.getConfigurationRequirements();

    // Check result
    assertEquals(3, actualConfigurationRequirements.size());
    assertEquals("test", ((ConfigurationRequirementString) actualConfigurationRequirements.get(1))
        .getDefaultValue(0));
  }

  /**
   * Test method for {@link ExampleAlgorithm#setStringConfigurationValue(String, String...) @link
   * ExampleAlorithm#setIntegerConfigurationValue(String, Integer...)} Test method for {@link
   * ExampleAlgorithm#setIntegerConfigurationValue(String, Integer...)} <p/> The algorithm should store
   * the path when it is supplied through setConfigurationValue.
   */
  @Test
  public void testSetConfigurationValue() throws AlgorithmConfigurationException {
    // Setup
    // Expected values
    String expectedConfigurationValue1 = "test";
    int expectedConfigurationValue2 = 7;

    // Execute functionality
    this.algorithm.setStringConfigurationValue(ExampleAlgorithm.STRING_IDENTIFIER,
                                               expectedConfigurationValue1);
    this.algorithm.setIntegerConfigurationValue(ExampleAlgorithm.INTEGER_IDENTIFIER,
                                                expectedConfigurationValue2);

    // Check result
    assertEquals(expectedConfigurationValue1, this.algorithm.tableName);
    assertEquals(expectedConfigurationValue2, this.algorithm.numberOfTables);

    try {
      this.algorithm.setIntegerConfigurationValue("someIdentifier", expectedConfigurationValue2);
      fail("Exception should have been thrown.");
    } catch (AlgorithmConfigurationException e) {
      // Intentionally left blank
    }
  }

  /**
   * When the algorithm is started after configuration a result should be received.
   */
  @Test
  public void testStart()
      throws AlgorithmExecutionException, UnsupportedEncodingException {
    // Setup
    ConditionalInclusionDependencyResultReceiver
        resultReceiver =
        mock(ConditionalInclusionDependencyResultReceiver.class);
    File tempFile = new File("testFile");
    tempFile.deleteOnExit();
    FileGenerator fileGenerator = mock(FileGenerator.class);
    when(fileGenerator.getTemporaryFile())
        .thenReturn(tempFile);
    this.algorithm.setStringConfigurationValue(ExampleAlgorithm.STRING_IDENTIFIER, "something");
    this.algorithm.setIntegerConfigurationValue(ExampleAlgorithm.INTEGER_IDENTIFIER, 7);
    this.algorithm
        .setFileInputConfigurationValue(fileInputIdentifier, mock(FileInputGenerator.class));

    // Execute functionality
    this.algorithm.setResultReceiver(resultReceiver);
    this.algorithm.setTempFileGenerator(fileGenerator);
    this.algorithm.execute();

    // Check result
    verify(resultReceiver).receiveResult(isA(ConditionalInclusionDependency.class));
    verify(fileGenerator).getTemporaryFile();

    // Cleanup
    tempFile.delete();
  }
}