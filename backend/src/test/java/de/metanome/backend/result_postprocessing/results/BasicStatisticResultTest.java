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
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.results.BasicStatistic;
import de.metanome.algorithm_integration.results.basic_statistic_values.BasicStatisticValueString;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class BasicStatisticResultTest {

  @Test
  public void testInitialization() throws InputGenerationException, InputIterationException {
    // Set up
    BasicStatisticValueString statisticValue = new BasicStatisticValueString("value");
    String statisticName = "name";
    ColumnIdentifier columnIdentifier1 = new ColumnIdentifier("table", "column1");
    ColumnIdentifier columnIdentifier2 = new ColumnIdentifier("table", "column2");

    // Expected Values
    BasicStatistic expectedResult = new BasicStatistic(
      columnIdentifier1,
      columnIdentifier2);
    expectedResult.addStatistic(statisticName, statisticValue);

    // Execute functionality
    BasicStatisticResult rankingResult = new BasicStatisticResult(expectedResult);

    // Check
    assertEquals(expectedResult, rankingResult.getResult());
    assertEquals("table", rankingResult.getTableName());
  }

  @Test
  public void testEquals() throws InputGenerationException, InputIterationException {
    // Set up
    BasicStatisticValueString statisticValue = new BasicStatisticValueString("value");
    String statisticName = "name";
    ColumnIdentifier columnIdentifier1 = new ColumnIdentifier("table", "column1");
    ColumnIdentifier columnIdentifier2 = new ColumnIdentifier("table", "column2");

    // Expected Values
    BasicStatistic result1 = new BasicStatistic(
      columnIdentifier1);
    result1.addStatistic(statisticName, statisticValue);
    BasicStatistic result2 = new BasicStatistic(
      columnIdentifier2);
    result2.addStatistic(statisticName, statisticValue);

    // Execute functionality
    BasicStatisticResult rankingResult1 = new BasicStatisticResult(result1);
    BasicStatisticResult rankingResult2 = new BasicStatisticResult(result2);

    // Check
    assertEquals(rankingResult1, rankingResult1);
    assertNotEquals(rankingResult1, rankingResult2);
  }

}
