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

import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingDataSource;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingSqlIterator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.DbSystem;
import de.uni_potsdam.hpi.metanome.frontend.client.TabContent;
import de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper;
import de.uni_potsdam.hpi.metanome.frontend.client.services.DatabaseConnectionService;
import de.uni_potsdam.hpi.metanome.frontend.client.services.DatabaseConnectionServiceAsync;
import de.uni_potsdam.hpi.metanome.results_db.DatabaseConnection;

import java.util.List;

public class DatabaseConnectionTab extends FlowPanel implements TabContent {

  private DatabaseConnectionServiceAsync databaseConnectionService;
  private DataSourcePage parent;

  protected FlexTable connectionInputList;

  private TabWrapper messageReceiver;

  protected DatabaseConnectionEditForm editForm;

  /**
   * @param parent the parent page
   */
  public DatabaseConnectionTab(DataSourcePage parent) {
    this.databaseConnectionService = GWT.create(DatabaseConnectionService.class);
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
  }

  /**
   * Lists all given database connections in a table.
   * @param inputs the database connections
   */
  private void listDatabaseConnections(List<DatabaseConnection> inputs) {
    if (inputs.isEmpty()) {
      this.add(new HTML("<h4>There are no Database Connections yet.</h4>"));
      return;
    }

    this.connectionInputList.setHTML(0, 0, "<b>Url</b>");
    this.connectionInputList.setHTML(0, 1, "<b>Username</b>");
    this.connectionInputList.setHTML(0, 2, "<b>Password</b>");

    for (final DatabaseConnection input : inputs) {
      this.addDatabaseConnectionToTable(input);
    }

    this.add(new HTML("<h3>List of all Database Connections</h3>"));
    this.add(this.connectionInputList);
  }

  /**
   * Adds the given database connection to the table.
   * @param input the database connection, which should be added.
   */
  public void addDatabaseConnectionToTable(final DatabaseConnection input) {
    int row = this.connectionInputList.getRowCount();

    Button deleteButton = new Button("Delete");
    // TODO: add click handler

    Button runButton = new Button("Run");
    runButton.setTitle(String.valueOf(input.getId()));
    runButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        parent.callRunConfiguration(convertDatabaseConnectionToDataSource(input));
      }
    });

    this.connectionInputList.setText(row, 0, input.getUrl());
    this.connectionInputList.setText(row, 1, input.getUsername());
    this.connectionInputList.setText(row, 2, input.getPassword());
    this.connectionInputList.setWidget(row, 3, runButton);
    this.connectionInputList.setWidget(row, 4, deleteButton);
  }

  /**
   * Converts a database connection into a ConfigurationSettingDataSource
   * @param input the database connection
   * @return      the ConfigurationSettingDataSource from the given database connection
   */
  private ConfigurationSettingDataSource convertDatabaseConnectionToDataSource(DatabaseConnection input) {
    return new ConfigurationSettingSqlIterator(input.getUrl(), input.getUsername(), input.getPassword(),
                                               DbSystem.DB2);
  }

  /**
   * Forwards the update-table-input-tab-command to its parent.
   * @param connection the new database connection
   */
  public void updateTableInputTab(DatabaseConnection connection) {
    parent.updateTableInputTab(connection);
  }

  @Override
  public void setMessageReceiver(TabWrapper tab) {
    this.messageReceiver = tab;
    this.editForm.setMessageReceiver(tab);
  }
}
