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
import com.google.gwt.user.client.ui.HTML;

import de.metanome.backend.results_db.EntityStorageException;
import de.metanome.backend.results_db.FileInput;
import de.metanome.frontend.client.BasePage;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.TestHelper;
import de.metanome.frontend.client.helpers.InputValidationException;

import org.fusesource.restygwt.client.MethodCallback;

import java.util.ArrayList;

public class GwtTestFileInputTab extends GWTTestCase {

  /**
   * Test method for {@link de.metanome.frontend.client.datasources.FileInputTab#addFileInputToTable(de.metanome.backend.results_db.FileInput)}
   */
  public void testAddFileInputToTable() {
    //Setup
    FileInput fileInput = new FileInput();
    fileInput.setFileName("name");

    FileInputTab input = new FileInputTab(new DataSourcePage(new BasePage()));
    int rowCount = input.fileInputList.getRowCount();

    // Execute
    input.addFileInputToTable(fileInput);

    //Check
    assertEquals(rowCount + 1, input.fileInputList.getRowCount());
  }

  /**
   * Test method for {@link de.metanome.frontend.client.datasources.DatabaseConnectionTab#listDatabaseConnections(java.util.List)}
   */
  public void testListDatabaseConnections() {
    //Setup
    FileInput fileInput1 = new FileInput();
    fileInput1.setFileName("name");

    FileInput fileInput2 = new FileInput();
    fileInput2.setFileName("name");

    ArrayList<FileInput> inputs = new ArrayList<FileInput>();
    inputs.add(fileInput1);
    inputs.add(fileInput2);

    FileInputTab input = new FileInputTab(new DataSourcePage(new BasePage()));

    int rowCount = input.fileInputList.getRowCount();

    // Execute
    input.listFileInputs(inputs);

    //Check
    assertEquals(rowCount + 3, input.fileInputList.getRowCount());
  }

  /**
   * Test method for {@link de.metanome.frontend.client.datasources.FileInputTab#getDeleteCallback(de.metanome.backend.results_db.FileInput)}
   */
  public void testDeleteCallback() throws EntityStorageException, InputValidationException {
    // Setup
    TestHelper.resetDatabaseSync();

    FileInput input1 = new FileInput("File 1");
    FileInput input2 = new FileInput("File 2");

    BasePage parent = new BasePage();
    DataSourcePage page = new DataSourcePage(parent);
    FileInputTab fileInputTab = new FileInputTab(page);
    page.setMessageReceiver(new TabWrapper());

    fileInputTab.fileInputList.setWidget(0, 0, new HTML("File 1"));
    fileInputTab.fileInputList.setWidget(1, 0, new HTML("File 2"));
    fileInputTab.fileInputList.setWidget(2, 0, new HTML("File 3"));

    int rowCount = fileInputTab.fileInputList.getRowCount();

    // Execute (delete File 2)
    MethodCallback<Void>
        callback =
        fileInputTab.getDeleteCallback(input2);
    callback.onSuccess(null, null);

    // Check
    assertEquals(rowCount - 1, fileInputTab.fileInputList.getRowCount());
    assertEquals("File 3", ((HTML) fileInputTab.fileInputList.getWidget(1, 0)).getText());

    // Execute (delete File 1)
    callback = fileInputTab.getDeleteCallback(input1);
    callback.onSuccess(null, null);

    // Check
    assertEquals(rowCount - 2, fileInputTab.fileInputList.getRowCount());
    assertEquals("File 3", ((HTML) fileInputTab.fileInputList.getWidget(0, 0)).getText());

    // Cleanup
    TestHelper.resetDatabaseSync();
  }


  /**
   * Test method for {@link de.metanome.frontend.client.datasources.FileInputTab#updateFileInputInTable(de.metanome.backend.results_db.FileInput, de.metanome.backend.results_db.FileInput)}
   */
  public void testUpdateFileInput() {
    // Setup
    TestHelper.resetDatabaseSync();

    FileInput oldFileInput = new FileInput("old").setComment("comment");
    ArrayList<FileInput> inputs = new ArrayList<FileInput>();
    inputs.add(oldFileInput);

    BasePage parent = new BasePage();
    DataSourcePage page = new DataSourcePage(parent);
    FileInputTab fileInputTab = new FileInputTab(page);
    page.setMessageReceiver(new TabWrapper());
    fileInputTab.listFileInputs(inputs);

    // Expected Values
    String expectedValue = "updated";
    FileInput updatedFileInput = new FileInput(expectedValue)
        .setComment(expectedValue);

    // Execute
    fileInputTab.updateFileInputInTable(updatedFileInput, oldFileInput);

    // Check
    assertEquals(2, fileInputTab.fileInputList.getRowCount());
    assertTrue(((HTML) (fileInputTab.fileInputList.getWidget(1, 0))).getText().contains(expectedValue));
    assertTrue(fileInputTab.fileInputList.getText(1, 1).contains(expectedValue));

    // Clean up
    TestHelper.resetDatabaseSync();
  }


  @Override
  public String getModuleName() {
    return "de.metanome.frontend.client.MetanomeTest";
  }

}
