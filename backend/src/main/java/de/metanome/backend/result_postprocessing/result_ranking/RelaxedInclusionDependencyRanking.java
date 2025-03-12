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
package de.metanome.backend.result_postprocessing.result_ranking;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.backend.result_postprocessing.helper.TableInformation;
import de.metanome.backend.result_postprocessing.results.RelaxedInclusionDependencyResult;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Calculates the rankings for relaxed inclusion dependency results.
 */
public class RelaxedInclusionDependencyRanking extends Ranking{

    protected List<RelaxedInclusionDependencyResult> results;

    public RelaxedInclusionDependencyRanking(List<RelaxedInclusionDependencyResult> results,
                                             Map<String, TableInformation> tableInformationMap) {
        super(tableInformationMap);
        this.results = results;
        this.occurrenceMap = new HashMap<>();
    }

    @Override
    public void calculateDataIndependentRankings() {
        //Nothing to calculate for our algorithm now!
        //See FunctionalDependencyRanking for a good example of how this method could look like.
    }

    @Override
    public void calculateDataDependentRankings() throws InputGenerationException, InputIterationException, AlgorithmConfigurationException {
        //Nothing to calculate for our algorithm now!
        //See FunctionalDependencyRanking for a good example of how this method could look like.
    }

}
