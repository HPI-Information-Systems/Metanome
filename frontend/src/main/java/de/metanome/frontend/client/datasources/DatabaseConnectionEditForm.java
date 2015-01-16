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
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

import de.metanome.algorithm_integration.configuration.DbSystem;
import de.metanome.backend.results_db.DatabaseConnection;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.helpers.InputValidationException;
import de.metanome.frontend.client.input_fields.ListBoxInput;
import de.metanome.frontend.client.services.DatabaseConnectionRestService;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.Arrays;

import javax.activation.DataContentHandler;

/**
 * Input field to configure a database connection.
 */
public class DatabaseConnectionEditForm extends Grid {

  protected TextBox dbUrlTextbox;
  protected TextBox usernameTextbox;
  protected PasswordTextBox passwordTextbox;
  protected ListBoxInput systemListBox;
  protected TextArea commentTextbox;
  private DatabaseConnectionRestService databaseConnectionService;
  private DatabaseConnectionTab parent;
  private TabWrapper messageReceiver;
  private Button saveButton;
  private Button updateButton;
  private DatabaseConnection oldDatabaseConnection;

  public DatabaseConnectionEditForm(DatabaseConnectionTab parent) {
    super(6, 2);

    this.parent = parent;
    this.databaseConnectionService = com.google.gwt.core.client.GWT.create(DatabaseConnectionRestService.class);

    this.dbUrlTextbox = new TextBox();
    this.dbUrlTextbox.getElement().setPropertyString("placeholder", "jdbc:mysql://localhost/db");
    this.setText(0, 0, "Database URL");
    this.setWidget(0, 1, this.dbUrlTextbox);

    this.systemListBox = new ListBoxInput(false);
    this.systemListBox.setValues(Arrays.asList(DbSystem.names()));
    this.setText(1, 0, "Database System");
    this.setWidget(1, 1, this.systemListBox);

    this.usernameTextbox = new TextBox();
    this.setText(2, 0, "User Name");
    this.setWidget(2, 1, this.usernameTextbox);

    this.passwordTextbox = new PasswordTextBox();
    this.setText(3, 0, "Password");
    this.setWidget(3, 1, this.passwordTextbox);

    this.commentTextbox = new TextArea();
    this.commentTextbox.setVisibleLines(3);
    this.setText(4, 0, "Comment");
    this.setWidget(4, 1, this.commentTextbox);

    this.saveButton = new Button("Save", new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        saveDatabaseConnection();
      }
    });
    this.updateButton = new Button("Update", new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        submitUpdate();
      }
    });
    this.setWidget(5, 1, new Button("Save", new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        saveDatabaseConnection();
      }
    }));
  }

  /**
   * Set the url,username and password in the according text boxes.
   *
   * @param url      the url for the database connection
   * @param system   the database system for the database connection
   * @param username the username for the database connection
   * @param password the password for the database connection
   * @param comment  the comment for the database connection
   */
  public void setValues(String url, String system, String username, String password, String comment) {
    this.dbUrlTextbox.setValue(url);
    this.usernameTextbox.setValue(username);
    this.passwordTextbox.setValue(password);
    this.systemListBox.setSelectedValue(system);
    this.commentTextbox.setValue(comment);
  }

  /**
   * Create a database connection with the specified values.
   *
   * @return a database connection
   * @throws InputValidationException if one of the fields is empty
   */
  public DatabaseConnection getValue() throws InputValidationException {
    DatabaseConnection connection = new DatabaseConnection();

    String url = this.dbUrlTextbox.getValue();
    String username = this.usernameTextbox.getValue();
    String password = this.passwordTextbox.getValue();
    String system = this.systemListBox.getSelectedValue();
    String comment = this.commentTextbox.getValue();

    if (url.isEmpty() || username.isEmpty() || password.isEmpty() || system.isEmpty()) {
      throw new InputValidationException(
          "The database url, username, password and system should all be set!");
    }

    connection
        .setUrl(url)
        .setUsername(username)
        .setPassword(password)
        .setSystem(DbSystem.valueOf(system))
        .setComment(comment);

    return connection;
  }

  /**
   * Stores the current database connection in the database.
   */
  private void saveDatabaseConnection() {
    messageReceiver.clearErrors();
    try {
      final DatabaseConnection currentConnection = this.getValue();

      this.databaseConnectionService
          .storeDatabaseConnection(currentConnection, new MethodCallback<DatabaseConnection>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
              messageReceiver
                  .addError("Database Connection could not be stored:" + method.getResponse().getText());
            }

            @Override
            public void onSuccess(Method method, DatabaseConnection connection) {
              reset();
              parent.addDatabaseConnectionToTable(connection);
              parent.updateTableInputTab(connection);
              parent.updateDataSourcesOnRunConfiguration();
            }

          });
    } catch (InputValidationException e) {
      messageReceiver.addError("Database Connection could not be stored: " + e.getMessage());
    }
  }

  /**
   * Updates the current database connection in the database.
   */
  private void submitUpdate() {
    messageReceiver.clearErrors();
    try {
      final DatabaseConnection currentConnection = this.getValue();

      this.databaseConnectionService
          .updateDatabaseConnection(currentConnection, new MethodCallback<DatabaseConnection>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
              messageReceiver
                  .addError("Database Connection could not be stored:" + method.getResponse().getText());
              reset();
              showSaveButton();
            }

            @Override
            public void onSuccess(Method method, DatabaseConnection connection) {
              reset();
              showSaveButton();
              parent.updateDatabaseConnectionInTable(connection, oldDatabaseConnection);
              parent.updateTableInputTab(connection, oldDatabaseConnection);
              parent.updateDataSourcesOnRunConfiguration();
            }

          });
    } catch (InputValidationException e) {
      messageReceiver.addError("Database Connection could not be stored: " + e.getMessage());
    }
  }

  /**
   * Fills the form with the values of the database connection, which should be updated.
   * @param databaseConnection the database connection
   */
  public void updateDatabaseConnection(DatabaseConnection databaseConnection) {
    this.dbUrlTextbox.setText(databaseConnection.getUrl());
    this.usernameTextbox.setText(databaseConnection.getUsername());
    this.passwordTextbox.setText(databaseConnection.getPassword());
    this.systemListBox.setSelectedValue(databaseConnection.getSystem().name());
    this.commentTextbox.setText(databaseConnection.getComment());

    this.setWidget(5, 1, updateButton);
    this.oldDatabaseConnection = databaseConnection;
  }

  /**
   * Resets all values in the url, username and password text boxes.
   */
  public void reset() {
    this.dbUrlTextbox.setText("");
    this.usernameTextbox.setText("");
    this.passwordTextbox.setText("");
    this.systemListBox.reset();
    this.commentTextbox.setText("");
  }

  /**
   * Shows the save button.
   */
  public void showSaveButton() {
    this.setWidget(5, 1, saveButton);
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
