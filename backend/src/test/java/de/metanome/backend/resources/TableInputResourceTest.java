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

import de.metanome.algorithm_integration.configuration.DbSystem;
import de.metanome.backend.results_db.DatabaseConnection;
import de.metanome.backend.results_db.EntityStorageException;
import de.metanome.backend.results_db.HibernateUtil;
import de.metanome.backend.results_db.TableInput;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests for {@link de.metanome.backend.resources.TableInputResource}
 */
public class TableInputResourceTest {

  private TableInputResource tableInputResource = new TableInputResource();

  private DatabaseConnectionResource dbResource = new DatabaseConnectionResource();

  /**
   * Test method for {@link de.metanome.backend.resources.TableInputResource#store(TableInput)} and
   * {@link de.metanome.backend.resources.TableInputResource#get(long)} <p/> TableInputs should be
   * storable and retrievable by id.
   */
  @Test
  public void testPersistence() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    TableInput expectedTableInput = new TableInput("tableInput");
    String expectedTableName = "some table name";
    expectedTableInput.setTableName(expectedTableName);

    // Execute functionality
    assertSame(expectedTableInput, tableInputResource.store(expectedTableInput));
    long id = expectedTableInput.getId();
    TableInput actualTableInput = tableInputResource.get(id);
    String actualTableName = actualTableInput.getTableName();

    // Check result
    assertEquals(expectedTableInput, actualTableInput);
    assertEquals(expectedTableName, actualTableName);

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.resources.TableInputResource#getAll()}
   */
  @Test
  public void testRetrieveAll() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    TableInput expectedInput = new TableInput("tableInput");
    tableInputResource.store(expectedInput);

    // Execute functionality
    List<TableInput> actualInputs = tableInputResource.getAll();

    // Check result
    assertThat(actualInputs, IsIterableContainingInAnyOrder
      .containsInAnyOrder(expectedInput));

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.resources.TableInputResource#delete(long)}
   */
  @Test
  public void testDeleteTableInput() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    DatabaseConnection
      dbConnection =
      new DatabaseConnection("url1", "db1", "password1", DbSystem.DB2);
    dbResource.store(dbConnection);

    TableInput expectedTableInput = new TableInput("table1", dbConnection);
    tableInputResource.store(expectedTableInput);

    long id = expectedTableInput.getId();

    // Check precondition
    TableInput actualTableInput = tableInputResource.get(id);
    assertEquals(expectedTableInput, actualTableInput);

    // Execute functionality
    tableInputResource.delete(expectedTableInput.getId());

    // Check result
    assertNull(tableInputResource.get(id));

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.resources.TableInputResource#update(TableInput)}
   * TableInputs should be storable and updatable.
   */
  @Test
  public void testUpdate() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    TableInput tableInput = new TableInput("tableInput").setTableName("old name");

    // Execute functionality
    TableInput actualTableInput = tableInputResource.store(tableInput);

    // Check result
    assertEquals(tableInput, actualTableInput);

    // Execute functionality
    tableInput.setComment("new comment").setTableName("new name");
    tableInputResource.update(tableInput);

    // Check result
    actualTableInput = tableInputResource.get(tableInput.getId());
    assertEquals(tableInput, actualTableInput);

    // Cleanup
    HibernateUtil.clear();
  }

}
