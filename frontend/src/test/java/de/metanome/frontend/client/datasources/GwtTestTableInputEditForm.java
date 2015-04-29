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
import com.google.gwt.user.client.ui.Button;

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

    DatabaseConnection
        dbConnection =
        new DatabaseConnection("url", "user", "password", DbSystem.DB2);

    TableInputEditForm
        editForm =
        new TableInputEditForm(new TableInputTab(new DataSourcePage(new BasePage())));
    editForm.dbConnectionListBox.addValue("1: url");
    editForm.dbMap.put("1: url", dbConnection);

    editForm.setValues("1: url", "table", "comment");

    // Expected values
    final TableInput expectedInput = new TableInput();
    expectedInput.setDatabaseConnection(dbConnection);
    expectedInput.setTableName("table");
    expectedInput.setComment("comment");

    // Check
    assertEquals(editForm.getValue(), expectedInput);

    // Cleanup
    TestHelper.resetDatabaseSync();
  }

  /**
   * Test method for {@link TableInputEditForm#reset()}
   */
  public void testResetValues() throws InputValidationException {
    // Set up
    TestHelper.resetDatabaseSync();

    TableInputEditForm
        input =
        new TableInputEditForm(new TableInputTab(new DataSourcePage(new BasePage())));
    input.dbConnectionListBox.addValue("--");
    input.dbConnectionListBox.addValue("1: db2");
    input.tableNameTextbox.setText("table name");
    input.commentTextbox.setText("some comment");

    // Execute
    input.reset();

    String actualDB = input.dbConnectionListBox.getSelectedValue();
    String actualTable = input.tableNameTextbox.getText();
    String actualComment = input.commentTextbox.getText();

    // Check
    assertEquals("", actualTable);
    assertEquals("--", actualDB);
    assertEquals("", actualComment);

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

  /**
   * Test method for {@link de.metanome.frontend.client.datasources.TableInputEditForm#updateTableInput(TableInput
   * tableInput)} and test method for {@link de.metanome.frontend.client.datasources.TableInputEditForm#showSaveButton}
   *
   * If the edit button for a table input is clicked, the edit form should contain the values of
   * that table input and the edit form should show a update button instead of an save button. If
   * the method 'show save button' is called, the save button should be visible again.
   */
  public void testEditButtonClicked() throws InputValidationException {
    // Setup
    TestHelper.resetDatabaseSync();

    TableInputEditForm
        editForm =
        new TableInputEditForm(new TableInputTab(new DataSourcePage(new BasePage())));

    DatabaseConnection databaseConnection = new DatabaseConnection()
        .setUrl("url")
        .setPassword("password")
        .setUsername("user")
        .setSystem(DbSystem.DB2);

    String expectedDatabaseIdentifier = databaseConnection.getIdentifier();
    String expectedTableName = "name";
    String expectedComment = "comment";

    editForm.dbConnectionListBox.addValue("--");
    editForm.dbConnectionListBox.addValue(expectedDatabaseIdentifier);
    editForm.dbConnectionListBox.addValue("1");

    TableInput tableInput = new TableInput()
        .setDatabaseConnection(databaseConnection)
        .setTableName(expectedTableName)
        .setComment(expectedComment);

    // Execute
    editForm.updateTableInput(tableInput);

    // Check results
    assertEquals(expectedDatabaseIdentifier, editForm.dbConnectionListBox.getSelectedValue());
    assertEquals(expectedTableName, editForm.tableNameTextbox.getValue());
    assertEquals(expectedComment, editForm.commentTextbox.getValue());

    assertEquals(((Button) editForm.getWidget(3, 1)).getText(), "Update");

    // Execute
    editForm.showSaveButton();

    // Check results
    assertEquals(((Button) editForm.getWidget(3, 1)).getText(), "Save");
  }


  @Override
  public String getModuleName() {
    return "de.metanome.frontend.client.MetanomeTest";
  }
}
