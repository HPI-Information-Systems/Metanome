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
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.algorithm_integration.results.FunctionalDependency;
import de.metanome.backend.result_postprocessing.helper.TableInformation;
import de.metanome.backend.result_postprocessing.result_ranking.FunctionalDependencyRanking;
import de.metanome.backend.result_postprocessing.results.FunctionalDependencyResult;
import de.metanome.backend.result_postprocessing.visualization.FunctionalDependency.FunctionalDependencyVisualization;

import java.util.*;

/**
 * Analyzes Functional Dependency Results.
 */
public class FunctionalDependencyResultAnalyzer
  extends ResultAnalyzer<FunctionalDependency, FunctionalDependencyResult> {

  public FunctionalDependencyResultAnalyzer(List<RelationalInputGenerator> inputGenerators,
                                            boolean useDataIndependentStatistics)
    throws InputGenerationException, InputIterationException, AlgorithmConfigurationException {
    super(inputGenerators, useDataIndependentStatistics);
  }

  @Override
  protected List<FunctionalDependencyResult> analyzeResultsDataIndependent(
    List<FunctionalDependency> prevResults) {
    List<FunctionalDependencyResult> results = convertResults(prevResults);

//    try {
//      if (!this.tableInformationMap.isEmpty()) {
//        results = extendDependantSide(results);
//        FunctionalDependencyRanking ranking =
//            new FunctionalDependencyRanking(results, tableInformationMap);
//        ranking.calculateDataIndependentRankings();
//      }
//    } catch (Exception e) {
//      // Could not analyze results due to error
//    }

    return results;
  }

  @Override
  protected List<FunctionalDependencyResult> analyzeResultsDataDependent(
    List<FunctionalDependency> prevResults) {
    List<FunctionalDependencyResult> results = convertResults(prevResults);

    try {
      if (!this.tableInformationMap.isEmpty()) {
        results = extendDependantSide(results);
        FunctionalDependencyRanking ranking =
          new FunctionalDependencyRanking(results, tableInformationMap);
        ranking.calculateDataDependentRankings();
      }

      if (this.tableInformationMap.size() == 1) {
        TableInformation tableInformation = this.tableInformationMap.values().iterator().next();
        FunctionalDependencyVisualization
          visualization =
          new FunctionalDependencyVisualization(results, tableInformation);
        visualization.createVisualizationData();
      }
    } catch (Exception e) {
      // Could not analyze results due to error
    }

    return results;
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
   *
   * @param result the result
   * @return bit set of the dependant column.
   */
  protected BitSet getDependantBitSet(FunctionalDependencyResult result) {
    String tableName = result.getDependantTableName();
    String columnName = result.getDependant().getColumnIdentifier();
    try {
      return this.tableInformationMap.get(tableName).getColumnInformationMap().get(columnName)
        .getBitSet();
    } catch (Exception e) {
      return new BitSet();
    }
  }

  /**
   * Gets the bit set of the determinant column.
   *
   * @param result the result
   * @return bit set of the determinant column.
   */
  protected BitSet getDeterminantBitSet(FunctionalDependencyResult result) {
    try {
      BitSet bitSet = new BitSet();
      String tableName = result.getDeterminantTableName();
      for (ColumnIdentifier column : result.getDeterminant().getColumnIdentifiers()) {
        bitSet.or(this.tableInformationMap.get(tableName).getColumnInformationMap()
          .get(column.getColumnIdentifier()).getBitSet());
      }
      return bitSet;
    } catch (Exception e) {
      return new BitSet();
    }
  }

  /**
   * Extend the dependant side by merging results with the same determinant and using transitivity.
   *
   * @param results the results
   * @return the results with the extended dependant side
   */
  public List<FunctionalDependencyResult> extendDependantSide(
    List<FunctionalDependencyResult> results) {
    int columnCount = 0;
    for (TableInformation tableInformation : this.tableInformationMap.values()) {
      columnCount += tableInformation.getColumnCount();
    }

    for (FunctionalDependencyResult curResult : results) {
      Set<ColumnIdentifier> extendedDependant = new HashSet<>();
      BitSet extendedDependantBitSet = new BitSet(columnCount);

      // Go over all results and check if we can extend the current dependant or not
      for (FunctionalDependencyResult otherResult : results) {
        BitSet curBitSet = (BitSet) curResult.getDeterminantAsBitSet().clone();
        BitSet otherBitSet = (BitSet) otherResult.getDeterminantAsBitSet().clone();

        // Example: AB -> C, B -> D (11 = AB, 10 = B)
        // If 11 & 10 == 10, we merge the dependants
        curBitSet.and(otherBitSet);
        if (curBitSet.equals(otherBitSet)) {
          extendedDependant.add(otherResult.getDependant());
          extendedDependantBitSet.or(otherResult.getDependantAsBitSet());
        }
      }

      // Set the extended dependant side
      ColumnCombination combination = new ColumnCombination();
      combination.setColumnIdentifiers(extendedDependant);
      curResult.setExtendedDependant(combination);
      curResult.setExtendedDependantAsBitSet(extendedDependantBitSet);
    }

    return results;
  }

}
