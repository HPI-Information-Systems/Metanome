/*
 * Copyright 2015 by the Metanome project
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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConditionalUniqueColumnCombinationResultComparatorTest {

  ColumnIdentifier column1 = new ColumnIdentifier("table1", "column1");
  ColumnIdentifier column2 = new ColumnIdentifier("table1", "column2");
  ColumnIdentifier column3 = new ColumnIdentifier("table1", "column3");
  ColumnIdentifier column4 = new ColumnIdentifier("table1", "column4");

  ColumnConditionOr orCondition = new ColumnConditionOr(
      new ColumnConditionAnd(new ColumnConditionValue(column1, "condition1"),
                             new ColumnConditionValue(column2, "condition2")));
  ConditionalUniqueColumnCombination cucc1 = new ConditionalUniqueColumnCombination(
      new ColumnCombination(column1, column2), orCondition);

  ColumnConditionAnd andCondition = new ColumnConditionAnd(
      new ColumnConditionAnd(new ColumnConditionValue(column3, "condition3"),
                             new ColumnConditionValue(column4, "condition4")));
  ConditionalUniqueColumnCombination cucc2 = new ConditionalUniqueColumnCombination(
      new ColumnCombination(column3, column4), andCondition);

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

}
