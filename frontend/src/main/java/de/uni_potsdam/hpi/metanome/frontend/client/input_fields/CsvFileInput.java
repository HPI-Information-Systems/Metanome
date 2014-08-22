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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingCsvFile;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingDataSource;
import de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper;
import de.uni_potsdam.hpi.metanome.frontend.client.services.FileInputService;
import de.uni_potsdam.hpi.metanome.frontend.client.services.FileInputServiceAsync;
import de.uni_potsdam.hpi.metanome.results_db.FileInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An input widget, used to specify settings for a file input.
 *
 * @see de.uni_potsdam.hpi.metanome.results_db.FileInput
 */
public class CsvFileInput extends InputField {

  public ListBoxInput listbox;
  public Map<String, FileInput> fileInputs;
  private TabWrapper messageReceiver;
  /**
   * When using the link from Data Sources page, this is where the selected file is stored.
   */
  private String preselectedFilename;

  /**
   * @param optional specifies whether a remove button should be displayed
   * @param messageReceiver the message receiver
   */
  public CsvFileInput(boolean optional, TabWrapper messageReceiver) {
    super(optional);

    this.messageReceiver = messageReceiver;
    this.fileInputs = new HashMap<>();

    listbox = new ListBoxInput(false);
    updateListBox();
    this.add(listbox);
  }

  /**
   * Get all file inputs from the database and put them into the list box.
   */
  private void updateListBox() {
    AsyncCallback<List<FileInput>> callback = new AsyncCallback<List<FileInput>>() {
      public void onFailure(Throwable caught) {
        messageReceiver.addError("There are no file inputs in the database!");
      }

      public void onSuccess(List<FileInput> result) {
        List<String> fileInputNames = new ArrayList<String>();
        fileInputNames.add("--");
        String preselectedIdentifier = null;

        if (result != null && result.size() > 0) {
          for (FileInput input : result) {
            String identifier = input.getFileName();
            fileInputNames.add(identifier);
            fileInputs.put(identifier, input);

            // set the preselected filename
            if (input.getFileName().equals(preselectedFilename))
              preselectedIdentifier = identifier;
          }
        } else {
          messageReceiver.addError("There are no file inputs in the database!");
        }

        listbox.clear();
        listbox.setValues(fileInputNames);
        listbox.disableFirstEntry();

        if (preselectedIdentifier != null)
          listbox.setSelectedValue(preselectedIdentifier);
      }
    };

    FileInputServiceAsync
        fileInputService = GWT.create(FileInputService.class);
    fileInputService.listFileInputs(callback);
  }

  /**
   * Selects the given data source in the list box.
   * If the list box has not yet been filled with the available values,
   * we save the value and set it when the list box is filled.
   *
   * @param dataSourceSetting the data source setting
   * @throws AlgorithmConfigurationException If the data source setting is not a csv file setting
   */
  public void selectDataSource(ConfigurationSettingDataSource dataSourceSetting)
      throws AlgorithmConfigurationException {
    this.preselectedFilename = dataSourceSetting.getValueAsString();

    if (!this.listbox.containsValues())
      return;

    if (dataSourceSetting instanceof ConfigurationSettingCsvFile) {
      ConfigurationSettingCsvFile setting = (ConfigurationSettingCsvFile) dataSourceSetting;
      this.setValues(setting);
    } else {
      throw new AlgorithmConfigurationException("This is not a csv file setting.");
    }
  }

  /**
   * Takes a setting a sets the selected value of the list box to the given setting.
   *
   * @param setting the settings to set
   * @throws AlgorithmConfigurationException if no file inputs are set
   */
  public void setValues(ConfigurationSettingCsvFile setting)
      throws AlgorithmConfigurationException {
    for (Map.Entry<String, FileInput> input : this.fileInputs.entrySet()) {
      FileInput current = input.getValue();
      if (current.getFileName().equals(setting.getFileName())) {
        this.listbox.setSelectedValue(input.getKey());
        return;
      }
    }
    throw new AlgorithmConfigurationException("The file inputs are not set yet.");
  }

  /**
   * Returns the current widget's settings as a setting
   *
   * @return the widget's settings
   */
  public ConfigurationSettingCsvFile getValues() {
    if (this.listbox.getSelectedValue().equals("--")) {
      this.messageReceiver.addError("You must choose a CSV file!");
      return null;
    }

    FileInput currentFileInput = this.fileInputs.get(
        this.listbox.getSelectedValue());

    return getCurrentSetting(currentFileInput);
  }

  /**
   * Creates a ConfigurationSettingCsvFile from the given FileInput
   * @param fileInput the file input
   * @return the setting generated from the file input
   */
  protected ConfigurationSettingCsvFile getCurrentSetting(FileInput fileInput) {
    ConfigurationSettingCsvFile setting = new ConfigurationSettingCsvFile();

    setting.setFileName(fileInput.getFileName());
    setting.setEscapeChar(fileInput.getEscapechar());
    setting.setHeader(fileInput.isHasHeader());
    setting.setIgnoreLeadingWhiteSpace(fileInput.isIgnoreLeadingWhiteSpace());
    setting.setQuoteChar(fileInput.getQuotechar());
    setting.setSeparatorChar(fileInput.getSeparator());
    setting.setSkipDifferingLines(fileInput.isSkipDifferingLines());
    setting.setSkipLines(fileInput.getSkipLines());
    setting.setStrictQuotes(fileInput.isStrictQuotes());

    return setting;
  }

}
