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

import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.results.BasicStatistic;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BasicStatisticResultComparatorTest {

  String name1 = "Min";
  String value1 = "minValue";
  ColumnIdentifier column1 = new ColumnIdentifier("table1", "column2");
  BasicStatistic statistic1 = new BasicStatistic(name1, value1, column1);

  String name2 = "Max";
  String value2 = "maxValue";
  ColumnIdentifier column2 = new ColumnIdentifier("table1", "column3");
  BasicStatistic statistic2 = new BasicStatistic(name2, value2, column2);

  @Test
  public void compare1() {
    BasicStatisticResultComparator resultComparator =
        new BasicStatisticResultComparator(BasicStatisticResultComparator.COLUMN_COMBINATION_COLUMN, true);
    assertEquals(-1, resultComparator.compare(statistic1, statistic2));
  }

  @Test
  public void compare2() {
    BasicStatisticResultComparator resultComparator =
        new BasicStatisticResultComparator(BasicStatisticResultComparator.COLUMN_COMBINATION_COLUMN, false);
    assertEquals(1, resultComparator.compare(statistic1, statistic2));
  }

  @Test
  public void compare3() {
    BasicStatisticResultComparator resultComparator =
        new BasicStatisticResultComparator(BasicStatisticResultComparator.NAME_COLUMN, true);
    assertTrue(resultComparator.compare(statistic1, statistic2) > 0);
  }

  @Test
  public void compare4() {
    BasicStatisticResultComparator resultComparator =
        new BasicStatisticResultComparator(BasicStatisticResultComparator.NAME_COLUMN, false);
    assertTrue(resultComparator.compare(statistic1, statistic2) < 0);
  }

  @Test
  public void compare5() {
    BasicStatisticResultComparator resultComparator =
        new BasicStatisticResultComparator(BasicStatisticResultComparator.VALUE_COLUMN, true);
    assertTrue(resultComparator.compare(statistic1, statistic2) > 0);
  }

  @Test
  public void compare6() {
    BasicStatisticResultComparator resultComparator =
        new BasicStatisticResultComparator(BasicStatisticResultComparator.VALUE_COLUMN, false);
    assertTrue(resultComparator.compare(statistic1, statistic2) < 0);
  }



}
