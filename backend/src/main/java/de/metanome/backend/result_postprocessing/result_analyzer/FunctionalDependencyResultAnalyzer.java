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

package de.metanome.backend.result_postprocessing.result_analyzer;

import de.metanome.algorithm_integration.ColumnCombination;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.algorithm_integration.results.FunctionalDependency;
import de.metanome.backend.result_postprocessing.result_ranking.FunctionalDependencyRanking;
import de.metanome.backend.result_postprocessing.results.FunctionalDependencyResult;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Analyzes Functional Dependency Results.
 */
public class FunctionalDependencyResultAnalyzer
    extends ResultAnalyzer<FunctionalDependency, FunctionalDependencyResult> {

  public FunctionalDependencyResultAnalyzer(List<RelationalInputGenerator> inputGenerators,
                                            boolean useDataDependentStatistics)
      throws InputGenerationException, InputIterationException {
    super(inputGenerators, useDataDependentStatistics);
  }

  @Override
  protected List<FunctionalDependencyResult> analyzeResultsDataIndependent(
      List<FunctionalDependency> prevResults) {
    List<FunctionalDependencyResult> results = convertResults(prevResults);

    if (!this.tableInformationMap.isEmpty()) {
      results = extendDependantSide(results);
      FunctionalDependencyRanking ranking =
          new FunctionalDependencyRanking(results, tableInformationMap);
      ranking.calculateDataIndependentRankings();
    }

    return results;
  }

  @Override
  protected List<FunctionalDependencyResult> analyzeResultsDataDependent(
      List<FunctionalDependency> prevResults) {
    List<FunctionalDependencyResult> results = convertResults(prevResults);

    if (!this.tableInformationMap.isEmpty()) {
      results = extendDependantSide(results);
      FunctionalDependencyRanking ranking =
          new FunctionalDependencyRanking(results, tableInformationMap);
      ranking.calculateDataDependentRankings();
    }

    return results;
  }

  @Override
  public void printResultsToFile() {

  }

  @Override
  public List<FunctionalDependencyResult> convertResults(
      List<FunctionalDependency> prevResults) {
    List<FunctionalDependencyResult> results = new ArrayList<>();

    for (FunctionalDependency prevResult : prevResults) {
      FunctionalDependencyResult result = new FunctionalDependencyResult(prevResult);

      if (!this.tableInformationMap.isEmpty()) {
        result.setDependantAsBitSet(getDependantBitSet(result));
        result.setDeterminantAsBitSet(getDeterminantBitSet(result));
      }

      results.add(result);
    }

    return results;
  }

  /**
   * Gets the bit set of the dependant column.
   * @param result the result
   * @return bit set of the dependant column.
   */
  protected BitSet getDependantBitSet(FunctionalDependencyResult result) {
    String tableName = result.getDependantTableName();
    String columnName = result.getDependant().getColumnIdentifier();
    return this.tableInformationMap.get(tableName).getColumnInformationMap().get(columnName)
        .getBitSet();
  }

  /**
   * Gets the bit set of the determinant column.
   * @param result the result
   * @return bit set of the determinant column.
   */
  protected BitSet getDeterminantBitSet(FunctionalDependencyResult result) {
    BitSet bitSet = new BitSet();
    String tableName = result.getDeterminantTableName();
    for (ColumnIdentifier column : result.getDeterminant().getColumnIdentifiers()) {
      bitSet.or(this.tableInformationMap.get(tableName).getColumnInformationMap()
                    .get(column.getColumnIdentifier()).getBitSet());
    }
    return bitSet;
  }

  /**
   * Extend the dependant side by merging results with the same determinant and using
   * transitivity.
   *
   * @param results the results
   * @return the results with the extended dependant side
   */
  public List<FunctionalDependencyResult> extendDependantSide(
      List<FunctionalDependencyResult> results) {
    for (FunctionalDependencyResult curResult : results) {
      Set<ColumnIdentifier> extendedDependant = new HashSet<>();

      // Go over all results and check if we can extend the current dependant or not
      for (FunctionalDependencyResult otherResult : results) {
        BitSet curBitSet = (BitSet) curResult.getDeterminantAsBitSet().clone();
        BitSet otherBitSet = (BitSet) otherResult.getDeterminantAsBitSet().clone();

        // Example: AB -> C, B -> D (11 = AB, 10 = B)
        // If 11 & 10 == 10, we merge the dependants
        curBitSet.and(otherBitSet);
        if (curBitSet.equals(otherBitSet)) {
          extendedDependant.add(otherResult.getDependant());
        }
      }

      // Set the extended dependant side
      ColumnCombination combination = new ColumnCombination();
      combination.setColumnIdentifiers(extendedDependant);
      curResult.setExtendedDependant(combination);
    }

    return results;
  }

}
