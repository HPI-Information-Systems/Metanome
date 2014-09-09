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

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;

import de.metanome.backend.results_db.EntityStorageException;
import de.metanome.frontend.client.BasePage;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.TestHelper;
import de.metanome.frontend.client.helpers.InputValidationException;

public class GwtTestDataSourcePage extends GWTTestCase {

  /**
   * Test method for {@link de.metanome.frontend.client.datasources.DataSourcePage#DataSourcePage(de.metanome.frontend.client.BasePage)}
   */
  public void testSetUp() {
    // Set up
    TestHelper.resetDatabaseSync();

    BasePage basePage = new BasePage();

    // Execute
    DataSourcePage dataSourcePage = new DataSourcePage(basePage);

    // Check
    assertNotNull(dataSourcePage.databaseConnectionTab);
    assertNotNull(dataSourcePage.fileInputTab);
    assertNotNull(dataSourcePage.tableInputTab);

    // Cleanup
    TestHelper.resetDatabaseSync();
  }

  /**
   * Test method for {@link DataSourcePage#getDeleteCallback(com.google.gwt.user.client.ui.FlexTable,
   * int, String)}
   */
  public void testDeleteCallback() throws EntityStorageException, InputValidationException {
    // Setup
    TestHelper.resetDatabaseSync();

    BasePage parent = new BasePage();
    DataSourcePage page = new DataSourcePage(parent);
    page.setMessageReceiver(new TabWrapper());

    page.fileInputTab.fileInputList.setWidget(0, 0, new HTML("File 1"));
    page.fileInputTab.fileInputList.setWidget(1, 0, new HTML("File 2"));
    page.fileInputTab.fileInputList.setWidget(2, 0, new HTML("File 3"));

    int rowCount = page.fileInputTab.fileInputList.getRowCount();

    // Execute (delete File 2)
    AsyncCallback<Void>
        callback =
        page.getDeleteCallback(page.fileInputTab.fileInputList, 1, "File Input");
    callback.onSuccess(null);

    // Check
    assertEquals(rowCount - 1, page.fileInputTab.fileInputList.getRowCount());
    assertEquals("File 3", ((HTML) page.fileInputTab.fileInputList.getWidget(1, 0)).getText());

    // Execute (delete File 1)
    callback = page.getDeleteCallback(page.fileInputTab.fileInputList, 0, "File Input");
    callback.onSuccess(null);

    // Check
    assertEquals(rowCount - 2, page.fileInputTab.fileInputList.getRowCount());
    assertEquals("File 3", ((HTML) page.fileInputTab.fileInputList.getWidget(0, 0)).getText());

    // Cleanup
    TestHelper.resetDatabaseSync();
  }

  @Override
  public String getModuleName() {
    return "de.uni_potsdam.hpi.metanome.frontend.client.MetanomeTest";
  }
}
