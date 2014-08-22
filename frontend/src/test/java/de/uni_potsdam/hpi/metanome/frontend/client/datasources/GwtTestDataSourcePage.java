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

import de.uni_potsdam.hpi.metanome.frontend.client.BasePage;
import de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper;
import de.uni_potsdam.hpi.metanome.frontend.client.TestHelper;
import de.uni_potsdam.hpi.metanome.frontend.client.helpers.InputValidationException;
import de.uni_potsdam.hpi.metanome.results_db.EntityStorageException;
import de.uni_potsdam.hpi.metanome.results_db.FileInput;
import de.uni_potsdam.hpi.metanome.results_db.Input;

import java.util.List;

public class GwtTestDataSourcePage extends GWTTestCase {

  /**
   * Test method for {@link DataSourcePage#DataSourcePage(de.uni_potsdam.hpi.metanome.frontend.client.BasePage)}
   */
  public void testSetUp() {
    // Set up
    TestHelper.resetDatabaseSync();

    BasePage basePage = new BasePage();

    // Execute
    DataSourcePage dataSourcePage = new DataSourcePage(basePage);

    // Check
    assertNotNull(dataSourcePage.databaseConnectionTab);
    assertNotNull(dataSourcePage.fileInputTab);
    assertNotNull(dataSourcePage.tableInputTab);

    // Cleanup
    TestHelper.resetDatabaseSync();
  }

  /**
   * Test method for {@link DataSourcePage#saveObject()}
   * TODO fix FieldSerializer bug
   */
/*
  public void testStoreTableInput() throws EntityStorageException, InputValidationException {
    // Setup
    TestHelper.resetDatabaseSync();

    DatabaseConnection dbConnection = new DatabaseConnection();
    dbConnection.setUrl("url");
    dbConnection.setPassword("password");
    dbConnection.setUsername("db");
    TestHelper.storeDatabaseConnectionSync(dbConnection);

    BasePage parent = new BasePage();
    final DataSourcePage page = new DataSourcePage(parent);
    page.setMessageReceiver(new TabWrapper());

    final TableInput[] expectedInput = new TableInput[1];
    // Timer waiting for DataSourcePage to initialize the TableInputEditForm
    Timer executeTimer = new Timer() {
      @Override
      public void run() {
        page.tableInputSelected = true;
        page.databaseConnectionSelected = false;
        page.fileInputSelected = false;
        page.editForm.clear();
        page.editForm.add(page.tableInputEditForm);

        page.tableInputEditForm.setValues("url", "table");

        // Expected values
        try {
          expectedInput[0] = page.tableInputEditForm.getValue();
        } catch (InputValidationException | EntityStorageException e) {
          e.printStackTrace();
          fail();
        }

        // Execute
        page.saveObject();
      }
    };

    // Timer waiting for saving object
    Timer checkTimer = new Timer() {
      @Override
      public void run() {
        // Check result
        TestHelper.getAllTableInputs(
          new AsyncCallback<List<Input>>() {
            @Override
            public void onFailure(Throwable throwable) {
              fail();
              // Cleanup
              TestHelper.resetDatabaseSync();
            }

            @Override
            public void onSuccess(List<Input> con) {
              TableInput actualInput = (TableInput) con.get(0);
              assertEquals(expectedInput[0].getDatabaseConnection(), actualInput.getDatabaseConnection());
              assertEquals(expectedInput[0].getTableName(), actualInput.getTableName());

              // Cleanup
              TestHelper.resetDatabaseSync();
              finishTest();
            }
          });
      }
    };

    executeTimer.schedule(2000);
    checkTimer.schedule(4000);

    delayTestFinish(6000);
  }
*/

  /**
   * Test method for {@link DataSourcePage#saveObject()}
   */
 /* public void testStoreFileInput() throws EntityStorageException, InputValidationException {
    // Setup
    TestHelper.resetDatabaseSync();

    BasePage parent = new BasePage();
    DataSourcePage page = new DataSourcePage(parent);
    page.setMessageReceiver(new TabWrapper());

    page.fileInputEditForm.setFileName("file_name");
    page.tableInputSelected = false;
    page.databaseConnectionSelected = false;
    page.fileInputSelected = true;
    page.editForm.clear();
    page.editForm.add(page.fileInputEditForm);

    // Expected values
    final FileInput expectedInput = page.fileInputEditForm.getValue();

    // Execute
    page.saveObject();

    // Check result
    // Timer waiting for saving object
    Timer timer = new Timer() {
      @Override
      public void run() {
        TestHelper.getAllFileInputs(
            new AsyncCallback<List<Input>>() {
              @Override
              public void onFailure(Throwable throwable) {
                fail();
                // Cleanup
                TestHelper.resetDatabaseSync();
              }

              @Override
              public void onSuccess(List<Input> con) {
                FileInput input = (FileInput) con.get(0);
                assertEquals(expectedInput.getFileName(), input.getFileName());

                // Cleanup
                TestHelper.resetDatabaseSync();
                finishTest();
              }
            });
      }
    };
    timer.schedule(1000);

    delayTestFinish(2000);
  }*/

  /**
   * Test method for {@link DataSourcePage#saveObject()}
   * TODO fix FieldSerializer bug
   */
/*
  public void testStoreDatabaseConnection() throws EntityStorageException, InputValidationException {
    // Setup
    TestHelper.resetDatabaseSync();

    BasePage parent = new BasePage();
    DataSourcePage page = new DataSourcePage(parent);
    page.setMessageReceiver(new TabWrapper());

    page.databaseConnectionEditForm.setValues("url", DbSystem.DB2.name(), "user", "password");
    page.tableInputSelected = false;
    page.databaseConnectionSelected = true;
    page.fileInputSelected = false;
    page.editForm.clear();
    page.editForm.add(page.databaseConnectionEditForm);

    // Expected values
    final DatabaseConnection expectedInput = page.databaseConnectionEditForm.getValue();

    // Execute
    page.saveObject();

    // Check result
    // Timer waiting for saving object
    Timer timer = new Timer() {
      @Override
      public void run() {
        TestHelper.getAllDatabaseConnections(
            new AsyncCallback<List<DatabaseConnection>>() {
              @Override
              public void onFailure(Throwable throwable) {
                fail();
                // Cleanup
                TestHelper.resetDatabaseSync();
              }

              @Override
              public void onSuccess(List<DatabaseConnection> con) {
                DatabaseConnection connection = con.get(0);

                assertEquals(expectedInput.getPassword(), connection.getPassword());
                assertEquals(expectedInput.getUrl(), connection.getUrl());
                assertEquals(expectedInput.getUsername(), connection.getUsername());

                // Cleanup
                TestHelper.resetDatabaseSync();
                finishTest();
              }
            });
      }
    };
    timer.schedule(1000);

    delayTestFinish(2000);
  }
*/

  @Override
  public String getModuleName() {
    return "de.uni_potsdam.hpi.metanome.frontend.client.MetanomeTest";
  }
}
