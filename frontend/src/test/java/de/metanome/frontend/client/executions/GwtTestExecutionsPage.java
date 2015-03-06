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

import de.metanome.backend.results_db.Algorithm;
import de.metanome.backend.results_db.Execution;
import de.metanome.frontend.client.BasePage;
import de.metanome.frontend.client.TestHelper;

import java.util.ArrayList;
import java.util.List;

public class GwtTestExecutionsPage extends GWTTestCase {

  /**
   * Test method for {@link de.metanome.frontend.client.executions.ExecutionsPage}
   *
   * When a new ExecutionPage is created, a table should be present, and service as well as
   * parent set.
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

  @Override
  public String getModuleName() {
    return "de.metanome.frontend.MetanomeTest";
  }
}
