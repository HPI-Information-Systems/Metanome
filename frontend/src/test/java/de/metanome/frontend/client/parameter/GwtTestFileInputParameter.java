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
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementFileInput;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingDataSource;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingFileInput;
import de.metanome.backend.results_db.FileInput;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.TestHelper;
import de.metanome.frontend.client.helpers.InputValidationException;
import de.metanome.frontend.client.input_fields.FileInputInput;

public class GwtTestFileInputParameter extends GWTTestCase {

  private String aFileName = "inputA.csv";

  /**
   * Tests the selection of a specific item corresponding to the given ConfigurationSetting.
   */
  public void testSelectDataSourceOnFilledDropdown()
      throws AlgorithmConfigurationException, InputValidationException {
    // Set up
    TestHelper.resetDatabaseSync();

    TabWrapper tabWrapper = new TabWrapper();

    FileInput fileInput = new FileInput();
    fileInput.setFileName(aFileName);

    FileInputInput widget = new FileInputInput(false, false, tabWrapper);
    ConfigurationSettingFileInput setting = new ConfigurationSettingFileInput();
    setting.setFileName(aFileName);

    widget.listBox.addValue("--");
    widget.listBox.addValue(aFileName);
    widget.fileInputs.put(aFileName, fileInput);

    // Execute
    widget.selectDataSource(setting);

    //Check
    assertEquals(aFileName, widget.listBox.getSelectedValue());
    assertEquals(aFileName, widget.getValues().getFileName());

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

    FileInput fileInput = new FileInput();
    fileInput.setFileName(aFileName);

    ConfigurationSettingFileInput setting = new ConfigurationSettingFileInput();
    setting.setFileName(aFileName);

    ConfigurationRequirementFileInput configSpec = new ConfigurationRequirementFileInput("test");
    InputParameterFileInputWidget
        dataSourceWidget =
        new InputParameterFileInputWidget(configSpec, tabWrapper);

    dataSourceWidget.inputWidgets.get(0).listBox.addValue(aFileName);
    dataSourceWidget.inputWidgets.get(0).fileInputs.put(aFileName, fileInput);

    // Execute
    dataSourceWidget.setDataSource(setting);

    // Check
    assertTrue(((FileInputInput) dataSourceWidget.getWidget(0)).listBox.getValues().size() == 1);

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
    ConfigurationRequirementFileInput
        specification =
        new ConfigurationRequirementFileInput("file input", 3, maxValue);

    //Execute
    InputParameterFileInputWidget
        widget =
        new InputParameterFileInputWidget(specification, new TabWrapper());

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
