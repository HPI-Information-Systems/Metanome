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

import de.metanome.frontend.client.BasePage;
import de.metanome.frontend.client.TestHelper;

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

  @Override
  public String getModuleName() {
    return "de.metanome.frontend.client.MetanomeTest";
  }
}
