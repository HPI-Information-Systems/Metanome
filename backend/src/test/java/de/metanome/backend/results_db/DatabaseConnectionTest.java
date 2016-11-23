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
package de.metanome.backend.results_db;

import de.metanome.algorithm_integration.configuration.DbSystem;
import de.metanome.backend.resources.DatabaseConnectionResource;
import de.metanome.test_helper.EqualsAndHashCodeTester;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link de.metanome.backend.results_db.DatabaseConnection}
 *
 * @author Jakob Zwiener
 */
public class DatabaseConnectionTest {

  private final DatabaseConnectionResource dbResource = new DatabaseConnectionResource();

  /**
   * Test method for {@link de.metanome.backend.results_db.DatabaseConnection#equals(Object)} and
   * {@link DatabaseConnection#hashCode()}
   */
  @Test
  public void testEqualsAndHashCode() {
    // Setup
    long id = 42;
    DatabaseConnection databaseConnection = new DatabaseConnection("db")
      .setId(id);
    DatabaseConnection equalDatabaseConnection = new DatabaseConnection("db")
      .setId(id);
    DatabaseConnection notEqualDatabaseConnection = new DatabaseConnection("db")
      .setId(23);

    // Execute functionality
    // Check result
    new EqualsAndHashCodeTester<DatabaseConnection>()
      .performBasicEqualsAndHashCodeChecks(databaseConnection, equalDatabaseConnection,
        notEqualDatabaseConnection);
  }

  /**
   * Test method for {@link de.metanome.backend.results_db.DatabaseConnection#getId()}
   */
  @Test
  public void testGetId() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Expected values

    dbResource.store(new DatabaseConnection("db"));
    dbResource.store(new DatabaseConnection("db"));

    // Execute functionality
    List<DatabaseConnection> actualConnections = dbResource.getAll();

    long actualId1 = actualConnections.get(0).getId();
    long actualId2 = actualConnections.get(1).getId();

    // Check result
    assertEquals(Math.abs(actualId1 - actualId2), 1);

    // Cleanup
    HibernateUtil.clear();
  }

  @Test
  public void testGetIdentifier() {
    // Setup
    String expectedIdentifier = "url; user; DB2";

    DatabaseConnection connection = new DatabaseConnection("url", "user", "pwd", DbSystem.DB2);

    // Execute
    String actualIdentifier = connection.getIdentifier();

    // Check
    assertEquals(expectedIdentifier, actualIdentifier);
  }

}
