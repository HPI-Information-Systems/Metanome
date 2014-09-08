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

package de.uni_potsdam.hpi.metanome.frontend.client.results;


import com.google.gwt.junit.client.GWTTestCase;

public class GwtTestResultTable extends GWTTestCase {

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.frontend.client.results.ResultTable#setText(int,
   * int, String)} and for {@link ResultTable#getRowCount()}
   */
  public void testResultTable() {
    // Set up
    ResultTable table = new ResultTable("test table");

    // Expected Values
    String expectedText = "text";

    // Execute
    table.setText(0, 0, expectedText);

    // Check
    assertEquals(1, table.getRowCount());
    assertEquals(expectedText, table.table.getText(0, 0));
  }

  @Override
  public String getModuleName() {
    return "de.uni_potsdam.hpi.metanome.frontend.MetanomeTest";
  }

}
