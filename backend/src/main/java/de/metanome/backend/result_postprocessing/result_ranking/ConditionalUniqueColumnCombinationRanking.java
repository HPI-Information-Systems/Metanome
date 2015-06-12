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
import de.metanome.backend.result_postprocessing.results.ConditionalUniqueColumnCombinationResult;

import java.util.List;
import java.util.Map;

/**
 * Calculates the rankings for conditional unique column combination results.
 */
public class ConditionalUniqueColumnCombinationRanking extends Ranking {

  protected List<ConditionalUniqueColumnCombinationResult> results;

  public ConditionalUniqueColumnCombinationRanking(
      List<ConditionalUniqueColumnCombinationResult> results,
      Map<String, TableInformation> tableInformationMap) {
    super(tableInformationMap);
    this.results = results;
  }


  @Override
  public void calculateDataIndependentRankings() {
    for (ConditionalUniqueColumnCombinationResult result : this.results) {
      calculateColumnRatio(result);
    }
  }

  @Override
  public void calculateDataDependentRankings() {
    for (ConditionalUniqueColumnCombinationResult result : this.results) {
      calculateColumnRatio(result);
    }
  }

  /**
   * Calculates the ratio of the size of the column combination and the column count of the
   * corresponding table.
   *
   * @param result the result
   */
  protected void calculateColumnRatio(ConditionalUniqueColumnCombinationResult result) {
    Integer columnCount = result.getColumnCombination().getColumnIdentifiers().size();
    Integer tableColumnCount = this.tableInformationMap.get(result.getTableName()).getColumnCount();
    result.setColumnRatio((float) columnCount / tableColumnCount);
  }

}
