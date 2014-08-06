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


import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.DbSystem;
import de.uni_potsdam.hpi.metanome.frontend.client.helpers.InputValidationException;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.ListBoxInput;
import de.uni_potsdam.hpi.metanome.results_db.DatabaseConnection;

import java.util.Arrays;

/**
 * Input field to configure a database connection.
 */
public class DatabaseConnectionEditForm extends FlowPanel {

  protected TextBox dbUrlTextbox;
  protected TextBox usernameTextbox;
  protected PasswordTextBox passwordTextbox;
  protected ListBoxInput systemListBox;
  private FlexTable layoutTable;

  public DatabaseConnectionEditForm() {
    this.addStyleName("left");

    this.layoutTable = new FlexTable();
    this.add(this.layoutTable);

    this.dbUrlTextbox = new TextBox();
    addRow(this.dbUrlTextbox, "Database URL", 0);

    this.systemListBox = new ListBoxInput(false);
    this.systemListBox.setValues(Arrays.asList(DbSystem.names()));
    addRow(this.systemListBox, "Db system", 1);

    this.usernameTextbox = new TextBox();
    addRow(this.usernameTextbox, "User Name", 2);

    this.passwordTextbox = new PasswordTextBox();
    addRow(this.passwordTextbox, "Password", 3);

  }

  protected void addRow(Widget inputWidget, String name, int row) {
    this.layoutTable.setText(row, 0, name);
    this.layoutTable.setWidget(row, 1, inputWidget);
  }

  /**
   * Set the url,username and password in the according text boxes.
   * @param url       the url for the database connection
   * @param system    the database system for the database connection
   * @param username  the username for the database connection
   * @param password  the password for the database connection
   */
  public void setValues(String url, String system, String username, String password) {
    this.dbUrlTextbox.setValue(url);
    this.usernameTextbox.setValue(username);
    this.passwordTextbox.setValue(password);
    this.systemListBox.setSelectedValue(system);
  }

  /**
   * Create a database connection with the specified values.
   * @return a database connection
   * @throws InputValidationException
   */
  public DatabaseConnection getValue() throws InputValidationException {
    DatabaseConnection connection = new DatabaseConnection();

    String url = this.dbUrlTextbox.getValue();
    String username = this.usernameTextbox.getValue();
    String password = this.passwordTextbox.getValue();
    String system = this.systemListBox.getSelectedValue();

    if (url.isEmpty() || username.isEmpty() || password.isEmpty() || system.isEmpty())
      throw new InputValidationException("The database url, username and password should all be set!");

    connection
        .setUrl(url)
        .setUsername(username)
        .setPassword(password);
    // TODO set system

    return connection;
  }

  /**
   * Reset all values in the url, username and password text boxes.
   */
  public void reset() {
    this.dbUrlTextbox.setText("");
    this.usernameTextbox.setText("");
    this.passwordTextbox.setText("");
    this.systemListBox.reset();
  }


}
