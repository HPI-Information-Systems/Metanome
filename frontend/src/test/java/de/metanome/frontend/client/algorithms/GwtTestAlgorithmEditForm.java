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

package de.metanome.frontend.client.algorithms;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.Button;

import de.metanome.backend.results_db.Algorithm;
import de.metanome.frontend.client.BasePage;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.TestHelper;
import de.metanome.frontend.client.helpers.InputValidationException;

import java.util.ArrayList;
import java.util.List;

public class GwtTestAlgorithmEditForm extends GWTTestCase {

  /**
   * Test method for {@link de.metanome.frontend.client.algorithms.AlgorithmEditForm}
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

    String fileName = "example_ind_algorithm.jar";
    List<String> names = new ArrayList<>();
    names.add(fileName);
    form.fileListBox.setValues(names);

    form.fileListBox.setSelectedValue(fileName);
    form.nameTextBox.setValue("some algorithm");
    form.authorTextBox.setValue("--");

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

    try {
      form.retrieveInputValues();
    } catch (InputValidationException e) {
      form.saveSubmit();
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
      form.saveSubmit();
      assertTrue(tab.isInError());
    }

    // Clean up
    TestHelper.resetDatabaseSync();
  }

  public void testReset() throws InputValidationException {
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
    form.descriptionTextArea.setValue("comment");

    // Execute
    form.reset();

    // Check
    assertEquals("", form.nameTextBox.getText());
    assertEquals("", form.authorTextBox.getText());
    assertEquals("", form.descriptionTextArea.getText());
    assertEquals("--", form.fileListBox.getSelectedValue());

    // Clean up
    TestHelper.resetDatabaseSync();
  }

  /**
   * Test method for {@link de.metanome.frontend.client.algorithms.AlgorithmEditForm#updateAlgorithm(de.metanome.backend.results_db.Algorithm)}
   * and test method for {@link AlgorithmEditForm#showSaveButton()}
   *
   * If the edit button for an algorithm is clicked, the edit form should contain the values
   * of that algorithm and the edit form should show a update button instead of an save button.
   * If the method 'show save button' is called, the save button should be visible again.
   */
  public void testEditButtonClicked() throws InputValidationException {
    // Setup
    TestHelper.resetDatabaseSync();

    TabWrapper tab = new TabWrapper();
    AlgorithmEditForm form = new AlgorithmEditForm(new AlgorithmsPage(new BasePage()), tab);

    // Expected Values
    String expectedFileName = "some file name";
    String expectedName = "some name";
    String expectedAuthor = "some author";
    String expectedDescription = "some description";
    Algorithm algorithm = new Algorithm(expectedFileName)
        .setAuthor(expectedAuthor)
        .setName(expectedName)
        .setDescription(expectedDescription);

    // Execute
    form.updateAlgorithm(algorithm);

    // Check results
    assertEquals(expectedAuthor, form.authorTextBox.getText());
    assertEquals(expectedDescription, form.descriptionTextArea.getText());
    assertEquals(expectedName, form.nameTextBox.getText());
    assertEquals(expectedFileName, form.fileListBox.getSelectedValue());
    assertEquals(2, form.fileListBox.getValues().size());

    assertEquals(((Button) form.getWidget(4, 1)).getText(), "Update");

    // Execute
    form.showSaveButton();

    // Check results
    assertEquals(((Button) form.getWidget(4, 1)).getText(), "Save");
    assertEquals(1, form.fileListBox.getValues().size());

  }

  @Override
  public String getModuleName() {
    return "de.metanome.frontend.client.MetanomeTest";
  }

}
