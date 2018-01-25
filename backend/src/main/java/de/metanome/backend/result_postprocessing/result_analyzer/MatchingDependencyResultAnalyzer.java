/**
 * Copyright 2015-2016 by Metanome Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package de.metanome.backend.result_postprocessing.result_analyzer;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.algorithm_integration.results.MatchingDependency;
import de.metanome.backend.result_postprocessing.result_ranking.MatchingDependencyRanking;
import de.metanome.backend.result_postprocessing.results.MatchingDependencyResult;
import java.util.ArrayList;
import java.util.List;

/**
 * Analyzes Matching Dependency Results.
 */
public class MatchingDependencyResultAnalyzer
    extends ResultAnalyzer<MatchingDependency, MatchingDependencyResult> {

  public MatchingDependencyResultAnalyzer(List<RelationalInputGenerator> inputGenerators,
      boolean useDataIndependentStatistics)
      throws InputGenerationException, InputIterationException, AlgorithmConfigurationException {
    super(inputGenerators, useDataIndependentStatistics);
  }

  @Override
  protected List<MatchingDependencyResult> analyzeResultsDataIndependent(
      List<MatchingDependency> prevResults) {
    return convertResults(prevResults);
  }

  @Override
  protected List<MatchingDependencyResult> analyzeResultsDataDependent(
      List<MatchingDependency> prevResults) {
    List<MatchingDependencyResult> results = convertResults(prevResults);

    try {
      if (!this.tableInformationMap.isEmpty()) {
        MatchingDependencyRanking ranking =
            new MatchingDependencyRanking(results, tableInformationMap);
        ranking.calculateDataDependentRankings();
      }
    } catch (Exception e) {
      // Could not analyze results due to error
    }

    return results;
  }

  @Override
  public List<MatchingDependencyResult> convertResults(
      List<MatchingDependency> prevResults) {
    List<MatchingDependencyResult> results = new ArrayList<>();

    for (MatchingDependency prevResult : prevResults) {
      MatchingDependencyResult result = new MatchingDependencyResult(prevResult);

      results.add(result);
    }

    return results;
  }

}
