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
import de.metanome.algorithm_integration.configuration.ConfigurationSettingTableInput;
import de.metanome.backend.results_db.DatabaseConnection;
import de.metanome.backend.results_db.TableInput;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.helpers.InputValidationException;
import de.metanome.frontend.client.services.TableInputRestService;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An input widget, used to specify settings for a table input.
 *
 * @see de.metanome.backend.results_db.TableInput
 */
public class TableInputInput extends InputField {

  public ListBoxInput listBox;
  public Map<String, TableInput> tableInputs;
  private TabWrapper messageReceiver;
  /**
   * When using the link from Data Sources page, this is where the selected table input is stored.
   */
  private String preselectedTableInput;

  /**
   * @param optional        specifies whether a remove button should be displayed
   * @param messageReceiver the message receiver
   */
  public TableInputInput(boolean optional, boolean required, TabWrapper messageReceiver) {
    super(optional, required);

    this.messageReceiver = messageReceiver;
    this.tableInputs = new HashMap<>();

    listBox = new ListBoxInput(false, false);
    updateListBox();
    this.add(listBox);
  }

  /**
   * Get all table inputs from the database and put them into the list box.
   */
  public void updateListBox() {
    MethodCallback<List<TableInput>> callback = new MethodCallback<List<TableInput>>() {
      public void onFailure(Method method, Throwable caught) {
        messageReceiver.addError("There are no table inputs in the database: " + method.getResponse().getText());
      }

      public void onSuccess(Method method, List<TableInput> result) {
        List<String> tableInputNames = new ArrayList<String>();
        tableInputNames.add("--");
        String preselectedIdentifier = null;

        if (result != null && result.size() > 0) {
          for (TableInput input : result) {
            String identifier = input.getIdentifier();

            tableInputNames.add(identifier);
            tableInputs.put(identifier, input);

            // set the preselected table input
            if (identifier.equals(preselectedTableInput)) {
              preselectedIdentifier = identifier;
            }
          }
        } else {
          messageReceiver.addError("There are no table inputs in the database!");
        }

        listBox.clear();
        listBox.setValues(tableInputNames);
        listBox.disableFirstEntry();

        if (preselectedIdentifier != null) {
          listBox.setSelectedValue(preselectedIdentifier);
        }
      }
    };

    TableInputRestService
        lemma = com.google.gwt.core.client.GWT.create(TableInputRestService.class);
    lemma.listTableInputs(callback);
  }

  /**
   * Selects the given data source in the list box. If the list box has not yet been filled with the
   * available values, we save the value and set it when the list box is filled.
   *
   * @param dataSourceSetting the data source setting
   * @throws de.metanome.algorithm_integration.AlgorithmConfigurationException If the data source setting is not a table input setting
   */
  public void selectDataSource(ConfigurationSettingDataSource dataSourceSetting)
      throws AlgorithmConfigurationException {
    this.preselectedTableInput = dataSourceSetting.getValueAsString();

    if (!this.listBox.containsValues()) {
      return;
    }

    if (dataSourceSetting instanceof ConfigurationSettingTableInput) {
      ConfigurationSettingTableInput setting = (ConfigurationSettingTableInput) dataSourceSetting;
      this.setValue(setting);
    } else {
      throw new AlgorithmConfigurationException("This is not a table input setting.");
    }
  }

  /**
   * Returns the current widget's settings as a setting
   *
   * @return the widget's settings
   */
  public ConfigurationSettingTableInput getValue() throws InputValidationException {
    String selectedValue = this.listBox.getSelectedValue();

    if (selectedValue == null || selectedValue.equals("--")) {
      if (isRequired) {
        throw new InputValidationException("You must choose a table input!");
      } else {
        return null;
      }
    }

    TableInput currentTableInput = this.tableInputs.get(selectedValue);

    return getCurrentSetting(currentTableInput);
  }

  /**
   * Takes a setting a sets the selected value of the list box to the given setting.
   *
   * @param setting the settings to set
   * @throws AlgorithmConfigurationException if no table inputs are set
   */
  public void setValue(ConfigurationSettingTableInput setting)
      throws AlgorithmConfigurationException {
    for (Map.Entry<String, TableInput> input : this.tableInputs.entrySet()) {
      TableInput current = input.getValue();
      if (current.getTableName().equals(setting.getTable())) {
        this.listBox.setSelectedValue(input.getKey());
        return;
      }
    }
    throw new AlgorithmConfigurationException("The table inputs are not set yet.");
  }

  /**
   * Creates a ConfigurationSettingTableInput from the given TableInput
   *
   * @param tableInput the table input
   * @return the setting generated from the table input
   */
  protected ConfigurationSettingTableInput getCurrentSetting(TableInput tableInput) {
    ConfigurationSettingTableInput setting = new ConfigurationSettingTableInput();

    DatabaseConnection databaseConnection = tableInput.getDatabaseConnection();
    ConfigurationSettingDatabaseConnection databaseConnectionSetting = new ConfigurationSettingDatabaseConnection(
        databaseConnection.getUrl(),
        databaseConnection.getUsername(),
        databaseConnection.getPassword(),
        databaseConnection.getSystem());
    databaseConnectionSetting.setId(databaseConnection.getId());

    setting.setTable(tableInput.getTableName());
    setting.setDatabaseConnection(databaseConnectionSetting);
    setting.setId(tableInput.getId());

    return setting;
  }

}
