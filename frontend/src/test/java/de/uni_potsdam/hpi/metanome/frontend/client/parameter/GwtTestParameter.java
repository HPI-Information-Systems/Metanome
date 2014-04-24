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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingCsvFile;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingDataSource;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationBoolean;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationCsvFile;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationSQLIterator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationString;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.CsvFileInput;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterBooleanWidget;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterCsvFileWidget;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterSQLIteratorWidget;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterStringWidget;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterWidget;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.ParameterTable;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.WidgetFactory;

public class GwtTestParameter extends GWTTestCase {

	@Test
	public void testParameterTable() {
		//Setup
		List<ConfigurationSpecification> paramList = new ArrayList<ConfigurationSpecification>();
		
		ConfigurationSpecificationString ConfigurationSpecificationString = new ConfigurationSpecificationString("Filename");
		ConfigurationSpecificationBoolean ConfigurationSpecificationBoolean = new ConfigurationSpecificationBoolean("Omit warnings");
		ConfigurationSpecificationCsvFile ConfigurationSpecificationCsvFile = new ConfigurationSpecificationCsvFile("inputData");
		
		paramList.add(ConfigurationSpecificationString);
		paramList.add(ConfigurationSpecificationBoolean);
		paramList.add(ConfigurationSpecificationCsvFile);
		
		//Execute
		ParameterTable pt = new ParameterTable(paramList, null);
		
		//Check
		assertEquals(4, pt.getRowCount());
		
		// - STRING row
		assertEquals(2, pt.getCellCount(0));
		assertEquals(InputParameterStringWidget.class, pt.getWidget(0, 1).getClass());
		
		// - BOOL row
		assertEquals(2, pt.getCellCount(1));
		assertEquals(InputParameterBooleanWidget.class, pt.getWidget(1, 1).getClass());
					
		// - CSV FILE row
		assertEquals(2, pt.getCellCount(2));
		assertEquals(InputParameterCsvFileWidget.class, pt.getWidget(2, 1).getClass());
		
		// - Submit button row
		assertEquals(1, pt.getCellCount(3));
		assertEquals(Button.class, pt.getWidget(3, 0).getClass());
	}
	
	@Test
	public void testRetrieveSimpleParameterValues(){	
		//Setup
		ArrayList<ConfigurationSpecification> paramList = new ArrayList<ConfigurationSpecification>();
		
		ConfigurationSpecificationString ConfigurationSpecificationString = new ConfigurationSpecificationString("string");
		ConfigurationSpecificationBoolean ConfigurationSpecificationBoolean = new ConfigurationSpecificationBoolean("bool");
		ConfigurationSpecificationCsvFile ConfigurationSpecificationCsvFile = new ConfigurationSpecificationCsvFile("csv");
		ConfigurationSpecificationSQLIterator ConfigurationSpecificationSQLIterator = new ConfigurationSpecificationSQLIterator("sql");
		
		paramList.add(ConfigurationSpecificationString);
		paramList.add(ConfigurationSpecificationBoolean);
		paramList.add(ConfigurationSpecificationCsvFile);
		paramList.add(ConfigurationSpecificationSQLIterator);
		
		ParameterTable pt = new ParameterTable(paramList, null);

		//Execute
		List<ConfigurationSpecification> retrievedParams = pt.getConfigurationSpecificationsWithValues();
		List<ConfigurationSpecification> retrievedDataSources = pt.getConfigurationSpecificationDataSourcesWithValues();
		
		//Check
		assertTrue(retrievedParams.contains(ConfigurationSpecificationString));
		assertTrue(retrievedParams.contains(ConfigurationSpecificationBoolean));
		assertTrue(!retrievedParams.contains(ConfigurationSpecificationCsvFile));
		assertTrue(!retrievedParams.contains(ConfigurationSpecificationSQLIterator));
		
		assertTrue(!retrievedDataSources.contains(ConfigurationSpecificationString));
		assertTrue(!retrievedDataSources.contains(ConfigurationSpecificationBoolean));
		assertTrue(retrievedDataSources.contains(ConfigurationSpecificationCsvFile));
		assertTrue(retrievedDataSources.contains(ConfigurationSpecificationSQLIterator));
	}
	
