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

package de.uni_potsdam.hpi.metanome.frontend.client.input_fields;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingCsvFile;
import de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper;
import de.uni_potsdam.hpi.metanome.frontend.client.TestHelper;
import de.uni_potsdam.hpi.metanome.results_db.FileInput;

/**
 * Tests for {@link de.uni_potsdam.hpi.metanome.frontend.client.input_fields.CsvFileInput}
 */
public class GwtTestCsvFileInput extends GWTTestCase {

  /**
   * Test method for
   * {@link de.uni_potsdam.hpi.metanome.frontend.client.input_fields.CsvFileInput#CsvFileInput(boolean, de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper)}
   * <p/>
   * After calling the constructor the optional parameter should be set correctly and all widgets
   * should be initialized.
   */
  public void testConstructor() {
    // Setup
    TabWrapper tabWrapper = new TabWrapper();

    // Expected values
    boolean expectedOptional = true;

    // Execute functionality
    CsvFileInput actualCsvFileInput = new CsvFileInput(expectedOptional, tabWrapper);

    // Check result
    assertEquals(expectedOptional, actualCsvFileInput.isOptional);
    assertEquals(2, actualCsvFileInput.getWidgetCount());
    assertNotNull(actualCsvFileInput.listbox);
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.frontend.client.input_fields.CsvFileInput#getValues()} and
   * {@link de.uni_potsdam.hpi.metanome.frontend.client.input_fields.CsvFileInput#setValues(de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingCsvFile)}
   * <p/>
   * The getValues and setValues methods should set and retrieve settings.
   */
  public void testGetSetValues() {
    // Setup
    TestHelper.resetDatabaseSync();

    final TabWrapper tabWrapper = new TabWrapper();

    FileInput fileInput = new FileInput();
    fileInput.setFileName("filename");
    TestHelper.storeFileInput(fileInput, new AsyncCallback<Long>() {
      @Override
      public void onFailure(Throwable caught) {
        System.out.println(caught.getMessage());
        fail();
      }

      @Override
      public void onSuccess(Long id) {

      }
    });

    // Expected values
    final ConfigurationSettingCsvFile expectedSetting =
        new ConfigurationSettingCsvFile("filename");

    // Initialize CsvFileInput (waiting for fetching all current file inputs)
    final CsvFileInput[] csvFileInputs = new CsvFileInput[1];
    Timer setupTimer = new Timer() {
      @Override
      public void run() {
        csvFileInputs[0] = new CsvFileInput(false, tabWrapper);
      }
    };

    Timer executeTimer = new Timer() {
      @Override
      public void run() {
        // Execute functionality
        try {
          csvFileInputs[0].setValues(expectedSetting);
        } catch (AlgorithmConfigurationException e) {
          fail();
        }

        ConfigurationSettingCsvFile actualSetting = csvFileInputs[0].getValues();

        // Check result
        assertEquals(expectedSetting.getFileName(), actualSetting.getFileName());

        // Cleanup
        TestHelper.resetDatabaseSync();

        finishTest();
      }
    };

    delayTestFinish(12000);

    // Waiting for asynchronous calls to finish.
    setupTimer.schedule(4000);
    executeTimer.schedule(8000);
  }

  @Override
  public String getModuleName() {
    return "de.uni_potsdam.hpi.metanome.frontend.client.MetanomeTest";
  }
}
