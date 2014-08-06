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

package de.uni_potsdam.hpi.metanome.frontend.client.input_fields;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingSqlIterator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.DbSystem;
import de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper;
import de.uni_potsdam.hpi.metanome.frontend.client.TestHelper;
import de.uni_potsdam.hpi.metanome.results_db.DatabaseConnection;

/**
 * Tests for {@link de.uni_potsdam.hpi.metanome.frontend.client.input_fields.SqlIteratorInput}
 * 
 * @author Jakob Zwiener
 */
public class GwtTestSqlIteratorInput extends GWTTestCase {

  /**
   * Test method for
   * {@link de.uni_potsdam.hpi.metanome.frontend.client.input_fields.SqlIteratorInput#SqlIteratorInput(boolean, de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper)}
   * <p/>
   * After calling the constructor the optional parameter should be set correctly and all widgets
   * should be initialized.
   */
  public void testConstructor() {
    // Setup
    TabWrapper tabWrapper = new TabWrapper();

    // Expected values
    boolean expectedOptional = true;

    // Execute functionality
    SqlIteratorInput actualSqlIteratorInput = new SqlIteratorInput(expectedOptional, tabWrapper);

    // Check result
    assertEquals(expectedOptional, actualSqlIteratorInput.isOptional);
    assertEquals(2, actualSqlIteratorInput.getWidgetCount());
    assertNotNull(actualSqlIteratorInput.listbox);
  }

  /**
   * Test method for {@link SqlIteratorInput#getValues()} and
   * {@link de.uni_potsdam.hpi.metanome.frontend.client.input_fields.SqlIteratorInput#setValues(de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingSqlIterator)}
   * <p/>
   * The getValues and setValues methods should set and retrieve settings.
   */
  public void testGetSetValues() {
    // Setup
    TestHelper.resetDatabaseSync();

    final TabWrapper tabWrapper = new TabWrapper();

    DatabaseConnection dbConnection = new DatabaseConnection();
    dbConnection.setUrl("url");
    dbConnection.setPassword("password");
    dbConnection.setUsername("username");
    // TODO set system
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

    // Expected values
    final ConfigurationSettingSqlIterator expectedSetting =
        new ConfigurationSettingSqlIterator("url", "username", "password", DbSystem.DB2);

    // Initialize SqlIteratorInput (waiting for fetching all current database connections)
    final SqlIteratorInput[] sqlIteratorInput = new SqlIteratorInput[1];
    Timer setupTimer = new Timer() {
      @Override
      public void run() {
        sqlIteratorInput[0] = new SqlIteratorInput(false, tabWrapper);
      }
    };

    Timer executeTimer = new Timer() {
      @Override
      public void run() {
        // Execute functionality
        try {
          sqlIteratorInput[0].setValues(expectedSetting);
        } catch (AlgorithmConfigurationException e) {
          e.printStackTrace();
          fail();
        }

        ConfigurationSettingSqlIterator actualSetting = sqlIteratorInput[0].getValues();

        // Check result
        assertEquals(expectedSetting.getDbUrl(), actualSetting.getDbUrl());
        assertEquals(expectedSetting.getPassword(), actualSetting.getPassword());
        assertEquals(expectedSetting.getUsername(), actualSetting.getUsername());
        assertEquals(expectedSetting.getSystem(), actualSetting.getSystem());

        // Cleanup
        TestHelper.resetDatabaseSync();

        finishTest();
      }
    };

    delayTestFinish(12000);

    // Waiting for asynchronous calls to finish.
    setupTimer.schedule(4000);
    executeTimer.schedule(8000);
  }

  @Override
  public String getModuleName() {
    return "de.uni_potsdam.hpi.metanome.frontend.client.MetanomeTest";
  }
}
