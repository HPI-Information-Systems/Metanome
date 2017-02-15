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
package de.metanome.backend.result_postprocessing.visualization.FunctionalDependency;

import de.metanome.algorithm_helper.data_structures.PositionListIndex;
import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.ColumnCombination;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.input.RelationalInput;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.algorithm_integration.results.FunctionalDependency;
import de.metanome.backend.result_postprocessing.file_fixture.FileFixtureFunctionalDependency;
import de.metanome.backend.result_postprocessing.helper.TableInformation;
import de.metanome.backend.result_postprocessing.result_analyzer.FunctionalDependencyResultAnalyzer;
import de.metanome.backend.result_postprocessing.result_ranking.FunctionalDependencyRanking;
import de.metanome.backend.result_postprocessing.results.FunctionalDependencyResult;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FunctionalDependencyVisualizationTest {

  Map<String, TableInformation> tableInformationMap;
  List<FunctionalDependencyResult> functionalDependencyResults;
  String tableName;

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

  @Test
  public void testSetUp() {
    // Execute functionality
    TableInformation tableInformation = tableInformationMap.values().iterator().next();
    FunctionalDependencyVisualization visualization = new FunctionalDependencyVisualization(
        functionalDependencyResults, tableInformation);

    // Check
    assertNotNull(visualization.tableInformation);
    assertNotNull(visualization.results);
  }

  @Test
  public void testGetColumnIdentifier() {
    // Set Up
    TableInformation tableInformation = tableInformationMap.values().iterator().next();
    FunctionalDependencyVisualization visualization = new FunctionalDependencyVisualization(
        functionalDependencyResults, tableInformation);

    ColumnIdentifier expectedColumnIdentifier = new ColumnIdentifier("expect", "expect");

    ColumnCombination combination1 = new ColumnCombination(
        new ColumnIdentifier("1", "1"), new ColumnIdentifier("1", "2"));
    ColumnCombination combination2 = new ColumnCombination(
        expectedColumnIdentifier, new ColumnIdentifier("f", "f"));
    ColumnCombination combination3 = new ColumnCombination(
        new ColumnIdentifier("1", "5"), new ColumnIdentifier("2", "2"), expectedColumnIdentifier);

    Set<ColumnCombination> columnCombinations = new TreeSet<>();
    columnCombinations.add(combination1);
    columnCombinations.add(combination2);
    columnCombinations.add(combination3);

    // Execute functionality
    ColumnIdentifier actualColumnIdentifier = visualization.getColumnIdentifier(columnCombinations,
                                                                                2, 2);
    // Check
    assertEquals(expectedColumnIdentifier, actualColumnIdentifier);

    // Execute functionality
    actualColumnIdentifier = visualization.getColumnIdentifier(columnCombinations, 1, 0);
    // Check
    assertEquals(expectedColumnIdentifier, actualColumnIdentifier);
  }

  @Test
  public void testCreateDependantMap() {
    // Set up
    TableInformation tableInformation = tableInformationMap.values().iterator().next();
    FunctionalDependencyVisualization visualization = new FunctionalDependencyVisualization(
        functionalDependencyResults, tableInformation);

    ColumnIdentifier expectedDependant = new ColumnIdentifier(tableName, "C");
    Set<ColumnCombination> expectedDeterminant = new TreeSet<>();
    expectedDeterminant.add(new ColumnCombination(
        new ColumnIdentifier(tableName, "A"), new ColumnIdentifier(tableName, "B")));

    // Execute functionality
    Map<ColumnIdentifier, Set<ColumnCombination>> actualDependantMap =
        visualization.createDependantMap();

    // Check
    assertEquals(3, actualDependantMap.keySet().size());
    assertEquals(1, actualDependantMap.get(expectedDependant).size());
    assertEquals(expectedDeterminant, actualDependantMap.get(expectedDependant));
  }

  @Test
  public void testPrint() throws InputGenerationException, InputIterationException, AlgorithmConfigurationException {
    // Set up
    FunctionalDependencyRanking ranking = new FunctionalDependencyRanking(functionalDependencyResults,
                                                                          tableInformationMap);

    TableInformation tableInformation = tableInformationMap.values().iterator().next();
    Map<BitSet, PositionListIndex> plis = ranking.createPLIs(tableInformation);
    tableInformation.setPLIs(plis);

    FunctionalDependencyVisualization visualization = new FunctionalDependencyVisualization(
        functionalDependencyResults, tableInformation);

    Map<ColumnIdentifier, Set<ColumnCombination>> dependantMap =
        visualization.createDependantMap();
    ColumnIdentifier dependant = new ColumnIdentifier(tableName, "C");

    // Execute functionality
    BitSet path = new BitSet();
    Set<ColumnCombination> determinants = dependantMap.get(dependant);
    BitSet dependantBitSet = new BitSet();
    dependantBitSet.set(tableInformation.getColumnInformationMap().get("C").getColumnIndex());
    JSONObject dependantJSON = visualization.printRecursive(dependantBitSet, determinants, path, -1, 0,
                                                            determinants.size(), "");

    // Check
    JSONObject firstDeterminant = (JSONObject) ((JSONArray) dependantJSON.get("children")).get(0);
    assertEquals(4, firstDeterminant.size());
    assertEquals("A", firstDeterminant.get("name"));
    assertEquals(0.6666, (double) firstDeterminant.get("size"), 0.0001);
    assertEquals(1L, (long) firstDeterminant.get("keyError"), 0.0001);
    JSONObject secondDeterminant = (JSONObject) ((JSONArray) firstDeterminant.get("children")).get(0);
    assertEquals(3, secondDeterminant.size());
    assertEquals("B", secondDeterminant.get("name"));
    assertEquals(0.6666, (double) secondDeterminant.get("size"), 0.0001);
    assertEquals(0L, (long) secondDeterminant.get("keyError"), 0.0001);
  }


}
