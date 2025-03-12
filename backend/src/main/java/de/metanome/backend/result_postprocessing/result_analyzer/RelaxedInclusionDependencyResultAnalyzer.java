/*
 * Copyright 2015-2025 by Metanome Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
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
import de.metanome.algorithm_integration.results.RelaxedInclusionDependency;
import de.metanome.backend.result_postprocessing.results.RelaxedInclusionDependencyResult;
import java.util.List;
import static java.util.stream.Collectors.toList;


/**
 * Analyzes Relaxed Inclusion Dependency Results.
 */
public class RelaxedInclusionDependencyResultAnalyzer extends ResultAnalyzer<RelaxedInclusionDependency, RelaxedInclusionDependencyResult>{

    public RelaxedInclusionDependencyResultAnalyzer(List<RelationalInputGenerator> inputGenerators, boolean useDataIndependentStatistics) throws InputGenerationException, InputIterationException, AlgorithmConfigurationException {
        super(inputGenerators, useDataIndependentStatistics);
    }

    @Override
    protected List<RelaxedInclusionDependencyResult> analyzeResultsDataIndependent(List<RelaxedInclusionDependency> prevResults) {
        return convertResults(prevResults);
    }

    @Override
    protected List<RelaxedInclusionDependencyResult> analyzeResultsDataDependent(List<RelaxedInclusionDependency> prevResults) {
        return convertResults(prevResults);
    }

    @Override
    protected List<RelaxedInclusionDependencyResult> convertResults(List<RelaxedInclusionDependency> prevResults) {
        return prevResults.stream()
                .map((prevResult) -> new RelaxedInclusionDependencyResult(prevResult))
                .collect(toList());
    }
}
