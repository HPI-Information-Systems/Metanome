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

package de.uni_potsdam.hpi.metanome.frontend.client;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.Label;

import de.uni_potsdam.hpi.metanome.frontend.client.algorithms.AlgorithmsPage;
import de.uni_potsdam.hpi.metanome.frontend.client.datasources.DataSourcesPage;
import de.uni_potsdam.hpi.metanome.frontend.client.runs.RunConfigurationPage;

/**
 * Tests related to the overall page.
 */
public class GwtTestTabWrapper extends GWTTestCase {

  /**
   * this must contain an algorithm and a data source that are currently available
   */
  private String message = "Could not find example_ucc_algorithm.jar";
  private TabWrapper tabWrapper;


  /**
   * Test constructor.
   */
  public void testCreate() {
    TabContent content = new RunConfigurationPage(null);

    //Execute
    tabWrapper = new TabWrapper(content);

    //Check
    assertEquals(content, tabWrapper.contentPanel);
    assertTrue(content.asWidget().isVisible());
    assertFalse(tabWrapper.isInError());
  }

  /**
   * Test method for {@link TabWrapper#addError(String)}
   */
  public void testAddError() {
    tabWrapper = new TabWrapper(new AlgorithmsPage(null));

    //Execute
    tabWrapper.addError(message);

    //Check
    assertTrue(tabWrapper.errorPanel.getWidgetCount() == 1);
    assertEquals(message, ((Label) tabWrapper.errorPanel.getWidget(0)).getText());
    assertTrue(tabWrapper.isInError());

    //Execute
    tabWrapper.addError(message);

    //Check
    assertTrue(tabWrapper.errorPanel.getWidgetCount() == 2);
    assertEquals(message, ((Label) tabWrapper.errorPanel.getWidget(1)).getText());
    assertTrue(tabWrapper.isInError());
    //requiring later added errors to be below earlier ones
  }


  /**
   * Test control flow from Algorithms to Run configuration
   */
  public void testClearErrors() {
    // Setup
    tabWrapper = new TabWrapper(new DataSourcesPage(null));
    tabWrapper.addError(message + "2");

    // Check precondition
    assertTrue(tabWrapper.isInError());

    // Execute
    tabWrapper.clearErrors();

    // Check
    assertTrue(tabWrapper.errorPanel.getWidgetCount() == 0);
    assertFalse(tabWrapper.isInError());
  }

  @Override
  public String getModuleName() {
    return "de.uni_potsdam.hpi.metanome.frontend.MetanomeTest";
  }

}
