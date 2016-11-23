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
package de.metanome.backend.result_postprocessing.result_comparator;

import de.metanome.algorithm_integration.ColumnCombination;
import de.metanome.algorithm_integration.ColumnConditionAnd;
import de.metanome.algorithm_integration.ColumnConditionOr;
import de.metanome.algorithm_integration.ColumnConditionValue;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.results.ConditionalUniqueColumnCombination;
import de.metanome.backend.result_postprocessing.results.ConditionalUniqueColumnCombinationResult;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConditionalUniqueColumnCombinationResultComparatorTest {

  ConditionalUniqueColumnCombinationResult cucc1;
  ConditionalUniqueColumnCombinationResult cucc2;

  @Before
  public void setUp() {
    ColumnIdentifier column1 = new ColumnIdentifier("table1", "column1");
    ColumnIdentifier column2 = new ColumnIdentifier("table1", "column2");
    ColumnIdentifier column3 = new ColumnIdentifier("table1", "column3");
    ColumnIdentifier column4 = new ColumnIdentifier("table1", "column4");

    ColumnConditionOr orCondition = new ColumnConditionOr(
        new ColumnConditionAnd(new ColumnConditionValue(column1, "condition1"),
                               new ColumnConditionValue(column2, "condition2")));
    ConditionalUniqueColumnCombination result1 = new ConditionalUniqueColumnCombination(
        new ColumnCombination(column1, column2), orCondition);
    cucc1 = new ConditionalUniqueColumnCombinationResult(result1);
    cucc1.setColumnRatio(3.5f);
    cucc1.setOccurrenceRatio(1.2f);
    cucc1.setUniquenessRatio(4.2f);

    ColumnConditionAnd andCondition = new ColumnConditionAnd(
        new ColumnConditionAnd(new ColumnConditionValue(column3, "condition3"),
                               new ColumnConditionValue(column4, "condition4")));
    ConditionalUniqueColumnCombination result2 = new ConditionalUniqueColumnCombination(
        new ColumnCombination(column3, column4), andCondition);
    cucc2 = new ConditionalUniqueColumnCombinationResult(result2);
    cucc2.setColumnRatio(1.3f);
    cucc2.setOccurrenceRatio(4.2f);
    cucc2.setUniquenessRatio(1.2f);
  }

  @Test
  public void compare1() {
    ConditionalUniqueColumnCombinationResultComparator resultComparator =
        new ConditionalUniqueColumnCombinationResultComparator(
            ConditionalUniqueColumnCombinationResultComparator.COLUMN_COMBINATION_COLUMN, true);
    assertEquals(-2, resultComparator.compare(cucc1, cucc2));
  }

  @Test
  public void compare2() {
    ConditionalUniqueColumnCombinationResultComparator resultComparator =
        new ConditionalUniqueColumnCombinationResultComparator(
            ConditionalUniqueColumnCombinationResultComparator.COLUMN_COMBINATION_COLUMN, false);
    assertEquals(2, resultComparator.compare(cucc1, cucc2));
  }

  @Test
  public void compare3() {
    ConditionalUniqueColumnCombinationResultComparator resultComparator =
        new ConditionalUniqueColumnCombinationResultComparator(
            ConditionalUniqueColumnCombinationResultComparator.CONDITION_COLUMN, true);
    assertEquals(-2, resultComparator.compare(cucc1, cucc2));
  }

  @Test
  public void compare4() {
    ConditionalUniqueColumnCombinationResultComparator resultComparator =
        new ConditionalUniqueColumnCombinationResultComparator(
            ConditionalUniqueColumnCombinationResultComparator.CONDITION_COLUMN, false);
    assertEquals(2, resultComparator.compare(cucc1, cucc2));
  }

  @Test
  public void compare7() {
    ConditionalUniqueColumnCombinationResultComparator resultComparator =
        new ConditionalUniqueColumnCombinationResultComparator(ConditionalUniqueColumnCombinationResultComparator.COLUMN_RATIO, true);
    assertTrue(resultComparator.compare(cucc1, cucc2) > 0);
  }

  @Test
  public void compare8() {
    ConditionalUniqueColumnCombinationResultComparator resultComparator =
        new ConditionalUniqueColumnCombinationResultComparator(ConditionalUniqueColumnCombinationResultComparator.COLUMN_RATIO, false);
    assertTrue(resultComparator.compare(cucc1, cucc2) < 0);
  }

  @Test
  public void compare9() {
    ConditionalUniqueColumnCombinationResultComparator resultComparator =
        new ConditionalUniqueColumnCombinationResultComparator(ConditionalUniqueColumnCombinationResultComparator.OCCURRENCE_RATIO, true);
    assertTrue(resultComparator.compare(cucc1, cucc2) < 0);
  }

  @Test
  public void compare10() {
    ConditionalUniqueColumnCombinationResultComparator resultComparator =
        new ConditionalUniqueColumnCombinationResultComparator(ConditionalUniqueColumnCombinationResultComparator.OCCURRENCE_RATIO, false);
    assertTrue(resultComparator.compare(cucc1, cucc2) > 0);
  }


  @Test
  public void compare11() {
    ConditionalUniqueColumnCombinationResultComparator resultComparator =
        new ConditionalUniqueColumnCombinationResultComparator(ConditionalUniqueColumnCombinationResultComparator.UNIQUENESS_RATIO, true);
    assertTrue(resultComparator.compare(cucc1, cucc2) > 0);
  }

  @Test
  public void compare12() {
    ConditionalUniqueColumnCombinationResultComparator resultComparator =
        new ConditionalUniqueColumnCombinationResultComparator(ConditionalUniqueColumnCombinationResultComparator.UNIQUENESS_RATIO, false);
    assertTrue(resultComparator.compare(cucc1, cucc2) < 0);
  }

}
