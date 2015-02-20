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
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementDatabaseConnection;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingDataSource;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingDatabaseConnection;
import de.metanome.algorithm_integration.configuration.DbSystem;
import de.metanome.backend.results_db.DatabaseConnection;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.TestHelper;
import de.metanome.frontend.client.helpers.InputValidationException;
import de.metanome.frontend.client.input_fields.DatabaseConnectionInput;

import java.util.ArrayList;

public class GwtTestDatabaseConnectionParameter extends GWTTestCase {

  private String aUrl = "url";
  private String aPassword = "password";
  private String aUser = "user";
  private DbSystem aSystem = DbSystem.DB2;

  /**
   * Tests the selection of a specific item corresponding to the given ConfigurationSetting.
   */
  public void testSelectDataSourceOnFilledDropdown()
      throws AlgorithmConfigurationException, InputValidationException {
    // Set up
    TestHelper.resetDatabaseSync();

    TabWrapper tabWrapper = new TabWrapper();

    DatabaseConnection databaseConnection = new DatabaseConnection();
    databaseConnection.setUrl(aUrl);
    databaseConnection.setPassword(aPassword);
    databaseConnection.setUsername(aUser);
    databaseConnection.setSystem(aSystem);

    DatabaseConnectionInput widget = new DatabaseConnectionInput(false, false, tabWrapper, new ArrayList<String>());
    ConfigurationSettingDatabaseConnection
        setting =
        new ConfigurationSettingDatabaseConnection(aUrl, aUser, aPassword, aSystem);

    widget.databaseConnections.put(aUrl, databaseConnection);
    widget.listBox.addValue("--");
    widget.listBox.addValue(aUrl);

    // Execute
    widget.selectDataSource(setting);

    // Check
    assertEquals(aUrl, widget.listBox.getSelectedValue());
    assertEquals(aUrl, widget.getValues().getDbUrl());
    assertEquals(aPassword, widget.getValues().getPassword());
    assertEquals(aUser, widget.getValues().getUsername());
    assertEquals(aSystem, widget.getValues().getSystem());

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

    String expectedIdentifier = aUrl + "; " + aUser + "; " + aSystem;

    DatabaseConnection databaseConnection = new DatabaseConnection();
    databaseConnection.setUrl(aUrl);
    databaseConnection.setPassword(aPassword);
    databaseConnection.setUsername(aUser);
    databaseConnection.setSystem(aSystem);

    ConfigurationSettingDatabaseConnection
        setting =
        new ConfigurationSettingDatabaseConnection(aUrl, aUser, aPassword, aSystem);

    ConfigurationRequirementDatabaseConnection
        configSpec =
        new ConfigurationRequirementDatabaseConnection("test");
    InputParameterDatabaseConnectionWidget
        dataSourceWidget =
        new InputParameterDatabaseConnectionWidget(configSpec, tabWrapper);

    dataSourceWidget.inputWidgets.get(0).listBox.addValue(aUrl);
    dataSourceWidget.inputWidgets.get(0).databaseConnections.put(aUrl, databaseConnection);

    // Execute
    dataSourceWidget.setDataSource(setting);

    assertTrue(
        ((DatabaseConnectionInput) dataSourceWidget.getWidget(0)).listBox.getValues().size() == 1);

    ConfigurationSettingDataSource retrievedSetting = null;
    retrievedSetting = (ConfigurationSettingDataSource) dataSourceWidget
        .getUpdatedSpecification()
        .getSettings()[0];

    assertEquals(expectedIdentifier, retrievedSetting.getValueAsString());

    // Cleanup
    TestHelper.resetDatabaseSync();
  }

  public void testCreateWithRangeNumber() throws AlgorithmConfigurationException {
    //Setup
    int maxValue = 5;
    ConfigurationRequirementDatabaseConnection
        specification =
        new ConfigurationRequirementDatabaseConnection("database connection", 3, maxValue);

    //Execute
    InputParameterDatabaseConnectionWidget
        widget =
        new InputParameterDatabaseConnectionWidget(specification, new TabWrapper());

    //Check
    assertEquals(maxValue, widget.inputWidgets.size());
    assertEquals(maxValue, widget.getWidgetCount());
    assertTrue(widget.inputWidgets.get(0).isRequired);
    assertTrue(widget.inputWidgets.get(1).isRequired);
    assertTrue(widget.inputWidgets.get(2).isRequired);
    assertFalse(widget.inputWidgets.get(3).isRequired);
    assertFalse(widget.inputWidgets.get(4).isRequired);
  }

  @Override
  public String getModuleName() {
    return "de.metanome.frontend.client.MetanomeTest";
  }
}
