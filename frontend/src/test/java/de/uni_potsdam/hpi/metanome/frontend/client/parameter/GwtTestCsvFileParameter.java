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

import com.google.gwt.junit.client.GWTTestCase;

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingCsvFile;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingDataSource;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationCsvFile;

public class GwtTestCsvFileParameter extends GWTTestCase {
	private String aFileName = "inputA.csv";
	private String[] csvFiles = {"inputB.csv", aFileName};
	
	private InputParameterCsvFileWidget setUpWidget() {
		ConfigurationSpecificationCsvFile configSpec = new ConfigurationSpecificationCsvFile("test");
		InputParameterCsvFileWidget dataSourceWidget = new InputParameterCsvFileWidget(configSpec);
		dataSourceWidget.getCallback(dataSourceWidget.inputWidgets).onSuccess(csvFiles);
		return dataSourceWidget;
	}
	
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
		assertEquals(aFileName, widget.getValuesAsSetting().getFileName());
	}
	
	@Test
	public void testSetDataSource() throws AlgorithmConfigurationException {
		//Setup
		InputParameterCsvFileWidget dataSourceWidget = setUpWidget();
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
	
	@Test
	public void testAdvancedDefaultEntries() {
		
	}
	
	
	@Override
	public String getModuleName() {
		return "de.uni_potsdam.hpi.metanome.frontend.Metanome";
	}
}
