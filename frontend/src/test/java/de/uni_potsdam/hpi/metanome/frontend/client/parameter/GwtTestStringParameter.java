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

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingString;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationString;

import org.junit.Test;

public class GwtTestStringParameter extends GWTTestCase {

  @Test
  public void testCreateWithFixedNumber() throws AlgorithmConfigurationException {
    //Setup
    int noOfValues = 3;
    ConfigurationSpecificationString
        specification =
        new ConfigurationSpecificationString("string", noOfValues);

    //Execute
    InputParameterStringWidget widget = new InputParameterStringWidget(specification);

    //Check
    assertEquals(noOfValues, widget.inputWidgets.size());
    assertEquals(noOfValues, widget.getWidgetCount());
    assertFalse(widget.inputWidgets.get(0).isOptional);
  }

  @Test
  public void testCreateWithArbitraryNumber() throws AlgorithmConfigurationException {
    //Setup
    int noOfValues = ConfigurationSpecification.ARBITRARY_NUMBER_OF_VALUES;
    ConfigurationSpecificationString
        specification =
        new ConfigurationSpecificationString("string", noOfValues);

    //Execute
    InputParameterStringWidget widget = new InputParameterStringWidget(specification);

    //Check
    assertEquals(1, widget.inputWidgets.size());        //expecting one default input field
    assertEquals(widget.getWidgetCount(), 2);            //default input field + add button
    assertTrue(widget.inputWidgets.get(0).isOptional);    //input field must be optional
  }

  @Test
  public void testAddInput() throws AlgorithmConfigurationException {
    //Setup
    ConfigurationSpecificationString specification = new ConfigurationSpecificationString("bool",
                                                                                          ConfigurationSpecification.ARBITRARY_NUMBER_OF_VALUES);
    InputParameterStringWidget widget = new InputParameterStringWidget(specification);
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
    ConfigurationSpecificationString specification = new ConfigurationSpecificationString("bool",
                                                                                          ConfigurationSpecification.ARBITRARY_NUMBER_OF_VALUES);
    InputParameterStringWidget widget = new InputParameterStringWidget(specification);
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
    String value = "something";
    ConfigurationSpecificationString specification = new ConfigurationSpecificationString("bool",
                                                                                          ConfigurationSpecification.ARBITRARY_NUMBER_OF_VALUES);
    InputParameterStringWidget widget = new InputParameterStringWidget(specification);

    //Execute
    ((StringInput) widget.getWidget(0)).textbox.setValue(value, true);
    ConfigurationSettingString[] settings = widget.getUpdatedSpecification().getSettings();

    //Check
    assertEquals(1, settings.length);
    assertEquals(value, settings[0].value);
  }

  @Override
  public String getModuleName() {
    return "de.uni_potsdam.hpi.metanome.frontend.MetanomeTest";
  }
}
