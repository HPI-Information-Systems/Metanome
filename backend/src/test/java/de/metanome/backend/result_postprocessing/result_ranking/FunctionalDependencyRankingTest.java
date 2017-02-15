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

import de.metanome.algorithm_helper.data_structures.PositionListIndex;
import de.metanome.algorithm_integration.ColumnCombination;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.input.RelationalInput;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.algorithm_integration.results.FunctionalDependency;
import de.metanome.backend.result_postprocessing.file_fixture.FileFixtureFunctionalDependency;
import de.metanome.backend.result_postprocessing.file_fixture.FileFixtureInformationGain;
import de.metanome.backend.result_postprocessing.file_fixture.FileFixturePollution;
import de.metanome.backend.result_postprocessing.helper.TableInformation;
import de.metanome.backend.result_postprocessing.result_analyzer.FunctionalDependencyResultAnalyzer;
import de.metanome.backend.result_postprocessing.results.FunctionalDependencyResult;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class FunctionalDependencyRankingTest {

  Map<String, TableInformation> tableInformationMap;
  List<FunctionalDependencyResult> functionalDependencyResults;
  String tableName;

  private void setUpNormal() throws Exception {
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
	  @Override
	  public void close() throws Exception {}
    };
    tableName = FileFixtureFunctionalDependency.TABLE_NAME;

    TableInformation
      tableInformation =
      new TableInformation(relationalInputGenerator, false, new BitSet());
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
    this.functionalDependencyResults =
      analyzer.extendDependantSide(this.functionalDependencyResults);

  }

  private void setUpPollution() throws Exception {
    final FileFixturePollution fileFixturePollution = new FileFixturePollution();
    RelationalInputGenerator relationalInputGeneratorPollution = new RelationalInputGenerator() {
      @Override
      public RelationalInput generateNewCopy() throws InputGenerationException {
        try {
          return fileFixturePollution.getTestData();
        } catch (InputIterationException e) {
          return null;
        }
      }
	  @Override
	  public void close() throws Exception {}
    };
    tableName = FileFixturePollution.TABLE_NAME;

    TableInformation tableInformation = new TableInformation(relationalInputGeneratorPollution, false, new BitSet());
    tableInformationMap = new HashMap<>();
    tableInformationMap.put(tableName, tableInformation);

    FunctionalDependency fd1 = new FunctionalDependency(
      new ColumnCombination(
        new ColumnIdentifier(tableName, "B")),
      new ColumnIdentifier(tableName, "C"));
    FunctionalDependency fd2 = new FunctionalDependency(
      new ColumnCombination(
        new ColumnIdentifier(tableName, "B")),
      new ColumnIdentifier(tableName, "E"));
    List<FunctionalDependency> fds = new ArrayList<>();
    fds.add(fd1);
    fds.add(fd2);

    List<RelationalInputGenerator> generators = new ArrayList<>();
    generators.add(relationalInputGeneratorPollution);
    FunctionalDependencyResultAnalyzer analyzer = new FunctionalDependencyResultAnalyzer(
      generators, true);
    functionalDependencyResults = analyzer.convertResults(fds);
    functionalDependencyResults = analyzer.extendDependantSide(functionalDependencyResults);
  }

  private void setUpInformationGain() throws Exception {
    final FileFixtureInformationGain fileFixture = new FileFixtureInformationGain();
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
    tableName = FileFixtureInformationGain.TABLE_NAME;

    TableInformation tableInformation = new TableInformation(relationalInputGenerator, false, new BitSet());
    tableInformationMap = new HashMap<>();
    tableInformationMap.put(tableName, tableInformation);

    FunctionalDependency fd1 = new FunctionalDependency(
      new ColumnCombination(
        new ColumnIdentifier(tableName, "B")),
      new ColumnIdentifier(tableName, "C"));
    List<FunctionalDependency> fds = new ArrayList<>();
    fds.add(fd1);

    List<RelationalInputGenerator> generators = new ArrayList<>();
    generators.add(relationalInputGenerator);
    FunctionalDependencyResultAnalyzer analyzer = new FunctionalDependencyResultAnalyzer(
      generators, true);
    functionalDependencyResults = analyzer.convertResults(fds);
    functionalDependencyResults = analyzer.extendDependantSide(functionalDependencyResults);
  }

  @Test
  public void testInitialization() throws Exception {
    // Set up
    setUpNormal();

    // Execute functionality
    FunctionalDependencyRanking ranking = new FunctionalDependencyRanking(functionalDependencyResults,
      tableInformationMap);

    // Check
    assertNotNull(ranking.tableInformationMap);
    assertNotNull(ranking.results);
    assertNotNull(ranking.occurrenceMap);

    assertEquals(2, (int) ranking.occurrenceMap.get(tableName).get("A"));
    assertEquals(3, (int) ranking.occurrenceMap.get(tableName).get("B"));
    assertEquals(1, (int) ranking.occurrenceMap.get(tableName).get("C"));
    assertEquals(1, (int) ranking.occurrenceMap.get(tableName).get("D"));
    assertEquals(1, (int) ranking.occurrenceMap.get(tableName).get("E"));
  }

  @Test
  public void testCalculateColumnRatios() throws Exception {
    // Set up
    setUpNormal();
    FunctionalDependencyRanking ranking = new FunctionalDependencyRanking(functionalDependencyResults,
      tableInformationMap);
    FunctionalDependencyResult result = functionalDependencyResults.get(0);

    // Execute Functionality
    ranking.calculateColumnRatios(result);

    // Check
    assertEquals(0.4, result.getDeterminantColumnRatio(), 0.001);
    assertEquals(0.6, result.getDependantColumnRatio(), 0.001);
  }

  @Test
  public void testCalculateGeneralCoverage() throws Exception {
    // Set up
    setUpNormal();
    FunctionalDependencyRanking ranking = new FunctionalDependencyRanking(functionalDependencyResults,
      tableInformationMap);
    FunctionalDependencyResult result = functionalDependencyResults.get(1);

    // Execute Functionality
    ranking.calculateGeneralCoverage(result);

    // Check
    assertEquals(0.4, result.getGeneralCoverage(), 0.001);
  }

  @Test
  public void testCalculateOccurrenceRatio() throws Exception {
    // Set up
    setUpNormal();
    FunctionalDependencyRanking ranking = new FunctionalDependencyRanking(functionalDependencyResults,
      tableInformationMap);
    FunctionalDependencyResult result = functionalDependencyResults.get(0);

    // Execute Functionality
    ranking.calculateOccurrenceRatios(result);

    // Check
    assertEquals(0.4, result.getDeterminantOccurrenceRatio(), 0.001);
    assertEquals(1, result.getDependantOccurrenceRatio(), 0.001);
  }

  @Test
  public void testCalculateUniquenessRatio() throws Exception {
    // Set up
    setUpNormal();
    FunctionalDependencyRanking ranking = new FunctionalDependencyRanking(functionalDependencyResults,
      tableInformationMap);
    FunctionalDependencyResult result = functionalDependencyResults.get(2);

    // Execute Functionality
    ranking.calculateUniquenessRatios(result);

    // Check
    assertEquals(0.0, result.getDeterminantUniquenessRatio(), 0.01);
    assertEquals(0.33, result.getDependantUniquenessRatio(), 0.01);
  }

  @Test
  public void testCreatePLIs() throws Exception {
    // Set up
    setUpPollution();
    FunctionalDependencyRanking ranking = new FunctionalDependencyRanking(functionalDependencyResults,
      tableInformationMap);
    TableInformation tableInformation = tableInformationMap.values().iterator().next();

    // Execute functionality
    Map<BitSet, PositionListIndex> plis = ranking.createPLIs(tableInformation);

    // Check
    BitSet bitSet = new BitSet();
    bitSet.set(0);
    assertTrue(plis.containsKey(bitSet));
    bitSet.clear();
    bitSet.set(tableInformation.getColumnCount() - 1);
    assertTrue(plis.containsKey(bitSet));
  }

  @Test
  public void testCalculatePollution() throws Exception {
    // Set up
    setUpPollution();
    FunctionalDependencyRanking ranking = new FunctionalDependencyRanking(functionalDependencyResults,
      tableInformationMap);
    FunctionalDependencyResult result = functionalDependencyResults.get(1);

    TableInformation tableInformation = tableInformationMap.values().iterator().next();
    Map<BitSet, PositionListIndex> plis = ranking.createPLIs(tableInformation);
    ranking.PLIs = plis;

    // Execute Functionality
    ranking.calculatePollution(result, tableInformation);

    // Check
    assertEquals(0.4, result.getPollution(), 0.01);
    assertEquals("D", result.getPollutionColumn());
  }

  @Test
  public void testCalculateKeyError() throws Exception {
    // Set up
    setUpPollution();
    FunctionalDependencyRanking ranking = new FunctionalDependencyRanking(functionalDependencyResults,
      tableInformationMap);

    TableInformation tableInformation = tableInformationMap.values().iterator().next();
    Map<BitSet, PositionListIndex> plis = ranking.createPLIs(tableInformation);
    ranking.PLIs = plis;

    // Execute Functionality
    BitSet columns = new BitSet(5);
    columns.set(0);
    float keyError = ranking.calculateKeyError(columns);

    // Check
    assertEquals(0.0, keyError, 0.00);
    assertEquals(tableInformation.getColumnCount(), ranking.PLIs.keySet().size());

    // Execute Functionality
    columns = new BitSet(5);
    columns.set(0);
    columns.set(3);
    keyError = ranking.calculateKeyError(columns);

    // Check
    assertEquals(0.0, keyError, 0.0001);
    assertEquals(tableInformation.getColumnCount() + 1, ranking.PLIs.keySet().size());

    // Execute Functionality
    columns = new BitSet(5);
    columns.set(1);
    columns.set(3);
    keyError = ranking.calculateKeyError(columns);

    // Check
    assertEquals(1.0, keyError, 0.0001);
    assertEquals(tableInformation.getColumnCount() + 2, ranking.PLIs.keySet().size());

    // Execute Functionality
    columns = new BitSet(5);
    columns.set(2);
    columns.set(4);
    keyError = ranking.calculateKeyError(columns);

    // Check
    assertEquals(3.0, keyError, 0.0001);
    assertEquals(tableInformation.getColumnCount() + 3, ranking.PLIs.keySet().size());
  }

  @Test
  public void testCalculateInformationGainCell() throws Exception {
    // Set up
    setUpInformationGain();
    FunctionalDependencyRanking ranking = new FunctionalDependencyRanking(functionalDependencyResults,
      tableInformationMap);
    FunctionalDependencyResult result = functionalDependencyResults.get(0);

    TableInformation tableInformation = tableInformationMap.values().iterator().next();
    Map<BitSet, PositionListIndex> plis = ranking.createPLIs(tableInformation);
    ranking.PLIs = plis;

    // Execute Functionality
    ranking.calculateInformationGainCells(result, tableInformation);

    // Check
    assertEquals(-2.0, result.getInformationGainCells(), 0.00);
  }


  @Test
  public void testCalculateInformationGainBytes() throws Exception {
    // Set up
    setUpInformationGain();
    FunctionalDependencyRanking ranking = new FunctionalDependencyRanking(functionalDependencyResults,
      tableInformationMap);
    FunctionalDependencyResult result = functionalDependencyResults.get(0);

    TableInformation tableInformation = tableInformationMap.values().iterator().next();
    Map<BitSet, PositionListIndex> plis = ranking.createPLIs(tableInformation);
    ranking.PLIs = plis;

    // Execute Functionality
    ranking.calculateInformationGainBytes(result, tableInformation);

    // Check
    assertEquals(-4.0, result.getInformationGainBytes(), 0.00);
  }


}
