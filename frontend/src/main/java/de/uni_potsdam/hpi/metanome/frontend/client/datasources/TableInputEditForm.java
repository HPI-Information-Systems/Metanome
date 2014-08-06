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

package de.uni_potsdam.hpi.metanome.frontend.client.datasources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper;
import de.uni_potsdam.hpi.metanome.frontend.client.helpers.InputValidationException;
import de.uni_potsdam.hpi.metanome.frontend.client.input_fields.ListBoxInput;
import de.uni_potsdam.hpi.metanome.frontend.client.services.DatabaseConnectionService;
import de.uni_potsdam.hpi.metanome.frontend.client.services.DatabaseConnectionServiceAsync;
import de.uni_potsdam.hpi.metanome.results_db.DatabaseConnection;
import de.uni_potsdam.hpi.metanome.results_db.EntityStorageException;
import de.uni_potsdam.hpi.metanome.results_db.TableInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Input field to configure a table input.
 */
public class TableInputEditForm extends FlowPanel {

  private final DatabaseConnectionServiceAsync databaseConnectionService;
  private TabWrapper messageReceiver;

  protected Map<String, DatabaseConnection> dbMap = new HashMap<>();

  protected ListBoxInput dbConnectionListBox;
  protected TextBox tableNameTextbox;
  private FlexTable layoutTable;


  public TableInputEditForm() {
    this.addStyleName("left");

    this.databaseConnectionService = GWT.create(DatabaseConnectionService.class);

    this.layoutTable = new FlexTable();
    this.add(this.layoutTable);

    this.dbConnectionListBox = new ListBoxInput(false);
    updateDatabaseConnectionListBox();
    addRow(this.dbConnectionListBox, "Database Connection", 0);

    this.tableNameTextbox = new TextBox();
    addRow(this.tableNameTextbox, "Table Name", 2);
  }

  /**
   * Add another row to the bottom of a table
   *
   * @param inputWidget the widget to be used for input
   * @param name        the name of the input property
   */
  protected void addRow(Widget inputWidget, String name, int row) {
    this.layoutTable.setText(row, 0, name);
    this.layoutTable.setWidget(row, 1, inputWidget);
  }

  /**
   * Sets the selected database connection and the table name
   * @param connectionIdentifier the identifier of the database connection which should be selected in the list box
   * @param tableName the table name which should be set in the text box
   */
  protected void setValues(String connectionIdentifier, String tableName) {
    this.dbConnectionListBox.setSelectedValue(connectionIdentifier);
    this.tableNameTextbox.setValue(tableName);
  }

  /**
   * Creates a table input with the selected database connection and the given table name
   * @return a table input
   * @throws InputValidationException
   * @throws EntityStorageException
   */
  public TableInput getValue() throws InputValidationException, EntityStorageException {
    TableInput tableInput = new TableInput();

    String identifier = this.dbConnectionListBox.getSelectedValue();
    DatabaseConnection connection = this.dbMap.get(identifier);
    String tableName = this.tableNameTextbox.getValue();

    if (tableName.isEmpty() || connection == null) {
      this.messageReceiver.addError("The database connection and the table name should be set!");
      return null;
    }

    tableInput.setDatabaseConnection(connection);
    tableInput.setTableName(tableName);

    return tableInput;
  }

  /**
   * Get all database connection from the database and add them to the list box
   */
  public void updateDatabaseConnectionListBox() {
    AsyncCallback<List<DatabaseConnection>> callback = new AsyncCallback<List<DatabaseConnection>>() {

      public void onFailure(Throwable caught) {
        messageReceiver.addError("There are no database connections in the database!");
      }

      public void onSuccess(List<DatabaseConnection> result) {
        List<String> dbConnectionNames = new ArrayList<String>();
        dbConnectionNames.add("--");

        if (result != null) {
          for (DatabaseConnection db : result) {
            String identifier = db.getId() + ": " + db.getUrl();
            dbConnectionNames.add(identifier);
            dbMap.put(identifier, db);
          }
        } else {
          messageReceiver.addError("There are no database connections in the database!");
        }

        dbConnectionListBox.clear();
        dbConnectionListBox.setValues(dbConnectionNames);
        dbConnectionListBox.disableFirstEntry();
      }
    };

    databaseConnectionService.listDatabaseConnections(callback);
  }

  /**
   * Resets all values, sets the current database connection to the default value "--" and
   * clears the text of the table name input field.
   */
  public void reset() {
    this.dbConnectionListBox.reset();
    this.tableNameTextbox.setText("");
  }

  /**
   * Set the message receiver.
   * @param tab the message receiver tab wrapper
   */
  public void setMessageReceiver(TabWrapper tab) {
    this.messageReceiver = tab;
  }
}
