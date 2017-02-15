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
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.input.RelationalInput;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.algorithm_integration.results.UniqueColumnCombination;
import de.metanome.backend.result_postprocessing.file_fixture.FileFixtureGeneral;
import de.metanome.backend.result_postprocessing.helper.TableInformation;
import de.metanome.backend.result_postprocessing.results.UniqueColumnCombinationResult;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UniqueColumnCombinationRankingTest {

  Map<String, TableInformation> tableInformationMap;
  List<UniqueColumnCombinationResult> uniqueColumnCombinationResults;
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
    ColumnIdentifier column4 = new ColumnIdentifier(tableName, "column4");

    UniqueColumnCombination ucc1 = new UniqueColumnCombination(column1);
    UniqueColumnCombination ucc2 = new UniqueColumnCombination(column2, column4);

    UniqueColumnCombinationResult result1 = new UniqueColumnCombinationResult(ucc1);
    UniqueColumnCombinationResult result2 = new UniqueColumnCombinationResult(ucc2);

    uniqueColumnCombinationResults = new ArrayList<>();
    uniqueColumnCombinationResults.add(result1);
    uniqueColumnCombinationResults.add(result2);
  }

  @Test
  public void testInitialization() throws Exception {
    // Execute functionality
    UniqueColumnCombinationRanking ranking = new UniqueColumnCombinationRanking(
      uniqueColumnCombinationResults, tableInformationMap);

    // Check
    assertNotNull(ranking.tableInformationMap);
    assertNotNull(ranking.results);
    assertNotNull(ranking.occurrenceMap);

    assertEquals(1, (int) ranking.occurrenceMap.get(tableName).get("column1"));
    assertEquals(1, (int) ranking.occurrenceMap.get(tableName).get("column2"));
    assertEquals(0, (int) ranking.occurrenceMap.get(tableName).get("column3"));
    assertEquals(1, (int) ranking.occurrenceMap.get(tableName).get("column4"));
  }

  @Test
  public void testCalculateColumnRatio() throws Exception {
    // Set up
    UniqueColumnCombinationRanking ranking = new UniqueColumnCombinationRanking(
      uniqueColumnCombinationResults, tableInformationMap);
    UniqueColumnCombinationResult result = uniqueColumnCombinationResults.get(1);

    // Execute Functionality
    ranking.calculateColumnRatio(result);

    // Check
    assertEquals(0.5, result.getColumnRatio(), 0.0);
  }

  @Test
  public void testCalculateOccurrenceRatio() throws Exception {
    // Set up
    UniqueColumnCombinationRanking ranking = new UniqueColumnCombinationRanking(
      uniqueColumnCombinationResults, tableInformationMap);
    UniqueColumnCombinationResult result = uniqueColumnCombinationResults.get(1);

    // Execute Functionality
    ranking.calculateOccurrenceRatio(result);

    // Check
    assertEquals(1.0, result.getOccurrenceRatio(), 0.0);
  }

  @Test
  public void testCalculateUniquenessRatio() throws Exception {
    // Set up
    UniqueColumnCombinationRanking ranking = new UniqueColumnCombinationRanking(
      uniqueColumnCombinationResults, tableInformationMap);
    UniqueColumnCombinationResult result = uniqueColumnCombinationResults.get(1);

    // Execute Functionality
    ranking.calculateUniquenessRatio(result);

    // Check
    assertEquals(0.0, result.getUniquenessRatio(), 0.0);
  }

  @Test
  public void testCalculateRandomness() throws Exception {
    // Set up
    UniqueColumnCombinationRanking ranking = new UniqueColumnCombinationRanking(
      uniqueColumnCombinationResults, tableInformationMap);
    UniqueColumnCombinationResult result = uniqueColumnCombinationResults.get(1);

    // Execute Functionality
    ranking.calculateRandomness(result);

    // Check
    assertEquals(-0.66, result.getRandomness(), 0.01);


    result = uniqueColumnCombinationResults.get(0);

    // Execute Functionality
    ranking.calculateRandomness(result);

    // Check
    assertEquals(0.0, result.getRandomness(), 0.00);
  }


}
