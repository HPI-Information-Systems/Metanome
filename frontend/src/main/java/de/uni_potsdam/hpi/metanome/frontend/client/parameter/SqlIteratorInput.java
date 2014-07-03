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

public class SqlIteratorInput extends InputField {

  private TextBox dbUrlTextbox;
  private TextBox usernameTextbox;
  private TextBox passwordTextbox;
  private FlexTable layoutTable;


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

  }

  protected void addRow(Widget inputWidget, String name, int row) {
    this.layoutTable.setText(row, 0, name);
    this.layoutTable.setWidget(row, 1, inputWidget);
  }

  public void setValues(ConfigurationSettingSqlIterator dataSource) {
    this.dbUrlTextbox.setValue(dataSource.getDbUrl());
    this.usernameTextbox.setValue(dataSource.getUsername());
    this.passwordTextbox.setValue(dataSource.getPassword());
  }

  public ConfigurationSettingSqlIterator getValue() {
    ConfigurationSettingSqlIterator setting = new ConfigurationSettingSqlIterator();

    setting.setDbUrl(this.dbUrlTextbox.getValue());
    setting.setUsername(this.usernameTextbox.getValue());
    setting.setPassword(this.passwordTextbox.getValue());

    return setting;
  }

}
