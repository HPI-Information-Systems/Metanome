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
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.uni_potsdam.hpi.metanome.frontend.client.TestHelper;
import de.uni_potsdam.hpi.metanome.frontend.client.helpers.InputValidationException;
import de.uni_potsdam.hpi.metanome.results_db.DatabaseConnection;
import de.uni_potsdam.hpi.metanome.results_db.EntityStorageException;
import de.uni_potsdam.hpi.metanome.results_db.TableInput;

import org.junit.Test;


public class GwtTestTableInputField extends GWTTestCase {

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.frontend.client.datasources.TableInputEditForm#TableInputEditForm()}
   * <p/>
   * After initializing the list box of database connection should be filled and the table name text box should be present.
   */
  @Test
  public void testSetUp() throws EntityStorageException, InputValidationException {
    // Setup
    TestHelper.resetDatabaseSync();

    DatabaseConnection dbConnection = new DatabaseConnection();
    dbConnection.setUrl("url");
    dbConnection.setPassword("password");
    dbConnection.setUsername("db");

    TestHelper.storeDatabaseConnection(dbConnection, new AsyncCallback<Long>() {
      @Override
      public void onFailure(Throwable caught) {
        System.out.println(caught.getMessage());
        fail();
      }

      @Override
      public void onSuccess(Long id) {
      }
    });

    // Execute
    final TableInputEditForm field = new TableInputEditForm();

    // Check
    Timer timer = new Timer() {
      @Override
      public void run() {
        if (field.dbConnectionListBox.getValues().size() > 0 &&
            field.dbMap.size() > 0 &&
            field.tableNameTextbox != null)
          finishTest();
      }
    };

    // Waiting four seconds for asynchronous calls to finish.
    // Validate, if expected and actual objects are equal
    timer.schedule(4000);

    delayTestFinish(5000);
  }

  /**
   * Test method for {@link TableInputEditForm#getValue()}
   */
  @Test
  public void testGetValue() throws EntityStorageException, InputValidationException {
    // Setup
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
  }

  /**
   * Test method for {@link TableInputEditForm#reset()}
   */
  @Test
  public void testResetValues() {
    //Setup
    TableInputEditForm input = new TableInputEditForm();
    input.dbConnectionListBox.addValue("--");
    input.dbConnectionListBox.addValue("1: db2");
    input.tableNameTextbox.setText("table name");

    // Execute
    input.reset();

    String actualDB = input.dbConnectionListBox.getSelectedValue();
    String actualTable = input.tableNameTextbox.getText();

    //Check
    assertEquals("", actualTable);
    assertEquals("--", actualDB);
  }

  @Override
  public String getModuleName() {
    return "de.uni_potsdam.hpi.metanome.frontend.client.MetanomeTest";
  }
}
