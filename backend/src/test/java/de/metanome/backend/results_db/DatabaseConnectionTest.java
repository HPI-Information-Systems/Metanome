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

package de.metanome.backend.results_db;

import de.metanome.test_helper.EqualsAndHashCodeTester;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link de.metanome.backend.results_db.DatabaseConnection}
 *
 * @author Jakob Zwiener
 */
public class DatabaseConnectionTest {

  /**
   * Test method for {@link de.metanome.backend.results_db.DatabaseConnection#store()} and {@link
   * de.metanome.backend.results_db.DatabaseConnection#retrieve(long)} <p/> DatabaseConnections
   * should be storable and retrievable by id.
   */
  @Test
  public void testPersistence() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    DatabaseConnection expectedDatabaseConnection = new DatabaseConnection();

    // Execute functionality
    assertSame(expectedDatabaseConnection, expectedDatabaseConnection.store());
    long id = expectedDatabaseConnection.getId();
    DatabaseConnection actualDatabaseConnection = DatabaseConnection.retrieve(id);

    // Check result
    assertEquals(expectedDatabaseConnection, actualDatabaseConnection);

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.results_db.DatabaseConnection#equals(Object)} and
   * {@link DatabaseConnection#hashCode()}
   */
  @Test
  public void testEqualsAndHashCode() {
    // Setup
    long id = 42;
    DatabaseConnection databaseConnection = new DatabaseConnection()
        .setId(id);
    DatabaseConnection equalDatabaseConnection = new DatabaseConnection()
        .setId(id);
    DatabaseConnection notEqualDatabaseConnection = new DatabaseConnection()
        .setId(23);

    // Execute functionality
    // Check result
    new EqualsAndHashCodeTester<DatabaseConnection>()
        .performBasicEqualsAndHashCodeChecks(databaseConnection, equalDatabaseConnection,
                                             notEqualDatabaseConnection);
  }

  /**
   * Test method for {@link de.metanome.backend.results_db.DatabaseConnection#retrieveAll()}
   */
  @Test
  public void testRetrieveAll() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    DatabaseConnection expectedConnection = new DatabaseConnection()
        .store();

    // Execute functionality
    List<DatabaseConnection> actualConnections = DatabaseConnection.retrieveAll();

    // Check result
    assertThat(actualConnections, IsIterableContainingInAnyOrder
        .containsInAnyOrder(expectedConnection));

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link DatabaseConnection#getId()}
   */
  @Test
  public void testGetId() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Expected values

    new DatabaseConnection().store();
    new DatabaseConnection().store();

    // Execute functionality
    List<DatabaseConnection> actualConnections = DatabaseConnection.retrieveAll();

    long actualId1 = actualConnections.get(0).getId();
    long actualId2 = actualConnections.get(1).getId();

    // Check result
    assertEquals(Math.abs(actualId1 - actualId2), 1);

    // Cleanup
    HibernateUtil.clear();
  }


}
