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
import de.metanome.backend.results_db.HibernateUtil;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


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
    DatabaseConnection expectedDb1 = new DatabaseConnection();
    expectedDb1.setUrl("url1");
    expectedDb1.setPassword("password1");
    expectedDb1.setUsername("db1");
    DatabaseConnection expectedDb2 = new DatabaseConnection();
    expectedDb2.setUrl("url2");
    expectedDb2.setPassword("password2");
    expectedDb2.setUsername("db2");

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

    DatabaseConnection expectedDbConnection = new DatabaseConnection();
    expectedDbConnection.setUrl("url1");
    expectedDbConnection.setPassword("password1");
    expectedDbConnection.setUsername("db1");
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
    DatabaseConnection expectedDbConnection = new DatabaseConnection();
    expectedDbConnection.setUrl("url1");
    expectedDbConnection.setPassword("password1");
    expectedDbConnection.setUsername("db1");

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
    DatabaseConnection expectedDbConnection = new DatabaseConnection();
    expectedDbConnection.setUrl("url1");
    expectedDbConnection.setPassword("password1");
    expectedDbConnection.setUsername("db1");

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
    DatabaseConnection databaseConnection = new DatabaseConnection()
        .setUsername("old user")
        .setPassword("old password");

    // Execute functionality
    DatabaseConnection actualDatabaseConnection = dbResource.store(databaseConnection);

    // Check result
    assertEquals(databaseConnection, actualDatabaseConnection);

    // Execute functionality
    databaseConnection.setComment("new comment").setPassword("new password").setUsername("new user");
    dbResource.update(databaseConnection);

    // Check result
    actualDatabaseConnection = dbResource.get(databaseConnection.getId());
    assertEquals(databaseConnection, actualDatabaseConnection);

    // Cleanup
    HibernateUtil.clear();
  }

}
