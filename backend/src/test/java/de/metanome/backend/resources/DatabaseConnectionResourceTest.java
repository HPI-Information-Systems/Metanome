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
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;


/**
 * Tests for {@link de.metanome.backend.resources.DatabaseConnectionResource}
 */
public class DatabaseConnectionResourceTest {

  private final DatabaseConnectionResource dbResource = new DatabaseConnectionResource();

  /**
   * Test method for {@link de.metanome.backend.resources.DatabaseConnectionResource#getAll()}
   */
  @Test
  public void testListDatabaseConnections() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    DatabaseConnection
      expectedDb1 =
      new DatabaseConnection("url1", "password1", "db1", DbSystem.DB2);
    DatabaseConnection
      expectedDb2 =
      new DatabaseConnection("url2", "password2", "db2", DbSystem.DB2);

    DatabaseConnection[] expectedDbConnections = {expectedDb1, expectedDb2};

    for (DatabaseConnection db : expectedDbConnections) {
      dbResource.store(db);
    }

    // Execute functionality
    List<DatabaseConnection> actualDbConnections = dbResource.getAll();

    // Check result
    assertThat(actualDbConnections,
      IsIterableContainingInAnyOrder.containsInAnyOrder(expectedDbConnections));

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.resources.DatabaseConnectionResource#delete(long)}
   */
  @Test
  public void testDeleteDatabaseConnection() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    DatabaseConnection
      expectedDbConnection =
      new DatabaseConnection("url1", "password1", "db1", DbSystem.DB2);
    dbResource.store(expectedDbConnection);

    long id = expectedDbConnection.getId();

    // Check precondition
    DatabaseConnection actualDatabaseConnection = dbResource.get(id);
    assertEquals(expectedDbConnection, actualDatabaseConnection);

    // Execute functionality
    dbResource.delete(id);

    // Check result
    assertNull(dbResource.get(id));

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.resources.DatabaseConnectionResource#get(long)}
   */
  @Test
  public void testGetDatabaseConnection() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    DatabaseConnection
      expectedDbConnection =
      new DatabaseConnection("url1", "password1", "db1", DbSystem.DB2);

    dbResource.store(expectedDbConnection);

    long expectedId = expectedDbConnection.getId();

    // Execute functionality
    DatabaseConnection actualDbConnection = dbResource.get(expectedId);

    // Check result
    assertEquals(expectedDbConnection, actualDbConnection);

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.resources.DatabaseConnectionResource#get(long)}
   */
  @Test
  public void testGetDatabaseConnectionWithIncorrectId() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Execute functionality
    DatabaseConnection actualDbConnection = dbResource.get(2);

    // Check result
    assertEquals(null, actualDbConnection);

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.resources.DatabaseConnectionResource#store(DatabaseConnection)}
   */
  @Test
  public void testStoreDatabaseConnection() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    DatabaseConnection
      expectedDbConnection =
      new DatabaseConnection("url1", "password1", "db1", DbSystem.DB2);

    // Execute functionality
    dbResource.store(expectedDbConnection);
    List<DatabaseConnection> connections = dbResource.getAll();

    // Check result
    assertTrue(connections.contains(expectedDbConnection));

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.resources.DatabaseConnectionResource#update(de.metanome.backend.results_db.DatabaseConnection)}
   * Database Connection should be storable and updatable.
   */
  @Test
  public void testUpdate() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    DatabaseConnection
      databaseConnection =
      new DatabaseConnection("url1", "old user", "old password", DbSystem.DB2);

    // Execute functionality
    DatabaseConnection actualDatabaseConnection = dbResource.store(databaseConnection);

    // Check result
    assertEquals(databaseConnection, actualDatabaseConnection);

    // Execute functionality
    databaseConnection.setComment("new comment").setPassword("new password")
      .setUsername("new user");
    dbResource.update(databaseConnection);

    // Check result
    actualDatabaseConnection = dbResource.get(databaseConnection.getId());
    assertEquals(databaseConnection, actualDatabaseConnection);

    // Cleanup
    HibernateUtil.clear();
  }

}
