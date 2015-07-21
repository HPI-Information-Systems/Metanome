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
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementRelationalInput;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingDataSource;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingFileInput;
import de.metanome.backend.results_db.FileInput;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.TestHelper;
import de.metanome.frontend.client.helpers.InputValidationException;
import de.metanome.frontend.client.input_fields.RelationalInputInput;

public class GwtTestRelationalInputParameter extends GWTTestCase {

  private String aFileName = "inputA.csv";

  /**
   * Tests the selection of a specific item corresponding to the given ConfigurationSetting.
   */
  public void testSelectDataSourceOnFilledDropdown()
      throws AlgorithmConfigurationException, InputValidationException {
    // Set up
    TestHelper.resetDatabaseSync();

    TabWrapper tabWrapper = new TabWrapper();

    FileInput fileInput = new FileInput("fileInput");
    fileInput.setFileName(aFileName);

    RelationalInputInput widget = new RelationalInputInput(false, false, tabWrapper);
    ConfigurationSettingFileInput setting = new ConfigurationSettingFileInput();
    setting.setFileName(aFileName);

    widget.listbox.addValue("--");
    widget.listbox.addValue(aFileName);
    widget.inputs.put(aFileName, fileInput);

    // Execute
    widget.selectDataSource(setting);

    //Check
    assertEquals(aFileName, widget.listbox.getSelectedValue());
    assertEquals(aFileName, widget.getValues().getValueAsString());

    // Cleanup
    TestHelper.resetDatabaseSync();
  }

  /**
   * When setting a data source on the parent widget (InputParameter), it should be set in the first
   * child widget.
   */
  public void testSetDataSource() throws AlgorithmConfigurationException, InputValidationException {
    // Set up
    TestHelper.resetDatabaseSync();

    TabWrapper tabWrapper = new TabWrapper();

    FileInput fileInput = new FileInput("fileInput");
    fileInput.setFileName(aFileName);

    ConfigurationSettingFileInput setting = new ConfigurationSettingFileInput();
    setting.setFileName(aFileName);

    ConfigurationRequirementRelationalInput
        configSpec =
        new ConfigurationRequirementRelationalInput("test");
    InputParameterRelationalInputWidget
        dataSourceWidget =
        new InputParameterRelationalInputWidget(configSpec, tabWrapper);

    dataSourceWidget.inputWidgets.get(0).listbox.addValue(aFileName);
    dataSourceWidget.inputWidgets.get(0).inputs.put(aFileName, fileInput);

    // Execute
    dataSourceWidget.setDataSource(setting);

    // Check
    assertTrue(
        ((RelationalInputInput) dataSourceWidget.getWidget(0)).listbox.getValues().size() == 2);

    ConfigurationSettingDataSource retrievedSetting = null;
    try {
      retrievedSetting = (ConfigurationSettingDataSource) dataSourceWidget
          .getUpdatedSpecification()
          .getSettings()[0];
    } catch (InputValidationException e) {
      TestHelper.resetDatabaseSync();
      e.printStackTrace();
      fail();
    }
    assertEquals(aFileName, retrievedSetting.getValueAsString());

    // Cleanup
    TestHelper.resetDatabaseSync();
  }

  public void testCreateWithRangeNumber() throws AlgorithmConfigurationException {
    //Setup
    int maxValue = 5;
    ConfigurationRequirementRelationalInput
        specification =
        new ConfigurationRequirementRelationalInput("relation input", 3, maxValue);

    //Execute
    InputParameterRelationalInputWidget
        widget =
        new InputParameterRelationalInputWidget(specification, new TabWrapper());

    //Check
    assertEquals(maxValue, widget.inputWidgets.size());
    assertEquals(maxValue, widget.getWidgetCount());
    assertTrue(widget.inputWidgets.get(0).isRequired);
    assertTrue(widget.inputWidgets.get(1).isRequired);
    assertTrue(widget.inputWidgets.get(2).isRequired);
    assertFalse(widget.inputWidgets.get(3).isRequired);
    assertFalse(widget.inputWidgets.get(4).isRequired);
  }


  @Override
  public String getModuleName() {
    return "de.metanome.frontend.client.MetanomeTest";
  }
}
