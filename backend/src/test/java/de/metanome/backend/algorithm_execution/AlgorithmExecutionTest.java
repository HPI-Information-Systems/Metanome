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
package de.metanome.backend.algorithm_execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.configuration.ConfigurationValue;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.backend.configuration.ConfigurationValueString;
import de.metanome.backend.constants.Constants;
import de.metanome.backend.result_receiver.CloseableOmniscientResultReceiver;
import de.metanome.backend.result_receiver.ResultCounter;
import de.metanome.backend.results_db.ExecutionSetting;
import de.metanome.backend.results_db.FileInput;
import de.metanome.backend.results_db.Input;

public class AlgorithmExecutionTest {

  /**
   * Test method for {@link de.metanome.backend.algorithm_execution.AlgorithmExecution#buildExecutor(de.metanome.backend.results_db.ExecutionSetting, List<String>)}
   */

  @Test
  public void testBuildExecutor() throws Exception {
    //Setup
    String expectedIdentifier = "myIdentifier";
    ExecutionSetting setting = new ExecutionSetting(null, null, expectedIdentifier);
    setting.setCountResults(true);

    //execute functionality
    AlgorithmExecutor executor = AlgorithmExecution.buildExecutor(setting, null);

    //check result
    CloseableOmniscientResultReceiver receiver = executor.resultReceiver;
    assertTrue(receiver instanceof ResultCounter);
    ResultCounter resultCounter = (ResultCounter) receiver;
    assertTrue(resultCounter.getOutputFilePathPrefix().contains(expectedIdentifier));
  }

  /**
   * Test method for {@link de.metanome.backend.algorithm_execution.AlgorithmExecution#parseConfigurationValues(java.util.List)}
   */

  @Test
  public void testParseConfigurationValues() throws Exception {
    List<String> configurationJson = new ArrayList<>();
    String configurationStringJson =
      "{\"type\":\"configurationValueString\",\"identifier\":\"configId\",\"values\":[\"s1\",\"s2\"]}";
    configurationJson.add(configurationStringJson);

    ConfigurationValue
      configValue =
      AlgorithmExecution.parseConfigurationValues(configurationJson).get(0);

    assertTrue(configValue instanceof ConfigurationValueString);
    ConfigurationValueString actualConfigValue = (ConfigurationValueString) configValue;

    assertTrue(actualConfigValue.getIdentifier().equals("configId"));
    assertTrue(
      actualConfigValue.getValues()[0].equals("s1") &&
        actualConfigValue.getValues()[1].equals("s2")
    );
  }

  /**
   * Test method for {@link de.metanome.backend.algorithm_execution.AlgorithmExecution#parseInputs(java.util.List)}
   */

  @Test
  public void testParseInputs() throws Exception {
    //Setup
    List<String> inputsJson = new ArrayList<>();
    String inputJson =
      "{\"type\":\"fileInput\",\"fileName\":\"myFile\"}";
    inputsJson.add(inputJson);

    //execute functionality
    Input input = AlgorithmExecution.parseInputs(inputsJson).get(0);

    //test result
    assertTrue(input instanceof FileInput);
    assertTrue(((FileInput) input).getFileName().equals("myFile"));
  }


  @Test
  public void testExtractColumnNames() throws AlgorithmConfigurationException, InputGenerationException, FileNotFoundException, URISyntaxException {
    // Set up
    List<Input> inputs = new ArrayList<>();
    ClassLoader sysLoader = Thread.currentThread().getContextClassLoader();
    String url = sysLoader.getResource(Constants.INPUTDATA_RESOURCE_NAME).toString();
    URI uri = new URI(url);
    String pathToCsvFolder = uri.getPath();
    inputs.add(new FileInput(pathToCsvFolder + Constants.FILE_SEPARATOR + "inputC.tsv").setSeparator("\\t"));

    // Expected values
    List<ColumnIdentifier> expectedColumnNames = new ArrayList<>();
    expectedColumnNames.add(new ColumnIdentifier("inputC.tsv", "000"));
    expectedColumnNames.add(new ColumnIdentifier("inputC.tsv", "001"));
    expectedColumnNames.add(new ColumnIdentifier("inputC.tsv", "002"));
    expectedColumnNames.add(new ColumnIdentifier("inputC.tsv", "003"));
    expectedColumnNames.add(new ColumnIdentifier("inputC.tsv", "004"));
    expectedColumnNames.add(new ColumnIdentifier("inputC.tsv", "005"));
    expectedColumnNames.add(new ColumnIdentifier("inputC.tsv", "006"));
    expectedColumnNames.add(new ColumnIdentifier("inputC.tsv", "007"));
    expectedColumnNames.add(new ColumnIdentifier("inputC.tsv", "008"));
    expectedColumnNames.add(new ColumnIdentifier("inputC.tsv", "009"));
    expectedColumnNames.add(new ColumnIdentifier("inputC.tsv", "010"));
    expectedColumnNames.add(new ColumnIdentifier("inputC.tsv", "011"));
    expectedColumnNames.add(new ColumnIdentifier("inputC.tsv", "012"));
    expectedColumnNames.add(new ColumnIdentifier("inputC.tsv", "013"));
    expectedColumnNames.add(new ColumnIdentifier("inputC.tsv", "014"));

    // Execute functionality
    List<ColumnIdentifier> actualColumnNames = AlgorithmExecution.extractColumnNames(inputs);

    // Check
    assertEquals(expectedColumnNames, actualColumnNames);
  }
}
