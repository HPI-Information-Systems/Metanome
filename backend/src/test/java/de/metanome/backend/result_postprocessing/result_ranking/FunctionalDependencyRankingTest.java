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

import de.metanome.algorithm_integration.ColumnCombination;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.input.RelationalInput;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.algorithm_integration.results.FunctionalDependency;
import de.metanome.backend.result_postprocessing.FileFixtureFunctionalDependency;
import de.metanome.backend.result_postprocessing.helper.TableInformation;
import de.metanome.backend.result_postprocessing.result_analyzer.FunctionalDependencyResultAnalyzer;
import de.metanome.backend.result_postprocessing.results.FunctionalDependencyResult;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FunctionalDependencyRankingTest {

  Map<String, TableInformation> tableInformationMap;
  List<FunctionalDependencyResult> functionalDependencyResults;
  String tableName = FileFixtureFunctionalDependency.TABLE_NAME;

  @Before
  public void setUp() throws Exception {
    final FileFixtureFunctionalDependency fileFixture = new FileFixtureFunctionalDependency();
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

    TableInformation tableInformation = new TableInformation(relationalInputGenerator, true, new BitSet());
    tableInformationMap = new HashMap<>();
    tableInformationMap.put(tableName, tableInformation);

    FunctionalDependency fd1 = new FunctionalDependency(
        new ColumnCombination(
            new ColumnIdentifier(tableName, "A"), new ColumnIdentifier(tableName, "B")),
        new ColumnIdentifier(tableName, "C"));
    FunctionalDependency fd2 = new FunctionalDependency(
        new ColumnCombination(
            new ColumnIdentifier(tableName, "B")),
        new ColumnIdentifier(tableName, "D"));
    FunctionalDependency fd3 = new FunctionalDependency(
        new ColumnCombination(
            new ColumnIdentifier(tableName, "A"), new ColumnIdentifier(tableName, "B")),
        new ColumnIdentifier(tableName, "E"));

    List<FunctionalDependency> fds = new ArrayList<>();
    fds.add(fd1);
    fds.add(fd2);
    fds.add(fd3);
    List<RelationalInputGenerator> generators = new ArrayList<>();
    generators.add(relationalInputGenerator);

    FunctionalDependencyResultAnalyzer analyzer = new FunctionalDependencyResultAnalyzer(
        generators, true);
    this.functionalDependencyResults = analyzer.convertResults(fds);
    this.functionalDependencyResults = analyzer.extendDependantSide(this.functionalDependencyResults);
  }

  @Test
  public void testInitialization() throws Exception {
    // Execute functionality
    FunctionalDependencyRanking ranking = new FunctionalDependencyRanking(functionalDependencyResults,
                                                                          tableInformationMap);

    // Check
    assertNotNull(ranking.tableInformationMap);
    assertNotNull(ranking.results);
  }

  @Test
  public void testCalculateColumnRatios() throws Exception {
    // Set up
    FunctionalDependencyRanking ranking = new FunctionalDependencyRanking(functionalDependencyResults,
                                                                          tableInformationMap);
    FunctionalDependencyResult result = functionalDependencyResults.get(0);

    // Execute Functionality
    ranking.calculateColumnRatios(result);

    // Check
    assertEquals(0.4, result.getDeterminantColumnRatio(), 0.001);
    assertEquals(0.6, result.getDependantColumnRatio(), 0.001);
  }
}
