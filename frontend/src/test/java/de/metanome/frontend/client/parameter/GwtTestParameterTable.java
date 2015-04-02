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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementBoolean;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementDatabaseConnection;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementFileInput;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementInteger;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementListBox;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementRelationalInput;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementString;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementTableInput;
import de.metanome.algorithm_integration.configuration.DbSystem;
import de.metanome.backend.results_db.DatabaseConnection;
import de.metanome.backend.results_db.FileInput;
import de.metanome.backend.results_db.TableInput;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.TestHelper;
import de.metanome.frontend.client.helpers.InputValidationException;
import de.metanome.frontend.client.input_fields.DatabaseConnectionInput;
import de.metanome.frontend.client.input_fields.FileInputInput;
import de.metanome.frontend.client.input_fields.IntegerInput;
import de.metanome.frontend.client.input_fields.RelationalInputInput;
import de.metanome.frontend.client.input_fields.TableInputInput;

import java.util.ArrayList;
import java.util.List;

public class GwtTestParameterTable extends GWTTestCase {

  /**
   * Test method for {@link de.metanome.frontend.client.parameter.ParameterTable#ParameterTable(java.util.List,
   * de.metanome.algorithm_integration.configuration.ConfigurationSettingDataSource,
   * de.metanome.frontend.client.TabWrapper)}
   */
  public void testParameterTable() {
    // Setup
    TestHelper.resetDatabaseSync();

    List<ConfigurationRequirement> paramList = new ArrayList<>();

    ArrayList<String> values = new ArrayList<>();
    values.add("Column 1");
    values.add("Column 3");
    values.add("Column 2");

    ConfigurationRequirementString
        ConfigurationSpecificationString =
        new ConfigurationRequirementString("Filename");
    ConfigurationRequirementBoolean
        ConfigurationSpecificationBoolean =
        new ConfigurationRequirementBoolean("Omit warnings");
    ConfigurationRequirementFileInput
        ConfigurationSpecificationCsvFile =
        new ConfigurationRequirementFileInput("inputData");
    ConfigurationRequirementInteger
        ConfigurationSpecificationInteger =
        new ConfigurationRequirementInteger("NumberOfTables");
    ConfigurationRequirementListBox
        ConfigurationSpecificationListBox =
        new ConfigurationRequirementListBox("listBox", values);

    paramList.add(ConfigurationSpecificationString);
    paramList.add(ConfigurationSpecificationBoolean);
    paramList.add(ConfigurationSpecificationCsvFile);
    paramList.add(ConfigurationSpecificationListBox);
    paramList.add(ConfigurationSpecificationInteger);

    //Execute
    ParameterTable pt = new ParameterTable(paramList, null, new TabWrapper());

    //Check
    assertEquals(6, pt.table.getRowCount());

    // - STRING row
    assertEquals(2, pt.table.getCellCount(0));
    assertEquals(InputParameterStringWidget.class, pt.table.getWidget(0, 1).getClass());

    // - BOOL row
    assertEquals(2, pt.table.getCellCount(1));
    assertEquals(InputParameterBooleanWidget.class, pt.table.getWidget(1, 1).getClass());

    // - CSV FILE row
    assertEquals(2, pt.table.getCellCount(2));
    assertEquals(InputParameterFileInputWidget.class, pt.table.getWidget(2, 1).getClass());

    // - LIST BOX row
    assertEquals(2, pt.table.getCellCount(3));
    assertEquals(InputParameterListBoxWidget.class, pt.table.getWidget(3, 1).getClass());

    // - INTEGER row
    assertEquals(2, pt.table.getCellCount(4));
    assertEquals(InputParameterIntegerWidget.class, pt.table.getWidget(4, 1).getClass());

    // - Radio buttons
    assertEquals(FlexTable.class, pt.getWidget(1).getClass());

    // - Memory
    assertEquals(FlowPanel.class, pt.getWidget(2).getClass());

    // - Submit button row
    assertEquals(Button.class, pt.getWidget(3).getClass());

    // Cleanup
    TestHelper.resetDatabaseSync();
  }

