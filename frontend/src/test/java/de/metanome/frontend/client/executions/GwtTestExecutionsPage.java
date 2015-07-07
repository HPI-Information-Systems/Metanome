/*
 * Copyright 2015 by the Metanome project
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

package de.metanome.frontend.client.executions;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.HTML;

import de.metanome.backend.results_db.Algorithm;
import de.metanome.backend.results_db.EntityStorageException;
import de.metanome.backend.results_db.Execution;
import de.metanome.frontend.client.BasePage;
import de.metanome.frontend.client.TestHelper;
import de.metanome.frontend.client.helpers.InputValidationException;

import org.fusesource.restygwt.client.MethodCallback;

import java.util.ArrayList;
import java.util.List;

public class GwtTestExecutionsPage extends GWTTestCase {

  /**
   * Test method for {@link de.metanome.frontend.client.executions.ExecutionsPage}
   *
   * When a new ExecutionPage is created, a table should be present, and service as well as parent
   * set.
   */
  public void testSetup() {
    // Setup
    TestHelper.resetDatabaseSync();

    BasePage basePage = new BasePage();
    ExecutionsPage executionPage = new ExecutionsPage(basePage);

    // Check
    assertTrue(executionPage.getWidgetCount() == 2);
    assertEquals(basePage, executionPage.parent);

    // Clean up
    TestHelper.resetDatabaseSync();
  }

  /**
   * Test method for {@link de.metanome.frontend.client.executions.ExecutionsPage#addExecutionsToTable(List<de.metanome.backend.results_db.Execution>)}
   */
  public void testAddExecutions() {
    // Setup
    TestHelper.resetDatabaseSync();

    BasePage basePage = new BasePage();
    ExecutionsPage executionPage = new ExecutionsPage(basePage);

    Algorithm algorithm = new Algorithm("file");
    algorithm.setName("some name");

    Execution execution = new Execution(algorithm, 1000);
    execution.setCountResult(false);
    List<Execution> executionList = new ArrayList<>();
    executionList.add(execution);

    int rowCount = executionPage.executionsTable.getRowCount();

    // Execute functionality
    executionPage.addExecutionsToTable(executionList);

    // Check
    assertEquals(rowCount + 1, executionPage.executionsTable.getRowCount());

    // Clean up
    TestHelper.resetDatabaseSync();
  }

  /**
   * Test method for {@link de.metanome.frontend.client.executions.ExecutionsPage#deleteExecution(Execution
   * execution)}
   */
  public void testDeleteCallback() throws EntityStorageException, InputValidationException {
    // Setup
    TestHelper.resetDatabaseSync();

    Algorithm algorithm1 = new Algorithm("file");
    algorithm1.setName("algorithm 1");
    Algorithm algorithm2 = new Algorithm("file");
    algorithm2.setName("algorithm 2");
    Algorithm algorithm3 = new Algorithm("file");
    algorithm3.setName("algorithm 3");

    Execution execution1 = new Execution(algorithm1, 500);
    execution1.setCountResult(false);
    Execution execution2 = new Execution(algorithm2, 1000);
    execution2.setCountResult(false);
    Execution execution3 = new Execution(algorithm3, 1500);
    execution3.setCountResult(false);

    BasePage basePage = new BasePage();
    ExecutionsPage executionPage = new ExecutionsPage(basePage);

    executionPage.addExecution(execution1);
    executionPage.addExecution(execution2);
    executionPage.addExecution(execution3);

    int rowCount = executionPage.executionsTable.getRowCount();

    // Execute (delete execution 2)
    MethodCallback<Void>
        callback =
        executionPage.getDeleteCallback(execution2);
    callback.onSuccess(null, null);

    // Check
    assertEquals(rowCount - 1, executionPage.executionsTable.getRowCount());
    assertEquals("algorithm 3", ((HTML) executionPage.executionsTable.getWidget(1, 0)).getText());

    // Execute (delete execution 1)
    callback = executionPage.getDeleteCallback(execution1);
    callback.onSuccess(null, null);

    // Check
    assertEquals(rowCount - 2, executionPage.executionsTable.getRowCount());
    assertEquals("algorithm 3", ((HTML) executionPage.executionsTable.getWidget(1, 0)).getText());

    // Cleanup
    TestHelper.resetDatabaseSync();
  }

  @Override
  public String getModuleName() {
    return "de.metanome.frontend.MetanomeTest";
  }
}
