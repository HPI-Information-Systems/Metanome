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

package de.metanome.frontend.client.parameter;

import com.google.gwt.junit.client.GWTTestCase;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementTableInput;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingDataSource;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingDatabaseConnection;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingTableInput;
import de.metanome.algorithm_integration.configuration.DbSystem;
import de.metanome.backend.results_db.DatabaseConnection;
import de.metanome.backend.results_db.TableInput;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.TestHelper;
import de.metanome.frontend.client.helpers.InputValidationException;
import de.metanome.frontend.client.input_fields.TableInputInput;


public class GwtTestTableInputParameter  extends GWTTestCase {

  String aTableName = "table";

  /**
   * Tests the selection of a specific item corresponding to the given ConfigurationSetting.
   */
  public void testSelectDataSourceOnFilledDropdown()
      throws AlgorithmConfigurationException, InputValidationException {
    // Set up
    TestHelper.resetDatabaseSync();

    TabWrapper tabWrapper = new TabWrapper();

    DatabaseConnection connection = new DatabaseConnection();
    connection.setUrl("url");
    connection.setPassword("pwd");
    connection.setSystem(DbSystem.DB2);
    connection.setUsername("user");

    TableInput tableInput = new TableInput();
    tableInput.setTableName(aTableName);
    tableInput.setDatabaseConnection(connection);

    TableInputInput widget = new TableInputInput(false, tabWrapper);
    ConfigurationSettingTableInput setting = new ConfigurationSettingTableInput();
    setting.setTable(aTableName);
    setting.setDatabaseConnection(new ConfigurationSettingDatabaseConnection("url", "user", "pwd", DbSystem.DB2));

    widget.listbox.addValue("--");
    widget.listbox.addValue(aTableName);
    widget.tableInputs.put(aTableName, tableInput);

    // Execute
    widget.selectDataSource(setting);

    //Check
    assertEquals(aTableName, widget.listbox.getSelectedValue());
    assertEquals(aTableName, widget.getValues().getTable());

    // Cleanup
    TestHelper.resetDatabaseSync();
  }

  /**
   * When setting a data source on the parent widget (InputParameter), it should be set in the first
   * child widget.
   */
  public void testSetDataSource() throws AlgorithmConfigurationException, InputValidationException {
    // Set up
    TestHelper.resetDatabaseSync();

    TabWrapper tabWrapper = new TabWrapper();

    String expectedIdentifier = aTableName + "; url; user; DB2";

    DatabaseConnection connection = new DatabaseConnection();
    connection.setUrl("url");
    connection.setPassword("pwd");
    connection.setSystem(DbSystem.DB2);
    connection.setUsername("user");

    TableInput tableInput = new TableInput();
    tableInput.setTableName(aTableName);
    tableInput.setDatabaseConnection(connection);

    ConfigurationSettingTableInput setting = new ConfigurationSettingTableInput();
    setting.setTable(aTableName);
    setting.setDatabaseConnection(new ConfigurationSettingDatabaseConnection("url", "user", "pwd", DbSystem.DB2));

    ConfigurationRequirementTableInput configSpec = new ConfigurationRequirementTableInput("test");
    InputParameterTableInputWidget
        dataSourceWidget =
        new InputParameterTableInputWidget(configSpec, tabWrapper);

    dataSourceWidget.inputWidgets.get(0).listbox.addValue(aTableName);
    dataSourceWidget.inputWidgets.get(0).tableInputs.put(aTableName, tableInput);

    // Execute
    dataSourceWidget.setDataSource(setting);

    // Check
    assertTrue(((TableInputInput) dataSourceWidget.getWidget(0)).listbox.getValues().size() == 1);

    ConfigurationSettingDataSource retrievedSetting = null;
    try {
      retrievedSetting = (ConfigurationSettingDataSource) dataSourceWidget
          .getUpdatedSpecification()
          .getSettings()[0];
    } catch (InputValidationException e) {
      TestHelper.resetDatabaseSync();
      e.printStackTrace();
      fail();
    }
    assertEquals(expectedIdentifier, retrievedSetting.getValueAsString());

    // Cleanup
    TestHelper.resetDatabaseSync();
  }


  @Override
  public String getModuleName() {
    return "de.metanome.frontend.client.MetanomeTest";
  }
}
