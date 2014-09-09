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

package de.metanome.frontend.client.datasources;

import com.google.gwt.junit.client.GWTTestCase;

import de.metanome.algorithm_integration.configuration.DbSystem;
import de.metanome.backend.results_db.DatabaseConnection;
import de.metanome.backend.results_db.EntityStorageException;
import de.metanome.backend.results_db.TableInput;
import de.metanome.frontend.client.BasePage;
import de.metanome.frontend.client.TestHelper;
import de.metanome.frontend.client.helpers.InputValidationException;


public class GwtTestTableInputEditForm extends GWTTestCase {

  /**
   * Test method for {@link de.metanome.frontend.client.datasources.TableInputEditForm#getValue()}
   */
  public void testGetValue() throws EntityStorageException, InputValidationException {
    // Set up
    TestHelper.resetDatabaseSync();

    DatabaseConnection dbConnection = new DatabaseConnection();
    dbConnection.setUrl("url");
    dbConnection.setPassword("password");
    dbConnection.setUsername("db");

    TableInputEditForm
        editForm =
        new TableInputEditForm(new TableInputTab(new DataSourcePage(new BasePage())));
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

    TableInputEditForm
        input =
        new TableInputEditForm(new TableInputTab(new DataSourcePage(new BasePage())));
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

  /**
   * Test method for {@link TableInputEditForm#addDatabaseConnection(de.metanome.backend.results_db.DatabaseConnection)}
   * and {@link de.metanome.frontend.client.datasources.TableInputEditForm#removeDatabaseConnection(de.metanome.backend.results_db.DatabaseConnection)}
   */
  public void testAddAndRemoveDatabaseConnection() {
    // Set up
    DatabaseConnection connection = new DatabaseConnection();
    connection.setPassword("password");
    connection.setSystem(DbSystem.DB2);
    connection.setUrl("url");
    connection.setUsername("user");

    TableInputEditForm
        input =
        new TableInputEditForm(new TableInputTab(new DataSourcePage(new BasePage())));

    // Execute
    input.addDatabaseConnection(connection);

    // Check
    assertTrue(input.dbConnectionListBox.containsValues());

    // Execute
    input.removeDatabaseConnection(connection);

    // Check
    assertFalse(input.dbConnectionListBox.containsValues());
  }

  @Override
  public String getModuleName() {
    return "de.metanome.frontend.client.MetanomeTest";
  }
}
