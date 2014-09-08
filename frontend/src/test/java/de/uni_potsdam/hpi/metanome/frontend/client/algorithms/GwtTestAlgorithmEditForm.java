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

package de.uni_potsdam.hpi.metanome.frontend.client.algorithms;

import com.google.gwt.junit.client.GWTTestCase;

import de.uni_potsdam.hpi.metanome.frontend.client.BasePage;
import de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper;
import de.uni_potsdam.hpi.metanome.frontend.client.TestHelper;
import de.uni_potsdam.hpi.metanome.frontend.client.helpers.InputValidationException;

import java.util.ArrayList;
import java.util.List;

public class GwtTestAlgorithmEditForm extends GWTTestCase {

  /**
   * Test method for
   * {@link de.uni_potsdam.hpi.metanome.frontend.client.algorithms.AlgorithmEditForm}
   *
   * When a new AlgorithmEditForm is created, the algorithmsPage and messageReceiver should be set
   */
  public void testSetup() {
    // Set up
    TestHelper.resetDatabaseSync();

    AlgorithmsPage parent = new AlgorithmsPage(new BasePage());
    TabWrapper tab = new TabWrapper();
    AlgorithmEditForm form = new AlgorithmEditForm(parent, tab);

    // Check that parent was set
    assertEquals(parent, form.algorithmsPage);
    assertEquals(tab, form.messageReceiver);

    // Clean up
    TestHelper.resetDatabaseSync();
  }

  /**
   * Test method for {@link AlgorithmEditForm#retrieveInputValues()}
   */
  public void testValidInput() {
    // Set up
    TestHelper.resetDatabaseSync();

    TabWrapper tab = new TabWrapper();
    AlgorithmEditForm form = new AlgorithmEditForm(new AlgorithmsPage(new BasePage()), tab);

    String fileName = "some/file/name.jar";
    List<String> names = new ArrayList<>();
    names.add(fileName);
    form.fileListBox.setValues(names);

    form.fileListBox.setSelectedValue(fileName);
    form.nameTextBox.setValue("some algorithm");
    form.authorTextBox.setValue("--");
    form.indCheckBox.setValue(true);

    // the above is valid input and retrieving the values should not fail
    try {
      form.retrieveInputValues();
    } catch (InputValidationException e) {
      fail();
    }

    // Clean up
    TestHelper.resetDatabaseSync();
  }

  /**
   * Test method for {@link AlgorithmEditForm#retrieveInputValues()}
   */
  public void testEmptyFileName() {
    // Set up
    TestHelper.resetDatabaseSync();

    TabWrapper tab = new TabWrapper();
    AlgorithmEditForm form = new AlgorithmEditForm(new AlgorithmsPage(new BasePage()), tab);

    form.nameTextBox.setValue("some algorithm");
    form.authorTextBox.setValue("--");
    form.indCheckBox.setValue(true);

    try {
      form.retrieveInputValues();
    } catch (InputValidationException e) {
      form.submit();
      assertTrue(tab.isInError());
    }

    // Clean up
    TestHelper.resetDatabaseSync();
  }

  /**
   * Test method for {@link AlgorithmEditForm#retrieveInputValues()}
   */
  public void testNoInterfaces() {
    // Set up
    TestHelper.resetDatabaseSync();

    TabWrapper tab = new TabWrapper();
    AlgorithmEditForm form = new AlgorithmEditForm(new AlgorithmsPage(new BasePage()), tab);

    String fileName = "some/file/name.jar";
    List<String> names = new ArrayList<>();
    names.add(fileName);
    form.fileListBox.setValues(names);

    form.fileListBox.setSelectedValue(fileName);
    form.nameTextBox.setValue("some algorithm");
    form.authorTextBox.setValue("--");

    try {
      form.retrieveInputValues();
    } catch (InputValidationException e) {
      form.submit();
      assertTrue(tab.isInError());
    }

    // Clean up
    TestHelper.resetDatabaseSync();
  }

  @Override
  public String getModuleName() {
    return "de.uni_potsdam.hpi.metanome.frontend.client.MetanomeTest";
  }

}
