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
import com.google.gwt.user.client.ui.HTML;

import de.metanome.algorithm_integration.configuration.DbSystem;
import de.metanome.backend.results_db.DatabaseConnection;
import de.metanome.backend.results_db.EntityStorageException;
import de.metanome.backend.results_db.TableInput;
import de.metanome.frontend.client.BasePage;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.TestHelper;
import de.metanome.frontend.client.helpers.InputValidationException;

import org.fusesource.restygwt.client.MethodCallback;

import java.util.ArrayList;

public class GwtTestTableInputTab extends GWTTestCase {

  /**
   * Test method for {@link de.metanome.frontend.client.datasources.TableInputTab#TableInputTab(DataSourcePage)}
   */
  public void testSetUp() {
    // Setup
    TestHelper.resetDatabaseSync();

    // Execute
    TableInputTab input = new TableInputTab(new DataSourcePage(new BasePage()));

    // Check
    assertNotNull(input.parent);
    assertNotNull(input.tableInputList);
    assertNotNull(input.tableInputRestService);
    assertNotNull(input.editForm);

    TestHelper.resetDatabaseSync();
  }

  /**
   * Test method for {@link de.metanome.frontend.client.datasources.TableInputTab#addTableInputToTable(de.metanome.backend.results_db.TableInput)}
   */
  public void testAddTableInputToTable() {
    //Setup
    DatabaseConnection databaseConnection = new DatabaseConnection();
    databaseConnection.setUrl("url");
    databaseConnection.setPassword("password");
    databaseConnection.setUsername("user");
    databaseConnection.setSystem(DbSystem.DB2);

    TableInput tableInput = new TableInput();
    tableInput.setDatabaseConnection(databaseConnection);
    tableInput.setTableName("table");

    TableInputTab input = new TableInputTab(new DataSourcePage(new BasePage()));
    int rowCount = input.tableInputList.getRowCount();

    // Execute
    input.addTableInputToTable(tableInput);

    //Check
    assertEquals(rowCount + 1, input.tableInputList.getRowCount());
  }

  /**
   * Test method for {@link de.metanome.frontend.client.datasources.DatabaseConnectionTab#listDatabaseConnections(java.util.List)}
   */
  public void testListDatabaseConnections() {
    //Setup
    DatabaseConnection databaseConnection1 = new DatabaseConnection();
    databaseConnection1.setUrl("url");
    databaseConnection1.setPassword("password");
    databaseConnection1.setUsername("user");
    databaseConnection1.setSystem(DbSystem.DB2);

    TableInput tableInput1 = new TableInput();
    tableInput1.setDatabaseConnection(databaseConnection1);
    tableInput1.setTableName("table");

    DatabaseConnection databaseConnection2 = new DatabaseConnection();
    databaseConnection2.setUrl("url");
    databaseConnection2.setPassword("password");
    databaseConnection2.setUsername("user");
    databaseConnection2.setSystem(DbSystem.DB2);

    TableInput tableInput2 = new TableInput();
    tableInput2.setDatabaseConnection(databaseConnection2);
    tableInput2.setTableName("table");

    ArrayList<TableInput> inputs = new ArrayList<TableInput>();
    inputs.add(tableInput1);
    inputs.add(tableInput2);

    TableInputTab input = new TableInputTab(new DataSourcePage(new BasePage()));

    int rowCount = input.tableInputList.getRowCount();

    // Execute
    input.listTableInputs(inputs);

    //Check
    assertEquals(rowCount + 3, input.tableInputList.getRowCount());
  }

