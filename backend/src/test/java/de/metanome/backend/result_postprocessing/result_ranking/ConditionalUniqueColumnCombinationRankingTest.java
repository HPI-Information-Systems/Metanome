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

import de.metanome.algorithm_integration.*;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.input.RelationalInput;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.algorithm_integration.results.ConditionalUniqueColumnCombination;
import de.metanome.backend.result_postprocessing.file_fixture.FileFixtureGeneral;
import de.metanome.backend.result_postprocessing.helper.TableInformation;
import de.metanome.backend.result_postprocessing.results.ConditionalUniqueColumnCombinationResult;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ConditionalUniqueColumnCombinationRankingTest {
  Map<String, TableInformation> tableInformationMap;
  List<ConditionalUniqueColumnCombinationResult> results;
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

    ColumnIdentifier column1 = new ColumnIdentifier(tableName, "column1");
    ColumnIdentifier column2 = new ColumnIdentifier(tableName, "column2");
    ColumnIdentifier column3 = new ColumnIdentifier(tableName, "column3");
    ColumnIdentifier column4 = new ColumnIdentifier(tableName, "column4");

    ColumnConditionOr orCondition = new ColumnConditionOr(
      new ColumnConditionAnd(new ColumnConditionValue(column1, "condition1"),
        new ColumnConditionValue(column2, "condition2")));
    ConditionalUniqueColumnCombination cucc1 = new ConditionalUniqueColumnCombination(
      new ColumnCombination(column1, column2), orCondition);
    ConditionalUniqueColumnCombinationResult result1 = new ConditionalUniqueColumnCombinationResult(cucc1);

    ColumnConditionAnd andCondition = new ColumnConditionAnd(
      new ColumnConditionAnd(new ColumnConditionValue(column3, "condition3"),
        new ColumnConditionValue(column4, "condition4")));
    ConditionalUniqueColumnCombination cucc2 = new ConditionalUniqueColumnCombination(
      new ColumnCombination(column3, column4), andCondition);
    ConditionalUniqueColumnCombinationResult result2 = new ConditionalUniqueColumnCombinationResult(cucc2);

    results = new ArrayList<>();
    results.add(result1);
    results.add(result2);
  }

  @Test
  public void testInitialization() throws Exception {
    // Execute functionality
    ConditionalUniqueColumnCombinationRanking ranking = new ConditionalUniqueColumnCombinationRanking(
      results, tableInformationMap);

    // Check
    assertNotNull(ranking.tableInformationMap);
    assertNotNull(ranking.results);
  }

  @Test
  public void testCalculateColumnRatio() throws Exception {
    // Set up
    ConditionalUniqueColumnCombinationRanking ranking = new ConditionalUniqueColumnCombinationRanking(
      results, tableInformationMap);
    ConditionalUniqueColumnCombinationResult result = results.get(0);

    // Execute Functionality
    ranking.calculateColumnRatio(result);

    // Check
    assertEquals(0.5, result.getColumnRatio(), 0.0);
  }

  @Test
  public void testCalculateOccurrenceRatio() throws Exception {
    // Set up
    ConditionalUniqueColumnCombinationRanking ranking = new ConditionalUniqueColumnCombinationRanking(
      results, tableInformationMap);
    ConditionalUniqueColumnCombinationResult result = results.get(0);

    // Execute Functionality
    ranking.calculateOccurrenceRatio(result);

    // Check
    assertEquals(1.0, result.getOccurrenceRatio(), 0.0);
  }

  @Test
  public void testCalculateUniquenessRatio() throws Exception {
    // Set up
    ConditionalUniqueColumnCombinationRanking ranking = new ConditionalUniqueColumnCombinationRanking(
      results, tableInformationMap);
    ConditionalUniqueColumnCombinationResult result = results.get(0);

    // Execute Functionality
    ranking.calculateUniquenessRatio(result);

    // Check
    assertEquals(0.5, result.getUniquenessRatio(), 0.0);
  }

}
