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
import de.metanome.backend.results_db.FileInput;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.TestHelper;
import de.metanome.frontend.client.helpers.InputValidationException;

/**
 * Tests for {@link FileInputInput}
 */
public class GwtTestFileInputInput extends GWTTestCase {

  /**
   * Test method for {@link FileInputInput#FileInputInput(boolean, de.metanome.frontend.client.TabWrapper)}
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
    FileInputInput actualFileInputInput = new FileInputInput(expectedOptional, tabWrapper);

    // Check result
    assertEquals(expectedOptional, actualFileInputInput.isOptional);
    assertEquals(2, actualFileInputInput.getWidgetCount());
    assertNotNull(actualFileInputInput.listbox);

    // Cleanup
    TestHelper.resetDatabaseSync();
  }

  /**
   * Test method for {@link FileInputInput#getValues()} and {@link FileInputInput#setValues(de.metanome.algorithm_integration.configuration.ConfigurationSettingFileInput)}
   * <p/> The getValues and setValues methods should set and retrieve settings.
   */
  public void testGetSetValues() {
    // Set up
    TestHelper.resetDatabaseSync();

    FileInput fileInput = new FileInput();
    fileInput.setFileName("filename");

    // Expected values
    final ConfigurationSettingFileInput expectedSetting =
        new ConfigurationSettingFileInput("filename");

    // Initialize CsvFileInput (waiting for fetching all current file inputs)
    final FileInputInput fileInputInputs = new FileInputInput(false, new TabWrapper());

    fileInputInputs.listbox.addValue("filename");
    fileInputInputs.fileInputs.put("filename", fileInput);

    try {
      fileInputInputs.setValues(expectedSetting);
    } catch (AlgorithmConfigurationException e) {
      fail();
    }

    ConfigurationSettingFileInput actualSetting = null;
    try {
      actualSetting = fileInputInputs.getValues();
    } catch (InputValidationException e) {
      fail();
    }

    // Check result
    assertEquals(expectedSetting.getFileName(), actualSetting.getFileName());

    // Cleanup
    TestHelper.resetDatabaseSync();
  }

  @Override
  public String getModuleName() {
    return "de.metanome.frontend.client.MetanomeTest";
  }
}
