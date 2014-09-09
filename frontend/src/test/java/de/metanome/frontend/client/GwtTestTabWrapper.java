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

package de.metanome.frontend.client;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.Label;

import de.metanome.frontend.client.algorithms.AlgorithmsPage;
import de.metanome.frontend.client.runs.RunConfigurationPage;

/**
 * Tests related to the overall page.
 */
public class GwtTestTabWrapper extends GWTTestCase {

  private String errorMessage = "Could not find example_ucc_algorithm.jar";
  private String infoMessage = "Input successfully saved.";

  /**
   * Test constructor.
   */
  public void testCreate() {
    TabContent content = new RunConfigurationPage(null);

    // Execute
    TabWrapper tabWrapper = new TabWrapper(content);

    // Check
    assertEquals(content, tabWrapper.contentPanel);
    assertTrue(content.asWidget().isVisible());
    assertFalse(tabWrapper.isInError());
    assertFalse(tabWrapper.isInInfo());
  }

  /**
   * Test method for {@link TabWrapper#addError(String)}
   */
  public void testAddError() {
    TabWrapper tabWrapper = new TabWrapper(new AlgorithmsPage(null));

    // Execute
    tabWrapper.addError(errorMessage);

    // Check
    assertTrue(tabWrapper.errorPanel.getWidgetCount() == 1);
    assertEquals(errorMessage, ((Label) tabWrapper.errorPanel.getWidget(0)).getText());
    assertTrue(tabWrapper.isInError());

    // Execute
    tabWrapper.addError(errorMessage);

    // Check
    assertTrue(tabWrapper.errorPanel.getWidgetCount() == 2);
    assertEquals(errorMessage, ((Label) tabWrapper.errorPanel.getWidget(1)).getText());
    assertTrue(tabWrapper.isInError());
  }


  /**
   * Test method for {@link TabWrapper#addInfo(String)}
   */
  public void testAddInfo() {
    TabWrapper tabWrapper = new TabWrapper(new AlgorithmsPage(null));

    // Execute
    tabWrapper.addInfo(infoMessage);

    // Check
    assertTrue(tabWrapper.infoPanel.getWidgetCount() == 1);
    assertEquals(infoMessage, ((Label) tabWrapper.infoPanel.getWidget(0)).getText());
    assertTrue(tabWrapper.isInInfo());

    // Execute
    tabWrapper.addInfo(infoMessage);

    // Check
    assertTrue(tabWrapper.infoPanel.getWidgetCount() == 2);
    assertEquals(infoMessage, ((Label) tabWrapper.infoPanel.getWidget(1)).getText());
    assertTrue(tabWrapper.isInInfo());
  }


  /**
   * Test method for {@link de.metanome.frontend.client.TabWrapper#clearErrors()}
   */
  public void testClearErrors() {
    // Setup
    TabWrapper tabWrapper = new TabWrapper(new RunConfigurationPage(null));
    tabWrapper.addError(errorMessage + "2");

    // Check precondition
    assertTrue(tabWrapper.isInError());

    // Execute
    tabWrapper.clearErrors();

    // Check
    assertTrue(tabWrapper.errorPanel.getWidgetCount() == 0);
    assertFalse(tabWrapper.isInError());
  }

  /**
   * Test method for {@link TabWrapper#clearInfos()}
   */
  public void testClearInfos() {
    // Setup
    TabWrapper tabWrapper = new TabWrapper(new RunConfigurationPage(null));
    tabWrapper.addInfo(infoMessage + "2");

    // Check precondition
    assertTrue(tabWrapper.isInInfo());

    // Execute
    tabWrapper.clearInfos();

    // Check
    assertTrue(tabWrapper.infoPanel.getWidgetCount() == 0);
    assertFalse(tabWrapper.isInInfo());
  }

  @Override
  public String getModuleName() {
    return "de.uni_potsdam.hpi.metanome.frontend.MetanomeTest";
  }

}
