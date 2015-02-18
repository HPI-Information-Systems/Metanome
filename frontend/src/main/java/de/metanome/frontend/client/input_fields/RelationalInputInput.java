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
import de.metanome.algorithm_integration.configuration.ConfigurationSettingDatabaseConnection;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingFileInput;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingRelationalInput;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingTableInput;
import de.metanome.backend.results_db.DatabaseConnection;
import de.metanome.backend.results_db.FileInput;
import de.metanome.backend.results_db.Input;
import de.metanome.backend.results_db.TableInput;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.helpers.FilePathHelper;
import de.metanome.frontend.client.helpers.InputValidationException;
import de.metanome.frontend.client.services.InputRestService;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RelationalInputInput extends InputField {

  public ListBoxInput listbox;
  public Map<String, Input> inputs;
  private TabWrapper messageReceiver;
  /**
   * When using the link from Data Sources page, this is where the selected file is stored.
   */
  private String preselectedInput;

  /**
   * @param optional        specifies whether a remove button should be displayed
   * @param messageReceiver the message receiver
   */
  public RelationalInputInput(boolean optional, boolean required, TabWrapper messageReceiver) {
    super(optional, required);

    this.messageReceiver = messageReceiver;
    this.inputs = new HashMap<>();

    listbox = new ListBoxInput(false, false);
    updateListBox();
    this.add(listbox);
  }

  /**
   * Get all inputs from the database and put them into the list box.
   */
  public void updateListBox() {

    MethodCallback<List<Input>> callback = new MethodCallback<List<Input>>() {

      public void onFailure(Method method, Throwable caught) {
        messageReceiver.addError("There are no relational inputs in the database: " +
                                 method.getResponse().getText());
      }

      public void onSuccess(Method method, List<Input> result) {
        List<String> inputNames = new ArrayList<String>();
        inputNames.add("--");
        String preselectedIdentifier = null;

        if (result != null && result.size() > 0) {
          for (Input input : result) {
            String identifier = input.getIdentifier();

            if (input instanceof FileInput)
              identifier = FilePathHelper.getFileName(identifier);

            inputNames.add(identifier);
            inputs.put(identifier, input);

            // set the preselected filename
            if (input.getIdentifier().equals(preselectedInput)) {
              preselectedIdentifier = identifier;
            }
          }
        } else {
          messageReceiver.addError("There are no relational inputs in the database!");
        }

        listbox.clear();
        listbox.setValues(inputNames);
        listbox.disableFirstEntry();

        if (preselectedIdentifier != null) {
          listbox.setSelectedValue(preselectedIdentifier);
        }
      }

    };

    InputRestService inputService = com.google.gwt.core.client.GWT.create(InputRestService.class);
    inputService.listRelationalInputs(callback);
  }

  /**
   * Selects the given data source in the list box. If the list box has not yet been filled with the
   * available values, we save the value and set it when the list box is filled.
   *
   * @param dataSourceSetting the data source setting
   * @throws de.metanome.algorithm_integration.AlgorithmConfigurationException If the data source setting is not a relational input setting
   */
  public void selectDataSource(ConfigurationSettingDataSource dataSourceSetting)
      throws AlgorithmConfigurationException {
    this.preselectedInput = dataSourceSetting.getValueAsString();

    if (!this.listbox.containsValues()) {
      return;
    }

    if (dataSourceSetting instanceof ConfigurationSettingRelationalInput) {
      ConfigurationSettingRelationalInput setting = (ConfigurationSettingRelationalInput) dataSourceSetting;
      this.setValues(setting);
    } else {
      throw new AlgorithmConfigurationException("This is not a relational input setting.");
    }
  }

  /**
   * Returns the current widget's settings as a setting
   *
   * @return the widget's settings
   */
  public ConfigurationSettingRelationalInput getValues()
      throws InputValidationException, AlgorithmConfigurationException {
    String selectedValue = this.listbox.getSelectedValue();

    if (selectedValue == null || selectedValue.equals("--")) {
      if (isRequired) {
        throw new InputValidationException("You must choose an input!");
      } else {
        return null;
      }
    }

    Input currentInput = this.inputs.get(selectedValue);

    return getCurrentSetting(currentInput);
  }

  /**
   * Takes a setting a sets the selected value of the list box to the given setting.
   *
   * @param setting the settings to set
   * @throws AlgorithmConfigurationException if no inputs are set
   */
  public void setValues(ConfigurationSettingRelationalInput setting)
      throws AlgorithmConfigurationException {
    for (Map.Entry<String, Input> input : this.inputs.entrySet()) {
      Input current = input.getValue();
      if (current.getIdentifier().equals(setting.getValueAsString())) {
        this.listbox.setSelectedValue(input.getKey());
        return;
      }
    }
    throw new AlgorithmConfigurationException("The file inputs are not set yet.");
  }

  /**
   * Creates a ConfigurationSettingRelationalInput from the given Input
   *
   * @param input the input
   * @return the setting generated from the input
   */
  protected ConfigurationSettingRelationalInput getCurrentSetting(Input input)
      throws AlgorithmConfigurationException {
    if (input instanceof FileInput)
      return getConfigurationSettingFileInput((FileInput) input);
    if (input instanceof TableInput)
      return getConfigurationSettingTableInput((TableInput) input);

    throw new AlgorithmConfigurationException("Input not supported!");
  }

  private ConfigurationSettingRelationalInput getConfigurationSettingTableInput(TableInput tableInput) {
    ConfigurationSettingTableInput setting = new ConfigurationSettingTableInput();

    DatabaseConnection databaseConnection = tableInput.getDatabaseConnection();
    ConfigurationSettingDatabaseConnection databaseConnectionSetting = new ConfigurationSettingDatabaseConnection(
        databaseConnection.getUrl(),
        databaseConnection.getUsername(),
        databaseConnection.getPassword(),
        databaseConnection.getSystem());

    setting.setTable(tableInput.getTableName());
    setting.setDatabaseConnection(databaseConnectionSetting);

    return setting;
  }

  private ConfigurationSettingRelationalInput getConfigurationSettingFileInput(FileInput fileInput) {
    return new ConfigurationSettingFileInput()
        .setFileName(fileInput.getFileName())
        .setEscapeChar(fileInput.getEscapeCharAsChar())
        .setHeader(fileInput.isHasHeader())
        .setIgnoreLeadingWhiteSpace(fileInput.isIgnoreLeadingWhiteSpace())
        .setQuoteChar(fileInput.getQuoteCharAsChar())
        .setSeparatorChar(fileInput.getSeparatorAsChar())
        .setSkipDifferingLines(fileInput.isSkipDifferingLines())
        .setSkipLines(fileInput.getSkipLines())
        .setStrictQuotes(fileInput.isStrictQuotes());
  }


}
