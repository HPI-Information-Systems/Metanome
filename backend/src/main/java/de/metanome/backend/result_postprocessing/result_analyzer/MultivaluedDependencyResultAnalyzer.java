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

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.ColumnCombination;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.algorithm_integration.results.MultivaluedDependency;
import de.metanome.backend.result_postprocessing.helper.TableInformation;
import de.metanome.backend.result_postprocessing.result_ranking.MultivaluedDependencyRanking;
import de.metanome.backend.result_postprocessing.results.MultivaluedDependencyResult;

import java.util.*;

/**
 * Analyzes Multivalued Dependency Results.
 */
public class MultivaluedDependencyResultAnalyzer
  extends ResultAnalyzer<MultivaluedDependency, MultivaluedDependencyResult> {

  public MultivaluedDependencyResultAnalyzer(List<RelationalInputGenerator> inputGenerators,
                                            boolean useDataIndependentStatistics)
    throws InputGenerationException, InputIterationException, AlgorithmConfigurationException {
    super(inputGenerators, useDataIndependentStatistics);
  }

  @Override
  protected List<MultivaluedDependencyResult> analyzeResultsDataIndependent(
    List<MultivaluedDependency> prevResults) {
    List<MultivaluedDependencyResult> results = convertResults(prevResults);

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
  protected List<MultivaluedDependencyResult> analyzeResultsDataDependent(
    List<MultivaluedDependency> prevResults) {
    List<MultivaluedDependencyResult> results = convertResults(prevResults);

    try {
      if (!this.tableInformationMap.isEmpty()) {
//        results = extendDependantSide(results);
        MultivaluedDependencyRanking ranking =
          new MultivaluedDependencyRanking(results, tableInformationMap);
        ranking.calculateDataDependentRankings();
      }
    } catch (Exception e) {
      // Could not analyze results due to error
    }

    return results;
  }

  @Override
  public List<MultivaluedDependencyResult> convertResults(
    List<MultivaluedDependency> prevResults) {
    List<MultivaluedDependencyResult> results = new ArrayList<>();

    for (MultivaluedDependency prevResult : prevResults) {
    	MultivaluedDependencyResult result = new MultivaluedDependencyResult(prevResult);

      results.add(result);
    }

    return results;
  }

}
