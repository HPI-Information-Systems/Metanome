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

package de.metanome.frontend.client.datasources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

import de.metanome.backend.results_db.DatabaseConnection;
import de.metanome.backend.results_db.TableInput;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.helpers.InputValidationException;
import de.metanome.frontend.client.input_fields.ListBoxInput;
import de.metanome.frontend.client.services.DatabaseConnectionService;
import de.metanome.frontend.client.services.DatabaseConnectionServiceAsync;
import de.metanome.frontend.client.services.TableInputService;
import de.metanome.frontend.client.services.TableInputServiceAsync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Input field to configure a table input.
 */
public class TableInputEditForm extends Grid {

  private final DatabaseConnectionServiceAsync databaseConnectionService;
  protected Map<String, DatabaseConnection> dbMap = new HashMap<>();
  protected ListBoxInput dbConnectionListBox;
  protected TextBox tableNameTextbox;
  protected TextArea commentTextbox;
  private TableInputServiceAsync tableInputService;
  private TabWrapper messageReceiver;
  private TableInputTab parent;

  public TableInputEditForm(TableInputTab parent) {
    super(4, 2);

    this.parent = parent;

    this.databaseConnectionService = GWT.create(DatabaseConnectionService.class);
    this.tableInputService = GWT.create(TableInputService.class);

    this.dbConnectionListBox = new ListBoxInput(false);
    updateDatabaseConnectionListBox();
    this.setText(0, 0, "Database Connection");
    this.setWidget(0, 1, this.dbConnectionListBox);

    this.tableNameTextbox = new TextBox();
    this.setText(1, 0, "Table Name");
    this.setWidget(1, 1, this.tableNameTextbox);

    this.commentTextbox = new TextArea();
    this.commentTextbox.setVisibleLines(3);
    this.setText(2, 0, "Comment");
    this.setWidget(2, 1, this.commentTextbox);

    this.setWidget(3, 1, new Button("Save", new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        saveTableInput();
      }
    }));
  }

  /**
   * Sets the selected database connection and the table name
   *
   * @param connectionIdentifier the identifier of the database connection which should be selected
   *                             in the list box
   * @param tableName            the table name which should be set in the text box
   * @param comment               the comment of the table input
   */
  protected void setValues(String connectionIdentifier, String tableName, String comment) {
    this.dbConnectionListBox.setSelectedValue(connectionIdentifier);
    this.tableNameTextbox.setValue(tableName);
    this.commentTextbox.setValue(comment);
  }

  /**
   * Creates a table input with the selected database connection and the given table name
   *
   * @return a table input
   * @throws InputValidationException if the input is invalid.
   */
  public TableInput getValue() throws InputValidationException {
    TableInput tableInput = new TableInput();

    String identifier = this.dbConnectionListBox.getSelectedValue();
    DatabaseConnection connection = this.dbMap.get(identifier);
    String tableName = this.tableNameTextbox.getValue();
    String comment = this.commentTextbox.getValue();

    if (tableName.isEmpty() || connection == null) {
      throw new InputValidationException(
          "The database connection and the table name should be set!");
    }

    tableInput.setDatabaseConnection(connection);
    tableInput.setTableName(tableName);
    tableInput.setComment(comment);

    return tableInput;
  }

  /**
   * Get all database connection from the database and add them to the list box
   */
  public void updateDatabaseConnectionListBox() {
    AsyncCallback<List<DatabaseConnection>>
        callback =
        new AsyncCallback<List<DatabaseConnection>>() {

          public void onFailure(Throwable caught) {
            messageReceiver.addError("There are no database connections in the database: " + caught.getMessage());
          }

          public void onSuccess(List<DatabaseConnection> result) {
            List<String> dbConnectionNames = new ArrayList<String>();
            dbConnectionNames.add("--");

            if (result != null && result.size() > 0) {
              for (DatabaseConnection db : result) {
                String identifier = db.getIdentifier();
                dbConnectionNames.add(identifier);
                dbMap.put(identifier, db);
              }
            }

            dbConnectionListBox.clear();
            dbConnectionListBox.setValues(dbConnectionNames);
            dbConnectionListBox.disableFirstEntry();
          }
        };

    databaseConnectionService.listDatabaseConnections(callback);
  }

  /**
   * Resets all values, sets the current database connection to the default value "--" and clears
   * the text of the table name input field.
   */
  public void reset() {
    this.dbConnectionListBox.reset();
    this.tableNameTextbox.setText("");
    this.commentTextbox.setText("");
  }

  /**
   * Stores the table input in the database.
   */
  private void saveTableInput() {
    messageReceiver.clearErrors();
    try {
      final TableInput currentInput = this.getValue();

      this.tableInputService.storeTableInput(currentInput, new AsyncCallback<Void>() {
        @Override
        public void onFailure(Throwable throwable) {
          messageReceiver.addError("Table Input could not be stored: " + throwable.getMessage());
        }

        @Override
        public void onSuccess(Void aVoid) {
          reset();
          parent.addTableInputToTable(currentInput);
          parent.setEnableOfDeleteButton(currentInput.getDatabaseConnection(), false);
          parent.updateDataSourcesOnRunConfiguration();
        }
      });
    } catch (InputValidationException e) {
      messageReceiver.addError("Invalid Input: " + e.getMessage());
    }
  }

  public void addDatabaseConnection(DatabaseConnection connection) {
    String
        identifier = connection.getIdentifier();
    this.dbConnectionListBox.addValue(identifier);
    this.dbMap.put(identifier, connection);
  }

  public void removeDatabaseConnection(DatabaseConnection connection) {
    String
        identifier = connection.getIdentifier();
    this.dbConnectionListBox.removeValue(identifier);
    this.dbMap.remove(connection);
  }

  /**
   * Set the message receiver.
   *
   * @param tab the message receiver tab wrapper
   */
  public void setMessageReceiver(TabWrapper tab) {
    this.messageReceiver = tab;
  }
}
