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

package de.metanome.frontend.client.input_fields;

import com.google.gwt.junit.client.GWTTestCase;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingDatabaseConnection;
import de.metanome.algorithm_integration.configuration.DbSystem;
import de.metanome.backend.results_db.DatabaseConnection;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.TestHelper;
import de.metanome.frontend.client.helpers.InputValidationException;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for {@link DatabaseConnectionInput}
 *
 * @author Jakob Zwiener
 */
public class GwtTestDatabaseConnectionInput extends GWTTestCase {

  /**
   * Test method for {@link DatabaseConnectionInput#DatabaseConnectionInput(boolean, de.metanome.frontend.client.TabWrapper, java.util.List)} <p/> After calling the constructor the optional
   * parameter should be set correctly and all widgets should be initialized.
   */
  public void testConstructor() {
    // Set up
    TestHelper.resetDatabaseSync();

    TabWrapper tabWrapper = new TabWrapper();

    // Expected values
    boolean expectedOptional = true;

    // Execute functionality
    DatabaseConnectionInput
        actualDatabaseConnectionInput = new DatabaseConnectionInput(expectedOptional, tabWrapper, new ArrayList<String>());

    // Check result
    assertEquals(expectedOptional, actualDatabaseConnectionInput.isOptional);
    assertEquals(2, actualDatabaseConnectionInput.getWidgetCount());
    assertNotNull(actualDatabaseConnectionInput.listbox);

    // Cleanup
    TestHelper.resetDatabaseSync();
  }

  /**
   * Test method for {@link DatabaseConnectionInput#getValues()} and {@link
   * DatabaseConnectionInput#setValues(de.metanome.algorithm_integration.configuration.ConfigurationSettingDatabaseConnection)}
   * <p/> The getValues and setValues methods should set and retrieve settings.
   */
  public void testGetSetValues() throws AlgorithmConfigurationException {
    // Set up
    TestHelper.resetDatabaseSync();

    TabWrapper tabWrapper = new TabWrapper();

    DatabaseConnection dbConnection = new DatabaseConnection();
    dbConnection.setUrl("url");
    dbConnection.setPassword("password");
    dbConnection.setUsername("username");
    dbConnection.setSystem(DbSystem.DB2);

    // Expected values
    ConfigurationSettingDatabaseConnection expectedSetting =
        new ConfigurationSettingDatabaseConnection("url", "username", "password", DbSystem.DB2);

    // Initialize DatabaseConnectionInput (waiting for fetching all current database connections)
    DatabaseConnectionInput
        databaseConnectionInput =
        new DatabaseConnectionInput(false, tabWrapper, new ArrayList<String>());

    databaseConnectionInput.databaseConnections.put("url", dbConnection);
    databaseConnectionInput.listbox.addValue("--");
    databaseConnectionInput.listbox.addValue("url");

    // Execute functionality
    databaseConnectionInput.setValues(expectedSetting);

    ConfigurationSettingDatabaseConnection actualSetting = null;
    try {
      actualSetting = databaseConnectionInput.getValues();
    } catch (InputValidationException e) {
      fail();
    }

    // Check result
    assertEquals(expectedSetting.getDbUrl(), actualSetting.getDbUrl());
    assertEquals(expectedSetting.getPassword(), actualSetting.getPassword());
    assertEquals(expectedSetting.getUsername(), actualSetting.getUsername());
    assertEquals(expectedSetting.getSystem(), actualSetting.getSystem());

    // Cleanup
    TestHelper.resetDatabaseSync();
  }


  /**
   * Test method for {@link de.metanome.frontend.client.input_fields.DatabaseConnectionInput#handleSuccess(java.util.List)}
   */
  public void testHandleSuccess() {
    // Set up
    DatabaseConnection dbConnection1 = new DatabaseConnection();
    dbConnection1.setUrl("url1");
    dbConnection1.setPassword("password1");
    dbConnection1.setUsername("username1");
    dbConnection1.setSystem(DbSystem.DB2);

    DatabaseConnection dbConnection2 = new DatabaseConnection();
    dbConnection2.setUrl("url2");
    dbConnection2.setPassword("password2");
    dbConnection2.setUsername("username2");
    dbConnection2.setSystem(DbSystem.Oracle);

    List<DatabaseConnection> databaseConnectionList = new ArrayList<>();
    databaseConnectionList.add(dbConnection1);
    databaseConnectionList.add(dbConnection2);

    List<String> acceptedSystems = new ArrayList<>();
    acceptedSystems.add(DbSystem.Oracle.name());

    DatabaseConnectionInput
        databaseConnectionInput =
        new DatabaseConnectionInput(false, new TabWrapper(), acceptedSystems);

    // Expected
    // Execute
    databaseConnectionInput.handleSuccess(databaseConnectionList);

    // Check
    assertEquals(2, databaseConnectionInput.listbox.getValues().size());
    assertTrue(databaseConnectionInput.listbox.getValues().contains(dbConnection2.getIdentifier()));
    assertFalse(databaseConnectionInput.listbox.getValues().contains(dbConnection1.getIdentifier()));
  }

  /**
   * Test method for {@link de.metanome.frontend.client.input_fields.DatabaseConnectionInput#handleSuccess(java.util.List)}
   */
  public void testHandleSuccessWithNotListedDbSystem() {
    // Set up
    DatabaseConnection dbConnection1 = new DatabaseConnection();
    dbConnection1.setUrl("url1");
    dbConnection1.setPassword("password1");
    dbConnection1.setUsername("username1");
    dbConnection1.setSystem(DbSystem.DB2);

    DatabaseConnection dbConnection2 = new DatabaseConnection();
    dbConnection2.setUrl("url2");
    dbConnection2.setPassword("password2");
    dbConnection2.setUsername("username2");
    dbConnection2.setSystem(DbSystem.Oracle);

    List<DatabaseConnection> databaseConnectionList = new ArrayList<>();
    databaseConnectionList.add(dbConnection1);
    databaseConnectionList.add(dbConnection2);

    List<String> acceptedSystems = new ArrayList<>();
    acceptedSystems.add(DbSystem.HANA.name());

    DatabaseConnectionInput
        databaseConnectionInput =
        new DatabaseConnectionInput(false, new TabWrapper(), acceptedSystems);

    // Expected
    // Execute
    databaseConnectionInput.handleSuccess(databaseConnectionList);

    // Check
    assertEquals(1, databaseConnectionInput.listbox.getValues().size());
    assertFalse(databaseConnectionInput.listbox.getValues().contains(dbConnection2.getIdentifier()));
    assertFalse(databaseConnectionInput.listbox.getValues().contains(dbConnection1.getIdentifier()));
  }

  @Override
  public String getModuleName() {
    return "de.metanome.frontend.client.MetanomeTest";
  }
}
