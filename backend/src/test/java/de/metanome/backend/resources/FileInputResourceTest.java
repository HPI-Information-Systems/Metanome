/**
 * Copyright 2014-2016 by Metanome Project
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
package de.metanome.backend.resources;

import de.metanome.backend.results_db.EntityStorageException;
import de.metanome.backend.results_db.FileInput;
import de.metanome.backend.results_db.HibernateUtil;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests for {@link de.metanome.backend.resources.FileInputResource}
 */
public class FileInputResourceTest {

  /**
   * Test method for {@link de.metanome.backend.resources.FileInputResource#getAll()}
   */
  @Test
  public void testListFileInputs() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    FileInputResource fileInputResource = new FileInputResource();

    // Expected values
    FileInput expectedFileInput1 = new FileInput("file");
    expectedFileInput1.setFileName("file1");

    FileInput expectedFileInput2 = new FileInput("file");
    expectedFileInput2.setFileName("file2");

    FileInput[] expectedFileInputs = {expectedFileInput1, expectedFileInput2};

    for (FileInput input : expectedFileInputs) {
      fileInputResource.store(input);
    }

    // Execute functionality
    List<FileInput> actualFileInputs = fileInputResource.getAll();

    // Check result
    assertThat(actualFileInputs,
      IsIterableContainingInAnyOrder.containsInAnyOrder(expectedFileInputs));

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.resources.FileInputResource#get(long)}
   */
  @Test
  public void testGetFileInput() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    FileInputResource fileInputResource = new FileInputResource();

    // Expected values
    FileInput expectedFileInput = new FileInput("file");
    expectedFileInput.setFileName("file");

    fileInputResource.store(expectedFileInput);

    long id = expectedFileInput.getId();

    // Execute functionality
    FileInput actualFileInput = fileInputResource.get(id);

    // Check result
    assertEquals(expectedFileInput, actualFileInput);

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.resources.FileInputResource#get(long)}
   */
  @Test
  public void testGetFileInputWithIncorrectId() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    FileInputResource fileInputResource = new FileInputResource();

    // Execute functionality
    FileInput actualFileInput = fileInputResource.get(2);

    // Check result
    assertEquals(null, actualFileInput);

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.resources.FileInputResource#store(de.metanome.backend.results_db.FileInput)}
   */
  @Test
  public void testStoreFileInput() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    FileInputResource fileInputResource = new FileInputResource();

    // Expected values
    FileInput expectedFileInput = new FileInput("file");
    expectedFileInput.setFileName("file1");

    // Execute functionality
    expectedFileInput = fileInputResource.store(expectedFileInput);

    // Finds algorithms of all or no interfaces
    List<FileInput> inputs = fileInputResource.getAll();

    // Check result
    assertTrue(inputs.contains(expectedFileInput));
    assertTrue(expectedFileInput.getId() > 0);

    // Cleanup
    HibernateUtil.clear();
  }


  /**
   * Test method for {@link de.metanome.backend.resources.FileInputResource#delete(long)}
   */
  @Test
  public void testDeleteFileInput() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    FileInputResource fileInputResource = new FileInputResource();

    FileInput expectedFileInput = new FileInput("file");
    expectedFileInput.setFileName("file1");
    fileInputResource.store(expectedFileInput);

    long id = expectedFileInput.getId();

    // Check precondition
    FileInput actualFileInput = fileInputResource.get(id);
    assertEquals(expectedFileInput, actualFileInput);

    // Execute functionality
    fileInputResource.delete(expectedFileInput.getId());

    // Check result
    assertNull(fileInputResource.get(id));

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.resources.FileInputResource#update(de.metanome.backend.results_db.FileInput)}
   * FileInputs should be storable and updatable.
   */
  @Test
  public void testUpdate() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();
    FileInputResource fileInputResource = new FileInputResource();

    // Expected values
    FileInput fileInput = new FileInput("old file");

    // Execute functionality
    FileInput actualFileInput = fileInputResource.store(fileInput);

    // Check result
    assertEquals(fileInput, actualFileInput);

    // Execute functionality
    fileInput.setComment("new comment").setFileName("new file");
    fileInputResource.update(fileInput);

    // Check result
    actualFileInput = fileInputResource.get(fileInput.getId());
    assertEquals(fileInput, actualFileInput);

    // Cleanup
    HibernateUtil.clear();
  }

}