  /**
   * Test method for {@link ParameterTable#getConfigurationSpecificationDataSourcesWithValues()} and
   * for {@link ParameterTable#getConfigurationSpecificationsWithValues()}
   */
  public void testRetrieveSimpleParameterValues()
      throws InputValidationException, AlgorithmConfigurationException {
    //Setup
    TestHelper.resetDatabaseSync();

    ArrayList<ConfigurationRequirement> paramList = new ArrayList<>();

    ArrayList<String> values = new ArrayList<>();
    values.add("Column 1");
    values.add("Column 3");
    values.add("Column 2");

    final ConfigurationRequirementString
        ConfigurationSpecificationString =
        new ConfigurationRequirementString("string");
    final ConfigurationRequirementBoolean
        ConfigurationSpecificationBoolean =
        new ConfigurationRequirementBoolean("bool");
    final ConfigurationRequirementFileInput
        ConfigurationSpecificationCsvFile =
        new ConfigurationRequirementFileInput("csv");
    final ConfigurationRequirementDatabaseConnection
        ConfigurationSpecificationSQLIterator =
        new ConfigurationRequirementDatabaseConnection("sql");
    final ConfigurationRequirementInteger
        ConfigurationSpecificationInteger =
        new ConfigurationRequirementInteger("integer");
    final ConfigurationRequirementListBox
        ConfigurationSpecificationListBox =
        new ConfigurationRequirementListBox("listBox", values);
    final ConfigurationRequirementRelationalInput
        ConfigurationSpecificationRelationalInput =
        new ConfigurationRequirementRelationalInput("relational");
    final ConfigurationRequirementTableInput
        ConfigurationSpecificationTableInput =
        new ConfigurationRequirementTableInput("table");

    ConfigurationSpecificationString.setRequired(false);
    ConfigurationSpecificationBoolean.setRequired(false);
    ConfigurationSpecificationCsvFile.setRequired(false);
    ConfigurationSpecificationSQLIterator.setRequired(false);
    ConfigurationSpecificationInteger.setRequired(false);
    ConfigurationSpecificationListBox.setRequired(false);
    ConfigurationSpecificationRelationalInput.setRequired(false);
    ConfigurationSpecificationTableInput.setRequired(false);

    paramList.add(ConfigurationSpecificationString);
    paramList.add(ConfigurationSpecificationBoolean);
    paramList.add(ConfigurationSpecificationCsvFile);
    paramList.add(ConfigurationSpecificationSQLIterator);
    paramList.add(ConfigurationSpecificationInteger);
    paramList.add(ConfigurationSpecificationListBox);
    paramList.add(ConfigurationSpecificationRelationalInput);
    paramList.add(ConfigurationSpecificationTableInput);

    final ParameterTable pt = new ParameterTable(paramList, null, new TabWrapper());
    enterNumber((InputParameterIntegerWidget) pt.table.getWidget(4, 1));
    setDatabaseConnection((InputParameterDatabaseConnectionWidget) pt.table.getWidget(3, 1));
    setFileInput((InputParameterFileInputWidget) pt.table.getWidget(2, 1));
    setRelationalInput((InputParameterRelationalInputWidget) pt.table.getWidget(6, 1));
    setTableInput((InputParameterTableInputWidget) pt.table.getWidget(7, 1));

    //Execute
    List<ConfigurationRequirement> retrievedParams = null;
    List<ConfigurationRequirement> retrievedDataSources = null;
    try {
      retrievedParams = pt.getConfigurationSpecificationsWithValues();
      retrievedDataSources = pt.getConfigurationSpecificationDataSourcesWithValues();
    } catch (InputValidationException e) {
      e.printStackTrace();
      fail();
    }

    //Check
    assertTrue(retrievedParams.contains(ConfigurationSpecificationString));
    assertTrue(retrievedParams.contains(ConfigurationSpecificationBoolean));
    assertTrue(retrievedParams.contains(ConfigurationSpecificationListBox));
    assertTrue(retrievedParams.contains(ConfigurationSpecificationInteger));
    assertTrue(!retrievedParams.contains(ConfigurationSpecificationCsvFile));
    assertTrue(!retrievedParams.contains(ConfigurationSpecificationSQLIterator));
    assertTrue(!retrievedParams.contains(ConfigurationSpecificationTableInput));
    assertTrue(!retrievedParams.contains(ConfigurationSpecificationRelationalInput));

    assertTrue(!retrievedDataSources.contains(ConfigurationSpecificationString));
    assertTrue(!retrievedDataSources.contains(ConfigurationSpecificationBoolean));
    assertTrue(!retrievedDataSources.contains(ConfigurationSpecificationInteger));
    assertTrue(!retrievedDataSources.contains(ConfigurationSpecificationListBox));
    assertTrue(retrievedDataSources.contains(ConfigurationSpecificationCsvFile));
    assertTrue(retrievedDataSources.contains(ConfigurationSpecificationSQLIterator));
    assertTrue(retrievedDataSources.contains(ConfigurationSpecificationTableInput));
    assertTrue(retrievedDataSources.contains(ConfigurationSpecificationRelationalInput));

    // Cleanup
    TestHelper.resetDatabaseSync();
  }

