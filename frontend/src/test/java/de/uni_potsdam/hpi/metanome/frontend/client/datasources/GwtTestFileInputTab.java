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

package de.uni_potsdam.hpi.metanome.frontend.client.datasources;

import com.google.gwt.junit.client.GWTTestCase;

import de.metanome.backend.results_db.FileInput;
import de.uni_potsdam.hpi.metanome.frontend.client.BasePage;

import java.util.ArrayList;

public class GwtTestFileInputTab extends GWTTestCase {

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.frontend.client.datasources.FileInputTab#addFileInputToTable(de.metanome.backend.results_db.FileInput)}
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
   * Test method for {@link de.uni_potsdam.hpi.metanome.frontend.client.datasources.DatabaseConnectionTab#listDatabaseConnections(java.util.List)}
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


  @Override
  public String getModuleName() {
    return "de.uni_potsdam.hpi.metanome.frontend.client.MetanomeTest";
  }

}
