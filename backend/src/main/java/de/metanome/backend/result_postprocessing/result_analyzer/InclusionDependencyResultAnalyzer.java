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
import de.metanome.algorithm_integration.results.InclusionDependency;
import de.metanome.backend.result_postprocessing.result_ranking.InclusionDependencyRanking;
import de.metanome.backend.result_postprocessing.results.InclusionDependencyResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Analyzes Inclusion Dependency Results.
 */
public class InclusionDependencyResultAnalyzer
    extends ResultAnalyzer<InclusionDependency, InclusionDependencyResult> {

  public InclusionDependencyResultAnalyzer(List<RelationalInputGenerator> inputGenerators,
                                           boolean useDataDependentStatistics)
      throws InputGenerationException, InputIterationException {
    super(inputGenerators, useDataDependentStatistics);
  }

  @Override
  protected List<InclusionDependencyResult> analyzeResultsDataIndependent(
      List<InclusionDependency> prevResults) {
    List<InclusionDependencyResult> results = convertResults(prevResults);

    if (!this.tableInformationList.isEmpty()) {
      InclusionDependencyRanking
          ranking =
          new InclusionDependencyRanking(results, tableInformationList);
      ranking.calculateDatDependentRankings();
    }

    return results;
  }

  @Override
  protected List<InclusionDependencyResult> analyzeResultsDataDependent(
      List<InclusionDependency> prevResults) {
    List<InclusionDependencyResult> results = convertResults(prevResults);

    if (!this.tableInformationList.isEmpty()) {
      InclusionDependencyRanking ranking =
          new InclusionDependencyRanking(results, tableInformationList);
      ranking.calculateDataIndependentRankings();
      ranking.calculateDatDependentRankings();
    }

    return results;
  }

  @Override
  public void printResultsToFile() {

  }

  /**
   * Converts a InclusionDependency into a InclusionDependencyResult. The InclusionDependencyResult
   * contains additional information like different ranking values.
   *
   * @param prevResults the list of InclusionDependency results
   * @return a list of InclusionDependencyResult
   */
  protected List<InclusionDependencyResult> convertResults(List<InclusionDependency> prevResults) {
    List<InclusionDependencyResult> results = new ArrayList<>();
    for (InclusionDependency prevResult : prevResults) {
      InclusionDependencyResult result = new InclusionDependencyResult();
      result.setDependant(prevResult.getDependant());
      result.setReferenced(prevResult.getReferenced());
      result.setDependantTableName(
          prevResult.getDependant().getColumnIdentifiers().get(0).getTableIdentifier());
      result.setReferencedTableName(
          prevResult.getReferenced().getColumnIdentifiers().get(0).getTableIdentifier());
      results.add(result);
    }
    return results;
  }

}
