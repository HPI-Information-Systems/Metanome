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
import de.metanome.algorithm_integration.ColumnPermutation;
import de.metanome.algorithm_integration.results.OrderDependency;
import de.metanome.backend.result_postprocessing.results.OrderDependencyResult;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OrderDependencyResultComparatorTest {

  OrderDependencyResult od1;
  OrderDependencyResult od2;

  @Before
  public void setUp() {
    ColumnPermutation lhs1 =
        new ColumnPermutation(new ColumnIdentifier("table1", "column1"), new ColumnIdentifier(
            "table1", "column2"));
    ColumnPermutation rhs1 =
        new ColumnPermutation(new ColumnIdentifier("table1", "column7"));
    OrderDependency result1 = new OrderDependency(lhs1, rhs1, OrderDependency.OrderType.LEXICOGRAPHICAL,
                                                  OrderDependency.ComparisonOperator.SMALLER_EQUAL);
    od1 = new OrderDependencyResult(result1);
    od1.setLhsColumnRatio(4.3f);
    od1.setRhsColumnRatio(1.3f);
    od1.setGeneralCoverage(2.4f);
    od1.setLhsOccurrenceRatio(1.2f);
    od1.setRhsOccurrenceRatio(4.3f);
    od1.setLhsUniquenessRatio(2.1f);
    od1.setRhsUniquenessRatio(1.1f);

    ColumnPermutation lhs2 =
        new ColumnPermutation(new ColumnIdentifier("table1", "column2"), new ColumnIdentifier(
            "table1", "column3"));
    ColumnPermutation rhs2 =
        new ColumnPermutation(new ColumnIdentifier("table1", "column6"));
    OrderDependency result2 = new OrderDependency(lhs2, rhs2, OrderDependency.OrderType.LEXICOGRAPHICAL,
                                                  OrderDependency.ComparisonOperator.SMALLER_EQUAL);
    od2 = new OrderDependencyResult(result2);
    od2.setLhsColumnRatio(2.3f);
    od2.setRhsColumnRatio(5.2f);
    od2.setGeneralCoverage(2.4f);
    od2.setLhsOccurrenceRatio(1.2f);
    od2.setRhsOccurrenceRatio(3.3f);
    od2.setLhsUniquenessRatio(2.1f);
    od2.setRhsUniquenessRatio(3.2f);
  }

  @Test
  public void compare1() {
    OrderDependencyResultComparator resultComparator =
        new OrderDependencyResultComparator(OrderDependencyResultComparator.LHS_COLUMN, true);
    assertEquals(-1, resultComparator.compare(od1, od2));
  }

  @Test
  public void compare2() {
    OrderDependencyResultComparator resultComparator =
        new OrderDependencyResultComparator(OrderDependencyResultComparator.LHS_COLUMN, false);
    assertEquals(1, resultComparator.compare(od1, od2));
  }

  @Test
  public void compare3() {
    OrderDependencyResultComparator resultComparator =
        new OrderDependencyResultComparator(OrderDependencyResultComparator.RHS_COLUMN, true);
    assertEquals(1, resultComparator.compare(od1, od2));
  }

  @Test
  public void compare4() {
    OrderDependencyResultComparator resultComparator =
        new OrderDependencyResultComparator(OrderDependencyResultComparator.RHS_COLUMN, false);
    assertEquals(-1, resultComparator.compare(od1, od2));
  }

  @Test
  public void compare5() {
    OrderDependencyResultComparator resultComparator =
        new OrderDependencyResultComparator(OrderDependencyResultComparator.LHS_COLUMN_RATIO, true);
    assertTrue(resultComparator.compare(od1, od2) > 0);
  }

  @Test
  public void compare6() {
    OrderDependencyResultComparator resultComparator =
        new OrderDependencyResultComparator(OrderDependencyResultComparator.LHS_COLUMN_RATIO, false);
    assertTrue(resultComparator.compare(od1, od2) < 0);
  }

  @Test
  public void compare7() {
    OrderDependencyResultComparator resultComparator =
        new OrderDependencyResultComparator(OrderDependencyResultComparator.RHS_COLUMN_RATIO, true);
    assertTrue(resultComparator.compare(od1, od2) < 0);
  }

  @Test
  public void compare8() {
    OrderDependencyResultComparator resultComparator =
        new OrderDependencyResultComparator(OrderDependencyResultComparator.RHS_COLUMN_RATIO, false);
    assertTrue(resultComparator.compare(od1, od2) > 0);
  }

  @Test
  public void compare9() {
    OrderDependencyResultComparator resultComparator =
        new OrderDependencyResultComparator(OrderDependencyResultComparator.COVERAGE, true);
    assertEquals(0, resultComparator.compare(od1, od2));
  }

  @Test
  public void compare10() {
    OrderDependencyResultComparator resultComparator =
        new OrderDependencyResultComparator(OrderDependencyResultComparator.COVERAGE, false);
    assertEquals(0, resultComparator.compare(od1, od2));
  }

  @Test
  public void compare11() {
    OrderDependencyResultComparator resultComparator =
        new OrderDependencyResultComparator(OrderDependencyResultComparator.LHS_OCCURRENCE_RATIO, true);
    assertTrue(resultComparator.compare(od1, od2) == 0);
  }

  @Test
  public void compare12() {
    OrderDependencyResultComparator resultComparator =
        new OrderDependencyResultComparator(OrderDependencyResultComparator.LHS_OCCURRENCE_RATIO, false);
    assertTrue(resultComparator.compare(od1, od2) == 0);
  }

  @Test
  public void compare13() {
    OrderDependencyResultComparator resultComparator =
        new OrderDependencyResultComparator(OrderDependencyResultComparator.RHS_OCCURRENCE_RATIO, true);
    assertTrue(resultComparator.compare(od1, od2) > 0);
  }

  @Test
  public void compare14() {
    OrderDependencyResultComparator resultComparator =
        new OrderDependencyResultComparator(OrderDependencyResultComparator.RHS_OCCURRENCE_RATIO, false);
    assertTrue(resultComparator.compare(od1, od2) < 0);
  }

  @Test
  public void compare15() {
    OrderDependencyResultComparator resultComparator =
        new OrderDependencyResultComparator(OrderDependencyResultComparator.LHS_UNIQUENESS_RATIO, true);
    assertTrue(resultComparator.compare(od1, od2) == 0);
  }

  @Test
  public void compare16() {
    OrderDependencyResultComparator resultComparator =
        new OrderDependencyResultComparator(OrderDependencyResultComparator.LHS_UNIQUENESS_RATIO, false);
    assertTrue(resultComparator.compare(od1, od2) == 0);
  }

  @Test
  public void compare17() {
    OrderDependencyResultComparator resultComparator =
        new OrderDependencyResultComparator(OrderDependencyResultComparator.RHS_UNIQUENESS_RATIO, true);
    assertTrue(resultComparator.compare(od1, od2) < 0);
  }

  @Test
  public void compare18() {
    OrderDependencyResultComparator resultComparator =
        new OrderDependencyResultComparator(OrderDependencyResultComparator.RHS_UNIQUENESS_RATIO, false);
    assertTrue(resultComparator.compare(od1, od2) > 0);
  }

}
