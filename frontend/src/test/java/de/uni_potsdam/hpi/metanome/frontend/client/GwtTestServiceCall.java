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

package de.uni_potsdam.hpi.metanome.frontend.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingString;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationString;
import de.uni_potsdam.hpi.metanome.frontend.client.services.AlgorithmService;
import de.uni_potsdam.hpi.metanome.frontend.client.services.AlgorithmServiceAsync;
import de.uni_potsdam.hpi.metanome.frontend.client.services.ExecutionService;
import de.uni_potsdam.hpi.metanome.frontend.client.services.ExecutionServiceAsync;
import de.uni_potsdam.hpi.metanome.frontend.client.services.ParameterService;
import de.uni_potsdam.hpi.metanome.frontend.client.services.ParameterServiceAsync;
import de.uni_potsdam.hpi.metanome.results_db.Algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests RPC calls to server
 */
public class GwtTestServiceCall extends GWTTestCase {

  /**
   * tests the call from client to executionService.executeAlgorithm()
   */
  public void testExecutionService() {
    // Setup
    TestHelper.resetDatabaseSync();

    String algorithmFileName = "example_ucc_algorithm.jar";
    TestHelper.storeAlgorithmSync(new Algorithm(algorithmFileName));
    List<ConfigurationSpecification> configs = new ArrayList<>();
    ConfigurationSpecificationString inputParameter =
        new ConfigurationSpecificationString("pathToInputFile");
    inputParameter.setValues(new ConfigurationSettingString("path/to/file1"),
        new ConfigurationSettingString("path/to/file2"));
    configs.add(inputParameter);

    AsyncCallback<Long> callback = new AsyncCallback<Long>() {
      public void onFailure(Throwable caught) {
        fail();
      }

      public void onSuccess(Long executionTime) {
        assertTrue(executionTime > 0);
        finishTest();
      }
    };

    ExecutionServiceAsync executionService = GWT.create(ExecutionService.class);

    // Set a delay period
    delayTestFinish(500);

    // Execute
    executionService.executeAlgorithm(algorithmFileName, "executionIdentifier1", configs, callback);

    // Cleanup
    TestHelper.resetDatabaseSync();
  }

  /**
   * tests the call from client to parameterService.retrieveParameters
   */
  public void testParameterService() {
    // Setup
    AsyncCallback<List<ConfigurationSpecification>> callback =
        new AsyncCallback<List<ConfigurationSpecification>>() {
          public void onFailure(Throwable caught) {
            fail();
          }

          public void onSuccess(List<ConfigurationSpecification> result) {
            assertNotNull(result);
            finishTest();
          }
        };

    ParameterServiceAsync parameterService = GWT.create(ParameterService.class);

    // Set a delay period
    delayTestFinish(500);

    // Execute
    parameterService.retrieveParameters("example_ucc_algorithm.jar", callback);

  }

  /**
   * tests the call from client to algorithmService.listInclusionDependencyAlgorithmFileNames()
   */
  public void testFinderService() {
    // Setup
    AsyncCallback<List<Algorithm>> callback = new AsyncCallback<List<Algorithm>>() {
      @Override
      public void onFailure(Throwable caught) {
        fail();
      }

      @Override
      public void onSuccess(List<Algorithm> result) {
        assertNotNull(result);
        finishTest();
      }
    };

    AlgorithmServiceAsync algorithmService = GWT.create(AlgorithmService.class);

    // Set a delay period
    delayTestFinish(500);

    algorithmService.listInclusionDependencyAlgorithms(callback);
  }


  @Override
  public String getModuleName() {
    return "de.uni_potsdam.hpi.metanome.frontend.client.MetanomeTest";
  }
}
