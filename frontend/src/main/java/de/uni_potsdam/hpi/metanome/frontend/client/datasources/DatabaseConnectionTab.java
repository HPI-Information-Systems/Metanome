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
import de.metanome.algorithm_integration.configuration.ConfigurationSettingSqlIterator;
import de.metanome.backend.results_db.DatabaseConnection;
import de.metanome.backend.results_db.TableInput;
import de.uni_potsdam.hpi.metanome.frontend.client.TabContent;
import de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper;
import de.uni_potsdam.hpi.metanome.frontend.client.services.DatabaseConnectionService;
import de.uni_potsdam.hpi.metanome.frontend.client.services.DatabaseConnectionServiceAsync;
import de.uni_potsdam.hpi.metanome.frontend.client.services.TableInputService;
import de.uni_potsdam.hpi.metanome.frontend.client.services.TableInputServiceAsync;

import java.util.List;

public class DatabaseConnectionTab extends FlowPanel implements TabContent {

  protected FlexTable connectionInputList;
  protected DatabaseConnectionEditForm editForm;
  private DatabaseConnectionServiceAsync databaseConnectionService;
  private TableInputServiceAsync tableInputService;
  private DataSourcePage parent;
  private TabWrapper messageReceiver;

  /**
   * @param parent the parent page
   */
  public DatabaseConnectionTab(DataSourcePage parent) {
    this.databaseConnectionService = GWT.create(DatabaseConnectionService.class);
    this.tableInputService = GWT.create(TableInputService.class);
    this.parent = parent;

    this.connectionInputList = new FlexTable();
    this.editForm = new DatabaseConnectionEditForm(this);

    this.addDatabaseConnectionsToList(this);
  }

  private void addEditForm() {
    this.add(new HTML("<hr>"));
    this.add(new HTML("<h3>Add a new Database Connection</h3>"));
    this.add(this.editForm);
  }

  /**
   * Gets all Database Connections available in the database and adds them to the table.
   *
   * @param panel the parent widget of the table
   */
  private void addDatabaseConnectionsToList(final FlowPanel panel) {
    this.databaseConnectionService.listDatabaseConnections(
        new AsyncCallback<List<DatabaseConnection>>() {
          @Override
          public void onFailure(Throwable throwable) {
            panel.add(new Label("There are no Database Connections yet."));
            addEditForm();
          }

          @Override
          public void onSuccess(List<DatabaseConnection> connections) {
            listDatabaseConnections(connections);
            addEditForm();
          }
        });

    // disable all delete button of database connection which are referenced by a table input
    tableInputService.listTableInputs(new AsyncCallback<List<TableInput>>() {
      @Override
      public void onFailure(Throwable throwable) {
      }

      @Override
      public void onSuccess(List<TableInput> tableInputs) {
        for (TableInput input : tableInputs) {
          setEnableOfDeleteButton(input.getDatabaseConnection(), false);
        }
      }
    });
  }

  /**
   * Lists all given database connections in a table.
   *
   * @param inputs the database connections
   */
  protected void listDatabaseConnections(List<DatabaseConnection> inputs) {
    if (inputs.isEmpty()) {
      this.add(new HTML("<h4>There are no Database Connections yet.</h4>"));
      return;
    }

    this.connectionInputList.setHTML(0, 0, "<b>Url</b>");
    this.connectionInputList.setHTML(0, 1, "<b>Username</b>");
    this.connectionInputList.setHTML(0, 2, "<b>System</b>");

    for (final DatabaseConnection input : inputs) {
      this.addDatabaseConnectionToTable(input);
    }

    this.add(new HTML("<h3>List of all Database Connections</h3>"));
    this.add(this.connectionInputList);
  }

  /**
   * Adds the given database connection to the table.
   *
   * @param input the database connection, which should be added.
   */
  public void addDatabaseConnectionToTable(final DatabaseConnection input) {
    int row = this.connectionInputList.getRowCount();

    Button deleteButton = new Button("Delete");
    final int finalRow = row;
    deleteButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        parent.removeDatabaseConnectionFromTableInputTab(input);
        databaseConnectionService.deleteDatabaseConnection(input,
                                                           parent.getDeleteCallback(
                                                               connectionInputList,
                                                               finalRow,
                                                               "Database Connection"));
      }
    });

    Button runButton = new Button("Run");
    runButton.setTitle(String.valueOf(input.getId()));
    runButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        parent.callRunConfiguration(convertDatabaseConnectionToDataSource(input));
      }
    });

    this.connectionInputList.setWidget(row, 0, new HTML(input.getUrl()));
    this.connectionInputList.setText(row, 1, input.getUsername());
    this.connectionInputList.setText(row, 2, input.getSystem().name());
    this.connectionInputList.setWidget(row, 3, runButton);
    this.connectionInputList.setWidget(row, 4, deleteButton);
  }

  /**
   * Converts a database connection into a ConfigurationSettingDataSource
   *
   * @param input the database connection
   * @return the ConfigurationSettingDataSource from the given database connection
   */
  private ConfigurationSettingDataSource convertDatabaseConnectionToDataSource(
      DatabaseConnection input) {
    return new ConfigurationSettingSqlIterator(input.getUrl(), input.getUsername(),
                                               input.getPassword(),
                                               input.getSystem());
  }

  /**
   * Forwards the update-table-input-tab-command to its parent.
   *
   * @param connection the new database connection
   */
  public void updateTableInputTab(DatabaseConnection connection) {
    this.parent.addDatabaseConnectionToTableInputTab(connection);
  }

  /**
   * Forwards the command to update the data sources on the run configuration page to the data
   * source page.
   */
  public void updateDataSourcesOnRunConfiguration() {
    this.parent.updateDataSourcesOnRunConfiguration();
  }

  /**
   * If a table input has a reference to a database connection, the delete button of the database
   * connection should be disabled. To delete the database connection, first the related table input
   * has to be deleted. If the table input is deleted, the button should be enabled again.
   *
   * @param connection the database connection, which delete button should be enabled/disabled
   * @param enabled    true, if the button should be enabled, false otherwise
   */
  protected void setEnableOfDeleteButton(DatabaseConnection connection, boolean enabled) {
    int row = 0;
    while (row < this.connectionInputList.getRowCount()) {
      HTML textWidget = (HTML) this.connectionInputList.getWidget(row, 0);
      if (textWidget != null && connection.getUrl().equals(textWidget.getText())) {
        Button deleteButton = (Button) this.connectionInputList.getWidget(row, 4);
        deleteButton.setEnabled(enabled);
        return;
      }
      row++;
    }
  }

  @Override
  public void setMessageReceiver(TabWrapper tab) {
    this.messageReceiver = tab;
    this.editForm.setMessageReceiver(tab);
  }
}
