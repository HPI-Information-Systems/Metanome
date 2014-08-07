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
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingCsvFile;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingDataSource;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationCsvFile;
import de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper;
import de.uni_potsdam.hpi.metanome.frontend.client.TestHelper;
import de.uni_potsdam.hpi.metanome.frontend.client.helpers.InputValidationException;
import de.uni_potsdam.hpi.metanome.frontend.client.input_fields.CsvFileInput;
import de.uni_potsdam.hpi.metanome.results_db.FileInput;

public class GwtTestCsvFileParameter extends GWTTestCase {

  private String aFileName = "inputA.csv";

  /**
   * Tests the selection of a specific item corresponding to the given ConfigurationSetting.
   */
  public void testSelectDataSourceOnFilledDropdown()
      throws AlgorithmConfigurationException, InputValidationException {
    // Setup
    TestHelper.resetDatabaseSync();

    final TabWrapper tabWrapper = new TabWrapper();

    FileInput fileInput = new FileInput();
    fileInput.setFileName(aFileName);
    final long[] inputId = new long[1];
    TestHelper.storeFileInput(fileInput, new AsyncCallback<Long>() {
      @Override
      public void onFailure(Throwable caught) {
        System.out.println(caught.getMessage());
        fail();
      }

      @Override
      public void onSuccess(Long id) {
        inputId[0] = id;
      }
    });

    final CsvFileInput[] widget = new CsvFileInput[1];
    final ConfigurationSettingCsvFile setting = new ConfigurationSettingCsvFile();
    setting.setFileName(aFileName);

    Timer setUpTimer = new Timer() {
      @Override
      public void run() {
        widget[0] = new CsvFileInput(false, tabWrapper);
      }
    };

    Timer executeTimer = new Timer() {
      @Override
      public void run() {
        //Execute
        try {
          widget[0].selectDataSource(setting);
        } catch (AlgorithmConfigurationException e) {
          System.out.println(e.getMessage());
          fail();
        }

        //Check
        assertEquals(aFileName, widget[0].listbox.getSelectedValue());
        assertEquals(aFileName, widget[0].getValues().getFileName());

        // Cleanup
        TestHelper.resetDatabaseSync();

        finishTest();
      }
    };

    delayTestFinish(10000);

    // Waiting for asynchronous calls to finish.
    setUpTimer.schedule(4000);
    executeTimer.schedule(8000);

  }

  /**
   * When setting a data source on the parent widget (InputParameter), it should be set in the first
   * child widget.
   */
  public void testSetDataSource() throws AlgorithmConfigurationException, InputValidationException {
    // Setup
    TestHelper.resetDatabaseSync();

    final TabWrapper tabWrapper = new TabWrapper();

    FileInput fileInput = new FileInput();
    fileInput.setFileName(aFileName);
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

    final ConfigurationSettingCsvFile setting = new ConfigurationSettingCsvFile();
    setting.setFileName(aFileName);

    final InputParameterCsvFileWidget[] dataSourceWidget = new InputParameterCsvFileWidget[1];
    Timer setUpTimer = new Timer() {
      @Override
      public void run() {
        //Setup
        ConfigurationSpecificationCsvFile configSpec = new ConfigurationSpecificationCsvFile("test");
        dataSourceWidget[0] = new InputParameterCsvFileWidget(configSpec, tabWrapper);
      }
    };

    Timer executeTimer = new Timer() {
      @Override
      public void run() {
        //Execute
        try {
          dataSourceWidget[0].setDataSource(setting);
        } catch (AlgorithmConfigurationException e) {
          e.printStackTrace();
          fail();
        }

        //Check
        assertTrue(((CsvFileInput) dataSourceWidget[0].getWidget(0)).listbox.getValues().size() > 1);

        ConfigurationSettingDataSource retrievedSetting = null;
        try {
          retrievedSetting = (ConfigurationSettingDataSource) dataSourceWidget[0]
              .getUpdatedSpecification()
              .getSettings()[0];
        } catch (InputValidationException e) {
          e.printStackTrace();
          fail();
        }
        assertEquals(aFileName, retrievedSetting.getValueAsString());

        // Cleanup
        TestHelper.resetDatabaseSync();

        finishTest();
      }
    };

    delayTestFinish(10000);

    // Waiting for asynchronous calls to finish.
    setUpTimer.schedule(4000);
    executeTimer.schedule(8000);
  }


  @Override
  public String getModuleName() {
    return "de.uni_potsdam.hpi.metanome.frontend.client.MetanomeTest";
  }
}
