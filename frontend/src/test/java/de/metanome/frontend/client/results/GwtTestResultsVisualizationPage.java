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

package de.metanome.frontend.client.results;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.Label;

public class GwtTestResultsVisualizationPage extends GWTTestCase {

  /**
   * Test method for {@link FDResultsVisualizationPage#ResultsVisualizationPage()}
   */
  public void testResultsVisualizationPage() {
    // Set up
    // Expected Values
    // Execute
    FDResultsVisualizationPage page = new FDResultsVisualizationPage(0l);

    // Check
    assertTrue(page.getWidget(0) instanceof Label);
  }

  @Override
  public String getModuleName() {
    return "de.metanome.frontend.MetanomeTest";
  }

}
