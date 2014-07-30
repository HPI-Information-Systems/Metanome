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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingSqlIterator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.DbSystem;
import de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper;
import de.uni_potsdam.hpi.metanome.frontend.client.services.DatabaseConnectionService;
import de.uni_potsdam.hpi.metanome.frontend.client.services.DatabaseConnectionServiceAsync;
import de.uni_potsdam.hpi.metanome.results_db.DatabaseConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An input widget, used to specify settings for a database connection.
 *
 * @author Claudia Exeler
 * @see de.uni_potsdam.hpi.metanome.results_db.DatabaseConnection
 */
public class SqlIteratorInput extends InputField {

  protected ListBoxInput listbox;
  protected Map<String, DatabaseConnection> databaseConnections;
  private TabWrapper messageReceiver;

  public SqlIteratorInput(boolean optional, TabWrapper messageReceiver) {
    super(optional);

    this.messageReceiver = messageReceiver;
    this.databaseConnections = new HashMap<>();

    listbox = new ListBoxInput(false);
    updateListBox();
    this.add(listbox);
  }

  /**
   * Get all database connections from the database and put them into the list box.
   */
  public void updateListBox() {
    AsyncCallback<List<DatabaseConnection>> callback = new AsyncCallback<List<DatabaseConnection>>() {
      public void onFailure(Throwable caught) {
        messageReceiver.addError("There are no database connections in the database!");
      }

      public void onSuccess(List<DatabaseConnection> result) {
        List<String> dbConnectionNames = new ArrayList<String>();
        dbConnectionNames.add("--");

        if (result != null && result.size() > 0) {
          for (DatabaseConnection db : result) {
            String identifier = db.getId() + ": " + db.getUrl(); // TODO add system
            dbConnectionNames.add(identifier);
            databaseConnections.put(identifier, db);
          }
        } else {
          messageReceiver.addError("There are no database connections in the database!");
        }

        listbox.clear();
        listbox.setValues(dbConnectionNames);
        listbox.disableFirstEntry();
      }
    };

    DatabaseConnectionServiceAsync databaseConnectionService = GWT.create(DatabaseConnectionService.class);
    databaseConnectionService.listDatabaseConnections(callback);
  }

  /**
   * Returns the current widget's settings as a setting
   *
   * @return the widget's settings
   */
  public ConfigurationSettingSqlIterator getValues() {
    DatabaseConnection currentDatabaseConnection = this.databaseConnections.get(
        this.listbox.getSelectedValue());

    return new ConfigurationSettingSqlIterator(currentDatabaseConnection.getUrl(),
                                               currentDatabaseConnection.getUsername(),
                                               currentDatabaseConnection.getPassword(),
                                               DbSystem.DB2);
    // TODO DbSystem.valueOf(currentDatabaseConnection.getDbSystem()
  }

  /**
   * Takes a setting a sets the selected value of the list box to the given setting.
   *
   * @param setting the settings to set
   */
  public void setValues(ConfigurationSettingSqlIterator setting) {
    for (Map.Entry<String, DatabaseConnection> con : this.databaseConnections.entrySet()) {
      DatabaseConnection current = con.getValue();
      if (current.getUrl().equals(setting.getDbUrl()) &&
          current.getPassword().equals(setting.getPassword()) &&
          current.getUsername().equals(setting.getUsername()))
        // TODO check system
        this.listbox.setSelectedValue(con.getKey());
    }
  }

}
