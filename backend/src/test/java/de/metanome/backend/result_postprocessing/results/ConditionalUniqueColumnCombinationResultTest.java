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

import de.metanome.algorithm_integration.*;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.results.ConditionalUniqueColumnCombination;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ConditionalUniqueColumnCombinationResultTest {

  @Test
  public void testInitialization() throws InputGenerationException, InputIterationException {
    // Set up
    ColumnIdentifier column1 = new ColumnIdentifier("table1", "column3");
    ColumnIdentifier column2 = new ColumnIdentifier("table1", "column4");

    ColumnConditionOr orCondition = new ColumnConditionOr(
      new ColumnConditionAnd(new ColumnConditionValue(column1, "condition1"),
        new ColumnConditionValue(column2, "condition2")));

    // Expected Values
    ConditionalUniqueColumnCombination expectedResult = new ConditionalUniqueColumnCombination(
      new ColumnCombination(column1, column2), orCondition
    );

    // Execute functionality
    ConditionalUniqueColumnCombinationResult rankingResult = new ConditionalUniqueColumnCombinationResult(expectedResult);

    // Check
    assertEquals(expectedResult, rankingResult.getResult());
    assertEquals("table1", rankingResult.getTableName());
  }

  @Test
  public void testEquals() throws InputGenerationException, InputIterationException {
    // Set up
    ColumnIdentifier column1 = new ColumnIdentifier("table1", "column3");
    ColumnIdentifier column2 = new ColumnIdentifier("table1", "column4");
    ColumnConditionOr orCondition = new ColumnConditionOr(
      new ColumnConditionAnd(new ColumnConditionValue(column1, "condition1"),
        new ColumnConditionValue(column2, "condition2")));

    // Expected Values
    ConditionalUniqueColumnCombination result1 = new ConditionalUniqueColumnCombination(
      new ColumnCombination(column1), orCondition
    );
    ConditionalUniqueColumnCombination result2 = new ConditionalUniqueColumnCombination(
      new ColumnCombination(column1, column2), orCondition
    );

    // Execute functionality
    ConditionalUniqueColumnCombinationResult rankingResult1 = new ConditionalUniqueColumnCombinationResult(result1);
    ConditionalUniqueColumnCombinationResult rankingResult2 = new ConditionalUniqueColumnCombinationResult(result2);

    // Check
    assertEquals(rankingResult1, rankingResult1);
    assertNotEquals(rankingResult1, rankingResult2);
  }


}