  private void setFileInput(InputParameterFileInputWidget widget) {
    FileInput fileInput = new FileInput("name");

    for (FileInputInput fileInputInput : widget.inputWidgets) {
      fileInputInput.listBox.addValue("name");
      fileInputInput.fileInputs.put("name", fileInput);
      fileInputInput.listBox.setSelectedValue("name");
    }
  }

  private void setTableInput(InputParameterTableInputWidget widget) {
    DatabaseConnection dbConnection = new DatabaseConnection();
    dbConnection.setUrl("url");
    dbConnection.setPassword("password");
    dbConnection.setUsername("username");
    dbConnection.setSystem(DbSystem.DB2);

    TableInput tableInput = new TableInput();
    tableInput.setTableName("table");
    tableInput.setDatabaseConnection(dbConnection);

    for (TableInputInput tableInputInput : widget.inputWidgets) {
      tableInputInput.listBox.addValue("table;url;username;DB2");
      tableInputInput.tableInputs.put("table;url;username;DB2", tableInput);
      tableInputInput.listBox.setSelectedValue("table;url;username;DB2");
    }
  }

  private void setRelationalInput(InputParameterRelationalInputWidget widget) {
    FileInput fileInput = new FileInput("name");

    for (RelationalInputInput relationalInputInput : widget.inputWidgets) {
      relationalInputInput.listbox.addValue("name");
      relationalInputInput.inputs.put("name", fileInput);
      relationalInputInput.listbox.setSelectedValue("name");
    }
  }

  private void enterNumber(InputParameterIntegerWidget widget) {
    for (IntegerInput integerInput : widget.inputWidgets) {
      integerInput.textbox.setValue(7);
    }
  }

  private void setDatabaseConnection(InputParameterDatabaseConnectionWidget widget) {
    DatabaseConnection dbConnection = new DatabaseConnection();
    dbConnection.setUrl("url");
    dbConnection.setPassword("password");
    dbConnection.setUsername("username");
    dbConnection.setSystem(DbSystem.DB2);

    for (DatabaseConnectionInput databaseConnectionInput : widget.inputWidgets) {
      databaseConnectionInput.listBox.addValue("url");
      databaseConnectionInput.databaseConnections.put("url", dbConnection);
      databaseConnectionInput.listBox.setSelectedValue("url");
    }
  }

