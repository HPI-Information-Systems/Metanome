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
import de.metanome.backend.result_postprocessing.helper.TableInformation;
import de.metanome.backend.result_postprocessing.results.OrderDependencyResult;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Calculates the rankings for order dependency results.
 */
public class OrderDependencyRanking extends Ranking {

  protected List<OrderDependencyResult> results;

  public OrderDependencyRanking(List<OrderDependencyResult> results,
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

    for (OrderDependencyResult result : this.results) {
      for (ColumnIdentifier column : result.getLhs().getColumnIdentifiers()) {
        updateOccurrenceList(column);
      }
      for (ColumnIdentifier column : result.getRhs().getColumnIdentifiers()) {
        updateOccurrenceList(column);
      }
    }
  }

  @Override
  public void calculateDataIndependentRankings() {
    for (OrderDependencyResult result : this.results) {
      calculateColumnRatios(result);
      calculateGeneralCoverage(result);
      calculateOccurrenceRatios(result);
    }
  }

  @Override
  public void calculateDataDependentRankings() {
    for (OrderDependencyResult result : this.results) {
      calculateColumnRatios(result);
      calculateGeneralCoverage(result);
      calculateOccurrenceRatios(result);

      calculateUniquenessRatios(result);
    }
  }

  /**
   * Calculates the ratio of the lhs/rhs column count and the column count of the corresponding
   * table.
   *
   * @param result the result
   */
  protected void calculateColumnRatios(OrderDependencyResult result) {
    Integer lhsColumnCount = result.getLhs().getColumnIdentifiers().size();
    Integer rhsColumnCount = result.getRhs().getColumnIdentifiers().size();

    Integer
      lhsTableColumnCount =
      this.tableInformationMap.get(result.getLhsTableName()).getColumnCount();
    Integer
      rhsTableColumnCount =
      this.tableInformationMap.get(result.getRhsTableName()).getColumnCount();

    result.setLhsColumnRatio((float) lhsColumnCount / lhsTableColumnCount);
    result.setRhsColumnRatio((float) rhsColumnCount / rhsTableColumnCount);
  }


  /**
   * Calculates the relation between the total number of columns of the result and the number of
   * columns of tables, which are involved.
   *
   * @param result the result
   */
  protected void calculateGeneralCoverage(OrderDependencyResult result) {
    Integer referencedColumnCount = result.getLhs().getColumnIdentifiers().size();
    Integer dependantColumnCount = result.getRhs().getColumnIdentifiers().size();

    int tableCount;
    if (result.getRhsTableName().equals(result.getLhsTableName())) {
      tableCount = this.tableInformationMap.get(result.getRhsTableName()).getColumnCount();
    } else {
      tableCount = this.tableInformationMap.get(result.getRhsTableName()).getColumnCount() +
        this.tableInformationMap.get(result.getLhsTableName()).getColumnCount();
    }

    result.setGeneralCoverage(((float) referencedColumnCount + dependantColumnCount) / tableCount);
  }

  /**
   * Calculates the ratio of the lhs/rhs column count and the overall occurrence of the lhs/rhs
   * columns in the result.
   *
   * @param result the result
   */
  protected void calculateOccurrenceRatios(OrderDependencyResult result) {
    // calculate lhs occurrence ratio
    List<ColumnIdentifier> lhs = result.getLhs().getColumnIdentifiers();
    result.setLhsOccurrenceRatio(
      calculateOccurrenceRatio(new HashSet<>(lhs), result.getLhsTableName()));

    // calculate rhs occurrence ratio
    List<ColumnIdentifier> rhs = result.getRhs().getColumnIdentifiers();
    result.setRhsOccurrenceRatio(
      calculateOccurrenceRatio(new HashSet<>(rhs), result.getRhsTableName()));
  }

  /**
   * Calculates the ratio of columns of the lhs/rhs side, which are almost unique, and the column
   * count of the lhs/rhs side.
   *
   * @param result the result
   */
  protected void calculateUniquenessRatios(OrderDependencyResult result) {
    float lhsUniqueRatio = calculateUniquenessRatio(
      this.tableInformationMap.get(result.getLhsTableName()),
      new HashSet<>(result.getLhs().getColumnIdentifiers()));
    result.setLhsUniquenessRatio(lhsUniqueRatio);

    float rhsUniqueRatio = calculateUniquenessRatio(
      this.tableInformationMap.get(result.getRhsTableName()),
      new HashSet<>(result.getRhs().getColumnIdentifiers()));
    result.setRhsUniquenessRatio(rhsUniqueRatio);
  }


}
