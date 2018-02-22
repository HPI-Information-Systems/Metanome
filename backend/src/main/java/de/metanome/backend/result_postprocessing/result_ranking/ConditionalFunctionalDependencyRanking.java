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
package de.metanome.backend.result_postprocessing.result_ranking;

import de.metanome.algorithm_helper.data_structures.PLIBuilder;
import de.metanome.algorithm_helper.data_structures.PositionListIndex;
import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.backend.result_postprocessing.helper.ColumnInformation;
import de.metanome.backend.result_postprocessing.helper.TableInformation;
import de.metanome.backend.result_postprocessing.results.ConditionalFunctionalDependencyResult;
import de.metanome.backend.result_postprocessing.results.FunctionalDependencyResult;

import java.util.*;

/**
 * Calculates the rankings for functional dependency results.
 */
public class ConditionalFunctionalDependencyRanking extends Ranking {

  protected List<ConditionalFunctionalDependencyResult> results;

  public ConditionalFunctionalDependencyRanking(List<ConditionalFunctionalDependencyResult> results,
                                                Map<String, TableInformation> tableInformationMap) {
    super(tableInformationMap);
    this.results = results;
  }

  /**
   * Calculate data independent rankings.
   */
  @Override
  public void calculateDataIndependentRankings() {

  }

  /**
   * Calculate data dependent rankings.
   *
   * @throws InputGenerationException        if the input is not accessible
   * @throws InputIterationException         if the input is not iterable
   * @throws AlgorithmConfigurationException if input generator could not be build
   */
  @Override
  public void calculateDataDependentRankings() throws InputGenerationException, InputIterationException, AlgorithmConfigurationException {

  }
}
