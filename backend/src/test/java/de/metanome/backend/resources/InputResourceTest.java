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

package de.metanome.backend.resources;

import de.metanome.backend.results_db.DatabaseConnection;
import de.metanome.backend.results_db.EntityStorageException;
import de.metanome.backend.results_db.FileInput;
import de.metanome.backend.results_db.HibernateUtil;
import de.metanome.backend.results_db.Input;
import de.metanome.backend.results_db.TableInput;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link de.metanome.backend.resources.InputResource}
 */
public class InputResourceTest {

  private InputResource inputResource = new InputResource();
  private TableInputResource tableInputResource = new TableInputResource();
  private FileInputResource fileInputResource = new FileInputResource();
  private DatabaseConnectionResource databaseConnectionResource = new DatabaseConnectionResource();

  /**
   * Test method for {@link de.metanome.backend.resources.InputResource#getAll()}
   */
  @Test
  public void testRetrieveAll() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    Input expectedInput = inputResource.store(new Input());

    FileInput expectedFileInput = fileInputResource.store(new FileInput());

    TableInput expectedTableInput = tableInputResource.store(new TableInput());

    // Execute functionality
    List<Input> actualInputs = inputResource.getAll();

    // Check result
    assertThat(actualInputs, IsIterableContainingInAnyOrder
        .containsInAnyOrder(expectedInput, expectedFileInput, expectedTableInput));

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.resources.InputResource#store(Input)}
   * and {@link de.metanome.backend.resources.InputResource#get(long)}
   * <p/> Inputs should be storable and retrievable by id.
   */
  @Test
  public void testPersistence() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    Input expectedInput = new Input();

    // Execute functionality
    assertSame(expectedInput, inputResource.store(expectedInput));
    long id = expectedInput.getId();
    Input actualInput = inputResource.get(id);

    // Check result
    assertEquals(expectedInput, actualInput);

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.resources.InputResource#delete(long)}
   */
  @Test
  public void testDeleteInput() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    Input expectedInput = new Input();
    inputResource.store(expectedInput);

    long id = expectedInput.getId();

    // Check precondition
    Input actualInput = inputResource.get(id);
    assertEquals(expectedInput, actualInput);

    // Execute functionality
    inputResource.delete(expectedInput.getId());

    // Check result
    assertNull(inputResource.get(id));

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link InputResource#listRelationalInputs()}
   */
  @Test
  public void testListRelationalInputs() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    Input expectedInput = inputResource.store(new Input());
    FileInput expectedFileInput = fileInputResource.store(new FileInput());
    TableInput expectedTableInput = tableInputResource.store(new TableInput());
    DatabaseConnection expectedDatabaseConnection = databaseConnectionResource.store(new DatabaseConnection());

    // Execute functionality
    List<Input> actualInputs = inputResource.listRelationalInputs();

    // Check result
    assertThat(actualInputs, IsIterableContainingInAnyOrder
        .containsInAnyOrder(expectedFileInput, expectedTableInput));

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.resources.InputResource#update(de.metanome.backend.results_db.Input)}
   * Inputs should be storable and updatable.
   */
  @Test
  public void testUpdate() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    Input tableInput = new TableInput().setTableName("old name");

    // Execute functionality
    Input actualTableInput = inputResource.store(tableInput);

    // Check result
    assertEquals(tableInput, actualTableInput);

    // Execute functionality
    ((TableInput) tableInput).setComment("new comment").setTableName("new name");
    inputResource.update(tableInput);

    // Check result
    actualTableInput = inputResource.get(tableInput.getId());
    assertEquals(tableInput, actualTableInput);

    // Cleanup
    HibernateUtil.clear();
  }

}
