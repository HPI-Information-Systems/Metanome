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

import org.junit.Test;

import com.google.gwt.junit.client.GWTTestCase;

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationListBox;

public class GwtTestListBoxParameter extends GWTTestCase {

	@Test
	public void testCreateWithFixedNumber() throws AlgorithmConfigurationException {
		//Setup
		ArrayList<String> values = new ArrayList<>();
		values.add("Column 1");
		values.add("Column 3");
		values.add("Column 2");
		int noOfValues = 3;
		ConfigurationSpecificationListBox specification = new ConfigurationSpecificationListBox("enum", values, noOfValues);

		//Execute
		InputParameterListBoxWidget widget = new InputParameterListBoxWidget(specification);

		//Check
		assertEquals(noOfValues, widget.inputWidgets.size());
		assertEquals(noOfValues, widget.getWidgetCount());
		assertFalse(widget.inputWidgets.get(0).isOptional);
	}

	@Test
	public void testCreateWithArbitraryNumber() throws AlgorithmConfigurationException {
		//Setup
		ArrayList<String> values = new ArrayList<>();
		values.add("Column 1");
		values.add("Column 3");
		values.add("Column 2");
		int noOfValues = ConfigurationSpecification.ARBITRARY_NUMBER_OF_VALUES;
		ConfigurationSpecificationListBox specification = new ConfigurationSpecificationListBox("enum", values, noOfValues);

		//Execute
		InputParameterListBoxWidget widget = new InputParameterListBoxWidget(specification);

		//Check
		assertEquals(1, widget.inputWidgets.size());        //expecting one default input field
		assertEquals(widget.getWidgetCount(), 2);            //default input field + add button
		assertTrue(widget.inputWidgets.get(0).isOptional);    //input field must be optional
	}

	@Test
	public void testAddInput() throws AlgorithmConfigurationException {
		//Setup
		ArrayList<String> values = new ArrayList<>();
		values.add("Column 1");
		values.add("Column 3");
		values.add("Column 2");

		ConfigurationSpecificationListBox specification = new ConfigurationSpecificationListBox("enum", values,
				ConfigurationSpecification.ARBITRARY_NUMBER_OF_VALUES);
		InputParameterListBoxWidget widget = new InputParameterListBoxWidget(specification);
		int previousCount = widget.getWidgetCount();
		int listCount = widget.inputWidgets.size();

		//Execute
		widget.addInputField(true);

		//Check
		assertEquals(previousCount + 1, widget.getWidgetCount());
		assertEquals(listCount + 1, widget.inputWidgets.size());
	}

	@Test
	public void testRemoveInput() throws AlgorithmConfigurationException {
		//Setup
		ArrayList<String> values = new ArrayList<>();
		values.add("Column 1");
		values.add("Column 3");
		values.add("Column 2");
		ConfigurationSpecificationListBox specification = new ConfigurationSpecificationListBox("enum", values,
				ConfigurationSpecification.ARBITRARY_NUMBER_OF_VALUES);

		InputParameterListBoxWidget widget = new InputParameterListBoxWidget(specification);
		int previousCount = widget.getWidgetCount();
		int listCount = widget.inputWidgets.size();

		//Execute
		widget.inputWidgets.get(0).removeSelf();

		//Check
		assertEquals(previousCount - 1, widget.getWidgetCount());
		assertEquals(listCount - 1, widget.inputWidgets.size());
	}

	@Test
	public void testRetrieveValues() {
		//Setup
		ArrayList<String> values = new ArrayList<>();
		values.add("Column 1");
		values.add("Column 3");
		values.add("Column 2");
		String expectedSelectedValue = "Column 3";

		ConfigurationSpecificationListBox expectedSpecification = new ConfigurationSpecificationListBox("enum", values, ConfigurationSpecification.ARBITRARY_NUMBER_OF_VALUES);

		InputParameterListBoxWidget widget = new InputParameterListBoxWidget(expectedSpecification);
		widget.setSelection(expectedSelectedValue);

		//Execute
		ConfigurationSpecificationListBox specification = widget.getUpdatedSpecification();

		//Check
		assertEquals(expectedSpecification.getSettings().length, specification.getSettings().length);
		assertEquals(values.size(), specification.getValues().size());
		assertEquals(expectedSelectedValue, specification.getSettings()[0].selectedValue);
		assertEquals(values, specification.getValues());
	}

	@Override
	public String getModuleName() {
		return "de.uni_potsdam.hpi.metanome.frontend.MetanomeTest";
	}
}
