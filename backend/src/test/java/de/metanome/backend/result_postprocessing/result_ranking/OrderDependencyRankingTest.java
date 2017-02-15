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
package de.metanome.backend.result_postprocessing.result_ranking;

import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.ColumnPermutation;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.input.RelationalInput;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.algorithm_integration.results.OrderDependency;
import de.metanome.backend.result_postprocessing.file_fixture.FileFixtureGeneral;
import de.metanome.backend.result_postprocessing.helper.TableInformation;
import de.metanome.backend.result_postprocessing.results.OrderDependencyResult;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OrderDependencyRankingTest {

  Map<String, TableInformation> tableInformationMap;
  List<OrderDependencyResult> orderDependencyResults;
  String tableName = FileFixtureGeneral.TABLE_NAME;

  @Before
  public void setUp() throws Exception {
    final FileFixtureGeneral fileFixture = new FileFixtureGeneral();
    RelationalInputGenerator relationalInputGenerator = new RelationalInputGenerator() {
      @Override
      public RelationalInput generateNewCopy() throws InputGenerationException {
        try {
          return fileFixture.getTestData();
        } catch (InputIterationException e) {
          return null;
        }
      }
	  @Override
	  public void close() throws Exception {}
    };

    TableInformation tableInformation = new TableInformation(relationalInputGenerator, false, new BitSet());
    tableInformationMap = new HashMap<>();
    tableInformationMap.put(tableName, tableInformation);

    ColumnPermutation lhs1 =
      new ColumnPermutation(
        new ColumnIdentifier(tableName, "column2"));
    ColumnPermutation rhs1 =
      new ColumnPermutation(new ColumnIdentifier(tableName, "column1"));
    OrderDependency od1 = new OrderDependency(lhs1, rhs1, OrderDependency.OrderType.LEXICOGRAPHICAL,
      OrderDependency.ComparisonOperator.SMALLER_EQUAL);
    OrderDependencyResult result1 = new OrderDependencyResult(od1);

    ColumnPermutation lhs2 =
      new ColumnPermutation(
        new ColumnIdentifier(tableName, "column3"));
    ColumnPermutation rhs2 =
      new ColumnPermutation(new ColumnIdentifier(tableName, "column1"));
    OrderDependency od2 = new OrderDependency(lhs2, rhs2, OrderDependency.OrderType.LEXICOGRAPHICAL,
      OrderDependency.ComparisonOperator.SMALLER_EQUAL);
    OrderDependencyResult result2 = new OrderDependencyResult(od2);

    orderDependencyResults = new ArrayList<>();
    orderDependencyResults.add(result1);
    orderDependencyResults.add(result2);
  }

  @Test
  public void testInitialization() throws Exception {
    // Execute functionality
    OrderDependencyRanking ranking = new OrderDependencyRanking(orderDependencyResults,
      tableInformationMap);

    // Check
    assertNotNull(ranking.tableInformationMap);
    assertNotNull(ranking.results);
  }

  @Test
  public void testCalculateColumnRatio() throws Exception {
    // Set up
    OrderDependencyRanking ranking = new OrderDependencyRanking(orderDependencyResults,
      tableInformationMap);
    OrderDependencyResult result = orderDependencyResults.get(0);

    // Execute Functionality
    ranking.calculateColumnRatios(result);

    // Check
    assertEquals(0.25, result.getLhsColumnRatio(), 0.0);
    assertEquals(0.25, result.getRhsColumnRatio(), 0.0);
  }

  @Test
  public void testCalculateCoverage() throws Exception {
    // Set up
    OrderDependencyRanking ranking = new OrderDependencyRanking(orderDependencyResults,
      tableInformationMap);
    OrderDependencyResult result = orderDependencyResults.get(0);

    // Execute Functionality
    ranking.calculateGeneralCoverage(result);

    // Check
    assertEquals(0.5, result.getGeneralCoverage(), 0.0);
  }

  @Test
  public void testCalculateOccurrenceRatio() throws Exception {
    // Set up
    OrderDependencyRanking ranking = new OrderDependencyRanking(orderDependencyResults,
      tableInformationMap);
    OrderDependencyResult result = orderDependencyResults.get(0);

    // Execute Functionality
    ranking.calculateOccurrenceRatios(result);

    // Check
    assertEquals(1.0, result.getLhsOccurrenceRatio(), 0.0);
    assertEquals(0.5, result.getRhsOccurrenceRatio(), 0.0);
  }

  @Test
  public void testCalculateUniquenessRatio() throws Exception {
    // Set up
    OrderDependencyRanking ranking = new OrderDependencyRanking(orderDependencyResults,
      tableInformationMap);
    OrderDependencyResult result = orderDependencyResults.get(1);

    // Execute Functionality
    ranking.calculateUniquenessRatios(result);

    // Check
    assertEquals(0.0, result.getLhsUniquenessRatio(), 0.0);
    assertEquals(1.0, result.getRhsUniquenessRatio(), 0.0);
  }


}
