/**
 * Copyright 2015-2025 by Metanome Project
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
import de.metanome.algorithm_integration.results.RelaxedUniqueColumnCombination;
import de.metanome.backend.result_postprocessing.result_ranking.RelaxedUniqueColumnCombinationRanking;
import de.metanome.backend.result_postprocessing.results.RelaxedUniqueColumnCombinationResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Analyzes Relaxed Unique Column Combination Results.
 */
public class RelaxedUniqueColumnCombinationResultAnalyzer extends
        ResultAnalyzer<RelaxedUniqueColumnCombination, RelaxedUniqueColumnCombinationResult> {

    public RelaxedUniqueColumnCombinationResultAnalyzer(
            List<RelationalInputGenerator> inputGenerators,
            boolean useDataIndependentStatistics)
            throws InputGenerationException, InputIterationException, AlgorithmConfigurationException {
        super(inputGenerators, useDataIndependentStatistics);
    }

    @Override
    protected List<RelaxedUniqueColumnCombinationResult> analyzeResultsDataIndependent(
            List<RelaxedUniqueColumnCombination> prevResults) {
        List<RelaxedUniqueColumnCombinationResult> results = convertResults(prevResults);
        return results;
    }

    @Override
    protected List<RelaxedUniqueColumnCombinationResult> analyzeResultsDataDependent(
            List<RelaxedUniqueColumnCombination> prevResults) {
        List<RelaxedUniqueColumnCombinationResult> results = convertResults(prevResults);

        try {
            if (!this.tableInformationMap.isEmpty()) {
                RelaxedUniqueColumnCombinationRanking
                        ranking =
                        new RelaxedUniqueColumnCombinationRanking(results, tableInformationMap);
                ranking.calculateDataDependentRankings();
            }
        } catch (Exception e) {
            // Could not analyze results due to error
        }

        return results;
    }

    @Override
    protected List<RelaxedUniqueColumnCombinationResult> convertResults(
            List<RelaxedUniqueColumnCombination> prevResults) {
        List<RelaxedUniqueColumnCombinationResult> results = new ArrayList<>();

        for (RelaxedUniqueColumnCombination prevResult : prevResults) {
            RelaxedUniqueColumnCombinationResult
                    result =
                    new RelaxedUniqueColumnCombinationResult(prevResult);
            results.add(result);
        }

        return results;
    }


}
