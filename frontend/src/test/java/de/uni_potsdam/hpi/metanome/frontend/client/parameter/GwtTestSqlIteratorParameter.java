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

package de.uni_potsdam.hpi.metanome.frontend.client.parameter;

import com.google.common.base.Joiner;
import com.google.gwt.junit.client.GWTTestCase;

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingDataSource;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingSqlIterator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationSqlIterator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.DbSystem;
import de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper;
import de.uni_potsdam.hpi.metanome.frontend.client.TestHelper;
import de.uni_potsdam.hpi.metanome.frontend.client.helpers.InputValidationException;
import de.uni_potsdam.hpi.metanome.frontend.client.input_fields.SqlIteratorInput;
import de.uni_potsdam.hpi.metanome.results_db.DatabaseConnection;

public class GwtTestSqlIteratorParameter extends GWTTestCase {

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

    SqlIteratorInput widget = new SqlIteratorInput(false, tabWrapper);
    ConfigurationSettingSqlIterator setting = new ConfigurationSettingSqlIterator(aUrl, aUser, aPassword, aSystem);

    widget.databaseConnections.put(aUrl, databaseConnection);
    widget.listbox.addValue("--");
    widget.listbox.addValue(aUrl);

    // Execute
    widget.selectDataSource(setting);

    // Check
    assertEquals(aUrl, widget.listbox.getSelectedValue());
    assertEquals(aUrl, widget.getValues().getDbUrl());
    assertEquals(aPassword, widget.getValues().getPassword());
    assertEquals(aUser, widget.getValues().getUsername());

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

    DatabaseConnection databaseConnection = new DatabaseConnection();
    databaseConnection.setUrl(aUrl);
    databaseConnection.setPassword(aPassword);
    databaseConnection.setUsername(aUser);

    ConfigurationSettingSqlIterator setting = new ConfigurationSettingSqlIterator(aUrl, aUser, aPassword, aSystem);

    ConfigurationSpecificationSqlIterator configSpec = new ConfigurationSpecificationSqlIterator("test");
    InputParameterSqlIteratorWidget dataSourceWidget = new InputParameterSqlIteratorWidget(configSpec, tabWrapper);

    dataSourceWidget.inputWidgets.get(0).listbox.addValue(aUrl);
    dataSourceWidget.inputWidgets.get(0).databaseConnections.put(aUrl, databaseConnection);

    // Execute
    dataSourceWidget.setDataSource(setting);

    assertTrue(((SqlIteratorInput) dataSourceWidget.getWidget(0)).listbox.getValues().size() == 1);

    ConfigurationSettingDataSource retrievedSetting = null;
      retrievedSetting = (ConfigurationSettingDataSource) dataSourceWidget
          .getUpdatedSpecification()
          .getSettings()[0];

    assertEquals(Joiner.on(';').join(aUrl, aUser, aPassword, aSystem), retrievedSetting.getValueAsString());

    // Cleanup
    TestHelper.resetDatabaseSync();
  }

  @Override
  public String getModuleName() {
    return "de.uni_potsdam.hpi.metanome.frontend.client.MetanomeTest";
  }
}
