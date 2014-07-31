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

package de.uni_potsdam.hpi.metanome.example_ind_algorithm;

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmExecutionException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_execution.FileGenerator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.FileInputGenerator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.InclusionDependencyResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.InclusionDependency;

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
 * Test for {@link ExampleAlgorithm}
 *
 * @author Jakob Zwiener
 */
public class ExampleAlgorithmTest {

  protected ExampleAlgorithm algorithm;
  protected String fileInputIdentifier = "input file";

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
    List<ConfigurationSpecification>
        actualConfigurationRequirements =
        this.algorithm.getConfigurationRequirements();

    // Check result
    assertEquals(3, actualConfigurationRequirements.size());
  }

  /**
   * Test method for {@link ExampleAlgorithm#setStringConfigurationValue(String, String...) @link
   * ExampleAlorithm#setIntegerConfigurationValue(String, Integer...)} Test method for {@link
   * ExampleAlgorithm#setIntegerConfigurationValue(String, int...)} <p/> The algorithm should store
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
  public void testStart() throws AlgorithmExecutionException, UnsupportedEncodingException {
    // Setup
    InclusionDependencyResultReceiver
        resultReceiver =
        mock(InclusionDependencyResultReceiver.class);
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
    verify(resultReceiver).receiveResult(isA(InclusionDependency.class));
    verify(fileGenerator).getTemporaryFile();

    // Cleanup
    tempFile.delete();
  }
}
