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

package de.uni_potsdam.hpi.metanome.frontend.client.parameter;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingSqlIterator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.DbSystem;

import java.util.Arrays;

/**
 * An input widget, used to specify settings for a database connection.
 *
 * @author Claudia Exeler
 * @see de.uni_potsdam.hpi.metanome.results_db.DatabaseConnection
 */
public class SqlIteratorInput extends InputField {

  protected TextBox dbUrlTextbox;
  protected TextBox usernameTextbox;
  protected TextBox passwordTextbox;
  protected ListBoxInput systemListBox;
  protected FlexTable layoutTable;


  public SqlIteratorInput(boolean optional) {
    super(optional);

    this.layoutTable = new FlexTable();
    this.add(this.layoutTable);

    this.dbUrlTextbox = new TextBox();
    addRow(this.dbUrlTextbox, "Database URL", 0);

    this.usernameTextbox = new TextBox();
    addRow(this.usernameTextbox, "User Name", 1);

    this.passwordTextbox = new TextBox();
    addRow(this.passwordTextbox, "Password", 2);

    this.systemListBox = new ListBoxInput(false);
    this.systemListBox.setValues(Arrays.asList(DbSystem.names()));
    addRow(this.systemListBox, "Db system", 3);
  }

  protected void addRow(Widget inputWidget, String name, int row) {
    this.layoutTable.setText(row, 0, name);
    this.layoutTable.setWidget(row, 1, inputWidget);
  }

  /**
   * Returns the current widget's settings as a setting
   *
   * @return the widget's settings
   */
  public ConfigurationSettingSqlIterator getValues() {
    return new ConfigurationSettingSqlIterator(this.dbUrlTextbox.getValue(),
                                               this.usernameTextbox.getValue(),
                                               this.passwordTextbox.getValue(),
                                               DbSystem
                                                   .valueOf(this.systemListBox.getSelectedValue()));
  }

  /**
   * Takes a setting a sets the values on the widgets.
   *
   * @param setting the settings to set on the widgets
   */
  public void setValues(ConfigurationSettingSqlIterator setting) {
    this.dbUrlTextbox.setValue(setting.getDbUrl());
    this.usernameTextbox.setValue(setting.getUsername());
    this.passwordTextbox.setValue(setting.getPassword());
    this.systemListBox.setSelectedValue(setting.getSystem().name());
  }

}
