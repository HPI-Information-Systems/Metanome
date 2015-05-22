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
import de.metanome.frontend.client.BasePage;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.TestHelper;
import de.metanome.frontend.client.helpers.InputValidationException;

import org.fusesource.restygwt.client.MethodCallback;

import java.util.ArrayList;


public class GwtTestDatabaseConnectionTab extends GWTTestCase {

  /**
   * Test method for {@link de.metanome.frontend.client.datasources.DatabaseConnectionTab#DatabaseConnectionTab(DataSourcePage)}
   */
  public void testSetUp() {
    // Setup
    TestHelper.resetDatabaseSync();

    // Execute
    DatabaseConnectionTab input = new DatabaseConnectionTab(new DataSourcePage(new BasePage()));

    // Check
    assertNotNull(input.databaseConnectionService);
    assertNotNull(input.tableInputService);
    assertNotNull(input.parent);
    assertNotNull(input.connectionInputList);
    assertNotNull(input.editForm);

    TestHelper.resetDatabaseSync();
  }

  /**
   * Test method for {@link de.metanome.frontend.client.datasources.DatabaseConnectionTab#addDatabaseConnectionToTable(de.metanome.backend.results_db.DatabaseConnection)}
   */
  public void testAddDatabaseConnectionToTable() {
    //Setup
    DatabaseConnection databaseConnection = new DatabaseConnection();
    databaseConnection.setUrl("url");
    databaseConnection.setPassword("password");
    databaseConnection.setUsername("user");
    databaseConnection.setSystem(DbSystem.DB2);

    DatabaseConnectionTab input = new DatabaseConnectionTab(new DataSourcePage(new BasePage()));
    int rowCount = input.connectionInputList.getRowCount();

    // Execute
    input.addDatabaseConnectionToTable(databaseConnection);

    //Check
    assertEquals(rowCount + 1, input.connectionInputList.getRowCount());
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

    DatabaseConnection databaseConnection2 = new DatabaseConnection();
    databaseConnection2.setUrl("url");
    databaseConnection2.setPassword("password");
    databaseConnection2.setUsername("user");
    databaseConnection2.setSystem(DbSystem.DB2);

    ArrayList<DatabaseConnection> connections = new ArrayList<DatabaseConnection>();
    connections.add(databaseConnection1);
    connections.add(databaseConnection2);

    DatabaseConnectionTab input = new DatabaseConnectionTab(new DataSourcePage(new BasePage()));

    int rowCount = input.connectionInputList.getRowCount();

    // Execute
    input.listDatabaseConnections(connections);

    //Check
    assertEquals(rowCount + 3, input.connectionInputList.getRowCount());
  }


  /**
   * Test method for {@link de.metanome.frontend.client.datasources.DatabaseConnectionTab#setEnableOfButtons(de.metanome.backend.results_db.DatabaseConnection,
   * boolean)}
   */
  public void testSetEnableDeleteButton() throws EntityStorageException, InputValidationException {
    // Setup
    TestHelper.resetDatabaseSync();

    DatabaseConnectionTab page = new DatabaseConnectionTab(new DataSourcePage(new BasePage()));

    DatabaseConnection connection = new DatabaseConnection();
    connection.setUrl("url");
    connection.setUsername("user");
    connection.setSystem(DbSystem.DB2);

    page.connectionInputList.setWidget(0, 0, new HTML("url"));
    page.connectionInputList.setWidget(0, 1, new HTML("user"));
    page.connectionInputList.setWidget(0, 2, new HTML("DB2"));
    page.connectionInputList.setWidget(0, 3, new HTML("Comment"));
    page.connectionInputList.setWidget(0, 4, new Button("Run"));
    page.connectionInputList.setWidget(0, 5, new Button("Delete"));
    page.connectionInputList.setWidget(0, 6, new Button("Edit"));

    Button actualButton1 = (Button) page.connectionInputList.getWidget(0, 5);
    Button actualButton2 = (Button) page.connectionInputList.getWidget(0, 5);

    assertTrue(actualButton1.isEnabled());
    assertTrue(actualButton2.isEnabled());

    // Execute
    page.setEnableOfButtons(connection, false);

    // Check
    assertFalse(actualButton1.isEnabled());
    assertFalse(actualButton2.isEnabled());

    // Cleanup
    TestHelper.resetDatabaseSync();
  }

