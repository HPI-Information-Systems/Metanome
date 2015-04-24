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

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;

import de.metanome.algorithm_integration.configuration.ConfigurationSettingDataSource;
import de.metanome.backend.results_db.DatabaseConnection;
import de.metanome.frontend.client.BasePage;
import de.metanome.frontend.client.TabContent;
import de.metanome.frontend.client.TabWrapper;

/**
 * Page to configure data inputs.
 */
public class DataSourcePage extends TabLayoutPanel implements TabContent {

  private static final String DATABASE_CONNECTION = "Database Connection";
  private static final String FILE_INPUT = "File Input";
  private static final String TABLE_INPUT = "Table Input";
  protected TableInputTab tableInputTab;
  protected FileInputTab fileInputTab;
  protected DatabaseConnectionTab databaseConnectionTab;
  private TabWrapper messageReceiver;
  private BasePage basePage;

  /**
   * Constructor
   *
   * @param basePage the parent page
   */
  public DataSourcePage(BasePage basePage) {
    super(1, Style.Unit.CM);

    this.basePage = basePage;

    this.tableInputTab = new TableInputTab(this);
    this.fileInputTab = new FileInputTab(this);
    this.databaseConnectionTab = new DatabaseConnectionTab(this);

    this.add(new ScrollPanel(fileInputTab), FILE_INPUT);
    this.add(new ScrollPanel(databaseConnectionTab), DATABASE_CONNECTION);
    this.add(new ScrollPanel(tableInputTab), TABLE_INPUT);
  }

  /**
   * Switch to the run configuration page. The selected data source should be preselected.
   *
   * @param dataSource the preselected data source
   */
  public void callRunConfiguration(ConfigurationSettingDataSource dataSource) {
    this.basePage.switchToRunConfiguration(null, dataSource);
  }

  /**
   * Updates the table input tab. Adds a new database connection to the list of available database
   * connections.
   *
   * @param connection the connection which is new and should be added
   */
  public void removeDatabaseConnectionFromTableInputTab(DatabaseConnection connection) {
    this.tableInputTab.removeDatabaseConnection(connection);
  }

  /**
   * Updates the table input tab. Adds a new database connection to the list of available database
   * connections.
   *
   * @param connection the connection which is new and should be added
   */
  public void addDatabaseConnectionToTableInputTab(DatabaseConnection connection) {
    this.tableInputTab.addDatabaseConnection(connection);
  }

  /**
   * Updates the table input tab. Updates database connection in the list of available database
   * connections.
   *
   * @param connection    the connection which was updated
   * @param oldConnection the old connection
   */
  public void updateDatabaseConnectionToTableInputTab(DatabaseConnection connection,
                                                      DatabaseConnection oldConnection) {
    this.tableInputTab.updateDatabaseConnection(connection, oldConnection);
  }

  /**
   * Forwards the command to update the data sources on the run configuration page to the base
   * page.
   */
  public void updateDataSourcesOnRunConfiguration() {
    this.basePage.updateDataSourcesOnRunConfiguration();
  }

  public void setEnableOfButtons(DatabaseConnection databaseConnection, Boolean enabled) {
    this.databaseConnectionTab.setEnableOfButtons(databaseConnection, enabled);
  }

  @Override
  public void setMessageReceiver(TabWrapper tab) {
    this.messageReceiver = tab;
    this.tableInputTab.setMessageReceiver(tab);
    this.fileInputTab.setMessageReceiver(tab);
    this.databaseConnectionTab.setMessageReceiver(tab);
  }

//  public void showResultsFor(FileInput input) {
//    this.basePage.showResultsFor(input);
//  }
}
