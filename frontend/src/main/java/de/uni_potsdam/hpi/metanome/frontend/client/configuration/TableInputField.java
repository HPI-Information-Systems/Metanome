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

package de.uni_potsdam.hpi.metanome.frontend.client.configuration;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import de.uni_potsdam.hpi.metanome.frontend.client.helpers.InputValidationException;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.ListBoxInput;
import de.uni_potsdam.hpi.metanome.results_db.DatabaseConnection;
import de.uni_potsdam.hpi.metanome.results_db.EntityStorageException;
import de.uni_potsdam.hpi.metanome.results_db.TableInput;

import java.util.ArrayList;
import java.util.List;


public class TableInputField extends HorizontalPanel {

  private ListBoxInput dbConnectionIdentifier;
  private TextBox tableNameTextbox;
  private FlexTable layoutTable;

  public TableInputField() throws EntityStorageException {
    this.layoutTable = new FlexTable();
    this.add(this.layoutTable);

    this.dbConnectionIdentifier = new ListBoxInput(false);
    initializeDbConnectionListBox();
    addRow(this.dbConnectionIdentifier, "Database Connection", 0);

    this.tableNameTextbox = new TextBox();
    addRow(this.tableNameTextbox, "Table Name", 2);
  }

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
    this.dbConnectionIdentifier.setSelectedValue(connectionIdentifier);
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

    String databaseIdentifier = this.dbConnectionIdentifier.getSelectedValue();
    String tableName = this.tableNameTextbox.getValue();

    if (tableName.isEmpty() || databaseIdentifier.isEmpty())
      throw new InputValidationException("The database connection and the table name should be set!");

    DatabaseConnection connection = getDatabaseConnection(databaseIdentifier);

    tableInput.setDatabaseConnection(connection);
    tableInput.setTableName(tableName);

    return tableInput;
  }

  /**
   * Get the database connection with the given identifier.
   * @param identifier contains the id and url of the database connection
   * @return the database connection with the given identifier
   * @throws EntityStorageException
   */
  private DatabaseConnection getDatabaseConnection(String identifier)
      throws EntityStorageException {
    long id = Long.parseLong(identifier.split(":")[0]);
    return DatabaseConnection.retrieve(id);
  }

  /**
   * Get all database connection from the database and add them to the list box
   * @throws EntityStorageException
   */
  private void initializeDbConnectionListBox() throws EntityStorageException {
    List<DatabaseConnection> connections = DatabaseConnection.retrieveAll();

    List<String> identifiers = new ArrayList<>();
    for (DatabaseConnection connection :connections) {
      identifiers.add(connection.getId() + ": " + connection.getUrl());
    }

    this.dbConnectionIdentifier.setValues(identifiers);
  }
}
