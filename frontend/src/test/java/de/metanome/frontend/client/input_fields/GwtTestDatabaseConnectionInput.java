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

/**
 * Tests for {@link DatabaseConnectionInput}
 *
 * @author Jakob Zwiener
 */
public class GwtTestDatabaseConnectionInput extends GWTTestCase {

  /**
   * Test method for {@link DatabaseConnectionInput#DatabaseConnectionInput(boolean,
   * de.metanome.frontend.client.TabWrapper)} <p/> After calling the constructor the optional
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
        actualDatabaseConnectionInput = new DatabaseConnectionInput(expectedOptional, tabWrapper);

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
        new DatabaseConnectionInput(false, tabWrapper);

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

  @Override
  public String getModuleName() {
    return "de.metanome.frontend.client.MetanomeTest";
  }
}