  /**
   * Test method for {@link de.metanome.frontend.client.datasources.TableInputTab#getDeleteCallback(de.metanome.backend.results_db.TableInput)}
   * and test method for {@link de.metanome.frontend.client.datasources.TableInputEditForm#increaseDatabaseConnectionUsage(String
   * identifier)} and test method for {@link de.metanome.frontend.client.datasources.TableInputEditForm#decreaseDatabaseConnectionUsage(String
   * identifier)}
   */
  public void testDeleteCallback() throws EntityStorageException, InputValidationException {
    // Setup
    TestHelper.resetDatabaseSync();

    DatabaseConnection databaseConnection1 = new DatabaseConnection();
    databaseConnection1.setUrl("url1");
    databaseConnection1.setUsername("user1");
    databaseConnection1.setSystem(DbSystem.DB2);

    TableInput tableInput1 = new TableInput();
    tableInput1.setTableName("table1");
    tableInput1.setDatabaseConnection(databaseConnection1);

    DatabaseConnection databaseConnection2 = new DatabaseConnection();
    databaseConnection2.setUrl("url2");
    databaseConnection2.setUsername("user2");
    databaseConnection2.setSystem(DbSystem.DB2);

    TableInput tableInput2 = new TableInput();
    tableInput2.setTableName("table2");
    tableInput2.setDatabaseConnection(databaseConnection2);

    TableInput tableInput3 = new TableInput();
    tableInput3.setTableName("table3");
    tableInput3.setDatabaseConnection(databaseConnection2);

    BasePage parent = new BasePage();
    DataSourcePage page = new DataSourcePage(parent);
    TableInputTab tableInputTab = new TableInputTab(page);
    page.setMessageReceiver(new TabWrapper());

    page.databaseConnectionTab.addDatabaseConnectionToTable(databaseConnection1);
    page.databaseConnectionTab.addDatabaseConnectionToTable(databaseConnection2);

    tableInputTab.addTableInputToTable(tableInput1);
    tableInputTab.addTableInputToTable(tableInput2);
    tableInputTab.addTableInputToTable(tableInput3);
    tableInputTab.editForm.dbMap.put(databaseConnection1.getIdentifier(), databaseConnection1);
    tableInputTab.editForm.dbMap.put(databaseConnection2.getIdentifier(), databaseConnection2);
    tableInputTab.editForm.increaseDatabaseConnectionUsage(databaseConnection1.getIdentifier());
    tableInputTab.editForm.increaseDatabaseConnectionUsage(databaseConnection2.getIdentifier());
    tableInputTab.editForm.increaseDatabaseConnectionUsage(databaseConnection2.getIdentifier());

    int rowCount = tableInputTab.tableInputList.getRowCount();

    // Execute (delete Table Input 2)
    MethodCallback<Void>
        callback =
        tableInputTab.getDeleteCallback(tableInput2);
    callback.onSuccess(null, null);

    // Check
    assertEquals(rowCount - 1, tableInputTab.tableInputList.getRowCount());
    assertEquals("table3", ((HTML) tableInputTab.tableInputList.getWidget(1, 1)).getText());
    assertFalse(
        ((Button) page.databaseConnectionTab.connectionInputList.getWidget(1, 5)).isEnabled());

    // Execute (delete Table Input 1)
    callback = tableInputTab.getDeleteCallback(tableInput1);
    callback.onSuccess(null, null);

    // Check
    assertEquals(rowCount - 2, tableInputTab.tableInputList.getRowCount());
    assertEquals("table3", ((HTML) tableInputTab.tableInputList.getWidget(0, 1)).getText());
    assertTrue(
        ((Button) page.databaseConnectionTab.connectionInputList.getWidget(0, 5)).isEnabled());

    // Cleanup
    TestHelper.resetDatabaseSync();
  }

  /**
   * Test method for {@link de.metanome.frontend.client.datasources.TableInputTab#updateTableInputInTable(TableInput
   * updatedInput, TableInput oldInput)}
   */
  public void testUpdateTableInput() {
    // Setup
    TestHelper.resetDatabaseSync();

    DatabaseConnection databaseConnection1 = new DatabaseConnection();
    databaseConnection1.setUrl("url");
    databaseConnection1.setPassword("password");
    databaseConnection1.setUsername("user");
    databaseConnection1.setSystem(DbSystem.DB2);

    TableInput oldTableInput = new TableInput();
    oldTableInput.setDatabaseConnection(databaseConnection1);
    oldTableInput.setTableName("table");

    ArrayList<TableInput> inputs = new ArrayList<TableInput>();
    inputs.add(oldTableInput);

    TableInputTab tableInputTab = new TableInputTab(new DataSourcePage(new BasePage()));
    tableInputTab.listTableInputs(inputs);

    // Expected Values
    String expectedValue = "updated";
    DatabaseConnection databaseConnection2 = new DatabaseConnection();
    databaseConnection2.setUrl(expectedValue);
    databaseConnection2.setPassword(expectedValue);
    databaseConnection2.setUsername(expectedValue);
    databaseConnection2.setSystem(DbSystem.DB2);

    TableInput updatedTableInput = new TableInput();
    updatedTableInput.setDatabaseConnection(databaseConnection2);
    updatedTableInput.setTableName(expectedValue);
    updatedTableInput.setComment(expectedValue);

    // Execute
    tableInputTab.updateTableInputInTable(updatedTableInput, oldTableInput);

    // Check
    assertEquals(2, tableInputTab.tableInputList.getRowCount());
    assertTrue(
        ((HTML) (tableInputTab.tableInputList.getWidget(1, 0))).getText().contains(expectedValue));
    assertTrue(tableInputTab.tableInputList.getText(1, 1).contains(expectedValue));
    assertTrue(tableInputTab.tableInputList.getText(1, 2).contains(expectedValue));

    // Clean up
    TestHelper.resetDatabaseSync();
  }

  @Override
  public String getModuleName() {
    return "de.metanome.frontend.client.MetanomeTest";
  }

}
