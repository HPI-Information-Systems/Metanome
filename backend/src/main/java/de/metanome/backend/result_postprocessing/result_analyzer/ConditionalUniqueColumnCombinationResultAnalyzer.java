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
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.algorithm_integration.results.ConditionalUniqueColumnCombination;
import de.metanome.backend.result_postprocessing.result_ranking.ConditionalUniqueColumnCombinationRanking;
import de.metanome.backend.result_postprocessing.results.ConditionalUniqueColumnCombinationResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Analyzes Conditional Unique Column Combination Results.
 */
public class ConditionalUniqueColumnCombinationResultAnalyzer extends
  ResultAnalyzer<ConditionalUniqueColumnCombination, ConditionalUniqueColumnCombinationResult> {

  public ConditionalUniqueColumnCombinationResultAnalyzer(
    List<RelationalInputGenerator> inputGenerators,
    boolean useDataIndependentStatistics)
    throws InputGenerationException, InputIterationException, AlgorithmConfigurationException {
    super(inputGenerators, useDataIndependentStatistics);
  }

  @Override
  protected List<ConditionalUniqueColumnCombinationResult> analyzeResultsDataIndependent(
    List<ConditionalUniqueColumnCombination> prevResults) {
    List<ConditionalUniqueColumnCombinationResult> results = convertResults(prevResults);

//    try {
//      if (!this.tableInformationMap.isEmpty()) {
//        ConditionalUniqueColumnCombinationRanking
//            ranking =
//            new ConditionalUniqueColumnCombinationRanking(results, tableInformationMap);
//        ranking.calculateDataIndependentRankings();
//      }
//    } catch (Exception e) {
//      // Could not analyze results due to error
//    }

    return results;
  }

  @Override
  protected List<ConditionalUniqueColumnCombinationResult> analyzeResultsDataDependent(
    List<ConditionalUniqueColumnCombination> prevResults) {
    List<ConditionalUniqueColumnCombinationResult> results = convertResults(prevResults);

    try {
      if (!this.tableInformationMap.isEmpty()) {
        ConditionalUniqueColumnCombinationRanking
          ranking =
          new ConditionalUniqueColumnCombinationRanking(results, tableInformationMap);
        ranking.calculateDataDependentRankings();
      }
    } catch (Exception e) {
      // Could not analyze results due to error
    }

    return results;
  }

  @Override
  protected List<ConditionalUniqueColumnCombinationResult> convertResults(
    List<ConditionalUniqueColumnCombination> prevResults) {
    List<ConditionalUniqueColumnCombinationResult> results = new ArrayList<>();

    for (ConditionalUniqueColumnCombination prevResult : prevResults) {
      ConditionalUniqueColumnCombinationResult
        result =
        new ConditionalUniqueColumnCombinationResult(prevResult);
      results.add(result);
    }

    return results;
  }


}
