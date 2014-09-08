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

package de.uni_potsdam.hpi.metanome.frontend.server;

import de.uni_potsdam.hpi.metanome.results_db.EntityStorageException;
import de.uni_potsdam.hpi.metanome.results_db.FileInput;
import de.uni_potsdam.hpi.metanome.results_db.HibernateUtil;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link de.uni_potsdam.hpi.metanome.frontend.server.FileInputServiceImpl}
 */
public class FileInputServiceImplTest {

  /**
   * Test method for {@link FileInputServiceImpl#listFileInputs()}
   */
  @Test
  public void testListFileInputs() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    FileInputServiceImpl fileInputService = new FileInputServiceImpl();

    // Expected values
    FileInput expectedFileInput1 = new FileInput();
    expectedFileInput1.setFileName("file1");

    FileInput expectedFileInput2 = new FileInput();
    expectedFileInput2.setFileName("file2");

    FileInput[] expectedFileInputs = {expectedFileInput1, expectedFileInput2};

    for (FileInput input : expectedFileInputs) {
      fileInputService.storeFileInput(input);
    }

    // Execute functionality
    List<FileInput> actualFileInputs = fileInputService.listFileInputs();

    // Check result
    assertThat(actualFileInputs,
               IsIterableContainingInAnyOrder.containsInAnyOrder(expectedFileInputs));

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.frontend.server.FileInputServiceImpl#getFileInput(long)}
   */
  @Test
  public void testGetFileInput() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    FileInputServiceImpl fileInputService = new FileInputServiceImpl();

    // Expected values
    FileInput expectedFileInput = new FileInput();
    expectedFileInput.setFileName("file");

    fileInputService.storeFileInput(expectedFileInput);

    long id = expectedFileInput.getId();

    // Execute functionality
    FileInput actualFileInput = fileInputService.getFileInput(id);

    // Check result
    assertEquals(expectedFileInput, actualFileInput);

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.frontend.server.FileInputServiceImpl#getFileInput(long)}
   */
  @Test
  public void testGetFileInputWithIncorrectId() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    FileInputServiceImpl fileInputService = new FileInputServiceImpl();

    // Execute functionality
    FileInput actualFileInput = fileInputService.getFileInput(2);

    // Check result
    assertEquals(null, actualFileInput);

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.frontend.server.FileInputServiceImpl#storeFileInput(de.uni_potsdam.hpi.metanome.results_db.FileInput)}
   */
  @Test
  public void testStoreFileInput() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    FileInputServiceImpl fileInputService = new FileInputServiceImpl();

    // Expected values
    FileInput expectedFileInput = new FileInput();
    expectedFileInput.setFileName("file1");

    // Execute functionality
    fileInputService.storeFileInput(expectedFileInput);

    // Finds algorithms of all or no interfaces
    List<FileInput> inputs = fileInputService.listFileInputs();

    // Check result
    assertTrue(inputs.contains(expectedFileInput));

    // Cleanup
    HibernateUtil.clear();
  }


  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.frontend.server.FileInputServiceImpl#deleteFileInput(de.uni_potsdam.hpi.metanome.results_db.FileInput)}
   */
  @Test
  public void testDeleteFileInput() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    FileInputServiceImpl service = new FileInputServiceImpl();

    FileInput expectedFileInput = new FileInput();
    expectedFileInput.setFileName("file1");
    expectedFileInput.store();

    long id = expectedFileInput.getId();

    // Check precondition
    FileInput actualFileInput = FileInput.retrieve(id);
    assertEquals(expectedFileInput, actualFileInput);

    // Execute functionality
    service.deleteFileInput(expectedFileInput);

    // Check result
    assertNull(FileInput.retrieve(id));

    // Cleanup
    HibernateUtil.clear();
  }

}
