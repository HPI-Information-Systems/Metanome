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
import de.metanome.algorithm_integration.results.BasicStatistic;
import de.metanome.algorithm_integration.results.basic_statistic_values.BasicStatisticValueString;
import de.metanome.backend.result_postprocessing.results.BasicStatisticResult;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BasicStatisticResultComparatorTest {

  BasicStatisticResult statistic1;
  BasicStatisticResult statistic2;

  @Before
  public void setUp() {
    String name1 = "Min";
    BasicStatisticValueString value1 = new BasicStatisticValueString("minValue");
    String name2 = "Max";
    BasicStatisticValueString value2 = new BasicStatisticValueString("maxValue");

    ColumnIdentifier column1 = new ColumnIdentifier("table1", "column2");
    BasicStatistic result1 = new BasicStatistic(column1);
    result1.addStatistic(name1, value1);
    result1.addStatistic(name2, value1);
    statistic1 = new BasicStatisticResult(result1);
    statistic1.setColumnRatio(3.4f);
    statistic1.setOccurrenceRatio(1.2f);
    statistic1.setUniquenessRatio(3.2f);

    ColumnIdentifier column2 = new ColumnIdentifier("table1", "column3");
    BasicStatistic result2 = new BasicStatistic(column2);
    result2.addStatistic(name2, value2);
    statistic2 = new BasicStatisticResult(result2);
    statistic2.setColumnRatio(1.3f);
    statistic2.setOccurrenceRatio(4.2f);
    statistic2.setUniquenessRatio(1.2f);
  }

  @Test
  public void compare1() {
    BasicStatisticResultComparator resultComparator =
      new BasicStatisticResultComparator(BasicStatisticResultComparator.COLUMN_COMBINATION_COLUMN,
        true);
    assertEquals(-1, resultComparator.compare(statistic1, statistic2));
  }

  @Test
  public void compare2() {
    BasicStatisticResultComparator resultComparator =
      new BasicStatisticResultComparator(BasicStatisticResultComparator.COLUMN_COMBINATION_COLUMN,
        false);
    assertEquals(1, resultComparator.compare(statistic1, statistic2));
  }

  @Test
  public void compare3() {
    BasicStatisticResultComparator resultComparator =
      new BasicStatisticResultComparator("Min", true);
    assertTrue(resultComparator.compare(statistic1, statistic2) < 0);
  }

  @Test
  public void compare4() {
    BasicStatisticResultComparator resultComparator =
      new BasicStatisticResultComparator("Min", false);
    assertTrue(resultComparator.compare(statistic1, statistic2) > 0);
  }

  @Test
  public void compare5() {
    BasicStatisticResultComparator resultComparator =
      new BasicStatisticResultComparator("Max", true);
    assertTrue(resultComparator.compare(statistic1, statistic2) < 0);
  }

  @Test
  public void compare6() {
    BasicStatisticResultComparator resultComparator =
      new BasicStatisticResultComparator("Max", false);
    assertTrue(resultComparator.compare(statistic1, statistic2) > 0);
  }

  @Test
  public void compare7() {
    BasicStatisticResultComparator resultComparator =
      new BasicStatisticResultComparator(BasicStatisticResultComparator.COLUMN_RATIO, true);
    assertTrue(resultComparator.compare(statistic1, statistic2) > 0);
  }

  @Test
  public void compare8() {
    BasicStatisticResultComparator resultComparator =
      new BasicStatisticResultComparator(BasicStatisticResultComparator.COLUMN_RATIO, false);
    assertTrue(resultComparator.compare(statistic1, statistic2) < 0);
  }

  @Test
  public void compare9() {
    BasicStatisticResultComparator resultComparator =
      new BasicStatisticResultComparator(BasicStatisticResultComparator.OCCURRENCE_RATIO, true);
    assertTrue(resultComparator.compare(statistic1, statistic2) < 0);
  }

  @Test
  public void compare10() {
    BasicStatisticResultComparator resultComparator =
      new BasicStatisticResultComparator(BasicStatisticResultComparator.OCCURRENCE_RATIO, false);
    assertTrue(resultComparator.compare(statistic1, statistic2) > 0);
  }

  @Test
  public void compare11() {
    BasicStatisticResultComparator resultComparator =
      new BasicStatisticResultComparator(BasicStatisticResultComparator.UNIQUENESS_RATIO, true);
    assertTrue(resultComparator.compare(statistic1, statistic2) > 0);
  }

  @Test
  public void compare12() {
    BasicStatisticResultComparator resultComparator =
      new BasicStatisticResultComparator(BasicStatisticResultComparator.UNIQUENESS_RATIO, false);
    assertTrue(resultComparator.compare(statistic1, statistic2) < 0);
  }


}
