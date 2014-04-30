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

package de.uni_potsdam.hpi.metanome.frontend.client;

import org.junit.Test;

import com.google.gwt.junit.client.GWTTestCase;

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingCsvFile;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingDataSource;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationCsvFile;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.CsvFileInput;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterCsvFileWidget;
import de.uni_potsdam.hpi.metanome.frontend.client.runs.JarChooser;

public class GwtTestCommonWidgets extends GWTTestCase{

	@Test
	public void testJarChooser() {
		//Setup
		String[] filenames = {"filename1.jar", "filename2.jar"};
		
		//Execute
		JarChooser jarChooser = new JarChooser(filenames);
		
		//Test
		assertEquals(2, jarChooser.getWidgetCount());
		assertEquals(filenames.length + 1, jarChooser.getListItemCount());
	}

	//@Test 
//	public void testCsvFileWidget() throws AlgorithmConfigurationException {
//		//Setup
//		String path = "inputA.csv";
//		ConfigurationSpecificationCsvFile configSpec = new ConfigurationSpecificationCsvFile("test");
//		InputParameterCsvFileWidget dataSourceWidget = new InputParameterCsvFileWidget(configSpec);
//		ConfigurationSettingCsvFile setting = new ConfigurationSettingCsvFile();
//		setting.setFileName(path);
//		
//		//Execute
//		dataSourceWidget.setDataSource(setting);
//		
//		//Check 
//		assertTrue(((CsvFileInput) dataSourceWidget.getWidget(0)).listbox.getItemCount() > 1);
//		ConfigurationSettingDataSource retrievedSetting = 
//				(ConfigurationSettingDataSource) dataSourceWidget.getUpdatedSpecification().getSettings()[0];
//		assertEquals(path, retrievedSetting.getValueAsString());
//	}
	
	@Override
	public String getModuleName() {
		return "de.uni_potsdam.hpi.metanome.frontend.Hello";
	}

}