	@Test
	public void testConfigurationSpecificationWidgetCreation(){
		//Setup
		String identifierString = "stringParam";
		ConfigurationSpecification stringParam = new ConfigurationSpecificationString(identifierString);
		String identifierBoolean = "boolParam";
		ConfigurationSpecification boolParam = new ConfigurationSpecificationBoolean(identifierBoolean);
		String identifierCsv = "csvParam";
		ConfigurationSpecification csvParam = new ConfigurationSpecificationCsvFile(identifierCsv);
		String identifierSql = "sqlParam";
		ConfigurationSpecification sqlParam = new ConfigurationSpecificationSQLIterator(identifierSql);
		
		//Execute
		InputParameterWidget stringWidget = WidgetFactory.buildWidget(stringParam);
		InputParameterWidget boolWidget = WidgetFactory.buildWidget(boolParam);
		InputParameterWidget csvWidget = WidgetFactory.buildWidget(csvParam);
		InputParameterWidget sqlWidget = WidgetFactory.buildWidget(sqlParam);
		
		//Check
		assertTrue(stringWidget instanceof InputParameterStringWidget);
		assertEquals(identifierString, stringWidget.getUpdatedSpecification().getIdentifier());
		
		assertTrue(boolWidget instanceof InputParameterBooleanWidget);
		assertEquals(identifierBoolean, boolWidget.getUpdatedSpecification().getIdentifier());
		
		assertTrue(csvWidget instanceof InputParameterCsvFileWidget);
		assertEquals(identifierCsv, csvWidget.getUpdatedSpecification().getIdentifier());
		
		assertTrue(sqlWidget instanceof InputParameterSQLIteratorWidget);
		assertEquals(identifierSql, sqlWidget.getUpdatedSpecification().getIdentifier());
	}
	
	@Test
	public void testCsvFileWidget(){
		//Setup
		ConfigurationSettingCsvFile csvSpec = new ConfigurationSettingCsvFile();
		csvSpec.setAdvanced(true);
		CsvFileInput csvWidget = new CsvFileInput(csvSpec);
		FlexTable advancedPanel = (FlexTable) csvWidget.getWidget(1);
		String characterString = "X";
		int line = 5;
		boolean boolTrue = true;
		boolean exceptionCaught = false;

		//Execute
		((TextBox) advancedPanel.getWidget(0,1)).setValue(characterString);
		((TextBox) advancedPanel.getWidget(1,1)).setValue(characterString);
		((IntegerBox) advancedPanel.getWidget(3,1)).setValue(line);
		((CheckBox) advancedPanel.getWidget(4,1)).setValue(boolTrue);
		((CheckBox) advancedPanel.getWidget(5,1)).setValue(boolTrue);
		try{			
			csvSpec = csvWidget.getValuesAsSettings();
		} catch (Exception e){
			//TODO make sure some nice exception is thrown when not all values are set.
			exceptionCaught = true;
		}
		((TextBox) advancedPanel.getWidget(2,1)).setValue(characterString);
		csvSpec = csvWidget.getValuesAsSettings();
		
		//Check
//		assertTrue(exceptionCaught); TODO input validation

		assertEquals(characterString.charAt(0), csvSpec.getSeparatorChar());
		assertEquals(characterString.charAt(0), csvSpec.getQuoteChar());
		assertEquals(characterString.charAt(0), csvSpec.getEscapeChar());
		assertEquals(line, csvSpec.getLine());
		assertEquals(boolTrue, csvSpec.isStrictQuotes());
		assertEquals(boolTrue, csvSpec.isIgnoreLeadingWhiteSpace());		
	}
	
	@Test
	public void testSetPrimaryDataSource(){
		//Setup
		ConfigurationSettingCsvFile primaryDataSource = new ConfigurationSettingCsvFile();
		primaryDataSource.setFileName("inputA.csv");

		ArrayList<ConfigurationSpecification> paramList = new ArrayList<ConfigurationSpecification>();
		ConfigurationSpecificationCsvFile ConfigurationSpecificationCsvFile = new ConfigurationSpecificationCsvFile("csv");
		paramList.add(ConfigurationSpecificationCsvFile);
		
		//Execute
		ParameterTable pt = new ParameterTable(paramList, primaryDataSource);
		
		//Check
		boolean foundDataSource = false;
		for (ConfigurationSpecification dataSource : pt.getConfigurationSpecificationDataSourcesWithValues()){
			for (Object setting : dataSource.getSettings()) {
				if(((ConfigurationSettingDataSource) setting).getValueAsString() ==
						primaryDataSource.getValueAsString())
					foundDataSource = true;
			}
		}
		assertTrue(foundDataSource);
		
		ListBox listbox = ((InputParameterCsvFileWidget) pt.getWidget(0,1)).widgets.get(0).listbox;
		assertEquals(primaryDataSource.getValueAsString(), listbox.getValue(listbox.getSelectedIndex()));
	}

		
	@Override
	public String getModuleName() {
		return "de.uni_potsdam.hpi.metanome.frontend.Hello";
	}
}
