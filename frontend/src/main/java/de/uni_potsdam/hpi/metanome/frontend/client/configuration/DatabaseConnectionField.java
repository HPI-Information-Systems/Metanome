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
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import de.uni_potsdam.hpi.metanome.frontend.client.helpers.InputValidationException;
import de.uni_potsdam.hpi.metanome.results_db.DatabaseConnection;

public class DatabaseConnectionField extends HorizontalPanel {

  private TextBox dbUrlTextbox;
  private TextBox usernameTextbox;
  private PasswordTextBox passwordTextbox;
  private FlexTable layoutTable;


  public DatabaseConnectionField() {
    this.layoutTable = new FlexTable();
    this.add(this.layoutTable);

    this.dbUrlTextbox = new TextBox();
    addRow(this.dbUrlTextbox, "Database URL", 0);

    this.usernameTextbox = new TextBox();
    addRow(this.usernameTextbox, "User Name", 1);

    this.passwordTextbox = new PasswordTextBox();
    addRow(this.passwordTextbox, "Password", 2);

  }

  protected void addRow(Widget inputWidget, String name, int row) {
    this.layoutTable.setText(row, 0, name);
    this.layoutTable.setWidget(row, 1, inputWidget);
  }

  /**
   * Set the url, username and password in the according text boxes.
   * @param url       the url for the database connection
   * @param username  the username for the database connection
   * @param password  the password for the database connection
   */
  public void setValues(String url, String username, String password) {
    this.dbUrlTextbox.setValue(url);
    this.usernameTextbox.setValue(username);
    this.passwordTextbox.setValue(password);
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

    if (url.isEmpty() || username.isEmpty() || password.isEmpty())
      throw new InputValidationException("The database url, username and password should all be set!");

    connection.setUrl(url);
    connection.setUsername(username);
    connection.setPassword(password);

    return connection;
  }

  public void reset() {
    this.dbUrlTextbox.setText("");
    this.usernameTextbox.setText("");
    this.passwordTextbox.setText("");
  }


}