  /**
   * Test method for {@link de.metanome.frontend.client.datasources.DatabaseConnectionTab#getDeleteCallback(de.metanome.backend.results_db.DatabaseConnection)}
   */
  public void testDeleteCallback() throws EntityStorageException, InputValidationException {
    // Setup
    TestHelper.resetDatabaseSync();

    DatabaseConnection databaseConnection1 = new DatabaseConnection();
    databaseConnection1.setUrl("url1");
    databaseConnection1.setUsername("user1");
    databaseConnection1.setSystem(DbSystem.DB2);

    DatabaseConnection databaseConnection2 = new DatabaseConnection();
    databaseConnection2.setUrl("url2");
    databaseConnection2.setUsername("user2");
    databaseConnection2.setSystem(DbSystem.DB2);

    BasePage parent = new BasePage();
    DataSourcePage page = new DataSourcePage(parent);
    DatabaseConnectionTab databaseConnectionTab = new DatabaseConnectionTab(page);
    page.setMessageReceiver(new TabWrapper());

    databaseConnectionTab.connectionInputList.setWidget(0, 0, new HTML("url1"));
    databaseConnectionTab.connectionInputList.setWidget(0, 1, new HTML("user1"));
    databaseConnectionTab.connectionInputList.setWidget(0, 2, new HTML("DB2"));

    databaseConnectionTab.connectionInputList.setWidget(1, 0, new HTML("url2"));
    databaseConnectionTab.connectionInputList.setWidget(1, 1, new HTML("user2"));
    databaseConnectionTab.connectionInputList.setWidget(1, 2, new HTML("DB2"));

    databaseConnectionTab.connectionInputList.setWidget(2, 0, new HTML("url3"));
    databaseConnectionTab.connectionInputList.setWidget(2, 1, new HTML("user3"));
    databaseConnectionTab.connectionInputList.setWidget(2, 2, new HTML("DB2"));

    int rowCount = databaseConnectionTab.connectionInputList.getRowCount();

    // Execute (delete Database Connection 2)
    MethodCallback<Void>
        callback =
        databaseConnectionTab.getDeleteCallback(databaseConnection2);
    callback.onSuccess(null, null);

    // Check
    assertEquals(rowCount - 1, databaseConnectionTab.connectionInputList.getRowCount());
    assertEquals("url3",
                 ((HTML) databaseConnectionTab.connectionInputList.getWidget(1, 0)).getText());

    // Execute (delete DatabaseConnection 1)
    callback = databaseConnectionTab.getDeleteCallback(databaseConnection1);
    callback.onSuccess(null, null);

    // Check
    assertEquals(rowCount - 2, databaseConnectionTab.connectionInputList.getRowCount());
    assertEquals("url3",
                 ((HTML) databaseConnectionTab.connectionInputList.getWidget(0, 0)).getText());

    // Cleanup
    TestHelper.resetDatabaseSync();
  }


  /**
   * Test method for {@link de.metanome.frontend.client.datasources.DatabaseConnectionTab#updateDatabaseConnectionInTable(de.metanome.backend.results_db.DatabaseConnection,
   * de.metanome.backend.results_db.DatabaseConnection)}
   */
  public void testUpdateDatabaseConnection() {
    // Setup
    TestHelper.resetDatabaseSync();

    DatabaseConnection oldDatabaseConnection = new DatabaseConnection();
    oldDatabaseConnection.setUrl("url");
    oldDatabaseConnection.setPassword("password");
    oldDatabaseConnection.setUsername("user");
    oldDatabaseConnection.setSystem(DbSystem.DB2);
    ArrayList<DatabaseConnection> connections = new ArrayList<DatabaseConnection>();
    connections.add(oldDatabaseConnection);

    DatabaseConnectionTab
        connectionTab =
        new DatabaseConnectionTab(new DataSourcePage(new BasePage()));
    connectionTab.listDatabaseConnections(connections);

    // Expected Values
    String expectedValue = "updated";
    DatabaseConnection updatedDatabaseConnection = new DatabaseConnection()
        .setUrl(expectedValue)
        .setComment(expectedValue)
        .setUsername(expectedValue)
        .setPassword(expectedValue)
        .setSystem(DbSystem.HANA);

    // Execute
    connectionTab.updateDatabaseConnectionInTable(updatedDatabaseConnection, oldDatabaseConnection);

    // Check
    assertEquals(2, connectionTab.connectionInputList.getRowCount());
    assertTrue(((HTML) (connectionTab.connectionInputList.getWidget(1, 0))).getText()
                   .contains(expectedValue));
    assertTrue(((HTML) (connectionTab.connectionInputList.getWidget(1, 1))).getText()
                   .contains(expectedValue));
    assertEquals(((HTML) (connectionTab.connectionInputList.getWidget(1, 2))).getText(),
                 DbSystem.HANA.name());
    assertTrue(connectionTab.connectionInputList.getText(1, 3).contains(expectedValue));

    // Clean up
    TestHelper.resetDatabaseSync();
  }


  @Override
  public String getModuleName() {
    return "de.metanome.frontend.client.MetanomeTest";
  }


}
