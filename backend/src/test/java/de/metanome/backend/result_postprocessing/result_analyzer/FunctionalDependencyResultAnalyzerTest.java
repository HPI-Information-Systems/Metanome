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
package de.metanome.backend.result_postprocessing.result_analyzer;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.ColumnCombination;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.input.RelationalInput;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.algorithm_integration.results.FunctionalDependency;
import de.metanome.backend.result_postprocessing.file_fixture.FileFixtureFunctionalDependency;
import de.metanome.backend.result_postprocessing.results.FunctionalDependencyResult;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import static org.junit.Assert.*;

public class FunctionalDependencyResultAnalyzerTest {

  // AB -> C
  // B -> D
  // AB -> E

  List<FunctionalDependency> prevResults;
  List<FunctionalDependencyResult> results;
  List<RelationalInputGenerator> generators;
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
	  @Override
	  public void close() throws Exception {}
    };

    this.generators = new ArrayList<>();
    this.generators.add(relationalInputGenerator);

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


    prevResults = new ArrayList<>();
    prevResults.add(fd1);
    prevResults.add(fd2);
    prevResults.add(fd3);

    results = new ArrayList<>();
    results.add(new FunctionalDependencyResult(fd1));
    results.add(new FunctionalDependencyResult(fd2));
    results.add(new FunctionalDependencyResult(fd3));

  }

  @Test
  public void testInitialization() throws InputGenerationException, InputIterationException, AlgorithmConfigurationException {
    // Execute
    ResultAnalyzer<?, ?> analyzer = new FunctionalDependencyResultAnalyzer(this.generators, false);

    // Check
    assertEquals(1, analyzer.tableInformationMap.size());
    assertEquals(5, (analyzer.tableInformationMap.get(tableName)).getColumnCount());
    assertNotNull(analyzer.inputGenerators);
    assertFalse(analyzer.useDataIndependentStatistics);
  }

  @Test
  public void testGetDeterminantBitSet() throws InputGenerationException, InputIterationException, AlgorithmConfigurationException {
    // Set Up
    FunctionalDependencyResultAnalyzer analyzer =
      new FunctionalDependencyResultAnalyzer(this.generators, false);

    // Expected Values
    BitSet expectedBitSet = new BitSet();
    expectedBitSet.set(0);
    expectedBitSet.set(1);

    // Execute
    BitSet actualBitSet = analyzer.getDeterminantBitSet(this.results.get(0));

    // Check
    assertEquals(expectedBitSet, actualBitSet);

    // Expected Values
    expectedBitSet = new BitSet();
    expectedBitSet.set(1);

    // Execute
    actualBitSet = analyzer.getDeterminantBitSet(this.results.get(1));

    // Check
    assertEquals(expectedBitSet, actualBitSet);
  }

  @Test
  public void testGetDependantBitSet() throws InputGenerationException, InputIterationException, AlgorithmConfigurationException {
    // Set Up
    FunctionalDependencyResultAnalyzer analyzer =
      new FunctionalDependencyResultAnalyzer(this.generators, false);

    // Expected Values
    BitSet expectedBitSet = new BitSet();
    expectedBitSet.set(3);

    // Execute
    BitSet actualBitSet = analyzer.getDependantBitSet(this.results.get(1));

    // Check
    assertEquals(expectedBitSet, actualBitSet);

    // Expected Values
    expectedBitSet = new BitSet();
    expectedBitSet.set(4);

    // Execute
    actualBitSet = analyzer.getDependantBitSet(this.results.get(2));

    // Check
    assertEquals(expectedBitSet, actualBitSet);
  }

  @Test
  public void testExtendDependants() throws InputGenerationException, InputIterationException, AlgorithmConfigurationException {
    // Set Up
    FunctionalDependencyResultAnalyzer analyzer =
      new FunctionalDependencyResultAnalyzer(this.generators, false);
    this.results = analyzer.convertResults(this.prevResults);

    // Expected Value
    ColumnCombination extendedDependant1 = new ColumnCombination(
      new ColumnIdentifier(tableName, "C"),
      new ColumnIdentifier(tableName, "D"),
      new ColumnIdentifier(tableName, "E"));
    ColumnCombination extendedDependant2 = new ColumnCombination(
      new ColumnIdentifier(tableName, "D"));

    BitSet expectedBitSet = new BitSet(5);
    expectedBitSet.set(2);
    expectedBitSet.set(3);
    expectedBitSet.set(4);

    // Execute
    List<FunctionalDependencyResult> actualResults = analyzer.extendDependantSide(this.results);

    // Check
    assertEquals(extendedDependant1, actualResults.get(0).getExtendedDependant());
    assertEquals(extendedDependant2, actualResults.get(1).getExtendedDependant());
    assertEquals(extendedDependant1, actualResults.get(2).getExtendedDependant());
    assertEquals(expectedBitSet, actualResults.get(2).getExtendedDependantAsBitSet());
  }

}
