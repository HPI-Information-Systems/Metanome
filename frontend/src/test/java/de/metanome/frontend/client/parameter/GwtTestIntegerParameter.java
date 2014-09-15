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
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementInteger;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingInteger;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.helpers.InputValidationException;
import de.metanome.frontend.client.input_fields.IntegerInput;

import org.junit.Test;

public class GwtTestIntegerParameter extends GWTTestCase {

  @Test
  public void testCreateWithFixedNumber() throws AlgorithmConfigurationException {
    //Setup
    int noOfValues = 3;
    ConfigurationRequirementInteger
        specification =
        new ConfigurationRequirementInteger("integer", noOfValues);

    //Execute
    InputParameterIntegerWidget
        widget =
        new InputParameterIntegerWidget(specification, new TabWrapper());

    //Check
    assertEquals(noOfValues, widget.inputWidgets.size());
    assertEquals(noOfValues, widget.getWidgetCount());
    assertFalse(widget.inputWidgets.get(0).isOptional);
  }

  @Test
  public void testCreateWithArbitraryNumber() throws AlgorithmConfigurationException {
    //Setup
    int noOfValues = ConfigurationRequirement.ARBITRARY_NUMBER_OF_VALUES;
    ConfigurationRequirementInteger
        specification =
        new ConfigurationRequirementInteger("integer", noOfValues);

    //Execute
    InputParameterIntegerWidget
        widget =
        new InputParameterIntegerWidget(specification, new TabWrapper());

    //Check
    assertEquals(1, widget.inputWidgets.size());        //expecting one default input field
    assertEquals(widget.getWidgetCount(), 2);            //default input field + add button
    assertTrue(widget.inputWidgets.get(0).isOptional);    //input field must be optional
  }

  @Test
  public void testAddInput() throws AlgorithmConfigurationException {
    //Setup
    ConfigurationRequirementInteger specification = new ConfigurationRequirementInteger("bool",
                                                                                            ConfigurationRequirement.ARBITRARY_NUMBER_OF_VALUES);
    InputParameterIntegerWidget
        widget =
        new InputParameterIntegerWidget(specification, new TabWrapper());
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
    ConfigurationRequirementInteger specification = new ConfigurationRequirementInteger("bool",
                                                                                            ConfigurationRequirement.ARBITRARY_NUMBER_OF_VALUES);
    InputParameterIntegerWidget
        widget =
        new InputParameterIntegerWidget(specification, new TabWrapper());
    int previousCount = widget.getWidgetCount();
    int listCount = widget.inputWidgets.size();

    //Execute
    widget.inputWidgets.get(0).removeSelf();

    //Check
    assertEquals(previousCount - 1, widget.getWidgetCount());
    assertEquals(listCount - 1, widget.inputWidgets.size());
  }

  /**
   * Test method for {@link InputParameterIntegerWidget#getUpdatedSpecification()}
   */
  public void testRetrieveValues() throws AlgorithmConfigurationException {
    //Setup
    int value1 = 7;
    ConfigurationRequirementInteger
        specification1 =
        new ConfigurationRequirementInteger("integer",
                                              ConfigurationRequirement.ARBITRARY_NUMBER_OF_VALUES);
    InputParameterIntegerWidget
        widget1 =
        new InputParameterIntegerWidget(specification1, new TabWrapper());

    ConfigurationRequirementInteger
        specification2 =
        new ConfigurationRequirementInteger("integer",
                                              ConfigurationRequirement.ARBITRARY_NUMBER_OF_VALUES);
    InputParameterIntegerWidget
        widget2 =
        new InputParameterIntegerWidget(specification2, new TabWrapper());

    InputParameterIntegerWidget
        widget3 =
        new InputParameterIntegerWidget(specification2, new TabWrapper());

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
    return "de.metanome.frontend.MetanomeTest";
  }
}
