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
import de.metanome.algorithm_integration.ColumnPermutation;
import de.metanome.algorithm_integration.results.OrderDependency;
import de.metanome.backend.result_postprocessing.results.OrderDependencyResult;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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

    ColumnPermutation lhs2 =
        new ColumnPermutation(new ColumnIdentifier("table1", "column2"), new ColumnIdentifier(
            "table1", "column3"));
    ColumnPermutation rhs2 =
        new ColumnPermutation(new ColumnIdentifier("table1", "column6"));
    OrderDependency result2 = new OrderDependency(lhs2, rhs2, OrderDependency.OrderType.LEXICOGRAPHICAL,
                                                  OrderDependency.ComparisonOperator.SMALLER_EQUAL);
    od2 = new OrderDependencyResult(result2);
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


}
