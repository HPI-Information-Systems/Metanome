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

import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.results.UniqueColumnCombination;
import de.metanome.backend.result_postprocessing.results.UniqueColumnCombinationResult;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UniqueColumnCombinationResultComparatorTest {

  UniqueColumnCombinationResult ucc1;
  UniqueColumnCombinationResult ucc2;

  @Before
  public void setUp() {
    ColumnIdentifier column1 = new ColumnIdentifier("table1", "column1");
    ColumnIdentifier column2 = new ColumnIdentifier("table1", "column2");
    ColumnIdentifier column3 = new ColumnIdentifier("table1", "column3");
    ColumnIdentifier column4 = new ColumnIdentifier("table1", "column4");

    UniqueColumnCombination result1 = new UniqueColumnCombination(column1, column2, column2);
    UniqueColumnCombination result2 = new UniqueColumnCombination(column3, column4, column2);

    ucc1 = new UniqueColumnCombinationResult(result1);
    ucc1.setColumnRatio(3.2f);
    ucc1.setOccurrenceRatio(1.2f);
    ucc1.setUniquenessRatio(1.2f);
    ucc1.setRandomness(3.2f);
    ucc2 = new UniqueColumnCombinationResult(result2);
    ucc2.setColumnRatio(1.3f);
    ucc2.setOccurrenceRatio(5.3f);
    ucc2.setUniquenessRatio(4.2f);
    ucc1.setRandomness(-4.2f);
  }

  @Test
  public void compare1() {
    UniqueColumnCombinationResultComparator resultComparator =
        new UniqueColumnCombinationResultComparator(
            UniqueColumnCombinationResultComparator.COLUMN_COMBINATION_COLUMN, true);
    assertEquals(-1, resultComparator.compare(ucc1, ucc2));
  }

  @Test
  public void compare2() {
    UniqueColumnCombinationResultComparator resultComparator =
        new UniqueColumnCombinationResultComparator(
            UniqueColumnCombinationResultComparator.COLUMN_COMBINATION_COLUMN, false);
    assertEquals(1, resultComparator.compare(ucc1, ucc2));
  }

  @Test
  public void compare3() {
    UniqueColumnCombinationResultComparator resultComparator =
        new UniqueColumnCombinationResultComparator(
            UniqueColumnCombinationResultComparator.COLUMN_RATIO, true);
    assertTrue(resultComparator.compare(ucc1, ucc2) > 0);
  }

  @Test
  public void compare4() {
    UniqueColumnCombinationResultComparator resultComparator =
        new UniqueColumnCombinationResultComparator(
            UniqueColumnCombinationResultComparator.COLUMN_RATIO, false);
    assertTrue(resultComparator.compare(ucc1, ucc2) < 0);
  }

  @Test
  public void compare5() {
    UniqueColumnCombinationResultComparator resultComparator =
        new UniqueColumnCombinationResultComparator(
            UniqueColumnCombinationResultComparator.OCCURRENCE_RATIO, true);
    assertTrue(resultComparator.compare(ucc1, ucc2) < 0);
  }

  @Test
  public void compare6() {
    UniqueColumnCombinationResultComparator resultComparator =
        new UniqueColumnCombinationResultComparator(
            UniqueColumnCombinationResultComparator.OCCURRENCE_RATIO, false);
    assertTrue(resultComparator.compare(ucc1, ucc2) > 0);
  }

  @Test
  public void compare7() {
    UniqueColumnCombinationResultComparator resultComparator =
        new UniqueColumnCombinationResultComparator(
            UniqueColumnCombinationResultComparator.UNIQUENESS_RATIO, true);
    assertTrue(resultComparator.compare(ucc1, ucc2) < 0);
  }

  @Test
  public void compare8() {
    UniqueColumnCombinationResultComparator resultComparator =
        new UniqueColumnCombinationResultComparator(
            UniqueColumnCombinationResultComparator.UNIQUENESS_RATIO, false);
    assertTrue(resultComparator.compare(ucc1, ucc2) > 0);
  }

  @Test
  public void compare9() {
    UniqueColumnCombinationResultComparator resultComparator =
        new UniqueColumnCombinationResultComparator(
            UniqueColumnCombinationResultComparator.RANDOMNESS, true);
    assertTrue(resultComparator.compare(ucc1, ucc2) < 0);
  }

  @Test
  public void compare10() {
    UniqueColumnCombinationResultComparator resultComparator =
        new UniqueColumnCombinationResultComparator(
            UniqueColumnCombinationResultComparator.RANDOMNESS, false);
    assertTrue(resultComparator.compare(ucc1, ucc2) > 0);
  }

}