  /**
   * Test method for {@link de.metanome.frontend.client.parameter.WidgetFactory#buildWidget(de.metanome.algorithm_integration.configuration.ConfigurationRequirement,
   * de.metanome.frontend.client.TabWrapper)}
   */
  public void testConfigurationSpecificationWidgetCreation() throws InputValidationException {
    // Setup
    TestHelper.resetDatabaseSync();

    ArrayList<String> values = new ArrayList<>();
    values.add("Column 1");
    values.add("Column 3");
    values.add("Column 2");

    String identifierString = "stringParam";
    ConfigurationRequirement stringParam = new ConfigurationRequirementString(identifierString);
    String identifierInteger = "integerParam";
    ConfigurationRequirement
        integerParam =
        new ConfigurationRequirementInteger(identifierInteger);
    String identifierBoolean = "boolParam";
    ConfigurationRequirement boolParam = new ConfigurationRequirementBoolean(identifierBoolean);
    String identifierCsv = "csvParam";
    ConfigurationRequirement csvParam = new ConfigurationRequirementFileInput(identifierCsv);
    String identifierSql = "sqlParam";
    ConfigurationRequirement
        sqlParam =
        new ConfigurationRequirementDatabaseConnection(identifierSql);
    String identifierListbox = "listboxParam";
    ConfigurationRequirementListBox
        listboxParam =
        new ConfigurationRequirementListBox(identifierListbox, values);
    String identifierRelationalInput = "relationalParam";
    ConfigurationRequirementRelationalInput relationalParam = new ConfigurationRequirementRelationalInput(identifierRelationalInput);
    String identifierTable = "table";
    ConfigurationRequirementTableInput tableParam = new ConfigurationRequirementTableInput(identifierTable);

    TabWrapper tabWrapper = new TabWrapper();

    //Execute
    InputParameterWidget stringWidget = WidgetFactory.buildWidget(stringParam, tabWrapper);
    InputParameterWidget boolWidget = WidgetFactory.buildWidget(boolParam, tabWrapper);
    InputParameterWidget csvWidget = WidgetFactory.buildWidget(csvParam, tabWrapper);
    InputParameterWidget integerWidget = WidgetFactory.buildWidget(integerParam, tabWrapper);
    InputParameterWidget sqlWidget = WidgetFactory.buildWidget(sqlParam, tabWrapper);
    InputParameterWidget listboxWidget = WidgetFactory.buildWidget(listboxParam, tabWrapper);
    InputParameterWidget tableWidget = WidgetFactory.buildWidget(tableParam, tabWrapper);
    InputParameterWidget relationalWidget = WidgetFactory.buildWidget(relationalParam, tabWrapper);

    //Check
    assertTrue(stringWidget instanceof InputParameterStringWidget);
    assertEquals(identifierString, stringWidget.getSpecification().getIdentifier());

    assertTrue(boolWidget instanceof InputParameterBooleanWidget);
    assertEquals(identifierBoolean, boolWidget.getSpecification().getIdentifier());

    assertTrue(csvWidget instanceof InputParameterFileInputWidget);
    assertEquals(identifierCsv, csvWidget.getSpecification().getIdentifier());

    assertTrue(sqlWidget instanceof InputParameterDatabaseConnectionWidget);
    assertEquals(identifierSql, sqlWidget.getSpecification().getIdentifier());

    assertTrue(listboxWidget instanceof InputParameterListBoxWidget);
    assertEquals(identifierListbox, listboxWidget.getSpecification().getIdentifier());

    assertTrue(integerWidget instanceof InputParameterIntegerWidget);
    assertEquals(identifierInteger, integerWidget.getSpecification().getIdentifier());

    assertTrue(tableWidget instanceof InputParameterTableInputWidget);
    assertEquals(identifierTable, tableWidget.getSpecification().getIdentifier());

    assertTrue(relationalWidget instanceof InputParameterRelationalInputWidget);
    assertEquals(identifierRelationalInput, relationalWidget.getSpecification().getIdentifier());

    // Cleanup
    TestHelper.resetDatabaseSync();
  }

