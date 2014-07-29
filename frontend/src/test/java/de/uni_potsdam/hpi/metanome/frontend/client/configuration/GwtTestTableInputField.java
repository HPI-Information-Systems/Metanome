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

import de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper;
import de.uni_potsdam.hpi.metanome.frontend.client.TestHelper;
import de.uni_potsdam.hpi.metanome.frontend.client.helpers.InputValidationException;
import de.uni_potsdam.hpi.metanome.results_db.DatabaseConnection;
import de.uni_potsdam.hpi.metanome.results_db.EntityStorageException;
import de.uni_potsdam.hpi.metanome.results_db.TableInput;

import org.junit.Test;


public class GwtTestTableInputField extends GWTTestCase {

  /**
   * Test method for {@link TableInputField#getValue()}
   * <p/>
   */
  @Test
  public void testGetValue() throws EntityStorageException, InputValidationException {
    // Setup
    TestHelper.resetDatabaseSync();

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

    final TableInputField field = new TableInputField();
    field.setMessageReceiver(new TabWrapper());

    // Expected values
    final TableInput expectedInput = new TableInput();
    expectedInput.setDatabaseConnection(dbConnection);
    expectedInput.setTableName("table");

    Timer timer = new Timer() {
      @Override
      public void run() {
        // set the expected values in the table input field
        field.setValues(connectionId[0] + ": url", "table");

        try {
          if (expectedInput.equals(field.getValue())) {
            // Finished test successfully
            finishTest();
          }
        } catch (InputValidationException | EntityStorageException e) {
          fail();
        } finally {
          TestHelper.resetDatabaseSync();
        }
      }
    };

    // Waiting four seconds for asynchronous calls to finish.
    // Validate, if expected and actual objects are equal
    timer.schedule(4000);

    delayTestFinish(5000);
  }

  @Override
  public String getModuleName() {
    return "de.uni_potsdam.hpi.metanome.frontend.client.MetanomeTest";
  }
}
