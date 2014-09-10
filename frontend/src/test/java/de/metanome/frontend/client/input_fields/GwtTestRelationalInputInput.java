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

package de.metanome.frontend.client.input_fields;

import com.google.gwt.junit.client.GWTTestCase;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingFileInput;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingRelationalInput;
import de.metanome.backend.results_db.FileInput;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.TestHelper;
import de.metanome.frontend.client.helpers.InputValidationException;

public class GwtTestRelationalInputInput extends GWTTestCase {

  /**
   * Test method for {@link de.metanome.frontend.client.input_fields.RelationalInputInput#RelationalInputInput(boolean, de.metanome.frontend.client.TabWrapper)}
   * <p/> After calling the constructor the optional parameter should be set correctly and all
   * widgets should be initialized.
   */
  public void testConstructor() {
    // Set up
    TestHelper.resetDatabaseSync();

    // Setup
    TabWrapper tabWrapper = new TabWrapper();

    // Expected values
    boolean expectedOptional = true;

    // Execute functionality
    RelationalInputInput actualRelationalInputInput = new RelationalInputInput(expectedOptional, tabWrapper);

    // Check result
    assertEquals(expectedOptional, actualRelationalInputInput.isOptional);
    assertEquals(2, actualRelationalInputInput.getWidgetCount());
    assertNotNull(actualRelationalInputInput.listbox);

    // Cleanup
    TestHelper.resetDatabaseSync();
  }

  /**
   * Test method for {@link RelationalInputInput#getValues()} and {@link RelationalInputInput#setValues(de.metanome.algorithm_integration.configuration.ConfigurationSettingRelationalInput)}
   * <p/> The getValues and setValues methods should set and retrieve settings.
   */
  public void testGetSetValues() {
    // Set up
    TestHelper.resetDatabaseSync();

    FileInput fileInput = new FileInput();
    fileInput.setFileName("filename");

    // Expected values
    final ConfigurationSettingRelationalInput expectedSetting =
        new ConfigurationSettingFileInput("filename");

    // Initialize CsvFileInput (waiting for fetching all current file inputs)
    final RelationalInputInput relationalInputInputs = new RelationalInputInput(false, new TabWrapper());

    relationalInputInputs.listbox.addValue("filename");
    relationalInputInputs.inputs.put("filename", fileInput);

    try {
      relationalInputInputs.setValues(expectedSetting);
    } catch (AlgorithmConfigurationException e) {
      fail();
    }

    ConfigurationSettingRelationalInput actualSetting = null;
    try {
      actualSetting = relationalInputInputs.getValues();
    } catch (InputValidationException | AlgorithmConfigurationException e) {
      fail();
    }

    // Check result
    assertEquals(expectedSetting.getValueAsString(), actualSetting.getValueAsString());

    // Cleanup
    TestHelper.resetDatabaseSync();
  }


  @Override
  public String getModuleName() {
    return "de.metanome.frontend.client.MetanomeTest";
  }
}
