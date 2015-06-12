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

package de.metanome.backend.result_postprocessing.result_ranking;

import de.metanome.backend.result_postprocessing.helper.TableInformation;
import de.metanome.backend.result_postprocessing.results.FunctionalDependencyResult;

import java.util.List;
import java.util.Map;

/**
 * Calculates the rankings for functional dependency results.
 */
public class FunctionalDependencyRanking extends Ranking {

  protected List<FunctionalDependencyResult> results;
  protected Map<String, TableInformation> tableInformationMap;

  public FunctionalDependencyRanking(List<FunctionalDependencyResult> results,
                                     Map<String, TableInformation> tableInformationMap) {
    super(tableInformationMap);
    this.results = results;
  }

  @Override
  public void calculateDataIndependentRankings() {

  }

  @Override
  public void calculateDataDependentRankings() {

  }
}
