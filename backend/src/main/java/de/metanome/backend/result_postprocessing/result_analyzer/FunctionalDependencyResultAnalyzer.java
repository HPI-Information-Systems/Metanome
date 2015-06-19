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

import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.algorithm_integration.results.FunctionalDependency;
import de.metanome.backend.result_postprocessing.result_ranking.FunctionalDependencyRanking;
import de.metanome.backend.result_postprocessing.results.FunctionalDependencyResult;

import java.util.ArrayList;
import java.util.List;

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

    if (!this.tableInformationList.isEmpty()) {


      FunctionalDependencyRanking ranking =
          new FunctionalDependencyRanking(results, tableInformationList);
      ranking.calculateDataIndependentRankings();
    }

    return results;
  }

  @Override
  protected List<FunctionalDependencyResult> analyzeResultsDataDependent(
      List<FunctionalDependency> prevResults) {
    List<FunctionalDependencyResult> results = convertResults(prevResults);

    if (!this.tableInformationList.isEmpty()) {
      FunctionalDependencyRanking ranking =
          new FunctionalDependencyRanking(results, tableInformationList);
      ranking.calculateDataDependentRankings();
    }

    return results;
  }

  @Override
  public void printResultsToFile() {

  }

  @Override
  protected List<FunctionalDependencyResult> convertResults(
      List<FunctionalDependency> prevResults) {
    List<FunctionalDependencyResult> results = new ArrayList<>();

    for (FunctionalDependency prevResult : prevResults) {
      FunctionalDependencyResult result = new FunctionalDependencyResult(prevResult);
      results.add(result);
    }

    return results;
  }


}
