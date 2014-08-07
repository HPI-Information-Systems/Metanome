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
import de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper;
import de.uni_potsdam.hpi.metanome.frontend.client.input_fields.StringInput;

public class GwtTestStringParameter extends GWTTestCase {

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterStringWidget#InputParameterStringWidget(de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationString, de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper)}
   * @throws AlgorithmConfigurationException
   */
  public void testCreateWithFixedNumber() throws AlgorithmConfigurationException {
    //Setup
    int noOfValues = 3;
    ConfigurationSpecificationString
        specification =
        new ConfigurationSpecificationString("string", noOfValues);

    //Execute
    InputParameterStringWidget widget = new InputParameterStringWidget(specification, new TabWrapper());

    //Check
    assertEquals(noOfValues, widget.inputWidgets.size());
    assertEquals(noOfValues, widget.getWidgetCount());
    assertFalse(widget.inputWidgets.get(0).isOptional);
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterStringWidget#InputParameterStringWidget(de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationString, de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper)}
   * @throws AlgorithmConfigurationException
   */
  public void testCreateWithArbitraryNumber() throws AlgorithmConfigurationException {
    //Setup
    int noOfValues = ConfigurationSpecification.ARBITRARY_NUMBER_OF_VALUES;
    ConfigurationSpecificationString
        specification =
        new ConfigurationSpecificationString("string", noOfValues);

    //Execute
    InputParameterStringWidget widget = new InputParameterStringWidget(specification, new TabWrapper());

    //Check
    assertEquals(1, widget.inputWidgets.size());        //expecting one default input field
    assertEquals(widget.getWidgetCount(), 2);            //default input field + add button
    assertTrue(widget.inputWidgets.get(0).isOptional);    //input field must be optional
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterStringWidget#addInputField(boolean)}
   * @throws AlgorithmConfigurationException
   */
  public void testAddInput() throws AlgorithmConfigurationException {
    //Setup
    ConfigurationSpecificationString specification = new ConfigurationSpecificationString("bool",
                                                                                          ConfigurationSpecification.ARBITRARY_NUMBER_OF_VALUES);
    InputParameterStringWidget widget = new InputParameterStringWidget(specification, new TabWrapper());
    int previousCount = widget.getWidgetCount();
    int listCount = widget.inputWidgets.size();

    //Execute
    widget.addInputField(true);

    //Check
    assertEquals(previousCount + 1, widget.getWidgetCount());
    assertEquals(listCount + 1, widget.inputWidgets.size());
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterStringWidget#removeField(de.uni_potsdam.hpi.metanome.frontend.client.input_fields.InputField)}
   * @throws AlgorithmConfigurationException
   */
  public void testRemoveInput() throws AlgorithmConfigurationException {
    //Setup
    ConfigurationSpecificationString specification = new ConfigurationSpecificationString("bool",
                                                                                          ConfigurationSpecification.ARBITRARY_NUMBER_OF_VALUES);
    InputParameterStringWidget widget = new InputParameterStringWidget(specification, new TabWrapper());
    int previousCount = widget.getWidgetCount();
    int listCount = widget.inputWidgets.size();

    //Execute
    widget.inputWidgets.get(0).removeSelf();

    //Check
    assertEquals(previousCount - 1, widget.getWidgetCount());
    assertEquals(listCount - 1, widget.inputWidgets.size());
  }

  /**
   * Test method for {@link InputParameterStringWidget#getUpdatedSpecification()}
   * @throws AlgorithmConfigurationException
   */
  public void testRetrieveValues() throws AlgorithmConfigurationException {
    //Setup
    String value = "something";
    ConfigurationSpecificationString specification = new ConfigurationSpecificationString("bool",
                                                                                          ConfigurationSpecification.ARBITRARY_NUMBER_OF_VALUES);
    InputParameterStringWidget widget = new InputParameterStringWidget(specification, new TabWrapper());

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
