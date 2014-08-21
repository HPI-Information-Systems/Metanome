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
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationBoolean;
import de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper;

public class GwtTestBooleanParameter extends GWTTestCase {

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterBooleanWidget#InputParameterBooleanWidget(de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationBoolean, de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper)}
   * @throws AlgorithmConfigurationException
   */
  public void testCreateWithFixedNumber() throws AlgorithmConfigurationException {
    //Setup
    int noOfValues = 3;
    ConfigurationSpecificationBoolean
        specification =
        new ConfigurationSpecificationBoolean("bool", noOfValues);

    //Execute
    InputParameterBooleanWidget widget = new InputParameterBooleanWidget(specification, new TabWrapper());

    //Check
    assertEquals(noOfValues, widget.inputWidgets.size());
    assertEquals(noOfValues, widget.getWidgetCount());
    assertFalse(widget.inputWidgets.get(0).isOptional);
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterBooleanWidget#InputParameterBooleanWidget(de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationBoolean, de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper)}
   * @throws AlgorithmConfigurationException
   */
  public void testCreateWithArbitraryNumber() throws AlgorithmConfigurationException {
    //Setup
    int noOfValues = ConfigurationSpecification.ARBITRARY_NUMBER_OF_VALUES;
    ConfigurationSpecificationBoolean
        specification =
        new ConfigurationSpecificationBoolean("bool", noOfValues);

    //Execute
    InputParameterBooleanWidget widget = new InputParameterBooleanWidget(specification, new TabWrapper());

    //Check
    assertEquals(1, widget.inputWidgets.size());        //expecting one default input field
    assertEquals(widget.getWidgetCount(), 2);            //default input field + add button
    assertTrue(widget.inputWidgets.get(0).isOptional);    //input field must be optional
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterBooleanWidget#addInputField(boolean)}
   * @throws AlgorithmConfigurationException
   */
  public void testAddInput() throws AlgorithmConfigurationException {
    //Setup
    ConfigurationSpecificationBoolean specification = new ConfigurationSpecificationBoolean("bool",
                                                                                            ConfigurationSpecification.ARBITRARY_NUMBER_OF_VALUES);
    InputParameterBooleanWidget widget = new InputParameterBooleanWidget(specification, new TabWrapper());
    int previousCount = widget.getWidgetCount();
    int listCount = widget.inputWidgets.size();

    //Execute
    widget.addInputField(true);

    //Check
    assertEquals(previousCount + 1, widget.getWidgetCount());
    assertEquals(listCount + 1, widget.inputWidgets.size());
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterBooleanWidget#removeField(de.uni_potsdam.hpi.metanome.frontend.client.input_fields.InputField)}
   * @throws AlgorithmConfigurationException
   */
  public void testRemoveInput() throws AlgorithmConfigurationException {
    //Setup
    ConfigurationSpecificationBoolean specification = new ConfigurationSpecificationBoolean("bool",
                                                                                            ConfigurationSpecification.ARBITRARY_NUMBER_OF_VALUES);
    InputParameterBooleanWidget widget = new InputParameterBooleanWidget(specification, new TabWrapper());
    int previousCount = widget.getWidgetCount();
    int listCount = widget.inputWidgets.size();

    //Execute
    widget.inputWidgets.get(0).removeSelf();

    //Check
    assertEquals(previousCount - 1, widget.getWidgetCount());
    assertEquals(listCount - 1, widget.inputWidgets.size());
  }

  /**
   * Test method for {@link InputParameterBooleanWidget#getUpdatedSpecification()}
   */
  public void testGetUpdatedSpecification() {
    //Setup
    ConfigurationSpecificationBoolean specification = new ConfigurationSpecificationBoolean("bool",
                                                                                            ConfigurationSpecification.ARBITRARY_NUMBER_OF_VALUES);
    InputParameterBooleanWidget
        widget =
        new InputParameterBooleanWidget(specification, new TabWrapper());

    assertEquals(widget.getSpecification(), specification);

    // Execute
    widget.inputWidgets.get(0).setValue(false);
    ConfigurationSpecificationBoolean
        updatedSpecification =
        (ConfigurationSpecificationBoolean) widget.getUpdatedSpecification();

    //Check
    assertFalse(updatedSpecification.getSettings()[0].value);
  }

  @Override
  public String getModuleName() {
    return "de.uni_potsdam.hpi.metanome.frontend.MetanomeTest";
  }
}
