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

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;

import de.metanome.algorithm_integration.configuration.ConfigurationSettingDataSource;
import de.metanome.backend.results_db.DatabaseConnection;
import de.metanome.backend.results_db.TableInput;
import de.metanome.frontend.client.TabContent;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.services.TableInputService;
import de.metanome.frontend.client.services.TableInputServiceAsync;

import java.util.List;

public class TableInputTab extends FlowPanel implements TabContent {

  protected FlexTable tableInputList;
  protected TableInputEditForm editForm;
  private TableInputServiceAsync tableInputService;
  private DataSourcePage parent;
  private TabWrapper messageReceiver;

  /**
   * @param parent the parent page
   */
  public TableInputTab(DataSourcePage parent) {
    this.tableInputService = GWT.create(TableInputService.class);
    this.parent = parent;

    this.tableInputList = new FlexTable();
    this.editForm = new TableInputEditForm(this);

    this.addTableInputsToList(this);
  }

  /**
   * Adds the edit form to the content.
   */
  private void addEditForm() {
    this.add(new HTML("<hr>"));
    this.add(new HTML("<h3>Add a new Table Input</h3>"));
    this.add(this.editForm);
  }

  /**
   * Gets all Table Inputs available in the database and adds them to the table.
   *
   * @param panel the parent widget of the table
   */
  private void addTableInputsToList(final FlowPanel panel) {
    tableInputService.listTableInputs(new AsyncCallback<List<TableInput>>() {
      @Override
      public void onFailure(Throwable throwable) {
        panel.add(new Label("There are no Table Inputs yet."));
        addEditForm();
      }

      @Override
      public void onSuccess(List<TableInput> tableInputs) {
        listTableInputs(tableInputs);
        addEditForm();
      }
    });
  }

  /**
   * Lists all given table inputs in a table.
   *
   * @param inputs the table inputs
   */
  protected void listTableInputs(List<TableInput> inputs) {
    if (inputs.isEmpty()) {
      this.add(new HTML("<h4>There are no Table Inputs yet.</h4>"));
      return;
    }

    this.tableInputList.setHTML(0, 0, "<b>Database Connection</b>");
    this.tableInputList.setHTML(0, 1, "<b>Table Name</b>");

    for (TableInput input : inputs) {
      this.addTableInputToTable(input);
    }

    this.add(new HTML("<h3>List of all Table Inputs</h3>"));
    this.add(this.tableInputList);
  }

  /**
   * Adds the given table input to the table.
   *
   * @param input the table input, which should be added.
   */
  public void addTableInputToTable(final TableInput input) {
    int row = this.tableInputList.getRowCount();

    Button deleteButton = new Button("Delete");
    final int finalRow = row;
    deleteButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        tableInputService.deleteTableInput(input,
                                           getDeleteCallback(finalRow,
                                                             input.getDatabaseConnection()));
      }
    });

    Button runButton = new Button("Analyze");
    runButton.setTitle(String.valueOf(input.getId()));
    runButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        parent.callRunConfiguration(convertTableInputToDataSource(input));
      }
    });

    DatabaseConnection connection = input.getDatabaseConnection();

    this.tableInputList.setText(row, 0,
                                connection.getSystem().name() + "; " + connection.getUrl() + "; "
                                + connection.getUsername());
    this.tableInputList.setText(row, 1, input.getTableName());
    this.tableInputList.setWidget(row, 2, runButton);
    this.tableInputList.setWidget(row, 3, deleteButton);
  }

  /**
   * Converts a table input into a ConfigurationSettingDataSource
   *
   * @param input the table input
   * @return the ConfigurationSettingDataSource from the given table input
   */
  private ConfigurationSettingDataSource convertTableInputToDataSource(TableInput input) {
    // TODO configuration setting is missing
    return null;
  }

  /**
   * Forwards the add-database-connection-command to the edit form.
   *
   * @param connection the new database connection
   */
  public void addDatabaseConnection(DatabaseConnection connection) {
    this.editForm.addDatabaseConnection(connection);
  }

  /**
   * Forwards the remove-database-connection-command to the edit form.
   *
   * @param connection the database connection
   */
  public void removeDatabaseConnection(DatabaseConnection connection) {
    this.editForm.removeDatabaseConnection(connection);
  }

  /**
   * Forwards the command to update the data sources on the run configuration page to the data
   * source page.
   */
  public void updateDataSourcesOnRunConfiguration() {
    this.parent.updateDataSourcesOnRunConfiguration();
  }

  /**
   * Creates the callback for the delete call.
   *
   * @param row The row, which contains the input.
   * @return The callback
   */
  protected AsyncCallback<Void> getDeleteCallback(final int row,
                                                  final DatabaseConnection connection) {
    return new AsyncCallback<Void>() {
      @Override
      public void onFailure(Throwable throwable) {
        messageReceiver.addError("Could not delete the Table Input: " + throwable.getMessage());
      }

      @Override
      public void onSuccess(Void aVoid) {
        tableInputList.removeRow(row);
        setEnableOfDeleteButton(connection, true);
      }
    };
  }

  public void setEnableOfDeleteButton(DatabaseConnection databaseConnection, Boolean enabled) {
    this.parent.setEnableOfDeleteButton(databaseConnection, enabled);
  }

  @Override
  public void setMessageReceiver(TabWrapper tab) {
    this.messageReceiver = tab;
    this.editForm.setMessageReceiver(tab);
  }
}
