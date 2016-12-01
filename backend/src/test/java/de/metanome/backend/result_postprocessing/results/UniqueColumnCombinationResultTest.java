/**
 * Copyright 2015-2016 by Metanome Project
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
package de.metanome.backend.result_postprocessing.results;

import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.results.UniqueColumnCombination;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class UniqueColumnCombinationResultTest {

  @Test
  public void testInitialization() throws InputGenerationException, InputIterationException {
    // Set up
    ColumnIdentifier column1 = new ColumnIdentifier("table1", "column1");
    ColumnIdentifier column2 = new ColumnIdentifier("table1", "column2");

    // Expected Values
    UniqueColumnCombination expectedResult = new UniqueColumnCombination(column1, column2);

    // Execute functionality
    UniqueColumnCombinationResult rankingResult = new UniqueColumnCombinationResult(expectedResult);

    // Check
    assertEquals(expectedResult, rankingResult.getResult());
    assertEquals("table1", rankingResult.getTableName());
  }

  @Test
  public void testEquals() throws InputGenerationException, InputIterationException {
    // Set up
    ColumnIdentifier column1 = new ColumnIdentifier("table1", "column1");
    ColumnIdentifier column2 = new ColumnIdentifier("table1", "column2");

    // Expected Values
    UniqueColumnCombination result1 = new UniqueColumnCombination(column1, column2);
    UniqueColumnCombination result2 = new UniqueColumnCombination(column1);

    // Execute functionality
    UniqueColumnCombinationResult rankingResult1 = new UniqueColumnCombinationResult(result1);
    UniqueColumnCombinationResult rankingResult2 = new UniqueColumnCombinationResult(result2);

    // Check
    assertEquals(rankingResult1, rankingResult1);
    assertNotEquals(rankingResult1, rankingResult2);
  }

}
