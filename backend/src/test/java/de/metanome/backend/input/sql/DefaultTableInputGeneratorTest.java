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

package de.metanome.backend.input.sql;

import de.metanome.algorithm_integration.input.InputGenerationException;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link de.metanome.backend.input.sql.DefaultTableInputGenerator}
 *
 * @author Jakob Zwiener
 */
public class DefaultTableInputGeneratorTest {

  /**
   * Test method for {@link DefaultTableInputGenerator#generateNewCopy()}
   *
   * The table input generator should call the underlying {@link DefaultDatabaseConnectionGenerator}
   * to execute a select table for the given table.
   */
  @Test
  public void testGenerateNewCopy() throws InputGenerationException {
    // Setup
    // Expected values
    DefaultDatabaseConnectionGenerator
        defaultDatabaseConnectionGenerator = mock(DefaultDatabaseConnectionGenerator.class);
    String expectedTable = "some table";
    DefaultTableInputGenerator tableInputGenerator =
        new DefaultTableInputGenerator(defaultDatabaseConnectionGenerator, expectedTable);

    // Execute functionality
    tableInputGenerator.generateNewCopy();

    // Check result
    verify(defaultDatabaseConnectionGenerator)
        .generateRelationalInputFromSql(String.format(DefaultTableInputGenerator.BASE_STATEMENT,expectedTable));
  }
}
