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

package de.uni_potsdam.hpi.metanome.results_db;

import de.uni_potsdam.hpi.metanome.test_helper.EqualsAndHashCodeTester;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * Tests for {@link de.uni_potsdam.hpi.metanome.results_db.DatabaseConnection}
 *
 * @author Jakob Zwiener
 */
public class DatabaseConnectionTest {

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.results_db.DatabaseConnection#store()} and
   * {@link de.uni_potsdam.hpi.metanome.results_db.DatabaseConnection#retrieve(long)} <p/>
   * DatabaseConnections should be storable and retrievable by id.
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
   * Test method for {@link DatabaseConnection#delete()}
   *
   * DatabaseConnections should be deletable. After deletion they should no longer be retrievable.
   */
  @Test
  public void testDelete() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    DatabaseConnection expectedDatabaseConnection = new DatabaseConnection()
        .store();
    long id = expectedDatabaseConnection.getId();

    // Check precondition
    DatabaseConnection actualDatabaseConnection = DatabaseConnection.retrieve(id);
    assertEquals(expectedDatabaseConnection, actualDatabaseConnection);

    // Execute functionality
    expectedDatabaseConnection.delete();

    // Check result
    assertNull(DatabaseConnection.retrieve(id));

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link DatabaseConnection#delete()}
   *
   * Calling delete on an DatabaseConnection that has not yet been stored should be successful with
   * no result.
   */
  @Test
  public void testDeleteNotStored() {
    // Setup
    HibernateUtil.clear();

    // Expected values
    DatabaseConnection expectedDatabaseConnection = new DatabaseConnection();

    // Execute functionality
    expectedDatabaseConnection.delete();

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.results_db.DatabaseConnection#equals(Object)}
   * and {@link DatabaseConnection#hashCode()}
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
}
