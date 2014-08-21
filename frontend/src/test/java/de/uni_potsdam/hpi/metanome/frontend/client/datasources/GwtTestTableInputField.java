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

package de.uni_potsdam.hpi.metanome.frontend.client.datasources;

import com.google.gwt.junit.client.GWTTestCase;

import de.uni_potsdam.hpi.metanome.frontend.client.TestHelper;
import de.uni_potsdam.hpi.metanome.frontend.client.helpers.InputValidationException;
import de.uni_potsdam.hpi.metanome.results_db.DatabaseConnection;
import de.uni_potsdam.hpi.metanome.results_db.EntityStorageException;
import de.uni_potsdam.hpi.metanome.results_db.TableInput;


public class GwtTestTableInputField extends GWTTestCase {

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.frontend.client.datasources.TableInputEditForm#TableInputEditForm()}
   * <p/>
   * After initializing the list box of database connection should be filled and the table name text box should be present.
   * TODO fix FieldSerializer Bug
   */
/*  public void testSetUp() throws EntityStorageException, InputValidationException {
    // Setup
    TestHelper.resetDatabaseSync();

    DatabaseConnection dbConnection = new DatabaseConnection();
    dbConnection.setUrl("url");
    dbConnection.setPassword("password");
    dbConnection.setUsername("db");
    TestHelper.storeDatabaseConnectionSync(dbConnection);

    // Execute
    final TableInputEditForm field = new TableInputEditForm();
    field.setMessageReceiver(new TabWrapper());

    // Check
    Timer timer = new Timer() {
      @Override
      public void run() {
        if (field.dbConnectionListBox.getValues().size() > 0 &&
            field.dbMap.size() > 0 &&
            field.tableNameTextbox != null) {
          TestHelper.resetDatabaseSync();
          finishTest();
        }
        TestHelper.resetDatabaseSync();
      }
    };

    // Waiting four seconds for asynchronous calls to finish.
    // Validate, if expected and actual objects are equal
    timer.schedule(1000);

    delayTestFinish(2000);
  }
*/

  /**
   * Test method for {@link TableInputEditForm#getValue()}
   */
  public void testGetValue() throws EntityStorageException, InputValidationException {
    // Set up
    TestHelper.resetDatabaseSync();

    DatabaseConnection dbConnection = new DatabaseConnection();
    dbConnection.setUrl("url");
    dbConnection.setPassword("password");
    dbConnection.setUsername("db");

    TableInputEditForm editForm = new TableInputEditForm();
    editForm.dbConnectionListBox.addValue("1: url");
    editForm.dbMap.put("1: url", dbConnection);

    editForm.setValues("1: url", "table");

    // Expected values
    final TableInput expectedInput = new TableInput();
    expectedInput.setDatabaseConnection(dbConnection);
    expectedInput.setTableName("table");

    // Check
    assertEquals(editForm.getValue(), expectedInput);

    // Cleanup
    TestHelper.resetDatabaseSync();
  }

  /**
   * Test method for {@link TableInputEditForm#reset()}
   */
  public void testResetValues() {
    // Set up
    TestHelper.resetDatabaseSync();

    TableInputEditForm input = new TableInputEditForm();
    input.dbConnectionListBox.addValue("--");
    input.dbConnectionListBox.addValue("1: db2");
    input.tableNameTextbox.setText("table name");

    // Execute
    input.reset();

    String actualDB = input.dbConnectionListBox.getSelectedValue();
    String actualTable = input.tableNameTextbox.getText();

    // Check
    assertEquals("", actualTable);
    assertEquals("--", actualDB);

    // Cleanup
    TestHelper.resetDatabaseSync();
  }

  @Override
  public String getModuleName() {
    return "de.uni_potsdam.hpi.metanome.frontend.client.MetanomeTest";
  }
}
