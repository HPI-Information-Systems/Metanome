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
import de.metanome.algorithm_integration.configuration.ConfigurationSettingTableInput;
import de.metanome.algorithm_integration.configuration.DbSystem;
import de.metanome.backend.results_db.DatabaseConnection;
import de.metanome.backend.results_db.TableInput;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.TestHelper;
import de.metanome.frontend.client.helpers.InputValidationException;


public class GwtTestTableInputInput extends GWTTestCase {

  /**
   * Test method for {@link de.metanome.frontend.client.input_fields.TableInputInput#TableInputInput(boolean,
   * de.metanome.frontend.client.TabWrapper)} <p/> After calling the constructor the optional
   * parameter should be set correctly and all widgets should be initialized.
   */
  public void testConstructor() {
    // Set up
    TestHelper.resetDatabaseSync();

    // Setup
    TabWrapper tabWrapper = new TabWrapper();

    // Expected values
    boolean expectedOptional = true;

    // Execute functionality
    TableInputInput actualTableInputInput = new TableInputInput(expectedOptional, tabWrapper);

    // Check result
    assertEquals(expectedOptional, actualTableInputInput.isOptional);
    assertEquals(2, actualTableInputInput.getWidgetCount());
    assertNotNull(actualTableInputInput.listbox);

    // Cleanup
    TestHelper.resetDatabaseSync();
  }

  /**
   * Test method for {@link TableInputInput#getValues()} and
   * {@link TableInputInput#setValues(de.metanome.algorithm_integration.configuration.ConfigurationSettingTableInput)}
   * <p/> The getValues and setValues methods should set and retrieve settings.
   */
  public void testGetSetValues() {
    // Set up
    TestHelper.resetDatabaseSync();

    DatabaseConnection databaseConnection = new DatabaseConnection();
    databaseConnection.setUrl("url");
    databaseConnection.setUsername("user");
    databaseConnection.setPassword("pwd");
    databaseConnection.setSystem(DbSystem.DB2);

    TableInput tableInput = new TableInput();
    tableInput.setTableName("table");
    tableInput.setDatabaseConnection(new DatabaseConnection());

    // Expected values
    final ConfigurationSettingTableInput expectedSetting =
        new ConfigurationSettingTableInput();
    expectedSetting.setTable("table");
    expectedSetting.setDatabaseConnection(new ConfigurationSettingDatabaseConnection("url", "user", "pwd",
                                                                                     DbSystem.DB2));

    // Initialize TableInputInput (waiting for fetching all current file inputs)
    final TableInputInput tableInputInputs = new TableInputInput(false, new TabWrapper());

    tableInputInputs.listbox.addValue(tableInput.getIdentifier());
    tableInputInputs.tableInputs.put(tableInput.getIdentifier(), tableInput);

    try {
      tableInputInputs.setValues(expectedSetting);
    } catch (AlgorithmConfigurationException e) {
      fail();
    }

    ConfigurationSettingTableInput actualSetting = null;
    try {
      actualSetting = tableInputInputs.getValues();
    } catch (InputValidationException e) {
      fail();
    }

    // Check result
    assertEquals(expectedSetting.getTable(), actualSetting.getTable());

    // Cleanup
    TestHelper.resetDatabaseSync();
  }

  @Override
  public String getModuleName() {
    return "de.metanome.frontend.client.MetanomeTest";
  }
}
