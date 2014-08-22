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

import de.uni_potsdam.hpi.metanome.results_db.DatabaseConnection;
import de.uni_potsdam.hpi.metanome.results_db.EntityStorageException;
import de.uni_potsdam.hpi.metanome.results_db.HibernateUtil;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link de.uni_potsdam.hpi.metanome.frontend.server.DatabaseConnectionServiceImpl}
 */
public class DatabaseConnectionServiceImplTest {

  /**
   * Test method for {@link DatabaseConnectionServiceImpl#listDatabaseConnections()}
   */
  @Test
  public void testListDatabaseConnections() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    DatabaseConnectionServiceImpl dbService = new DatabaseConnectionServiceImpl();

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
      dbService.storeDatabaseConnection(db);
    }

    // Execute functionality
    List<DatabaseConnection> actualDbConnections = dbService.listDatabaseConnections();

    // Check result
    assertThat(actualDbConnections,
               IsIterableContainingInAnyOrder.containsInAnyOrder(expectedDbConnections));

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.frontend.server.DatabaseConnectionServiceImpl#deleteDatabaseConnection(de.uni_potsdam.hpi.metanome.results_db.DatabaseConnection)}
   */
  @Test
  public void testDeleteDatabaseConnection() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    DatabaseConnectionServiceImpl service = new DatabaseConnectionServiceImpl();

    DatabaseConnection expectedDbConnection = new DatabaseConnection();
    expectedDbConnection.setUrl("url1");
    expectedDbConnection.setPassword("password1");
    expectedDbConnection.setUsername("db1");
    expectedDbConnection.store();

    long id = expectedDbConnection.getId();

    // Check precondition
    DatabaseConnection actualDatabaseConnection = DatabaseConnection.retrieve(id);
    assertEquals(expectedDbConnection, actualDatabaseConnection);

    // Execute functionality
    service.deleteDatabaseConnection(expectedDbConnection);

    // Check result
    assertNull(DatabaseConnection.retrieve(id));

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link DatabaseConnectionServiceImpl#getDatabaseConnection(long)}
   */
  @Test
  public void testGetDatabaseConnection() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    DatabaseConnectionServiceImpl dbService = new DatabaseConnectionServiceImpl();

    // Expected values
    DatabaseConnection expectedDbConnection = new DatabaseConnection();
    expectedDbConnection.setUrl("url1");
    expectedDbConnection.setPassword("password1");
    expectedDbConnection.setUsername("db1");

    dbService.storeDatabaseConnection(expectedDbConnection);

    long expectedId = expectedDbConnection.getId();

    // Execute functionality
    DatabaseConnection actualDbConnection = dbService.getDatabaseConnection(expectedId);

    // Check result
    assertEquals(expectedDbConnection, actualDbConnection);

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link DatabaseConnectionServiceImpl#getDatabaseConnection(long)}
   */
  @Test
  public void testGetDatabaseConnectionWithIncorrectId() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    DatabaseConnectionServiceImpl dbService = new DatabaseConnectionServiceImpl();

    // Execute functionality
    DatabaseConnection actualDbConnection = dbService.getDatabaseConnection(2);

    // Check result
    assertEquals(null, actualDbConnection);

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link DatabaseConnectionServiceImpl#storeDatabaseConnection(DatabaseConnection)}
   */
  @Test
  public void testStoreDatabaseConnection() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    DatabaseConnectionServiceImpl dbService = new DatabaseConnectionServiceImpl();

    // Expected values
    DatabaseConnection expectedDbConnection = new DatabaseConnection();
    expectedDbConnection.setUrl("url1");
    expectedDbConnection.setPassword("password1");
    expectedDbConnection.setUsername("db1");

    // Execute functionality
    dbService.storeDatabaseConnection(expectedDbConnection);
    List<DatabaseConnection> connections = dbService.listDatabaseConnections();

    // Check result
    assertTrue(connections.contains(expectedDbConnection));

    // Cleanup
    HibernateUtil.clear();
  }

}
