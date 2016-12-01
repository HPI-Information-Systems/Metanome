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

import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.backend.result_postprocessing.helper.ColumnInformation;
import de.metanome.backend.result_postprocessing.helper.TableInformation;
import de.metanome.backend.result_postprocessing.results.UniqueColumnCombinationResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Calculates the rankings for unique column combination results.
 */
public class UniqueColumnCombinationRanking extends Ranking {

  protected List<UniqueColumnCombinationResult> results;

  public UniqueColumnCombinationRanking(List<UniqueColumnCombinationResult> results,
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

    for (UniqueColumnCombinationResult result : this.results) {
      for (ColumnIdentifier column : result.getColumnCombination().getColumnIdentifiers()) {
        updateOccurrenceList(column);
      }
    }
  }

  @Override
  public void calculateDataIndependentRankings() {
    for (UniqueColumnCombinationResult result : this.results) {
      calculateColumnRatio(result);
      calculateOccurrenceRatio(result);
    }
  }

  @Override
  public void calculateDataDependentRankings() {
    for (UniqueColumnCombinationResult result : this.results) {
      calculateColumnRatio(result);
      calculateOccurrenceRatio(result);

      calculateUniquenessRatio(result);
      calculateRandomness(result);
    }
  }

  /**
   * Calculates the ratio of the size of the column combination and the column count of the
   * corresponding table.
   *
   * @param result the result
   */
  protected void calculateColumnRatio(UniqueColumnCombinationResult result) {
    Integer columnCount = result.getColumnCombination().getColumnIdentifiers().size();
    Integer tableColumnCount = this.tableInformationMap.get(result.getTableName()).getColumnCount();
    result.setColumnRatio((float) columnCount / tableColumnCount);
  }

  /**
   * Calculates the ratio of the unique column combination count and the overall occurrence of the
   * columns in the result.
   *
   * @param result the result
   */
  protected void calculateOccurrenceRatio(UniqueColumnCombinationResult result) {
    Set<ColumnIdentifier> columns = result.getColumnCombination().getColumnIdentifiers();
    String tableName = result.getTableName();

    result.setOccurrenceRatio(calculateOccurrenceRatio(columns, tableName));
  }

  /**
   * Calculate the ratio of the number of almost unique columns and all columns.
   *
   * @param result the result
   */
  protected void calculateUniquenessRatio(UniqueColumnCombinationResult result) {
    TableInformation table = this.tableInformationMap.get(result.getTableName());
    Set<ColumnIdentifier> columns = result.getColumnCombination().getColumnIdentifiers();

    result.setUniquenessRatio(calculateUniquenessRatio(table, columns));
  }

  /**
   * Calculates how unique the given column combination is. Therefor the uniqueness of each column
   * of the column combination is determined and multiplied. Afterward this value is normalized.
   *
   * @param result the result
   */
  protected void calculateRandomness(UniqueColumnCombinationResult result) {
    TableInformation table = this.tableInformationMap.get(result.getTableName());
    Map<String, ColumnInformation> columnInformationMap = table.getColumnInformationMap();
    Set<ColumnIdentifier> columns = result.getColumnCombination().getColumnIdentifiers();

    long min = table.getRowCount();
    long max = (min - 1) ^ columns.size();
    float distinctValue = 1;

    for (ColumnIdentifier column : columns) {
      distinctValue =
        distinctValue * columnInformationMap.get(column.getColumnIdentifier())
          .getDistinctValuesCount();
    }

    float randomness = (distinctValue - min) / (max - min);
    result.setRandomness(randomness);
  }

}