  /**
   * Test method for {@link de.metanome.frontend.client.parameter.WidgetFactory#buildWidget(de.metanome.algorithm_integration.configuration.ConfigurationRequirement,
   * de.metanome.frontend.client.TabWrapper)}
   */
  public void testMultipleValuesWidgetCreation() throws AlgorithmConfigurationException {
    // Setup
    TestHelper.resetDatabaseSync();

    ArrayList<String> values = new ArrayList<>();
    values.add("Column 1");
    values.add("Column 3");
    values.add("Column 2");

    String identifierString = "stringParam";
    ConfigurationRequirement
        stringParam =
        new ConfigurationRequirementString(identifierString, 2);
    String identifierInteger = "integerParam";
    ConfigurationRequirement
        integerParam =
        new ConfigurationRequirementInteger(identifierInteger, 2);
    String identifierBoolean = "boolParam";
    ConfigurationRequirement
        boolParam =
        new ConfigurationRequirementBoolean(identifierBoolean, 2);
    String identifierCsv = "csvParam";
    ConfigurationRequirement csvParam = new ConfigurationRequirementFileInput(identifierCsv, 2);
    String identifierSql = "sqlParam";
    ConfigurationRequirement
        sqlParam =
        new ConfigurationRequirementDatabaseConnection(identifierSql, 2);
    String identifierListbox = "listboxParam";
    ConfigurationRequirementListBox
        listboxParam =
        new ConfigurationRequirementListBox(identifierListbox, values, 2);
    String identifierRelationalInput = "relationalParam";
    ConfigurationRequirementRelationalInput relationalParam = new ConfigurationRequirementRelationalInput(identifierRelationalInput, 2);
    String identifierTable = "table";
    ConfigurationRequirementTableInput tableParam = new ConfigurationRequirementTableInput(identifierTable, 2);

    TabWrapper tabWrapper = new TabWrapper();

    //Execute
    InputParameterWidget stringWidget = WidgetFactory.buildWidget(stringParam, tabWrapper);
    InputParameterWidget boolWidget = WidgetFactory.buildWidget(boolParam, tabWrapper);
    InputParameterWidget csvWidget = WidgetFactory.buildWidget(csvParam, tabWrapper);
    InputParameterWidget integerWidget = WidgetFactory.buildWidget(integerParam, tabWrapper);
    InputParameterWidget sqlWidget = WidgetFactory.buildWidget(sqlParam, tabWrapper);
    InputParameterWidget listboxWidget = WidgetFactory.buildWidget(listboxParam, tabWrapper);
    InputParameterWidget tableWidget = WidgetFactory.buildWidget(tableParam, tabWrapper);
    InputParameterWidget relationalWidget = WidgetFactory.buildWidget(relationalParam, tabWrapper);

    //Check
    assertTrue(stringWidget instanceof InputParameterStringWidget);
    assertEquals(2, ((InputParameterStringWidget) stringWidget).getWidgetCount());

    assertTrue(boolWidget instanceof InputParameterBooleanWidget);
    assertEquals(2, ((InputParameterBooleanWidget) boolWidget).getWidgetCount());

    assertTrue(integerWidget instanceof InputParameterIntegerWidget);
    assertEquals(2, ((InputParameterIntegerWidget) integerWidget).getWidgetCount());

    assertTrue(csvWidget instanceof InputParameterFileInputWidget);
    assertEquals(2, ((InputParameterFileInputWidget) csvWidget).getWidgetCount());

    assertTrue(sqlWidget instanceof InputParameterDatabaseConnectionWidget);
    assertEquals(2, ((InputParameterDatabaseConnectionWidget) sqlWidget).getWidgetCount());

    assertTrue(listboxWidget instanceof InputParameterListBoxWidget);
    assertEquals(2, ((InputParameterListBoxWidget) listboxWidget).getWidgetCount());

    assertTrue(tableWidget instanceof InputParameterTableInputWidget);
    assertEquals(2, ((InputParameterTableInputWidget) tableWidget).getWidgetCount());

    assertTrue(relationalWidget instanceof InputParameterRelationalInputWidget);
    assertEquals(2, ((InputParameterRelationalInputWidget) relationalWidget).getWidgetCount());

    // Cleanup
    TestHelper.resetDatabaseSync();
  }

  /**
   * Test method for {@link ParameterTable#getConfigurationSpecificationDataSourcesWithValues()}
   * TODO fix FieldSerializer bug
   */
/*
  public void testSetPrimaryDataSource() {
    // Setup
    TestHelper.resetDatabaseSync();

    FileInput fileInput = new FileInput();
    fileInput.setFileName("/inputA.csv");
    TestHelper.storeFileInputSync(fileInput);

    final ConfigurationSettingCsvFile primaryDataSource = new ConfigurationSettingCsvFile();
    primaryDataSource.setFileName("/inputA.csv");

    final ArrayList<ConfigurationSpecification> paramList = new ArrayList<>();
    ConfigurationSpecificationCsvFile ConfigurationSpecificationCsvFile = new ConfigurationSpecificationCsvFile("csv");
    paramList.add(ConfigurationSpecificationCsvFile);

    final ParameterTable pt = new ParameterTable(paramList, primaryDataSource, new TabWrapper());

    Timer executeTimer = new Timer() {
      @Override
      public void run() {
        //Check
        boolean foundDataSource = false;
          try {
            for (ConfigurationSpecification dataSource : pt.getConfigurationSpecificationDataSourcesWithValues()){
              for (Object setting : dataSource.getSettings()) {
                if(((ConfigurationSettingDataSource) setting).getValueAsString().equals(primaryDataSource.getValueAsString()))
                  foundDataSource = true;
              }
            }
          } catch (InputValidationException e) {
            TestHelper.resetDatabaseSync();
            e.printStackTrace();
            fail();
          }
          assertTrue(foundDataSource);

        ListBoxInput
            listBox = ((InputParameterCsvFileWidget) pt.getWidget(0, 1)).inputWidgets.get(0).listBox;
        assertEquals(primaryDataSource.getValueAsString(), listBox.getSelectedValue());

        // Cleanup
        TestHelper.resetDatabaseSync();

        finishTest();
      }
    };
    // Waiting for asynchronous calls to finish.
    executeTimer.schedule(2000);

    delayTestFinish(4000);
  }
*/
  @Override
  public String getModuleName() {
    return "de.metanome.frontend.client.MetanomeTest";
  }
}
