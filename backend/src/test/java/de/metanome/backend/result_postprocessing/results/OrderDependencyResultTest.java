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
import de.metanome.algorithm_integration.ColumnPermutation;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.results.OrderDependency;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class OrderDependencyResultTest {

  @Test
  public void testInitialization() throws InputGenerationException, InputIterationException {
    // Set up
    ColumnIdentifier column1 = new ColumnIdentifier("table2", "column1");
    ColumnIdentifier column2 = new ColumnIdentifier("table2", "column2");
    ColumnIdentifier column3 = new ColumnIdentifier("table1", "column3");
    ColumnPermutation lhs = new ColumnPermutation(column1, column2);
    ColumnPermutation rhs = new ColumnPermutation(column3);
    OrderDependency.OrderType orderType = OrderDependency.OrderType.LEXICOGRAPHICAL;
    OrderDependency.ComparisonOperator comparisonOperator = OrderDependency.ComparisonOperator.SMALLER_EQUAL;

    // Expected Values
    OrderDependency expectedResult = new OrderDependency(lhs, rhs, orderType, comparisonOperator);

    // Execute functionality
    OrderDependencyResult rankingResult = new OrderDependencyResult(expectedResult);

    // Check
    assertEquals(expectedResult, rankingResult.getResult());
    assertEquals("table2", rankingResult.getLhsTableName());
    assertEquals("table1", rankingResult.getRhsTableName());
  }

  @Test
  public void testEquals() throws InputGenerationException, InputIterationException {
    // Set up
    ColumnIdentifier column1 = new ColumnIdentifier("table2", "column1");
    ColumnIdentifier column2 = new ColumnIdentifier("table2", "column2");
    ColumnIdentifier column3 = new ColumnIdentifier("table1", "column3");
    ColumnPermutation lhs1 = new ColumnPermutation(column1, column2);
    ColumnPermutation lhs2 = new ColumnPermutation(column1);
    ColumnPermutation rhs1 = new ColumnPermutation(column3);
    ColumnPermutation rhs2 = new ColumnPermutation(column2, column3);
    OrderDependency.OrderType orderType = OrderDependency.OrderType.LEXICOGRAPHICAL;
    OrderDependency.ComparisonOperator comparisonOperator = OrderDependency.ComparisonOperator.SMALLER_EQUAL;

    // Expected Values
    OrderDependency result1 = new OrderDependency(lhs1, rhs1, orderType, comparisonOperator);
    OrderDependency result2 = new OrderDependency(lhs2, rhs2, orderType, comparisonOperator);

    // Execute functionality
    OrderDependencyResult rankingResult1 = new OrderDependencyResult(result1);
    OrderDependencyResult rankingResult2 = new OrderDependencyResult(result2);

    // Check
    assertEquals(rankingResult1, rankingResult1);
    assertNotEquals(rankingResult1, rankingResult2);
  }

}
