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

package de.metanome.frontend.client.parameter;

import com.google.gwt.junit.client.GWTTestCase;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementString;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingString;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.helpers.InputValidationException;
import de.metanome.frontend.client.input_fields.StringInput;

public class GwtTestStringParameter extends GWTTestCase {

  /**
   * Test method for {@link de.metanome.frontend.client.parameter.InputParameterStringWidget#InputParameterStringWidget(de.metanome.algorithm_integration.configuration.ConfigurationRequirementString,
   * de.metanome.frontend.client.TabWrapper)}
   */
  public void testCreateWithFixedNumber() throws AlgorithmConfigurationException {
    //Setup
    int noOfValues = 3;
    ConfigurationRequirementString
        specification =
        new ConfigurationRequirementString("string", noOfValues);

    //Execute
    InputParameterStringWidget
        widget =
        new InputParameterStringWidget(specification, new TabWrapper());

    //Check
    assertEquals(noOfValues, widget.inputWidgets.size());
    assertEquals(noOfValues, widget.getWidgetCount());
    assertFalse(widget.inputWidgets.get(0).isOptional);
  }

  /**
   * Test method for {@link de.metanome.frontend.client.parameter.InputParameterStringWidget#InputParameterStringWidget(de.metanome.algorithm_integration.configuration.ConfigurationRequirementString,
   * de.metanome.frontend.client.TabWrapper)}
   */
  public void testCreateWithArbitraryNumber() throws AlgorithmConfigurationException {
    //Setup
    int noOfValues = ConfigurationRequirement.ARBITRARY_NUMBER_OF_VALUES;
    ConfigurationRequirementString
        specification =
        new ConfigurationRequirementString("string", noOfValues);

    //Execute
    InputParameterStringWidget
        widget =
        new InputParameterStringWidget(specification, new TabWrapper());

    //Check
    assertEquals(1, widget.inputWidgets.size());        //expecting one default input field
    assertEquals(widget.getWidgetCount(), 2);            //default input field + add button
    assertTrue(widget.inputWidgets.get(0).isOptional);    //input field must be optional
  }

  public void testCreateWithRangeNumber() throws AlgorithmConfigurationException {
    //Setup
    int maxValue = 5;
    ConfigurationRequirementString
        specification =
        new ConfigurationRequirementString("string", 3, maxValue);

    //Execute
    InputParameterStringWidget
        widget =
        new InputParameterStringWidget(specification, new TabWrapper());

    //Check
    assertEquals(maxValue, widget.inputWidgets.size());
    assertEquals(maxValue, widget.getWidgetCount());
    assertTrue(widget.inputWidgets.get(0).isRequired);
    assertTrue(widget.inputWidgets.get(1).isRequired);
    assertTrue(widget.inputWidgets.get(2).isRequired);
    assertFalse(widget.inputWidgets.get(3).isRequired);
    assertFalse(widget.inputWidgets.get(4).isRequired);
  }

  /**
   * Test method for {@link de.metanome.frontend.client.parameter.InputParameterStringWidget#addInputField(boolean,
   * boolean, int)}
   */
  public void testAddInput() throws AlgorithmConfigurationException, InputValidationException {
    //Setup
    String expectedValue = "test";
    ConfigurationRequirementString specification = new ConfigurationRequirementString("bool",
                                                                                      ConfigurationRequirement.ARBITRARY_NUMBER_OF_VALUES);
    specification.checkAndSetDefaultValues(expectedValue);
    InputParameterStringWidget
        widget =
        new InputParameterStringWidget(specification, new TabWrapper());
    int previousCount = widget.getWidgetCount();
    int listCount = widget.inputWidgets.size();

    //Execute
    widget.addInputField(true, false, 0);

    //Check
    assertEquals(previousCount + 1, widget.getWidgetCount());
    assertEquals(listCount + 1, widget.inputWidgets.size());
    assertEquals(expectedValue, widget.inputWidgets.get(0).getValue());
  }

  /**
   * Test method for {@link de.metanome.frontend.client.parameter.InputParameterStringWidget#removeField(de.metanome.frontend.client.input_fields.InputField)}
   */
  public void testRemoveInput() throws AlgorithmConfigurationException {
    //Setup
    ConfigurationRequirementString specification = new ConfigurationRequirementString("bool",
                                                                                      ConfigurationRequirement.ARBITRARY_NUMBER_OF_VALUES);
    InputParameterStringWidget
        widget =
        new InputParameterStringWidget(specification, new TabWrapper());
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
   */
  public void testRetrieveValues() throws AlgorithmConfigurationException,
                                          InputValidationException {
    //Setup
    String value = "something";
    ConfigurationRequirementString specification = new ConfigurationRequirementString("bool",
                                                                                      ConfigurationRequirement.ARBITRARY_NUMBER_OF_VALUES);
    InputParameterStringWidget
        widget =
        new InputParameterStringWidget(specification, new TabWrapper());

    //Execute
    ((StringInput) widget.getWidget(0)).textbox.setValue(value, true);
    ConfigurationSettingString[] settings = widget.getUpdatedSpecification().getSettings();

    //Check
    assertEquals(1, settings.length);
    assertEquals(value, settings[0].value);
  }

  @Override
  public String getModuleName() {
    return "de.metanome.frontend.MetanomeTest";
  }
}
