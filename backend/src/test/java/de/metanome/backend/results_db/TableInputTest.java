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
import de.metanome.backend.resources.TableInputResource;
import de.metanome.test_helper.EqualsAndHashCodeTester;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link de.metanome.backend.results_db.TableInput}
 *
 * @author Jakob Zwiener
 */
public class TableInputTest {

  private TableInputResource tableInputResource = new TableInputResource();

  /**
   * Test method for {@link de.metanome.backend.results_db.TableInput#equals(Object)} and {@link
   * de.metanome.backend.results_db.TableInput#hashCode()} <p/> Note that the equals and hashCode
   * methods are inherited from {@link de.metanome.backend.results_db.Input}.
   */
  @Test
  public void testEqualsAndHashCode() {
    // Setup
    long id = 42;
    TableInput tableInput = new TableInput("tableInput")
      .setId(id);
    TableInput equalTableInput = new TableInput("tableInput")
      .setId(id);
    TableInput notEqualTableInput = new TableInput("tableInput")
      .setId(23);

    // Execute functionality
    // Check result
    new EqualsAndHashCodeTester<TableInput>()
      .performBasicEqualsAndHashCodeChecks(tableInput, equalTableInput, notEqualTableInput);
  }

  @Test
  public void testGetIdentifier() {
    // Setup
    String tableName = "tableName";

    DatabaseConnection connection = new DatabaseConnection("url", "user", "pwd", DbSystem.DB2);

    String expectedIdentifier = "tableName; url; user; DB2";

    TableInput input = new TableInput("tableName", connection);

    // Execute
    String actualIdentifier = input.getIdentifier();

    // Check
    assertEquals(expectedIdentifier, actualIdentifier);
  }
}
