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

import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.DbSystem;
import de.uni_potsdam.hpi.metanome.frontend.client.BasePage;
import de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper;
import de.uni_potsdam.hpi.metanome.frontend.client.TestHelper;
import de.uni_potsdam.hpi.metanome.frontend.client.helpers.InputValidationException;
import de.uni_potsdam.hpi.metanome.results_db.DatabaseConnection;
import de.uni_potsdam.hpi.metanome.results_db.EntityStorageException;
import de.uni_potsdam.hpi.metanome.results_db.FileInput;
import de.uni_potsdam.hpi.metanome.results_db.TableInput;

import org.junit.Test;

import java.util.List;


public class GwtTestDataSourcePage extends GWTTestCase {

  /**
   * Test method for {@link DataSourcePage#DataSourcePage(de.uni_potsdam.hpi.metanome.frontend.client.BasePage)}
   */
  @Test
  public void testSetUp() {
    // Set up
    BasePage basePage = new BasePage();

    // Execute
    DataSourcePage dataSourcePage = new DataSourcePage(basePage);

    // Check
    assertTrue(dataSourcePage.databaseConnectionEditForm != null);
    assertTrue(dataSourcePage.tableInputEditForm != null);
    assertTrue(dataSourcePage.fileInputEditForm != null);

    assertTrue(dataSourcePage.databaseConnectionSelected);
    assertFalse(dataSourcePage.tableInputSelected);
    assertFalse(dataSourcePage.fileInputSelected);
  }

  /**
   * Test method for {@link DataSourcePage#saveObject()}
   */
  @Test
  public void testStoreTableInput() throws EntityStorageException, InputValidationException {
    // Setup
    TestHelper.resetDatabaseSync();
    final boolean[] blocked = {true};

    BasePage parent = new BasePage();
    DataSourcePage page = new DataSourcePage(parent);
    page.setMessageReceiver(new TabWrapper());

    DatabaseConnection dbConnection = new DatabaseConnection();
    dbConnection.setUrl("url");
    dbConnection.setPassword("password");
    dbConnection.setUsername("db");

    final long[] connectionId = new long[1];
    TestHelper.storeDatabaseConnection(dbConnection, new AsyncCallback<Long>() {
      @Override
      public void onFailure(Throwable caught) {
        System.out.println(caught.getMessage());
        fail();
      }

      @Override
      public void onSuccess(Long id) {
        connectionId[0] = id;
      }
    });

    page.tableInputEditForm.setValues(connectionId[0] + ": url", "table");
    page.tableInputSelected = true;
    page.databaseConnectionSelected = false;
    page.fileInputSelected = false;
    page.editForm.clear();
    page.editForm.add(page.tableInputEditForm);

    // Execute
    page.saveObject();

    // Expected values
    final TableInput expectedInput = page.tableInputEditForm.getValue();

    // Check result
    TestHelper.getAllTableInputs(
        new AsyncCallback<List<TableInput>>() {
          @Override
          public void onFailure(Throwable throwable) {
            fail();
          }

          @Override
          public void onSuccess(List<TableInput> con) {
            blocked[0] = false;
            assertTrue(con.contains(expectedInput));
          }
        });

    Timer rpcCheck = new Timer() {
      @Override
      public void run() {
        if (blocked[0]) {
          this.schedule(100);
        }
      }
    };
    rpcCheck.schedule(100);

    // Cleanup
    TestHelper.resetDatabaseSync();
  }

  /**
   * Test method for {@link DataSourcePage#saveObject()}
   */
  @Test
  public void testStoreFileInput() throws EntityStorageException, InputValidationException {
    // Setup
    TestHelper.resetDatabaseSync();
    final boolean[] blocked = {true};

    BasePage parent = new BasePage();
    DataSourcePage page = new DataSourcePage(parent);
    page.setMessageReceiver(new TabWrapper());

    page.fileInputEditForm.setFileName("file_name");
    page.tableInputSelected = false;
    page.databaseConnectionSelected = false;
    page.fileInputSelected = true;
    page.editForm.clear();
    page.editForm.add(page.fileInputEditForm);

    // Execute
    page.saveObject();

    // Expected values
    final FileInput expectedInput = page.fileInputEditForm.getValue();

    // Check result
    TestHelper.getAllFileInputs(
        new AsyncCallback<List<FileInput>>() {
          @Override
          public void onFailure(Throwable throwable) {
            fail();
          }

          @Override
          public void onSuccess(List<FileInput> con) {
            blocked[0] = false;
            assertTrue(con.contains(expectedInput));
          }
        });

    Timer rpcCheck = new Timer() {
      @Override
      public void run() {
        if (blocked[0]) {
          this.schedule(100);
        }
      }
    };
    rpcCheck.schedule(100);

    // Cleanup
    TestHelper.resetDatabaseSync();
  }

  /**
   * Test method for {@link DataSourcePage#saveObject()}
   */
  @Test
  public void testStoreDatabaseConnection() throws EntityStorageException, InputValidationException {
    // Setup
    TestHelper.resetDatabaseSync();
    final boolean[] blocked = {true};

    BasePage parent = new BasePage();
    DataSourcePage page = new DataSourcePage(parent);
    page.setMessageReceiver(new TabWrapper());

    page.databaseConnectionEditForm.setValues("url", DbSystem.DB2.name(), "user", "password");
    page.tableInputSelected = false;
    page.databaseConnectionSelected = true;
    page.fileInputSelected = false;
    page.editForm.clear();
    page.editForm.add(page.databaseConnectionEditForm);

    // Execute
    page.saveObject();

    // Expected values
    final DatabaseConnection expectedInput = page.databaseConnectionEditForm.getValue();

    // Check result
    TestHelper.getAllDatabaseConnections(
        new AsyncCallback<List<DatabaseConnection>>() {
          @Override
          public void onFailure(Throwable throwable) {
            fail();
          }

          @Override
          public void onSuccess(List<DatabaseConnection> con) {
            blocked[0] = false;
            assertTrue(con.contains(expectedInput));
          }
        });

    Timer rpcCheck = new Timer() {
      @Override
      public void run() {
        if (blocked[0]) {
          this.schedule(100);
        }
      }
    };
    rpcCheck.schedule(100);

    // Cleanup
    TestHelper.resetDatabaseSync();
  }

  @Override
  public String getModuleName() {
    return "de.uni_potsdam.hpi.metanome.frontend.client.MetanomeTest";
  }
}
