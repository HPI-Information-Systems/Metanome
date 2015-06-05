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

package de.metanome.backend.result_postprocessing.result_ranking;

import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.ColumnPermutation;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.input.RelationalInput;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.backend.result_postprocessing.helper.TableInformation;
import de.metanome.backend.result_postprocessing.results.InclusionDependencyResult;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class InclusionDependencyRankingTest {

  Map<String, TableInformation> tableInformationMap;
  List<InclusionDependencyResult> inclusionDependencyResults;
  String tableName = InclusionDependencyFileFixture.TABLE_NAME;

  @Before
  public void setUp() throws Exception {
    final InclusionDependencyFileFixture fileFixture = new InclusionDependencyFileFixture();
    RelationalInputGenerator relationalInputGenerator = new RelationalInputGenerator() {
      @Override
      public RelationalInput generateNewCopy() throws InputGenerationException {
        try {
          return fileFixture.getTestData();
        } catch (InputIterationException e) {
          return null;
        }
      }
    };

    TableInformation tableInformation = new TableInformation(relationalInputGenerator, false);
    tableInformationMap = new HashMap<>();
    tableInformationMap.put(tableName, tableInformation);

    InclusionDependencyResult result1 = new InclusionDependencyResult();
    result1.setDependant(new ColumnPermutation(new ColumnIdentifier(tableName, "column2")));
    result1.setReferenced(new ColumnPermutation(new ColumnIdentifier(tableName, "column1")));
    result1.setDependantTableName(tableName);
    result1.setReferencedTableName(tableName);
    InclusionDependencyResult result2 = new InclusionDependencyResult();
    result2.setDependant(new ColumnPermutation(new ColumnIdentifier(tableName, "column3")));
    result2.setReferenced(new ColumnPermutation(new ColumnIdentifier(tableName, "column1")));
    result2.setDependantTableName(tableName);
    result2.setReferencedTableName(tableName);
    InclusionDependencyResult result3 = new InclusionDependencyResult();
    result3.setDependant(new ColumnPermutation(new ColumnIdentifier(tableName, "column3")));
    result3.setReferenced(new ColumnPermutation(new ColumnIdentifier(tableName, "column2")));
    result3.setDependantTableName(tableName);
    result3.setReferencedTableName(tableName);

    inclusionDependencyResults = new ArrayList<>();
    inclusionDependencyResults.add(result1);
    inclusionDependencyResults.add(result2);
    inclusionDependencyResults.add(result3);
  }

  @Test
  public void testInitialization() throws Exception {
    // Execute functionality
    InclusionDependencyRanking ranking = new InclusionDependencyRanking(inclusionDependencyResults,
                                                                        tableInformationMap);

    // Check
    assertNotNull(ranking.tableInformationMap);
    assertNotNull(ranking.inclusionDependencyResults);
    assertNotNull(ranking.occurrenceMap);

    assertEquals(2, (int) ranking.occurrenceMap.get(tableName).get("column1"));
    assertEquals(2, (int) ranking.occurrenceMap.get(tableName).get("column2"));
    assertEquals(2, (int) ranking.occurrenceMap.get(tableName).get("column3"));
    assertEquals(0, (int) ranking.occurrenceMap.get(tableName).get("column4"));
  }

  @Test
  public void testCalculateColumnCountOccurrenceRatios() throws Exception {
    // Set up
    InclusionDependencyRanking ranking = new InclusionDependencyRanking(inclusionDependencyResults,
                                                                        tableInformationMap);
    InclusionDependencyResult result = inclusionDependencyResults.get(0);

    // Execute Functionality
    ranking.calculateOccurrenceRatios(result);

    // Check
    assertEquals(0.5, result.getDependantOccurrenceRatio(), 0.0);
    assertEquals(0.5, result.getReferencedOccurrenceRatio(), 0.0);
  }

  @Test
  public void testCalculateSizeRatios() throws Exception {
    // Set up
    InclusionDependencyRanking ranking = new InclusionDependencyRanking(inclusionDependencyResults,
                                                                        tableInformationMap);
    InclusionDependencyResult result = inclusionDependencyResults.get(1);

    // Execute Functionality
    ranking.calculateColumnRatios(result);

    // Check
    assertEquals(0.25, result.getDependantColumnRatio(), 0.0);
    assertEquals(0.25, result.getReferencedColumnRatio(), 0.0);
  }

  @Test
  public void testCalculateUniquenessRatios() throws Exception {
    // Set up
    InclusionDependencyRanking ranking = new InclusionDependencyRanking(inclusionDependencyResults,
                                                                        tableInformationMap);
    InclusionDependencyResult result = inclusionDependencyResults.get(2);

    // Execute Functionality
    ranking.calculateUniquenessRatios(result);

    // Check
    assertEquals(0.0, result.getDependantUniquenessRatio(), 0.0);
    assertEquals(0.0, result.getReferencedUniquenessRatio(), 0.0);
  }

}
