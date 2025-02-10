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
import de.metanome.algorithm_integration.results.PartialUniqueColumnCombination;
import de.metanome.backend.result_postprocessing.result_ranking.PartialUniqueColumnCombinationRanking;
import de.metanome.backend.result_postprocessing.results.PartialUniqueColumnCombinationResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Analyzes Partial Unique Column Combination Results.
 */
public class PartialUniqueColumnCombinationResultAnalyzer extends
        ResultAnalyzer<PartialUniqueColumnCombination, PartialUniqueColumnCombinationResult> {

    public PartialUniqueColumnCombinationResultAnalyzer(
            List<RelationalInputGenerator> inputGenerators,
            boolean useDataIndependentStatistics)
            throws InputGenerationException, InputIterationException, AlgorithmConfigurationException {
        super(inputGenerators, useDataIndependentStatistics);
    }

    @Override
    protected List<PartialUniqueColumnCombinationResult> analyzeResultsDataIndependent(
            List<PartialUniqueColumnCombination> prevResults) {
        List<PartialUniqueColumnCombinationResult> results = convertResults(prevResults);
        return results;
    }

    @Override
    protected List<PartialUniqueColumnCombinationResult> analyzeResultsDataDependent(
            List<PartialUniqueColumnCombination> prevResults) {
        List<PartialUniqueColumnCombinationResult> results = convertResults(prevResults);

        try {
            if (!this.tableInformationMap.isEmpty()) {
                PartialUniqueColumnCombinationRanking
                        ranking =
                        new PartialUniqueColumnCombinationRanking(results, tableInformationMap);
                ranking.calculateDataDependentRankings();
            }
        } catch (Exception e) {
            // Could not analyze results due to error
        }

        return results;
    }

    @Override
    protected List<PartialUniqueColumnCombinationResult> convertResults(
            List<PartialUniqueColumnCombination> prevResults) {
        List<PartialUniqueColumnCombinationResult> results = new ArrayList<>();

        for (PartialUniqueColumnCombination prevResult : prevResults) {
            PartialUniqueColumnCombinationResult
                    result =
                    new PartialUniqueColumnCombinationResult(prevResult);
            results.add(result);
        }

        return results;
    }


}
