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

import org.junit.Test;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;

import com.google.gwt.junit.client.GWTTestCase;

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingCsvFile;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingDataSource;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationCsvFile;
import de.uni_potsdam.hpi.metanome.frontend.client.helpers.StringHelper;

public class GwtTestCsvFileParameter extends GWTTestCase {
	private String aFileName = "inputA.csv";
	private String[] csvFiles = {"inputB.csv", aFileName};
	
	private InputParameterCsvFileWidget setUpInputParameterWidget() {
		ConfigurationSpecificationCsvFile configSpec = new ConfigurationSpecificationCsvFile("test");
		InputParameterCsvFileWidget dataSourceWidget = new InputParameterCsvFileWidget(configSpec);
		dataSourceWidget.getCallback(dataSourceWidget.inputWidgets).onSuccess(csvFiles);
		return dataSourceWidget;
	}
	
	/**
	 * Tests the selection of a specific item corresponding to the given ConfigurationSetting.
	 * 
	 * @throws AlgorithmConfigurationException
	 */
	@Test
	public void testSelectDataSourceOnFilledDropdown() throws AlgorithmConfigurationException {
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
	 * 
	 * @throws AlgorithmConfigurationException
	 */
	@Test
	public void testSetDataSource() throws AlgorithmConfigurationException {
		//Setup
		InputParameterCsvFileWidget dataSourceWidget = setUpInputParameterWidget();
		ConfigurationSettingCsvFile setting = new ConfigurationSettingCsvFile();
		setting.setFileName(aFileName);
		
		//Execute
		dataSourceWidget.setDataSource(setting);
		
		//Check 
		assertTrue(((CsvFileInput) dataSourceWidget.getWidget(0)).listbox.getItemCount() > 1);
		ConfigurationSettingDataSource retrievedSetting = 
				(ConfigurationSettingDataSource) dataSourceWidget.getUpdatedSpecification().getSettings()[0];
		assertEquals(aFileName, retrievedSetting.getValueAsString());
	}
	
	/**
	 * When selecting the "Advanced" checkbox, additional input fields become visible, containing the default values
	 * that will be used if none are specified.
	 */
	@Test
	public void testAdvancedDefaultEntries() {
		CsvFileInput widget = new CsvFileInput(false);
		assertFalse(widget.advancedTable.isVisible());
		assertFalse(widget.escapeTextbox.isAttached() && widget.escapeTextbox.isVisible());	
		assertFalse(widget.skiplinesIntegerbox.isAttached() && widget.skiplinesIntegerbox.isVisible());	
		assertFalse(widget.separatorTextbox.isAttached() && widget.separatorTextbox.isVisible());	
		assertFalse(widget.quoteTextbox.isAttached() && widget.quoteTextbox.isVisible());	
		assertFalse(widget.strictQuotesCheckbox.isAttached() && widget.strictQuotesCheckbox.isVisible());	
		assertFalse(widget.ignoreLeadingWhiteSpaceCheckbox.isAttached() && widget.ignoreLeadingWhiteSpaceCheckbox.isVisible());	
		
		// Execute
		widget.advancedCheckbox.setValue(true, true);
		
		// Check visibility
		assertTrue(widget.advancedTable.isVisible());
		assertTrue(widget.escapeTextbox.isAttached() && widget.escapeTextbox.isVisible());	
		assertTrue(widget.skiplinesIntegerbox.isAttached() && widget.skiplinesIntegerbox.isVisible());	
		assertTrue(widget.separatorTextbox.isAttached() && widget.separatorTextbox.isVisible());	
		assertTrue(widget.quoteTextbox.isAttached() && widget.quoteTextbox.isVisible());	
		assertTrue(widget.strictQuotesCheckbox.isAttached() && widget.strictQuotesCheckbox.isVisible());	
		assertTrue(widget.ignoreLeadingWhiteSpaceCheckbox.isAttached() && widget.ignoreLeadingWhiteSpaceCheckbox.isVisible());
		
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
	}
	
	
	@Override
	public String getModuleName() {
		return "de.uni_potsdam.hpi.metanome.frontend.Metanome";
	}
}
