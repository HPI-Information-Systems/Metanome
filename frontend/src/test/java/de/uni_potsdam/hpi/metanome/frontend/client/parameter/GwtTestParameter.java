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

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.TextBox;

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingCsvFile;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationBoolean;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationCsvFile;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationInteger;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationListBox;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationSqlIterator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationString;
import de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper;
import de.uni_potsdam.hpi.metanome.frontend.client.helpers.InputValidationException;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class GwtTestParameter extends GWTTestCase {

  @Test
  public void testParameterTable() {
    //Setup
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
  }

  @Test
  public void testRetrieveSimpleParameterValues() throws InputValidationException {
    //Setup
    ArrayList<ConfigurationSpecification> paramList = new ArrayList<>();

    ArrayList<String> values = new ArrayList<>();
    values.add("Column 1");
    values.add("Column 3");
    values.add("Column 2");

    ConfigurationSpecificationString
        ConfigurationSpecificationString =
        new ConfigurationSpecificationString("string");
    ConfigurationSpecificationBoolean
        ConfigurationSpecificationBoolean =
        new ConfigurationSpecificationBoolean("bool");
    ConfigurationSpecificationCsvFile
        ConfigurationSpecificationCsvFile =
        new ConfigurationSpecificationCsvFile("csv");
    ConfigurationSpecificationSqlIterator
        ConfigurationSpecificationSQLIterator =
        new ConfigurationSpecificationSqlIterator("sql");
    ConfigurationSpecificationInteger
        ConfigurationSpecificationInteger =
        new ConfigurationSpecificationInteger("integer");
    ConfigurationSpecificationListBox
        ConfigurationSpecificationListBox =
        new ConfigurationSpecificationListBox("listBox", values);

    paramList.add(ConfigurationSpecificationString);
    paramList.add(ConfigurationSpecificationBoolean);
    paramList.add(ConfigurationSpecificationCsvFile);
    paramList.add(ConfigurationSpecificationSQLIterator);
    paramList.add(ConfigurationSpecificationInteger);
    paramList.add(ConfigurationSpecificationListBox);

    ParameterTable pt = new ParameterTable(paramList, null, new TabWrapper());
    chooseCsvFile((InputParameterCsvFileWidget) pt.getWidget(2, 1));
    enterNumber((InputParameterIntegerWidget) pt.getWidget(4, 1));

    //Execute
    List<ConfigurationSpecification>
        retrievedParams =
        pt.getConfigurationSpecificationsWithValues();
    List<ConfigurationSpecification>
        retrievedDataSources =
        pt.getConfigurationSpecificationDataSourcesWithValues();

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
  }

  /**
   * @param widget
   */
  private void chooseCsvFile(InputParameterCsvFileWidget widget) {
    for (CsvFileInput csvInput : widget.inputWidgets) {
      csvInput.listbox.addItem("some file");
      csvInput.listbox.setSelectedIndex(1);
    }
  }

  /**
   * @param widget
   */
  private void enterNumber(InputParameterIntegerWidget widget) {
    for (IntegerInput integerInput : widget.inputWidgets) {
      integerInput.textbox.setValue(7);
    }
  }

  @Test
  public void testConfigurationSpecificationWidgetCreation() throws InputValidationException {
    //Setup
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

    //Execute
    InputParameterWidget stringWidget = WidgetFactory.buildWidget(stringParam);
    InputParameterWidget boolWidget = WidgetFactory.buildWidget(boolParam);
    InputParameterWidget csvWidget = WidgetFactory.buildWidget(csvParam);
    InputParameterWidget integerWidget = WidgetFactory.buildWidget(integerParam);
    InputParameterWidget sqlWidget = WidgetFactory.buildWidget(sqlParam);
    InputParameterWidget listboxWidget = WidgetFactory.buildWidget(listboxParam);

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
  }

  @Test
  public void testMultipleValuesWidgetCreation() throws AlgorithmConfigurationException {
    //Setup
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

    //Execute
    InputParameterWidget stringWidget = WidgetFactory.buildWidget(stringParam);
    InputParameterWidget boolWidget = WidgetFactory.buildWidget(boolParam);
    InputParameterWidget csvWidget = WidgetFactory.buildWidget(csvParam);
    InputParameterWidget integerWidget = WidgetFactory.buildWidget(integerParam);
    InputParameterWidget sqlWidget = WidgetFactory.buildWidget(sqlParam);
    InputParameterWidget listboxWidget = WidgetFactory.buildWidget(listboxParam);

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
  }

  @Test
  public void testCsvFileWidget() throws InputValidationException {
    //Setup
    ConfigurationSettingCsvFile csvSpec = new ConfigurationSettingCsvFile();
    csvSpec.setAdvanced(true);
    CsvFileInput csvWidget = new CsvFileInput(csvSpec, false);
    FlexTable advancedPanel = (FlexTable) csvWidget.getWidget(1);
    String characterString = "X";
    int line = 5;
    boolean boolTrue = true;
    boolean noCharExceptionCaught = false;
    boolean noFileExceptionCaught = false;

    //Execute
    csvWidget.listbox.addItem("new file");
    csvWidget.listbox.setSelectedIndex(1);

    ((TextBox) advancedPanel.getWidget(0, 1)).setValue(characterString);
    ((TextBox) advancedPanel.getWidget(1, 1)).setValue(characterString);
    ((IntegerBox) advancedPanel.getWidget(3, 1)).setValue(line);
    ((CheckBox) advancedPanel.getWidget(4, 1)).setValue(boolTrue);
    ((CheckBox) advancedPanel.getWidget(5, 1)).setValue(boolTrue);
    try {
      csvSpec = csvWidget.getValuesAsSettings();
    } catch (InputValidationException e) {
      noCharExceptionCaught = true;
    }
    ((TextBox) advancedPanel.getWidget(2, 1)).setValue(characterString);
    csvWidget.listbox.setSelectedIndex(0);
    try {
      csvSpec = csvWidget.getValuesAsSettings();
    } catch (InputValidationException e) {
      noFileExceptionCaught = true;
    }

    csvWidget.listbox.setSelectedIndex(1);

    csvSpec = csvWidget.getValuesAsSettings();

    //Check
    assertTrue(noCharExceptionCaught);
    assertTrue(noFileExceptionCaught);

    assertEquals(characterString.charAt(0), csvSpec.getSeparatorChar());
    assertEquals(characterString.charAt(0), csvSpec.getQuoteChar());
    assertEquals(characterString.charAt(0), csvSpec.getEscapeChar());
    assertEquals(line, csvSpec.getSkipLines());
    assertEquals(boolTrue, csvSpec.isStrictQuotes());
    assertEquals(boolTrue, csvSpec.isIgnoreLeadingWhiteSpace());
  }

  //@Test
  public void testSetPrimaryDataSource() {
    //Setup
    ConfigurationSettingCsvFile primaryDataSource = new ConfigurationSettingCsvFile();
    primaryDataSource.setFileName("/inputA.csv");

    ArrayList<ConfigurationSpecification> paramList = new ArrayList<>();
    ConfigurationSpecificationCsvFile
        ConfigurationSpecificationCsvFile =
        new ConfigurationSpecificationCsvFile("csv");
    paramList.add(ConfigurationSpecificationCsvFile);

    //Execute
    ParameterTable pt = new ParameterTable(paramList, primaryDataSource, new TabWrapper());

    //Check
//		boolean foundDataSource = false;
//		for (ConfigurationSpecification dataSource : pt.getConfigurationSpecificationDataSourcesWithValues()){
//			for (Object setting : dataSource.getSettings()) {
//				System.out.println(((ConfigurationSettingDataSource) setting).getValueAsString() + " vs " + primaryDataSource.getValueAsString());
//				if(((ConfigurationSettingDataSource) setting).getValueAsString() ==
//						primaryDataSource.getValueAsString())
//					foundDataSource = true;
//			}
//		}
//		assertTrue(foundDataSource); FIXME why aren't resources found for tests?
//		 
//		ListBox listbox = ((InputParameterCsvFileWidget) pt.getWidget(0,1)).widgets.get(0).listbox;
//		assertEquals(primaryDataSource.getValueAsString(), listbox.getValue(listbox.getSelectedIndex()));
  }


  @Override
  public String getModuleName() {
    return "de.uni_potsdam.hpi.metanome.frontend.MetanomeTest";
  }
}
