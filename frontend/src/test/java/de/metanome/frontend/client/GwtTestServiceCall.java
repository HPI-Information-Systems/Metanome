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

import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.backend.results_db.FileInput;
import de.metanome.backend.results_db.TableInput;
import de.metanome.frontend.client.services.FileInputRestService;
import de.metanome.frontend.client.services.ParameterService;
import de.metanome.frontend.client.services.ParameterServiceAsync;
import de.metanome.frontend.client.services.TableInputService;
import de.metanome.frontend.client.services.TableInputServiceAsync;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.List;

/**
 * Tests RPC calls to server
 */
public class GwtTestServiceCall extends GWTTestCase {

// FIXME
//  /**
//   * tests the call from client to executionService.executeAlgorithm()
//   */
//  public void testExecutionService() throws AlgorithmConfigurationException,
//                                            EntityStorageException {
//    // Setup
//    TestHelper.resetDatabaseSync();
//
//    String algorithmFileName = "example_ucc_algorithm.jar";
//    long id = 0;
//
//    Algorithm algorithm = TestHelper.storeAlgorithmSync(new Algorithm(algorithmFileName));
//
//    List<ConfigurationRequirement> configs = new ArrayList<>();
//    ConfigurationRequirementString inputParameter =
//        new ConfigurationRequirementString("pathToInputFile", 2);
//    inputParameter.checkAndSetSettings(new ConfigurationSettingString("path/to/file1"),
//                               new ConfigurationSettingString("path/to/file2"));
//    configs.add(inputParameter);
//
//    AsyncCallback<Long> callback = new AsyncCallback<Long>() {
//      public void onFailure(Throwable caught) {
//        fail();
//      }
//
//      public void onSuccess(Long executionTime) {
//        assertTrue(executionTime > 0);
//        finishTest();
//      }
//    };
//
//    ExecutionServiceAsync executionService = GWT.create(ExecutionService.class);
//
//    // Set a delay period
//    delayTestFinish(500);
//
//    // Execute
//    executionService.executeAlgorithm(algorithmFileName, algorithm.getId(), "executionIdentifier1", configs, callback);
//
//    // Cleanup
//    TestHelper.resetDatabaseSync();
//  }

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
  /* FIXME
  public void testFileInputService() {
    // Setup
    MethodCallback<List<FileInput>> callback = new MethodCallback<List<FileInput>>() {
      @Override
      public void onFailure(Method method, Throwable caught) {
        fail();
      }

      @Override
      public void onSuccess(Method method, List<FileInput> result) {
        assertNotNull(result);
        finishTest();
      }

    };

    FileInputRestService service = com.google.gwt.core.client.GWT.create(FileInputRestService.class);

    // Set a delay period
    delayTestFinish(500);

    service.listFileInputs(callback);
  }
  */

  @Override
  public String getModuleName() {
    return "de.metanome.frontend.client.MetanomeTest";
  }
}
