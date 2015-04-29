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
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

import de.metanome.algorithm_integration.configuration.ConfigurationSettingDataSource;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingDatabaseConnection;
import de.metanome.backend.results_db.DatabaseConnection;
import de.metanome.backend.results_db.TableInput;
import de.metanome.frontend.client.TabContent;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.services.DatabaseConnectionRestService;
import de.metanome.frontend.client.services.TableInputRestService;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.List;

public class DatabaseConnectionTab extends FlowPanel implements TabContent {

  protected FlexTable connectionInputList;
  protected DatabaseConnectionEditForm editForm;

  private DatabaseConnectionRestService databaseConnectionService;
  private TableInputRestService lemma;

  private DataSourcePage parent;
  private TabWrapper messageReceiver;

  /**
   * @param parent the parent page
   */
  public DatabaseConnectionTab(DataSourcePage parent) {
    this.databaseConnectionService =
        com.google.gwt.core.client.GWT.create(DatabaseConnectionRestService.class);
    this.lemma = com.google.gwt.core.client.GWT.create(TableInputRestService.class);
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
        new MethodCallback<List<DatabaseConnection>>() {
          @Override
          public void onFailure(Method method, Throwable throwable) {
            messageReceiver.addError("There are no database connection in the database.");
            addEditForm();
          }

          @Override
          public void onSuccess(Method method, List<DatabaseConnection> connections) {
            listDatabaseConnections(connections);
            addEditForm();

            // disable all delete button of database connection which are referenced by a table input
            lemma.listTableInputs(new MethodCallback<List<TableInput>>() {
              @Override
              public void onFailure(Method method, Throwable throwable) {
                messageReceiver.addError(method.getResponse().getText());
              }

              @Override
              public void onSuccess(Method method, List<TableInput> tableInputs) {
                for (TableInput input : tableInputs) {
                  setEnableOfButtons(input.getDatabaseConnection(), false);
                }
              }
            });
          }

        });
  }

  /**
   * Lists all given database connections in a table.
   *
   * @param inputs the database connections
   */
  protected void listDatabaseConnections(List<DatabaseConnection> inputs) {
    this.connectionInputList.setHTML(0, 0, "<b>Url</b>");
    this.connectionInputList.setHTML(0, 1, "<b>Username</b>");
    this.connectionInputList.setHTML(0, 2, "<b>System</b>");
    this.connectionInputList.setHTML(0, 3, "<b>Comment</b>");

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
    deleteButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        databaseConnectionService.deleteDatabaseConnection(input.getId(),
                                                           getDeleteCallback(input));
      }
    });

    Button editButton = new Button("Edit");
    editButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        editForm.updateDatabaseConnection(input);
      }
    });

    Button runButton = new Button("Analyze");
    runButton.setTitle(String.valueOf(input.getId()));
    runButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        parent.callRunConfiguration(convertDatabaseConnectionToDataSource(input));
      }
    });

    this.connectionInputList.setWidget(row, 0, new HTML(input.getUrl()));
    this.connectionInputList.setWidget(row, 1, new HTML(input.getUsername()));
    this.connectionInputList.setWidget(row, 2, new HTML(input.getSystem().name()));
    this.connectionInputList.setText(row, 3, input.getComment());
    this.connectionInputList.setWidget(row, 4, runButton);
    this.connectionInputList.setWidget(row, 5, deleteButton);
    this.connectionInputList.setWidget(row, 6, editButton);
  }

  /**
   * Converts a database connection into a ConfigurationSettingDataSource
   *
   * @param input the database connection
   * @return the ConfigurationSettingDataSource from the given database connection
   */
  private ConfigurationSettingDataSource convertDatabaseConnectionToDataSource(
      DatabaseConnection input) {
    return new ConfigurationSettingDatabaseConnection(input.getUrl(), input.getUsername(),
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
   * Forwards the update-table-input-tab-command to its parent.
   *
   * @param connection    the new database connection
   * @param oldConnection the old database connection
   */
  public void updateTableInputTab(DatabaseConnection connection, DatabaseConnection oldConnection) {
    this.parent.updateDatabaseConnectionToTableInputTab(connection, oldConnection);
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
  protected void setEnableOfButtons(DatabaseConnection connection, boolean enabled) {
    int row = findRow(connection);

    Button deleteButton = (Button) this.connectionInputList.getWidget(row, 5);
    deleteButton.setEnabled(enabled);
    Button editButton = (Button) this.connectionInputList.getWidget(row, 6);
    editButton.setEnabled(enabled);
  }

  /**
   * Creates the callback for the delete call.
   *
   * @param connection The database connection which should be removed from the table input tab.
   * @return The callback
   */
  protected MethodCallback<Void> getDeleteCallback(final DatabaseConnection connection) {
    return new MethodCallback<Void>() {

      @Override
      public void onFailure(Method method, Throwable throwable) {
        messageReceiver
            .addError("Could not delete database connection: " + method.getResponse().getText());
      }

      @Override
      public void onSuccess(Method method, Void aVoid) {
        connectionInputList.removeRow(findRow(connection));
        parent.removeDatabaseConnectionFromTableInputTab(connection);
      }
    };
  }

  /**
   * Find the row in the table, which contains the given database connection.
   *
   * @param connection the database connection
   * @return the row number
   */
  private int findRow(DatabaseConnection connection) {
    int row = 0;

    while (row < this.connectionInputList.getRowCount()) {
      HTML urlWidget = (HTML) this.connectionInputList.getWidget(row, 0);
      HTML userWidget = (HTML) this.connectionInputList.getWidget(row, 1);
      HTML systemWidget = (HTML) this.connectionInputList.getWidget(row, 2);

      if (urlWidget != null && connection.getUrl().equals(urlWidget.getText()) &&
          userWidget != null && connection.getUsername().equals(userWidget.getText()) &&
          systemWidget != null && connection.getSystem().name().equals(systemWidget.getText())) {
        return row;
      }
      row++;
    }
    return -1;
  }

  /**
   * Find the row of the old database connection and updates the values.
   *
   * @param updatedDatabaseConnection the updated database connection
   * @param oldDatabaseConnection     the old database connection
   */
  public void updateDatabaseConnectionInTable(final DatabaseConnection updatedDatabaseConnection,
                                              DatabaseConnection oldDatabaseConnection) {
    int row = this.findRow(oldDatabaseConnection);

    this.connectionInputList.setWidget(row, 0, new HTML(updatedDatabaseConnection.getUrl()));
    this.connectionInputList.setWidget(row, 1, new HTML(updatedDatabaseConnection.getUsername()));
    this.connectionInputList
        .setWidget(row, 2, new HTML(updatedDatabaseConnection.getSystem().name()));
    this.connectionInputList.setText(row, 3, updatedDatabaseConnection.getComment());

    Button editButton = new Button("Edit");
    editButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        editForm.updateDatabaseConnection(updatedDatabaseConnection);
      }
    });
    this.connectionInputList.setWidget(row, 6, editButton);
  }

  @Override
  public void setMessageReceiver(TabWrapper tab) {
    this.messageReceiver = tab;
    this.editForm.setMessageReceiver(tab);
  }
}
