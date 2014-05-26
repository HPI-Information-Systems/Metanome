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
import com.google.gwt.user.client.ui.*;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.*;
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

        ConfigurationSpecificationString ConfigurationSpecificationString = new ConfigurationSpecificationString("Filename");
        ConfigurationSpecificationBoolean ConfigurationSpecificationBoolean = new ConfigurationSpecificationBoolean("Omit warnings");
        ConfigurationSpecificationCsvFile ConfigurationSpecificationCsvFile = new ConfigurationSpecificationCsvFile("inputData");

        paramList.add(ConfigurationSpecificationString);
        paramList.add(ConfigurationSpecificationBoolean);
        paramList.add(ConfigurationSpecificationCsvFile);

        //Execute
        ParameterTable pt = new ParameterTable(paramList, null, new TabWrapper());

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
    public void testRetrieveParameterValues() throws InputValidationException {
        //Setup
        ArrayList<ConfigurationSpecification> paramList = new ArrayList<>();

        ConfigurationSpecificationString configurationSpecificationString = new ConfigurationSpecificationString("string");
        ConfigurationSpecificationBoolean configurationSpecificationBoolean = new ConfigurationSpecificationBoolean("bool");
        ConfigurationSpecificationCsvFile configurationSpecificationCsvFile = new ConfigurationSpecificationCsvFile("csv");
        ConfigurationSpecificationSqlIterator configurationSpecificationSQLIterator = new ConfigurationSpecificationSqlIterator("sql");

        paramList.add(configurationSpecificationString);
        paramList.add(configurationSpecificationBoolean);
        paramList.add(configurationSpecificationCsvFile);
        paramList.add(configurationSpecificationSQLIterator);

        ParameterTable pt = new ParameterTable(paramList, null, new TabWrapper());
        chooseCsvFile((InputParameterCsvFileWidget) pt.getWidget(2, 1));

        //Execute
        List<ConfigurationSpecification> retrievedParams = pt.getConfigurationSpecificationsWithValues();
        List<ConfigurationSpecification> retrievedDataSources = pt.getConfigurationSpecificationDataSourcesWithValues();

        //Check
        assertTrue(retrievedParams.contains(configurationSpecificationString));
        assertTrue(configurationSpecificationString.getSettings().length == 1);
        assertTrue(!retrievedDataSources.contains(configurationSpecificationString));
        
        assertTrue(retrievedParams.contains(configurationSpecificationBoolean));
        assertTrue(configurationSpecificationBoolean.getSettings().length == 1);
        assertTrue(!retrievedDataSources.contains(configurationSpecificationBoolean));
        
        assertTrue(retrievedDataSources.contains(configurationSpecificationCsvFile));
        assertTrue(configurationSpecificationCsvFile.getSettings().length == 1);
        assertTrue(!retrievedParams.contains(configurationSpecificationCsvFile));

        assertTrue(retrievedDataSources.contains(configurationSpecificationSQLIterator));
        assertTrue(configurationSpecificationSQLIterator.getSettings().length == 1);
        assertTrue(!retrievedParams.contains(configurationSpecificationSQLIterator));
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

	@Test
    public void testConfigurationSpecificationWidgetCreation() throws InputValidationException {
        //Setup
        String identifierString = "stringParam";
        ConfigurationSpecification stringParam = new ConfigurationSpecificationString(identifierString);
        String identifierBoolean = "boolParam";
        ConfigurationSpecification boolParam = new ConfigurationSpecificationBoolean(identifierBoolean);
        String identifierCsv = "csvParam";
        ConfigurationSpecification csvParam = new ConfigurationSpecificationCsvFile(identifierCsv);
        String identifierSql = "sqlParam";
        ConfigurationSpecification sqlParam = new ConfigurationSpecificationSqlIterator(identifierSql);

        //Execute
        InputParameterWidget stringWidget = WidgetFactory.buildWidget(stringParam);
        InputParameterWidget boolWidget = WidgetFactory.buildWidget(boolParam);
        InputParameterWidget csvWidget = WidgetFactory.buildWidget(csvParam);
        InputParameterWidget sqlWidget = WidgetFactory.buildWidget(sqlParam);

        //Check
        assertTrue(stringWidget instanceof InputParameterStringWidget);
        assertEquals(identifierString, stringWidget.getSpecification().getIdentifier());

        assertTrue(boolWidget instanceof InputParameterBooleanWidget);
        assertEquals(identifierBoolean, boolWidget.getSpecification().getIdentifier());

        assertTrue(csvWidget instanceof InputParameterCsvFileWidget);
        assertEquals(identifierCsv, csvWidget.getSpecification().getIdentifier());

        assertTrue(sqlWidget instanceof InputParameterSqlIteratorWidget);
        assertEquals(identifierSql, sqlWidget.getSpecification().getIdentifier());
    }

    @Test
    public void testMultipleValuesWidgetCreation() {
        //Setup
        String identifierString = "stringParam";
        ConfigurationSpecification stringParam = new ConfigurationSpecificationString(identifierString, 2);
        String identifierBoolean = "boolParam";
        ConfigurationSpecification boolParam = new ConfigurationSpecificationBoolean(identifierBoolean, 2);
        String identifierCsv = "csvParam";
        ConfigurationSpecification csvParam = new ConfigurationSpecificationCsvFile(identifierCsv, 2);
        String identifierSql = "sqlParam";
        ConfigurationSpecification sqlParam = new ConfigurationSpecificationSqlIterator(identifierSql, 2);

        //Execute
        InputParameterWidget stringWidget = WidgetFactory.buildWidget(stringParam);
        InputParameterWidget boolWidget = WidgetFactory.buildWidget(boolParam);
        InputParameterWidget csvWidget = WidgetFactory.buildWidget(csvParam);
        InputParameterWidget sqlWidget = WidgetFactory.buildWidget(sqlParam);

        //Check
        assertTrue(stringWidget instanceof InputParameterStringWidget);
        assertEquals(2, ((InputParameterStringWidget) stringWidget).getWidgetCount());

        assertTrue(boolWidget instanceof InputParameterBooleanWidget);
        assertEquals(2, ((InputParameterBooleanWidget) boolWidget).getWidgetCount());

        assertTrue(csvWidget instanceof InputParameterCsvFileWidget);
        assertEquals(2, ((InputParameterCsvFileWidget) csvWidget).getWidgetCount());

        assertTrue(sqlWidget instanceof InputParameterSqlIteratorWidget);
        assertEquals(2, ((InputParameterSqlIteratorWidget) sqlWidget).getWidgetCount());
    }

    //@Test
    public void testSetPrimaryDataSource() {
        //Setup
        ConfigurationSettingCsvFile primaryDataSource = new ConfigurationSettingCsvFile();
        primaryDataSource.setFileName("/inputA.csv");

        ArrayList<ConfigurationSpecification> paramList = new ArrayList<>();
        ConfigurationSpecificationCsvFile ConfigurationSpecificationCsvFile = new ConfigurationSpecificationCsvFile("csv");
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
        return "de.uni_potsdam.hpi.metanome.frontend.Metanome";
    }
}
