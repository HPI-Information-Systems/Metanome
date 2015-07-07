/*
 * Copyright 2014 by the Metanome project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.metanome.backend.resources;

import de.metanome.algorithm_integration.configuration.ConfigurationValue;
import de.metanome.backend.configuration.ConfigurationValueString;
import de.metanome.backend.results_db.Input;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

public class AlgorithmExecutionResourceTest {

  AlgorithmExecutionResource algorithmExecutionResource = new AlgorithmExecutionResource();

  /**
   * Test method for {@link de.metanome.backend.resources.AlgorithmExecutionResource#executeAlgorithm(AlgorithmExecutionParams)}
   */

  @Test
  public void testExecuteAlgorithm() throws Exception {
    //Todo:test
  }

  /**
   * Test method for {@link de.metanome.backend.resources.AlgorithmExecutionResource#buildExecutionSetting(AlgorithmExecutionParams)}
   */

  @Test
  public void testBuildExecutionSetting() throws Exception {
    //Todo:test
  }

  /**
   * Test method for {@link de.metanome.backend.resources.AlgorithmExecutionResource#configurationValuesToJson(java.util.List)}
   */

  @Test
  public void testConfigurationValuesToJson() throws Exception {
    List<ConfigurationValue> configValues = new ArrayList<>();
    configValues.add(
        new ConfigurationValueString("pathToOutputFile", "path/to/file1", "path/to/file1"));

    List<String> configJsons = algorithmExecutionResource.configurationValuesToJson(configValues);

    String expectedJson =  "{\"type\":\"configurationValueString\",\"identifier\":\"pathToOutputFile\",\"values\":[\"path/to/file1\",\"path/to/file1\"]}";


    assertThat(configJsons, contains(expectedJson));

  }

  /**
   * Test method for {@link de.metanome.backend.resources.AlgorithmExecutionResource#inputsToJson(java.util.List)}
   */

  @Test
  public void testInputsToJson() throws Exception {
    List<Input> inputValues = new ArrayList<>();
    inputValues.add(new Input("myFile"));

    List<String> inputsJson = algorithmExecutionResource.inputsToJson(inputValues);

    String expectedJson = "{\"type\":\"Input\",\"id\":0,\"name\":\"myFile\",\"identifier\":\"0\"}";

    assertThat(inputsJson, contains(expectedJson));


  }
}