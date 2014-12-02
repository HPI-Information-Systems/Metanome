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

package de.metanome.frontend.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementString;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingString;
import de.metanome.backend.results_db.Algorithm;
import de.metanome.backend.results_db.DatabaseConnection;
import de.metanome.backend.results_db.FileInput;
import de.metanome.backend.results_db.TableInput;
import de.metanome.frontend.client.services.DatabaseConnectionService;
import de.metanome.frontend.client.services.DatabaseConnectionServiceAsync;
import de.metanome.frontend.client.services.ExecutionService;
import de.metanome.frontend.client.services.ExecutionServiceAsync;
import de.metanome.frontend.client.services.FileInputService;
import de.metanome.frontend.client.services.FileInputServiceAsync;
import de.metanome.frontend.client.services.ParameterService;
import de.metanome.frontend.client.services.ParameterServiceAsync;
import de.metanome.frontend.client.services.TableInputService;
import de.metanome.frontend.client.services.TableInputServiceAsync;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests RPC calls to server
 */
public class GwtTestServiceCall extends GWTTestCase {

  /**
   * tests the call from client to executionService.executeAlgorithm()
   */
  public void testExecutionService() throws AlgorithmConfigurationException {
    // Setup
    TestHelper.resetDatabaseSync();

    String algorithmFileName = "example_ucc_algorithm.jar";
    TestHelper.storeAlgorithmSync(new Algorithm(algorithmFileName));
    List<ConfigurationRequirement> configs = new ArrayList<>();
    ConfigurationRequirementString inputParameter =
        new ConfigurationRequirementString("pathToInputFile", 2);
    inputParameter.setSettings(new ConfigurationSettingString("path/to/file1"),
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
    AsyncCallback<List<ConfigurationRequirement>> callback =
        new AsyncCallback<List<ConfigurationRequirement>>() {
          public void onFailure(Throwable caught) {
            fail();
          }

          public void onSuccess(List<ConfigurationRequirement> result) {
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
   * tests the call from client to databaseConnectionService
   */
  public void testDatabaseConnectionService() {
    // Setup
    AsyncCallback<List<DatabaseConnection>>
        callback =
        new AsyncCallback<List<DatabaseConnection>>() {
          @Override
          public void onFailure(Throwable caught) {
            fail();
          }

          @Override
          public void onSuccess(List<DatabaseConnection> result) {
            assertNotNull(result);
            finishTest();
          }
        };

    DatabaseConnectionServiceAsync service = GWT.create(DatabaseConnectionService.class);

    // Set a delay period
    delayTestFinish(500);

    service.listDatabaseConnections(callback);
  }

  /**
   * tests the call from client to tableInputService
   */
  public void testTableInputService() {
    // Setup
    AsyncCallback<List<TableInput>> callback = new AsyncCallback<List<TableInput>>() {
      @Override
      public void onFailure(Throwable caught) {
        fail();
      }

      @Override
      public void onSuccess(List<TableInput> result) {
        assertNotNull(result);
        finishTest();
      }
    };

    TableInputServiceAsync service = GWT.create(TableInputService.class);

    // Set a delay period
    delayTestFinish(500);

    service.listTableInputs(callback);
  }

  /**
   * tests the call from client to tableInputService
   */
  public void testFileInputService() {
    // Setup
    AsyncCallback<List<FileInput>> callback = new AsyncCallback<List<FileInput>>() {
      @Override
      public void onFailure(Throwable caught) {
        fail();
      }

      @Override
      public void onSuccess(List<FileInput> result) {
        assertNotNull(result);
        finishTest();
      }
    };

    FileInputServiceAsync service = GWT.create(FileInputService.class);

    // Set a delay period
    delayTestFinish(500);

    service.listFileInputs(callback);
  }


  @Override
  public String getModuleName() {
    return "de.metanome.frontend.client.MetanomeTest";
  }
}
