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
import com.google.gwt.user.client.ui.Button;

import de.metanome.algorithm_integration.configuration.DbSystem;
import de.metanome.backend.results_db.DatabaseConnection;
import de.metanome.frontend.client.BasePage;
import de.metanome.frontend.client.TestHelper;
import de.metanome.frontend.client.helpers.InputValidationException;

import java.util.Arrays;


public class GwtTestDatabaseConnectionEditForm extends GWTTestCase {

  /**
   * Test method for {@link de.metanome.frontend.client.datasources.DatabaseConnectionEditForm#getValue()}
   */
  public void testGetValue() throws InputValidationException {
    //Setup
    String expectedUrl = "url";
    String expectedUser = "user";
    String expectedPassword = "password";
    String expectedComment = "comment";
    String expectedSystem = DbSystem.DB2.name();

    DatabaseConnectionEditForm
        input =
        new DatabaseConnectionEditForm(
            new DatabaseConnectionTab(new DataSourcePage(new BasePage())));

    // Execute
    input.setValues(expectedUrl, expectedSystem, expectedUser, expectedPassword, expectedComment);
    DatabaseConnection actualConnection = input.getValue();

    //Check
    assertEquals(expectedUrl, actualConnection.getUrl());
    assertEquals(expectedSystem, actualConnection.getSystem().name());
    assertEquals(expectedUser, actualConnection.getUsername());
    assertEquals(expectedPassword, actualConnection.getPassword());
    assertEquals(expectedComment, actualConnection.getComment());
  }

  /**
   * Test method for {@link DatabaseConnectionEditForm#getValue()}
   */
  public void testGetValueWithInvalidValues() {
    //Setup
    String expectedUrl = "url";
    String expectedUser = "";
    String expectedPassword = "";
    String expectedSystem = DbSystem.DB2.name();

    DatabaseConnectionEditForm
        input =
        new DatabaseConnectionEditForm(
            new DatabaseConnectionTab(new DataSourcePage(new BasePage())));

    // Execute
    input.setValues(expectedUrl, expectedSystem, expectedUser, expectedPassword, "");

    //Check
    try {
      input.getValue();
    } catch (InputValidationException e) {
      assertTrue(true);
    }
  }

  /**
   * Test method for {@link DatabaseConnectionEditForm#reset()}
   */
  public void testResetValues() throws InputValidationException {
    //Setup
    DatabaseConnectionEditForm
        input =
        new DatabaseConnectionEditForm(
            new DatabaseConnectionTab(new DataSourcePage(new BasePage())));
    input.setValues("url", DbSystem.DB2.name(), "user", "password", "some comment");

    // Execute
    input.reset();

    String actualUser = input.usernameTextbox.getText();
    String actualPassword = input.passwordTextbox.getText();
    String actualSystem = input.systemListBox.getSelectedValue();
    String actualUrl = input.dbUrlTextbox.getText();
    String actualComment = input.commentTextbox.getText();

    //Check
    assertEquals("", actualUser);
    assertEquals(Arrays.asList(DbSystem.names()).get(0), actualSystem);
    assertEquals("", actualPassword);
    assertEquals("", actualUrl);
    assertEquals("", actualComment);
  }

  /**
   * Test method for {@link de.metanome.frontend.client.datasources.DatabaseConnectionEditForm#updateDatabaseConnection(de.metanome.backend.results_db.DatabaseConnection)}
   * and test method for {@link DatabaseConnectionEditForm#showSaveButton()}
   *
   * If the edit button for a database connection is clicked, the edit form should contain the values
   * of that database connection and the edit form should show a update button instead of an save button.
   * If the method 'show save button' is called, the save button should be visible again.
   */
  public void testEditButtonClicked() throws InputValidationException {
    // Setup
    TestHelper.resetDatabaseSync();

    DatabaseConnectionEditForm
        editForm =
        new DatabaseConnectionEditForm(
            new DatabaseConnectionTab(new DataSourcePage(new BasePage())));

    // Expected Values
    String expectedUrl = "url";
    String expectedUser = "user";
    String expectedPassword = "password";
    DbSystem expectedSystem = DbSystem.DB2;
    String expectedComment = "comment";
    DatabaseConnection connection = new DatabaseConnection()
        .setUrl(expectedUrl)
        .setComment(expectedComment)
        .setPassword(expectedPassword)
        .setSystem(expectedSystem)
        .setUsername(expectedUser);

    // Execute
    editForm.updateDatabaseConnection(connection);

    // Check results
    assertEquals(expectedComment, editForm.commentTextbox.getText());
    assertEquals(expectedPassword, editForm.passwordTextbox.getText());
    assertEquals(expectedSystem.name(), editForm.systemListBox.getSelectedValue());
    assertEquals(expectedUrl, editForm.dbUrlTextbox.getText());
    assertEquals(expectedUser, editForm.usernameTextbox.getText());

    assertEquals(((Button) editForm.getWidget(5, 1)).getText(), "Update");

    // Execute
    editForm.showSaveButton();

    // Check results
    assertEquals(((Button) editForm.getWidget(5, 1)).getText(), "Save");
  }

  @Override
  public String getModuleName() {
    return "de.metanome.frontend.client.MetanomeTest";
  }

}
