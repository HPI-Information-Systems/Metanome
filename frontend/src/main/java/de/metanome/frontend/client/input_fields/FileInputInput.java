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

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingDataSource;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingFileInput;
import de.metanome.backend.results_db.FileInput;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.helpers.FilePathHelper;
import de.metanome.frontend.client.helpers.InputValidationException;
import de.metanome.frontend.client.services.FileInputRestService;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An input widget, used to specify settings for a file input.
 *
 * @see de.metanome.backend.results_db.FileInput
 */
public class FileInputInput extends InputField {

  public ListBoxInput listbox;
  public Map<String, FileInput> fileInputs;
  private TabWrapper messageReceiver;
  /**
   * When using the link from Data Sources page, this is where the selected file is stored.
   */
  private String preselectedFilename;

  /**
   * @param optional        specifies whether a remove button should be displayed
   * @param messageReceiver the message receiver
   */
  public FileInputInput(boolean optional, boolean required, TabWrapper messageReceiver) {
    super(optional, required);

    this.messageReceiver = messageReceiver;
    this.fileInputs = new HashMap<>();

    listbox = new ListBoxInput(false ,false);
    updateListBox();
    this.add(listbox);
  }

  /**
   * Get all file inputs from the database and put them into the list box.
   */
  public void updateListBox() {
    MethodCallback<List<FileInput>> callback = new MethodCallback<List<FileInput>>() {
      public void onFailure(Method method, Throwable caught) {
        messageReceiver.addError("There are no file inputs in the database: " + method.getResponse().getText());
      }

      public void onSuccess(Method method, List<FileInput> result) {
        List<String> fileInputNames = new ArrayList<String>();
        fileInputNames.add("--");
        String preselectedIdentifier = null;

        if (result != null && result.size() > 0) {
          for (FileInput input : result) {
            String identifier = FilePathHelper.getFileName(input.getFileName());
            fileInputNames.add(identifier);
            fileInputs.put(identifier, input);

            // set the preselected filename
            if (input.getFileName().equals(preselectedFilename)) {
              preselectedIdentifier = identifier;
            }
          }
        } else {
          messageReceiver.addError("There are no file inputs in the database!");
        }

        listbox.clear();
        listbox.setValues(fileInputNames);
        listbox.disableFirstEntry();

        if (preselectedIdentifier != null) {
          listbox.setSelectedValue(preselectedIdentifier);
        }
      }
    };

    FileInputRestService
        fileInputService = com.google.gwt.core.client.GWT.create(FileInputRestService.class);
    fileInputService.listFileInputs(callback);
  }

  /**
   * Selects the given data source in the list box. If the list box has not yet been filled with the
   * available values, we save the value and set it when the list box is filled.
   *
   * @param dataSourceSetting the data source setting
   * @throws AlgorithmConfigurationException If the data source setting is not a csv file setting
   */
  public void selectDataSource(ConfigurationSettingDataSource dataSourceSetting)
      throws AlgorithmConfigurationException {
    this.preselectedFilename = dataSourceSetting.getValueAsString();

    if (!this.listbox.containsValues()) {
      return;
    }

    if (dataSourceSetting instanceof ConfigurationSettingFileInput) {
      ConfigurationSettingFileInput setting = (ConfigurationSettingFileInput) dataSourceSetting;
      this.setValues(setting);
    } else {
      throw new AlgorithmConfigurationException("This is not a csv file setting.");
    }
  }

  /**
   * Returns the current widget's settings as a setting
   *
   * @return the widget's settings
   */
  public ConfigurationSettingFileInput getValues() throws InputValidationException {
    String selectedValue = this.listbox.getSelectedValue();

    if (selectedValue == null || selectedValue.equals("--")) {
      if (isRequired) {
        throw new InputValidationException("You must choose a file input!");
      } else {
        return null;
      }
    }

    FileInput currentFileInput = this.fileInputs.get(selectedValue);

    return getCurrentSetting(currentFileInput);
  }

  /**
   * Takes a setting a sets the selected value of the list box to the given setting.
   *
   * @param setting the settings to set
   * @throws AlgorithmConfigurationException if no file inputs are set
   */
  public void setValues(ConfigurationSettingFileInput setting)
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
   * Creates a ConfigurationSettingCsvFile from the given FileInput
   *
   * @param fileInput the file input
   * @return the setting generated from the file input
   */
  protected ConfigurationSettingFileInput getCurrentSetting(FileInput fileInput) {
    ConfigurationSettingFileInput setting = new ConfigurationSettingFileInput()
        .setId(fileInput.getId())
        .setFileName(fileInput.getFileName())
        .setEscapeChar(fileInput.getEscapechar())
        .setHeader(fileInput.isHasHeader())
        .setIgnoreLeadingWhiteSpace(fileInput.isIgnoreLeadingWhiteSpace())
        .setQuoteChar(fileInput.getQuotechar())
        .setSeparatorChar(fileInput.getSeparator())
        .setSkipDifferingLines(fileInput.isSkipDifferingLines())
        .setSkipLines(fileInput.getSkipLines())
        .setStrictQuotes(fileInput.isStrictQuotes());

    return setting;
  }

}
