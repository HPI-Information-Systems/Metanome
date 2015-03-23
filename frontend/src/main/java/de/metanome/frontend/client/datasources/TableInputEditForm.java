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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

import de.metanome.backend.results_db.DatabaseConnection;
import de.metanome.backend.results_db.TableInput;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.helpers.InputValidationException;
import de.metanome.frontend.client.input_fields.ListBoxInput;
import de.metanome.frontend.client.services.DatabaseConnectionRestService;
import de.metanome.frontend.client.services.TableInputRestService;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Input field to configure a table input.
 */
public class TableInputEditForm extends Grid {

  private final DatabaseConnectionRestService databaseConnectionService;
  protected Map<String, DatabaseConnection> dbMap = new HashMap<>();
  protected Map<String, Integer> dbUsageMap = new HashMap<>();
  protected ListBoxInput dbConnectionListBox;
  protected TextBox tableNameTextbox;
  protected TextArea commentTextbox;
  private TableInputRestService tableInputService;
  private TabWrapper messageReceiver;
  private TableInputTab parent;
  private Button saveButton;
  private Button updateButton;
  private TableInput oldTableInput;

  public TableInputEditForm(TableInputTab parent) {
    super(4, 2);

    this.parent = parent;

    this.databaseConnectionService = com.google.gwt.core.client.GWT.create(DatabaseConnectionRestService.class);
    this.tableInputService = com.google.gwt.core.client.GWT.create(TableInputRestService.class);

    this.dbConnectionListBox = new ListBoxInput(false, false);
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

    this.saveButton = new Button("Save", new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        saveTableInput();
      }
    });
    this.updateButton = new Button("Update", new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        submitUpdate();
      }
    });
    this.setWidget(3, 1, saveButton);
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
    String identifier = this.dbConnectionListBox.getSelectedValue();
    DatabaseConnection connection = this.dbMap.get(identifier);
    String tableName = this.tableNameTextbox.getValue();
    String comment = this.commentTextbox.getValue();

    if (tableName.isEmpty() || connection == null) {
      throw new InputValidationException(
          "The database connection and the table name should be set!");
    }

    TableInput tableInput = new TableInput(tableName, connection);
    tableInput.setComment(comment);

    return tableInput;
  }

  /**
   * Get all database connection from the database and add them to the list box
   */
  public void updateDatabaseConnectionListBox() {
    MethodCallback<List<DatabaseConnection>>
        callback =
        new MethodCallback<List<DatabaseConnection>>() {

          public void onFailure(Method method, Throwable caught) {
            messageReceiver.addError(
                "There are no database connections in the database: " + method.getResponse()
                    .getText());
          }

          public void onSuccess(Method method, List<DatabaseConnection> result) {
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
      this.tableInputService.storeTableInput(this.getValue(), new MethodCallback<TableInput>() {
        @Override
        public void onFailure(Method method, Throwable throwable) {
          messageReceiver
              .addError("Table Input could not be stored: " + method.getResponse().getText());
        }

        @Override
        public void onSuccess(Method method, TableInput input) {
          reset();
          increaseDatabaseConnectionUsage(input.getDatabaseConnection().getIdentifier());
          parent.addTableInputToTable(input);
          parent.setEnableOfButtons(input.getDatabaseConnection(), false);
          parent.updateDataSourcesOnRunConfiguration();
        }

      });
    } catch (InputValidationException e) {
      messageReceiver.addError("Invalid Input: " + e.getMessage());
    }
  }

  /**
   * Increase the number of references a database connection has to table inputs.
   * @param identifier the identifier of the database connection
   */
  public void increaseDatabaseConnectionUsage(String identifier) {
    if (dbUsageMap.containsKey(identifier)) {
      Integer usage = dbUsageMap.get(identifier) + 1;
      if (usage == 1)  {
        parent.setEnableOfButtons(dbMap.get(identifier), false);
      }
      dbUsageMap.put(identifier, usage);
    } else {
      dbUsageMap.put(identifier, 1);
      parent.setEnableOfButtons(dbMap.get(identifier), false);
    }
  }

  /**
   * Decreases the number of references a database connection has to table inputs.
   * @param identifier the identifier of the database connection
   */
  public void decreaseDatabaseConnectionUsage(String identifier) {
    Integer usage = Math.max(dbUsageMap.get(identifier) - 1, 0);
    if (usage == 0) {
      parent.setEnableOfButtons(dbMap.get(identifier), true);
    }
    dbUsageMap.put(identifier, usage);
  }

  /**
   * Updates the current table input in the database.
   */
  private void submitUpdate() {
    messageReceiver.clearErrors();
    try {
      this.tableInputService.updateTableInput(this.getValue().setId(oldTableInput.getId()), new MethodCallback<TableInput>() {
        @Override
        public void onFailure(Method method, Throwable throwable) {
          messageReceiver
              .addError("Table Input could not be updated: " + method.getResponse().getText());
          reset();
          showSaveButton();
        }

        @Override
        public void onSuccess(Method method, TableInput input) {
          reset();
          showSaveButton();
          if (!input.getIdentifier().equals(oldTableInput.getIdentifier())) {
            increaseDatabaseConnectionUsage(input.getIdentifier());
            decreaseDatabaseConnectionUsage(oldTableInput.getIdentifier());
          }
          parent.updateTableInputInTable(input, oldTableInput);
          parent.updateDataSourcesOnRunConfiguration();
        }

      });
    } catch (InputValidationException e) {
      messageReceiver.addError("Invalid Input: " + e.getMessage());
    }
  }


  /**
   * Fills the form with the values of the current table input, which should be updated.
   * @param tableInput the table input
   */
  public void updateTableInput(TableInput tableInput) {
    setValues(tableInput.getDatabaseConnection().getIdentifier(),
              tableInput.getTableName(),
              tableInput.getComment());

    this.setWidget(3, 1, updateButton);
    this.oldTableInput = tableInput;
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
   * Shows the save button.
   */
  protected void showSaveButton() {
    this.setWidget(3, 1, saveButton);
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
