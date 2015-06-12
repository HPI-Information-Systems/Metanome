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

import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.backend.result_postprocessing.helper.ColumnInformation;
import de.metanome.backend.result_postprocessing.helper.TableInformation;
import de.metanome.backend.result_postprocessing.results.ConditionalUniqueColumnCombinationResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    this.occurrenceMap = new HashMap<>();

    createOccurrenceList();
  }

  /**
   * The occurrence list stores how often a column occurs in the results.
   */
  protected void createOccurrenceList() {
    initializeOccurrenceList();

    for (ConditionalUniqueColumnCombinationResult result : this.results) {
      for (ColumnIdentifier column : result.getColumnCombination().getColumnIdentifiers()) {
        updateOccurrenceList(column);
      }
    }
  }


  @Override
  public void calculateDataIndependentRankings() {
    for (ConditionalUniqueColumnCombinationResult result : this.results) {
      calculateColumnRatio(result);
      calculateOccurrenceRatio(result);
    }
  }

  @Override
  public void calculateDataDependentRankings() {
    for (ConditionalUniqueColumnCombinationResult result : this.results) {
      calculateColumnRatio(result);
      calculateOccurrenceRatio(result);

      calculateUniquenessRatio(result);
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

  /**
   * Calculates the ratio of the column count and the overall occurrence of the
   * columns in the result.
   *
   * @param result the result
   */
  protected void calculateOccurrenceRatio(ConditionalUniqueColumnCombinationResult result) {
    Integer occurrences = 0;
    for (ColumnIdentifier column : result.getColumnCombination().getColumnIdentifiers()) {
      occurrences += this.occurrenceMap.get(result.getTableName()).get(column.getColumnIdentifier());
    }
    result.setOccurrenceRatio((float) result.getColumnCombination().getColumnIdentifiers().size() / occurrences);
  }


  /**
   * Calculate the ratio of the number of almost unique columns and all columns.
   *
   * @param result the result
   */
  protected void calculateUniquenessRatio(ConditionalUniqueColumnCombinationResult result) {
    TableInformation table = this.tableInformationMap.get(result.getTableName());
    Set<ColumnIdentifier> columns = result.getColumnCombination().getColumnIdentifiers();

    Map<String, ColumnInformation> columnInformationList = table.getColumnInformationList();
    Integer uniqueColumns = 0;

    for (ColumnIdentifier column : columns) {
      if (columnInformationList.get(column.getColumnIdentifier()).getUniquenessRate()
          >= UNIQUENESS_THRESHOLD) {
        uniqueColumns++;
      }
    }

    result.setUniquenessRatio((float) uniqueColumns / columns.size());
  }

}
