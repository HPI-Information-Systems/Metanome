/*
 * Copyright 2015 by the Metanome project
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

package de.metanome.frontend.client.results;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.Label;

import de.metanome.backend.results_db.ResultType;
import de.metanome.frontend.client.results.pagination_table.FunctionalDependencyPaginationTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GwtTestResultsPaginationTablePage extends GWTTestCase {

  public void testSetup() {
    // Execute
    ResultsPaginationTablePage page = new ResultsPaginationTablePage();

    // Check
    assertNotNull(page.resultService);
    assertNotNull(page.resultStoreService);
  }

  public void testAddTables() throws Exception {
    // Set up
    ResultsPaginationTablePage page = new ResultsPaginationTablePage();

    List<String> types = new ArrayList<>();
    types.add(ResultType.FD.getName());

    // Execute functionality
    page.addTables(types);

    // Check
    assertEquals(2, page.getWidgetCount());
    assertEquals(ResultType.FD.getName(), ((Label) page.getWidget(0)).getText());
    assertEquals(FunctionalDependencyPaginationTable.class, page.getWidget(1).getClass());
  }

  public void testDisplayCountResult() throws Exception {
    // Set up
    ResultsPaginationTablePage page = new ResultsPaginationTablePage();

    Map<String, Integer> results = new HashMap<>();
    results.put(ResultType.FD.getName(), 3);

    // Execute functionality
    page.displayCountResult(results);

    // Check
    assertEquals(2, page.getWidgetCount());
    assertEquals(ResultType.FD.getName(), ((Label) page.getWidget(0)).getText());
    assertEquals("# 3", ((Label) page.getWidget(1)).getText());
  }

  @Override
  public String getModuleName() {
    return "de.metanome.frontend.MetanomeTest";
  }

}
