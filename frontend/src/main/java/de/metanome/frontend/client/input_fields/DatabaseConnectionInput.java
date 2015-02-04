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
import de.metanome.backend.results_db.DatabaseConnection;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.helpers.InputValidationException;
import de.metanome.frontend.client.services.DatabaseConnectionRestService;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An input widget, used to specify settings for a database connection.
 *
 * @author Claudia Exeler
 * @see de.metanome.backend.results_db.DatabaseConnection
 */
public class DatabaseConnectionInput extends InputField {

  public ListBoxInput listbox;
  public Map<String, DatabaseConnection> databaseConnections;

  private List<String> acceptedDBSystems;
  private TabWrapper messageReceiver;
  /**
   * When using the link from Data Sources page, this is where the selected database connection is
   * stored.
   */
  private String preselectedDatabaseConnection;

  public DatabaseConnectionInput(boolean optional, TabWrapper messageReceiver, List<String> acceptedDBSystems) {
    super(optional);

    this.messageReceiver = messageReceiver;
    this.databaseConnections = new HashMap<>();
    this.acceptedDBSystems = acceptedDBSystems;

    this.listbox = new ListBoxInput(false);
    updateListBox();
    this.add(this.listbox);
  }

  /**
   * Get all database connections from the database.
   * On success list all database connection, which are accepted by the algorithm, in the list box.
   */
  public void updateListBox() {
    MethodCallback<List<DatabaseConnection>>
      callback =
      new MethodCallback<List<DatabaseConnection>>() {

        @Override
        public void onFailure(Method method, Throwable throwable) {
          messageReceiver.addError("There are no database connections in the database!");
        }

        @Override
        public void onSuccess(Method method, List<DatabaseConnection> databaseConnections) {
          handleSuccess(databaseConnections);
        }
      };

    DatabaseConnectionRestService
        databaseConnectionService =
        com.google.gwt.core.client.GWT.create(DatabaseConnectionRestService.class);
    databaseConnectionService.listDatabaseConnections(callback);

  }

  /**
   * Filters the database connections and puts them into the list box.
   * @param result all database connections available in the database
   */
  protected void handleSuccess(List<DatabaseConnection> result) {
    if (result == null || result.size() == 0) {
      messageReceiver.addError("There are no database connections in the database!");
      return;
    }

    List<String> dbConnectionNames = new ArrayList<>();
    dbConnectionNames.add("--");
    String preselectedIdentifier = null;

    for (DatabaseConnection db : result) {
      String identifier = db.getIdentifier();

      // if the database connection is accepted by the algorithm, add the database connection
      // to the list box
      if (this.acceptedDBSystems.contains(db.getSystem().name()) ||
          this.acceptedDBSystems.isEmpty()) {
        this.databaseConnections.put(identifier, db);
        dbConnectionNames.add(identifier);

        // set the preselected filename
        if (db.getUrl().equals(preselectedDatabaseConnection)) {
          preselectedIdentifier = identifier;
        }
      }

      listbox.clear();
      listbox.setValues(dbConnectionNames);
      listbox.disableFirstEntry();

      if (preselectedIdentifier != null) {
        listbox.setSelectedValue(preselectedIdentifier);
      }
    }
  }

  /**
   * Selects the given data source in the list box. If the list box has not yet been filled with the
   * available values, we save the value and set it when the list box is filled.
   *
   * @param dataSourceSetting the data source setting
   * @throws AlgorithmConfigurationException If the data source setting is not a database connection
   *                                         setting
   */
  public void selectDataSource(ConfigurationSettingDataSource dataSourceSetting)
      throws AlgorithmConfigurationException {
    this.preselectedDatabaseConnection = dataSourceSetting.getValueAsString();

    if (!this.listbox.containsValues()) {
      return;
    }

    if (dataSourceSetting instanceof ConfigurationSettingDatabaseConnection) {
      ConfigurationSettingDatabaseConnection
          setting =
          (ConfigurationSettingDatabaseConnection) dataSourceSetting;
      this.setValues(setting);
    } else {
      throw new AlgorithmConfigurationException("This is not a database connection setting.");
    }
  }

  /**
   * Returns the current widget's settings as a setting
   *
   * @return the widget's settings
   */
  public ConfigurationSettingDatabaseConnection getValues() throws InputValidationException {
    String selectedValue = this.listbox.getSelectedValue();

    if (selectedValue.equals("--")) {
      throw new InputValidationException("You must choose a database connection!");
    }

    DatabaseConnection currentDatabaseConnection = this.databaseConnections.get(selectedValue);

    return new ConfigurationSettingDatabaseConnection()
        .setDbUrl(currentDatabaseConnection.getUrl())
        .setPassword(currentDatabaseConnection.getPassword())
        .setUsername(currentDatabaseConnection.getUsername())
        .setSystem(currentDatabaseConnection.getSystem())
        .setId(currentDatabaseConnection.getId());
  }

  /**
   * Takes a setting a sets the selected value of the list box to the given setting.
   *
   * @param setting the settings to set
   * @throws AlgorithmConfigurationException if no database connections are set
   */
  public void setValues(ConfigurationSettingDatabaseConnection setting)
      throws AlgorithmConfigurationException {
    for (Map.Entry<String, DatabaseConnection> con : this.databaseConnections.entrySet()) {
      DatabaseConnection current = con.getValue();
      if (current.getUrl().equals(setting.getDbUrl()) &&
          current.getPassword().equals(setting.getPassword()) &&
          current.getUsername().equals(setting.getUsername()) &&
          current.getSystem().equals(setting.getSystem())) {
        this.listbox.setSelectedValue(con.getKey());
        return;
      }
    }
    throw new AlgorithmConfigurationException("The database connections are not set yet.");
  }

}
