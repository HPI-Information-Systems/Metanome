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
import com.google.gwt.user.client.ui.FlexTable;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingCsvFile;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingDataSource;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationCsvFile;
import de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper;
import de.uni_potsdam.hpi.metanome.frontend.client.helpers.InputValidationException;
import de.uni_potsdam.hpi.metanome.frontend.client.helpers.StringHelper;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class GwtTestCsvFileParameter extends GWTTestCase {

  private String aFileName = "inputA.csv";
  private String[] csvFiles = {"inputB.csv", aFileName};

  private InputParameterCsvFileWidget setUpInputParameterWidget()
      throws AlgorithmConfigurationException {
    ConfigurationSpecificationCsvFile configSpec = new ConfigurationSpecificationCsvFile("test");
    InputParameterCsvFileWidget dataSourceWidget = new InputParameterCsvFileWidget(configSpec);
    dataSourceWidget.getCallback(dataSourceWidget.inputWidgets).onSuccess(csvFiles);
    return dataSourceWidget;
  }

  /**
   * Tests the selection of a specific item corresponding to the given ConfigurationSetting.
   */
  @Test
  public void testSelectDataSourceOnFilledDropdown()
      throws AlgorithmConfigurationException, InputValidationException {
    CsvFileInput widget = new CsvFileInput(false);
    widget.addToListbox(csvFiles);
    ConfigurationSettingCsvFile setting = new ConfigurationSettingCsvFile();
    setting.setFileName(aFileName);

    //Execute
    widget.selectDataSource(setting);

    //Check
    assertEquals(aFileName, widget.listbox.getItemText(widget.listbox.getSelectedIndex()));
    assertEquals(aFileName, widget.getValuesAsSettings().getFileName());
  }

  /**
   * When setting a data source on the parent widget (InputParameter), it should be set in the first
   * child widget.
   */
  @Test
  public void testSetDataSource() throws AlgorithmConfigurationException, InputValidationException {
    //Setup
    InputParameterCsvFileWidget dataSourceWidget = setUpInputParameterWidget();
    ConfigurationSettingCsvFile setting = new ConfigurationSettingCsvFile();
    setting.setFileName(aFileName);

    //Execute
    dataSourceWidget.setDataSource(setting);

    //Check
    assertTrue(((CsvFileInput) dataSourceWidget.getWidget(0)).listbox.getItemCount() > 1);
    ConfigurationSettingDataSource retrievedSetting =
        (ConfigurationSettingDataSource) dataSourceWidget.getUpdatedSpecification()
            .getSettings()[0];
    assertEquals(aFileName, retrievedSetting.getValueAsString());
  }

  /**
   * When selecting the "Advanced" checkbox, additional input fields become visible, containing the
   * default values that will be used if none are specified.
   */
  @Test
  public void testAdvancedDefaultEntries() {
    CsvFileInput widget = new CsvFileInput(false);
    assertFalse(widget.advancedTable.isVisible());
    assertFalse(widget.escapeTextbox.isAttached() && widget.escapeTextbox.isVisible());
    assertFalse(widget.skiplinesIntegerbox.isAttached() && widget.skiplinesIntegerbox.isVisible());
    assertFalse(widget.separatorTextbox.isAttached() && widget.separatorTextbox.isVisible());
    assertFalse(widget.quoteTextbox.isAttached() && widget.quoteTextbox.isVisible());
    assertFalse(
        widget.strictQuotesCheckbox.isAttached() && widget.strictQuotesCheckbox.isVisible());
    assertFalse(widget.ignoreLeadingWhiteSpaceCheckbox.isAttached()
                && widget.ignoreLeadingWhiteSpaceCheckbox.isVisible());
    assertFalse(widget.headerCheckbox.isAttached() && widget.headerCheckbox.isVisible());
    assertFalse(widget.skipDifferingLinesCheckbox.isAttached() && widget.skipDifferingLinesCheckbox
        .isVisible());

    // Execute
    widget.advancedCheckbox.setValue(true, true);

    // Check visibility
    assertTrue(widget.advancedTable.isVisible());
    assertTrue(widget.escapeTextbox.isVisible());
    assertTrue(widget.skiplinesIntegerbox.isVisible());
    assertTrue(widget.separatorTextbox.isVisible());
    assertTrue(widget.quoteTextbox.isVisible());
    assertTrue(widget.strictQuotesCheckbox.isVisible());
    assertTrue(widget.ignoreLeadingWhiteSpaceCheckbox.isVisible());
    assertTrue(widget.headerCheckbox.isVisible());
    assertTrue(widget.skipDifferingLinesCheckbox.isVisible());

    // Check values
    assertEquals(CSVParser.DEFAULT_ESCAPE_CHARACTER,
                 StringHelper.getFirstCharFromInput(widget.escapeTextbox.getValue()));
    assertEquals(CSVParser.DEFAULT_SEPARATOR,
                 StringHelper.getFirstCharFromInput(widget.separatorTextbox.getValue()));
    assertEquals(CSVParser.DEFAULT_QUOTE_CHARACTER,
                 StringHelper.getFirstCharFromInput(widget.quoteTextbox.getValue()));
    assertEquals(CSVReader.DEFAULT_SKIP_LINES,
                 widget.skiplinesIntegerbox.getValue().intValue());
    assertEquals(CSVParser.DEFAULT_IGNORE_LEADING_WHITESPACE,
                 widget.ignoreLeadingWhiteSpaceCheckbox.getValue().booleanValue());
    assertEquals(CSVParser.DEFAULT_STRICT_QUOTES,
                 widget.strictQuotesCheckbox.getValue().booleanValue());
    assertEquals(true, widget.headerCheckbox.getValue().booleanValue());
    assertEquals(false, widget.skipDifferingLinesCheckbox.getValue().booleanValue());
  }

  @Test
  public void testRetrieveAdvancedValues()
      throws InputValidationException, AlgorithmConfigurationException {
    boolean boolValue = true;
    String charValue = "t";
    int intValue = 2;
    ConfigurationSpecificationCsvFile configSpec = new ConfigurationSpecificationCsvFile("spec_id");
    InputParameterCsvFileWidget widget = new InputParameterCsvFileWidget(configSpec);
    CsvFileInput input = widget.inputWidgets.get(0);
    String filename = "someCSVfile";
    input.listbox.insertItem(filename, 1);

    input.listbox.setSelectedIndex(1);
    input.advancedCheckbox.setValue(true, true);
    input.escapeTextbox.setValue(charValue);
    input.skiplinesIntegerbox.setValue(intValue);
    input.separatorTextbox.setValue(charValue);
    input.quoteTextbox.setValue(charValue);
    input.strictQuotesCheckbox.setValue(boolValue);
    input.ignoreLeadingWhiteSpaceCheckbox.setValue(boolValue);
    input.headerCheckbox.setValue(boolValue);
    input.skipDifferingLinesCheckbox.setValue(boolValue);

    ConfigurationSettingCsvFile setting = widget.getUpdatedSpecification().getSettings()[0];

    assertEquals(setting.getFileName(), filename);
    assertEquals(setting.isAdvanced(), true);
    assertEquals(setting.getEscapeChar(), charValue.charAt(0));
    assertEquals(setting.getSkipLines(), intValue);
    assertEquals(setting.getSeparatorChar(), charValue.charAt(0));
    assertEquals(setting.getQuoteChar(), charValue.charAt(0));
    assertEquals(setting.isStrictQuotes(), boolValue);
    assertEquals(setting.isIgnoreLeadingWhiteSpace(), boolValue);
    assertEquals(setting.hasHeader(), boolValue);
    assertEquals(setting.isSkipDifferingLines(), boolValue);
  }

  @Test
  public void testValidation() throws InputValidationException {
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

    csvWidget.escapeTextbox.setValue(characterString);
    csvWidget.quoteTextbox.setValue(characterString);
    csvWidget.skiplinesIntegerbox.setValue(line);
    csvWidget.ignoreLeadingWhiteSpaceCheckbox.setValue(boolTrue);
    csvWidget.strictQuotesCheckbox.setValue(boolTrue);

    try {
      csvSpec = csvWidget.getValuesAsSettings();
    } catch (InputValidationException e) {
      noCharExceptionCaught = true;
    }
    csvWidget.separatorTextbox.setValue(characterString);
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
  }

  @Test
  public void testAdvancedSettingsThroughParameterTable() throws InputValidationException {
    //Setup
    boolean boolValue = true;
    String charValue = "#";
    int intValue = 4;

    List<ConfigurationSpecification> paramList = new ArrayList<ConfigurationSpecification>();
    ConfigurationSpecificationCsvFile
        configurationSpecificationCsvFile =
        new ConfigurationSpecificationCsvFile("inputData");
    paramList.add(configurationSpecificationCsvFile);
    ParameterTable pt = new ParameterTable(paramList, null, new TabWrapper());

    //Set Values
    CsvFileInput
        csvInput =
        ((InputParameterCsvFileWidget) pt.getInputParameterWidget("inputData")).inputWidgets.get(0);

    csvInput.listbox.addItem("new file");
    csvInput.listbox.setSelectedIndex(1);

    csvInput.advancedCheckbox.setValue(true, true);
    csvInput.escapeTextbox.setValue(charValue);
    csvInput.headerCheckbox.setValue(boolValue);
    csvInput.ignoreLeadingWhiteSpaceCheckbox.setValue(boolValue);
    csvInput.quoteTextbox.setValue(charValue);
    csvInput.separatorTextbox.setValue(charValue);
    csvInput.skipDifferingLinesCheckbox.setValue(boolValue);
    csvInput.skiplinesIntegerbox.setValue(intValue);
    csvInput.strictQuotesCheckbox.setValue(boolValue);

    //Retrieve
    List<ConfigurationSpecification>
        dataSources =
        pt.getConfigurationSpecificationDataSourcesWithValues();

    //Check
    assertEquals(1, dataSources.size());
    assertTrue(dataSources.get(0) instanceof ConfigurationSpecificationCsvFile);
    assertEquals(1, dataSources.get(0).getSettings().length);
    ConfigurationSettingCsvFile
        csvSetting =
        (ConfigurationSettingCsvFile) dataSources.get(0).getSettings()[0];
    //TODO test that advanced values are correct when calling ParameterTable.getDataSourcesWithValues
  }

  @Override
  public String getModuleName() {
    return "de.uni_potsdam.hpi.metanome.frontend.MetanomeTest";
  }
}
