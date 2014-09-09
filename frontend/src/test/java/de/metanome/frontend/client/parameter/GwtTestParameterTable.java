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

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.metanome.algorithm_integration.configuration.ConfigurationSpecificationBoolean;
import de.metanome.algorithm_integration.configuration.ConfigurationSpecificationCsvFile;
import de.metanome.algorithm_integration.configuration.ConfigurationSpecificationInteger;
import de.metanome.algorithm_integration.configuration.ConfigurationSpecificationListBox;
import de.metanome.algorithm_integration.configuration.ConfigurationSpecificationSqlIterator;
import de.metanome.algorithm_integration.configuration.ConfigurationSpecificationString;
import de.metanome.algorithm_integration.configuration.DbSystem;
import de.metanome.backend.results_db.DatabaseConnection;
import de.metanome.backend.results_db.FileInput;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.TestHelper;
import de.metanome.frontend.client.helpers.InputValidationException;
import de.metanome.frontend.client.input_fields.CsvFileInput;
import de.metanome.frontend.client.input_fields.IntegerInput;
import de.metanome.frontend.client.input_fields.SqlIteratorInput;

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

    List<ConfigurationSpecification> paramList = new ArrayList<>();

    ArrayList<String> values = new ArrayList<>();
    values.add("Column 1");
    values.add("Column 3");
    values.add("Column 2");

    ConfigurationSpecificationString
        ConfigurationSpecificationString =
        new ConfigurationSpecificationString("Filename");
    ConfigurationSpecificationBoolean
        ConfigurationSpecificationBoolean =
        new ConfigurationSpecificationBoolean("Omit warnings");
    ConfigurationSpecificationCsvFile
        ConfigurationSpecificationCsvFile =
        new ConfigurationSpecificationCsvFile("inputData");
    ConfigurationSpecificationInteger
        ConfigurationSpecificationInteger =
        new ConfigurationSpecificationInteger("NumberOfTables");
    ConfigurationSpecificationListBox
        ConfigurationSpecificationListBox =
        new ConfigurationSpecificationListBox("listBox", values);

    paramList.add(ConfigurationSpecificationString);
    paramList.add(ConfigurationSpecificationBoolean);
    paramList.add(ConfigurationSpecificationCsvFile);
    paramList.add(ConfigurationSpecificationListBox);
    paramList.add(ConfigurationSpecificationInteger);

    //Execute
    ParameterTable pt = new ParameterTable(paramList, null, new TabWrapper());

    //Check
    assertEquals(6, pt.getRowCount());

    // - STRING row
    assertEquals(2, pt.getCellCount(0));
    assertEquals(InputParameterStringWidget.class, pt.getWidget(0, 1).getClass());

    // - BOOL row
    assertEquals(2, pt.getCellCount(1));
    assertEquals(InputParameterBooleanWidget.class, pt.getWidget(1, 1).getClass());

    // - CSV FILE row
    assertEquals(2, pt.getCellCount(2));
    assertEquals(InputParameterCsvFileWidget.class, pt.getWidget(2, 1).getClass());

    // - LIST BOX row
    assertEquals(2, pt.getCellCount(3));
    assertEquals(InputParameterListBoxWidget.class, pt.getWidget(3, 1).getClass());

    // - INTEGER row
    assertEquals(2, pt.getCellCount(4));
    assertEquals(InputParameterIntegerWidget.class, pt.getWidget(4, 1).getClass());

    // - Submit button row
    assertEquals(1, pt.getCellCount(5));
    assertEquals(Button.class, pt.getWidget(5, 0).getClass());

    // Cleanup
    TestHelper.resetDatabaseSync();
  }

  /**
   * Test method for {@link ParameterTable#getConfigurationSpecificationDataSourcesWithValues()} and
   * for {@link ParameterTable#getConfigurationSpecificationsWithValues()}
   */
  public void testRetrieveSimpleParameterValues() throws InputValidationException {
    //Setup
    TestHelper.resetDatabaseSync();

    ArrayList<ConfigurationSpecification> paramList = new ArrayList<>();

    ArrayList<String> values = new ArrayList<>();
    values.add("Column 1");
    values.add("Column 3");
    values.add("Column 2");

    final ConfigurationSpecificationString
        ConfigurationSpecificationString =
        new ConfigurationSpecificationString("string");
    final ConfigurationSpecificationBoolean
        ConfigurationSpecificationBoolean =
        new ConfigurationSpecificationBoolean("bool");
    final ConfigurationSpecificationCsvFile
        ConfigurationSpecificationCsvFile =
        new ConfigurationSpecificationCsvFile("csv");
    final ConfigurationSpecificationSqlIterator
        ConfigurationSpecificationSQLIterator =
        new ConfigurationSpecificationSqlIterator("sql");
    final ConfigurationSpecificationInteger
        ConfigurationSpecificationInteger =
        new ConfigurationSpecificationInteger("integer");
    final ConfigurationSpecificationListBox
        ConfigurationSpecificationListBox =
        new ConfigurationSpecificationListBox("listBox", values);

    paramList.add(ConfigurationSpecificationString);
    paramList.add(ConfigurationSpecificationBoolean);
    paramList.add(ConfigurationSpecificationCsvFile);
    paramList.add(ConfigurationSpecificationSQLIterator);
    paramList.add(ConfigurationSpecificationInteger);
    paramList.add(ConfigurationSpecificationListBox);

    final ParameterTable pt = new ParameterTable(paramList, null, new TabWrapper());
    enterNumber((InputParameterIntegerWidget) pt.getWidget(4, 1));
    setDatabaseConnection((InputParameterSqlIteratorWidget) pt.getWidget(3, 1));
    setCsvFile((InputParameterCsvFileWidget) pt.getWidget(2, 1));

    //Execute
    List<ConfigurationSpecification> retrievedParams = null;
    List<ConfigurationSpecification> retrievedDataSources = null;
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

    assertTrue(!retrievedDataSources.contains(ConfigurationSpecificationString));
    assertTrue(!retrievedDataSources.contains(ConfigurationSpecificationBoolean));
    assertTrue(!retrievedDataSources.contains(ConfigurationSpecificationInteger));
    assertTrue(!retrievedDataSources.contains(ConfigurationSpecificationListBox));
    assertTrue(retrievedDataSources.contains(ConfigurationSpecificationCsvFile));
    assertTrue(retrievedDataSources.contains(ConfigurationSpecificationSQLIterator));

    // Cleanup
    TestHelper.resetDatabaseSync();
  }

  private void setCsvFile(InputParameterCsvFileWidget widget) {
    FileInput fileInput = new FileInput("name");

    for (CsvFileInput csvFileInput : widget.inputWidgets) {
      csvFileInput.listbox.addValue("name");
      csvFileInput.fileInputs.put("name", fileInput);
      csvFileInput.listbox.setSelectedValue("name");
    }
  }

  private void enterNumber(InputParameterIntegerWidget widget) {
    for (IntegerInput integerInput : widget.inputWidgets) {
      integerInput.textbox.setValue(7);
    }
  }

  private void setDatabaseConnection(InputParameterSqlIteratorWidget widget) {
    DatabaseConnection dbConnection = new DatabaseConnection();
    dbConnection.setUrl("url");
    dbConnection.setPassword("password");
    dbConnection.setUsername("username");
    dbConnection.setSystem(DbSystem.DB2);

    for (SqlIteratorInput sqlIteratorInput : widget.inputWidgets) {
      sqlIteratorInput.listbox.addValue("url");
      sqlIteratorInput.databaseConnections.put("url", dbConnection);
      sqlIteratorInput.listbox.setSelectedValue("url");
    }
  }

  /**
   * Test method for {@link de.metanome.frontend.client.parameter.WidgetFactory#buildWidget(de.metanome.algorithm_integration.configuration.ConfigurationSpecification,
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
    ConfigurationSpecification stringParam = new ConfigurationSpecificationString(identifierString);
    String identifierInteger = "integerParam";
    ConfigurationSpecification
        integerParam =
        new ConfigurationSpecificationInteger(identifierInteger);
    String identifierBoolean = "boolParam";
    ConfigurationSpecification boolParam = new ConfigurationSpecificationBoolean(identifierBoolean);
    String identifierCsv = "csvParam";
    ConfigurationSpecification csvParam = new ConfigurationSpecificationCsvFile(identifierCsv);
    String identifierSql = "sqlParam";
    ConfigurationSpecification sqlParam = new ConfigurationSpecificationSqlIterator(identifierSql);
    String identifierListbox = "listboxParam";
    ConfigurationSpecificationListBox
        listboxParam =
        new ConfigurationSpecificationListBox(identifierListbox, values);

    TabWrapper tabWrapper = new TabWrapper();

    //Execute
    InputParameterWidget stringWidget = WidgetFactory.buildWidget(stringParam, tabWrapper);
    InputParameterWidget boolWidget = WidgetFactory.buildWidget(boolParam, tabWrapper);
    InputParameterWidget csvWidget = WidgetFactory.buildWidget(csvParam, tabWrapper);
    InputParameterWidget integerWidget = WidgetFactory.buildWidget(integerParam, tabWrapper);
    InputParameterWidget sqlWidget = WidgetFactory.buildWidget(sqlParam, tabWrapper);
    InputParameterWidget listboxWidget = WidgetFactory.buildWidget(listboxParam, tabWrapper);

    //Check
    assertTrue(stringWidget instanceof InputParameterStringWidget);
    assertEquals(identifierString, stringWidget.getSpecification().getIdentifier());

    assertTrue(boolWidget instanceof InputParameterBooleanWidget);
    assertEquals(identifierBoolean, boolWidget.getSpecification().getIdentifier());

    assertTrue(csvWidget instanceof InputParameterCsvFileWidget);
    assertEquals(identifierCsv, csvWidget.getSpecification().getIdentifier());

    assertTrue(sqlWidget instanceof InputParameterSqlIteratorWidget);
    assertEquals(identifierSql, sqlWidget.getSpecification().getIdentifier());

    assertTrue(listboxWidget instanceof InputParameterListBoxWidget);
    assertEquals(identifierListbox, listboxWidget.getSpecification().getIdentifier());

    assertTrue(integerWidget instanceof InputParameterIntegerWidget);
    assertEquals(identifierInteger, integerWidget.getSpecification().getIdentifier());

    // Cleanup
    TestHelper.resetDatabaseSync();
  }

  /**
   * Test method for {@link de.metanome.frontend.client.parameter.WidgetFactory#buildWidget(de.metanome.algorithm_integration.configuration.ConfigurationSpecification,
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
    ConfigurationSpecification
        stringParam =
        new ConfigurationSpecificationString(identifierString, 2);
    String identifierInteger = "integerParam";
    ConfigurationSpecification
        integerParam =
        new ConfigurationSpecificationInteger(identifierInteger, 2);
    String identifierBoolean = "boolParam";
    ConfigurationSpecification
        boolParam =
        new ConfigurationSpecificationBoolean(identifierBoolean, 2);
    String identifierCsv = "csvParam";
    ConfigurationSpecification csvParam = new ConfigurationSpecificationCsvFile(identifierCsv, 2);
    String identifierSql = "sqlParam";
    ConfigurationSpecification
        sqlParam =
        new ConfigurationSpecificationSqlIterator(identifierSql, 2);
    String identifierListbox = "listboxParam";
    ConfigurationSpecificationListBox
        listboxParam =
        new ConfigurationSpecificationListBox(identifierListbox, values, 2);

    TabWrapper tabWrapper = new TabWrapper();

    //Execute
    InputParameterWidget stringWidget = WidgetFactory.buildWidget(stringParam, tabWrapper);
    InputParameterWidget boolWidget = WidgetFactory.buildWidget(boolParam, tabWrapper);
    InputParameterWidget csvWidget = WidgetFactory.buildWidget(csvParam, tabWrapper);
    InputParameterWidget integerWidget = WidgetFactory.buildWidget(integerParam, tabWrapper);
    InputParameterWidget sqlWidget = WidgetFactory.buildWidget(sqlParam, tabWrapper);
    InputParameterWidget listboxWidget = WidgetFactory.buildWidget(listboxParam, tabWrapper);

    //Check
    assertTrue(stringWidget instanceof InputParameterStringWidget);
    assertEquals(2, ((InputParameterStringWidget) stringWidget).getWidgetCount());

    assertTrue(boolWidget instanceof InputParameterBooleanWidget);
    assertEquals(2, ((InputParameterBooleanWidget) boolWidget).getWidgetCount());

    assertTrue(integerWidget instanceof InputParameterIntegerWidget);
    assertEquals(2, ((InputParameterIntegerWidget) integerWidget).getWidgetCount());

    assertTrue(csvWidget instanceof InputParameterCsvFileWidget);
    assertEquals(2, ((InputParameterCsvFileWidget) csvWidget).getWidgetCount());

    assertTrue(sqlWidget instanceof InputParameterSqlIteratorWidget);
    assertEquals(2, ((InputParameterSqlIteratorWidget) sqlWidget).getWidgetCount());

    assertTrue(listboxWidget instanceof InputParameterListBoxWidget);
    assertEquals(2, ((InputParameterListBoxWidget) listboxWidget).getWidgetCount());

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
            listbox = ((InputParameterCsvFileWidget) pt.getWidget(0, 1)).inputWidgets.get(0).listbox;
        assertEquals(primaryDataSource.getValueAsString(), listbox.getSelectedValue());

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
