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
import com.google.gwt.user.client.rpc.AsyncCallback;
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

import java.util.ArrayList;

public class GwtTestTableInputTab extends GWTTestCase {

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

    BasePage parent = new BasePage();
    DataSourcePage page = new DataSourcePage(parent);
    TableInputTab tableInputTab = new TableInputTab(page);
    page.setMessageReceiver(new TabWrapper());

    Button deleteButton1 = new Button();
    Button deleteButton2 = new Button();

    page.databaseConnectionTab.connectionInputList.setWidget(0, 0, new HTML("url1"));
    page.databaseConnectionTab.connectionInputList.setWidget(0, 1, new HTML("user1"));
    page.databaseConnectionTab.connectionInputList.setWidget(0, 2, new HTML("DB2"));
    page.databaseConnectionTab.connectionInputList.setWidget(0, 5, deleteButton1);

    page.databaseConnectionTab.connectionInputList.setWidget(1, 0, new HTML("url2"));
    page.databaseConnectionTab.connectionInputList.setWidget(1, 1, new HTML("user2"));
    page.databaseConnectionTab.connectionInputList.setWidget(1, 2, new HTML("DB2"));
    page.databaseConnectionTab.connectionInputList.setWidget(1, 5, deleteButton2);

    tableInputTab.tableInputList.setWidget(0, 0, new HTML(databaseConnection1.getIdentifier()));
    tableInputTab.tableInputList.setWidget(0, 1, new HTML("table1"));
    tableInputTab.tableInputList.setWidget(1, 0, new HTML(databaseConnection2.getIdentifier()));
    tableInputTab.tableInputList.setWidget(1, 1, new HTML("table2"));
    tableInputTab.tableInputList.setWidget(2, 0, new HTML("url3; user3; DB2"));
    tableInputTab.tableInputList.setWidget(2, 1, new HTML("table3"));

    int rowCount = tableInputTab.tableInputList.getRowCount();

    // Execute (delete Table Input 2)
    AsyncCallback<Void>
        callback =
        tableInputTab.getDeleteCallback(tableInput2);
    callback.onSuccess(null);

    // Check
    assertEquals(rowCount - 1, tableInputTab.tableInputList.getRowCount());
    assertEquals("table3", ((HTML) tableInputTab.tableInputList.getWidget(1, 1)).getText());
    assertTrue(((Button) page.databaseConnectionTab.connectionInputList.getWidget(1, 5)).isEnabled());

    // Execute (delete Table Input 1)
    callback = tableInputTab.getDeleteCallback(tableInput1);
    callback.onSuccess(null);

    // Check
    assertEquals(rowCount - 2, tableInputTab.tableInputList.getRowCount());
    assertEquals("table3", ((HTML) tableInputTab.tableInputList.getWidget(0, 1)).getText());
    assertTrue(((Button) page.databaseConnectionTab.connectionInputList.getWidget(0, 5)).isEnabled());

    // Cleanup
    TestHelper.resetDatabaseSync();
  }

  @Override
  public String getModuleName() {
    return "de.metanome.frontend.client.MetanomeTest";
  }

}
