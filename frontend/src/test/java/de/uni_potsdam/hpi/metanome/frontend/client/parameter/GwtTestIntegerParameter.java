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
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingInteger;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationInteger;
import de.uni_potsdam.hpi.metanome.frontend.client.helpers.InputValidationException;

public class GwtTestIntegerParameter extends GWTTestCase {

	@Test
	public void testCreateWithFixedNumber() throws AlgorithmConfigurationException {
		//Setup
		int noOfValues = 3;
		ConfigurationSpecificationInteger specification = new ConfigurationSpecificationInteger("integer", noOfValues);

		//Execute
		InputParameterIntegerWidget widget = new InputParameterIntegerWidget(specification);

		//Check
		assertEquals(noOfValues, widget.inputWidgets.size());
		assertEquals(noOfValues, widget.getWidgetCount());
		assertFalse(widget.inputWidgets.get(0).isOptional);
	}

	@Test
	public void testCreateWithArbitraryNumber() throws AlgorithmConfigurationException {
		//Setup
		int noOfValues = ConfigurationSpecification.ARBITRARY_NUMBER_OF_VALUES;
		ConfigurationSpecificationInteger specification = new ConfigurationSpecificationInteger("integer", noOfValues);

		//Execute
		InputParameterIntegerWidget widget = new InputParameterIntegerWidget(specification);

		//Check
		assertEquals(1, widget.inputWidgets.size());        //expecting one default input field
		assertEquals(widget.getWidgetCount(), 2);            //default input field + add button
		assertTrue(widget.inputWidgets.get(0).isOptional);    //input field must be optional
	}

	@Test
	public void testAddInput() throws AlgorithmConfigurationException {
		//Setup
		ConfigurationSpecificationInteger specification = new ConfigurationSpecificationInteger("bool",
				ConfigurationSpecification.ARBITRARY_NUMBER_OF_VALUES);
		InputParameterIntegerWidget widget = new InputParameterIntegerWidget(specification);
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
		ConfigurationSpecificationInteger specification = new ConfigurationSpecificationInteger("bool",
				ConfigurationSpecification.ARBITRARY_NUMBER_OF_VALUES);
		InputParameterIntegerWidget widget = new InputParameterIntegerWidget(specification);
		int previousCount = widget.getWidgetCount();
		int listCount = widget.inputWidgets.size();

		//Execute
		widget.inputWidgets.get(0).removeSelf();

		//Check
		assertEquals(previousCount - 1, widget.getWidgetCount());
		assertEquals(listCount - 1, widget.inputWidgets.size());
	}

	@Test
	public void testRetrieveValues() throws AlgorithmConfigurationException {
		//Setup
		int value1 = 7;
		ConfigurationSpecificationInteger specification1 = new ConfigurationSpecificationInteger("integer",
				ConfigurationSpecification.ARBITRARY_NUMBER_OF_VALUES);
		InputParameterIntegerWidget widget1 = new InputParameterIntegerWidget(specification1);

		ConfigurationSpecificationInteger specification2 = new ConfigurationSpecificationInteger("integer",
				ConfigurationSpecification.ARBITRARY_NUMBER_OF_VALUES);
		InputParameterIntegerWidget widget2 = new InputParameterIntegerWidget(specification2);

		InputParameterIntegerWidget widget3 = new InputParameterIntegerWidget(specification2);

		//Execute
		((IntegerInput) widget1.getWidget(0)).textbox.setValue(value1, true);
		ConfigurationSettingInteger[] settings1 = new ConfigurationSettingInteger[0];
		try {
			settings1 = widget1.getUpdatedSpecification().getSettings();
		} catch (InputValidationException e) {
			fail();
		}

		((IntegerInput) widget3.getWidget(0)).textbox.setText("not a number");

		//Check
		assertEquals(1, settings1.length);
		assertEquals(value1, settings1[0].value);

		try {
			widget2.getUpdatedSpecification().getSettings();
		} catch (InputValidationException e) {
			assertTrue(true);
		}

		try {
			widget3.getUpdatedSpecification().getSettings();
		} catch (InputValidationException e) {
			assertTrue(true);
		}
	}

	@Override
	public String getModuleName() {
		return "de.uni_potsdam.hpi.metanome.frontend.MetanomeTest";
	}
}
