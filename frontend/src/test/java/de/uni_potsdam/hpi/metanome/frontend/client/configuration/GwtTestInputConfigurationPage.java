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

package de.uni_potsdam.hpi.metanome.frontend.client.configuration;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

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


public class GwtTestInputConfigurationPage extends GWTTestCase {

  @Test
  public void testStoreTableInput() throws EntityStorageException, InputValidationException {
    // Setup
    TestHelper.resetDatabaseSync();
    final boolean[] blocked = {true};

    BasePage parent = new BasePage();
    InputConfigurationPage page = new InputConfigurationPage(parent);
    page.setErrorReceiver(new TabWrapper());

    DatabaseConnection dbConnection = new DatabaseConnection();
    dbConnection.setUrl("url");
    dbConnection.setPassword("password");
    dbConnection.setUsername("db");
    TestHelper.storeDatabaseConnectionSync(dbConnection);

    page.tableInputField.setValues("1: url", "table");
    page.tableInputFieldSelected = true;
    page.dbFieldSelected = false;
    page.fileInputFieldSelected = false;
    page.content.clear();
    page.content.add(page.tableInputField);

    // Execute
    page.saveObject();

    // Expected values
    final TableInput expectedInput = page.tableInputField.getValue();

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

  @Test
  public void testStoreFileInput() throws EntityStorageException, InputValidationException {
    // Setup
    TestHelper.resetDatabaseSync();
    final boolean[] blocked = {true};

    BasePage parent = new BasePage();
    InputConfigurationPage page = new InputConfigurationPage(parent);
    page.setErrorReceiver(new TabWrapper());

    page.fileInputField.setFileName("file_name");
    page.tableInputFieldSelected = false;
    page.dbFieldSelected = false;
    page.fileInputFieldSelected = true;
    page.content.clear();
    page.content.add(page.fileInputField);

    // Execute
    page.saveObject();

    // Expected values
    final FileInput expectedInput = page.fileInputField.getValue();

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

  @Test
  public void testStoreDatabaseConnection() throws EntityStorageException, InputValidationException {
    // Setup
    TestHelper.resetDatabaseSync();
    final boolean[] blocked = {true};

    BasePage parent = new BasePage();
    InputConfigurationPage page = new InputConfigurationPage(parent);
    page.setErrorReceiver(new TabWrapper());

    page.dbField.setValues("url", "user", "password");
    page.tableInputFieldSelected = false;
    page.dbFieldSelected = true;
    page.fileInputFieldSelected = false;
    page.content.clear();
    page.content.add(page.dbField);

    // Execute
    page.saveObject();

    // Expected values
    final DatabaseConnection expectedInput = page.dbField.getValue();

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
