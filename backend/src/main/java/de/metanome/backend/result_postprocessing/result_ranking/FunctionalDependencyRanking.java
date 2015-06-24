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
import de.metanome.backend.result_postprocessing.helper.TableInformation;
import de.metanome.backend.result_postprocessing.results.FunctionalDependencyResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Calculates the rankings for functional dependency results.
 */
public class FunctionalDependencyRanking extends Ranking {

  protected List<FunctionalDependencyResult> results;

  public FunctionalDependencyRanking(List<FunctionalDependencyResult> results,
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

    for (FunctionalDependencyResult result : this.results) {
      updateOccurrenceList(result.getDependant());
      for (ColumnIdentifier column : result.getDeterminant().getColumnIdentifiers()) {
        updateOccurrenceList(column);
      }
    }
  }

  @Override
  public void calculateDataIndependentRankings() {
    for (FunctionalDependencyResult result : this.results) {
      calculateColumnRatios(result);
      calculateGeneralCoverage(result);
      calculateOccurrenceRatios(result);
    }
  }

  @Override
  public void calculateDataDependentRankings() {
    for (FunctionalDependencyResult result : this.results) {
      calculateColumnRatios(result);
      calculateGeneralCoverage(result);
      calculateOccurrenceRatios(result);
      calculateUniquenessRatios(result);
    }
  }

  /**
   * Calculates the ratio of the determinant/dependant column count and the column count of the
   * whole result.
   *
   * @param result the result
   */
  protected void calculateColumnRatios(FunctionalDependencyResult result) {
    int determinantColumnCount = result.getDeterminant().getColumnIdentifiers().size();
    int dependantColumnCount = result.getExtendedDependant().getColumnIdentifiers().size();

    int overallSize = dependantColumnCount + determinantColumnCount;

    result.setDeterminantColumnRatio((float) determinantColumnCount / overallSize);
    result.setDependantColumnRatio((float) dependantColumnCount / overallSize);
  }

  /**
   * Calculates the relation between the total number of columns of the result and the number of
   * columns of tables, which are involved.
   *
   * @param result the result
   */
  protected void calculateGeneralCoverage(FunctionalDependencyResult result) {
    int referencedColumnCount = result.getDeterminant().getColumnIdentifiers().size();
    int dependantColumnCount = result.getExtendedDependant().getColumnIdentifiers().size();

    int tableCount;
    if (result.getDeterminantTableName().equals(result.getDependantTableName())) {
      tableCount = this.tableInformationMap.get(result.getDeterminantTableName()).getColumnCount();
    } else {
      tableCount = this.tableInformationMap.get(result.getDeterminantTableName()).getColumnCount() +
                   this.tableInformationMap.get(result.getDependantTableName()).getColumnCount();
    }

    result.setGeneralCoverage(((float) referencedColumnCount + dependantColumnCount) / tableCount);
  }

  /**
   * Calculates the ratio of the determinant/dependant column count and the overall occurrence of the
   * determinant/dependant columns in the result.
   *
   * @param result the result
   */
  protected void calculateOccurrenceRatios(FunctionalDependencyResult result) {
    // calculate dependant occurrence ratio
    Set<ColumnIdentifier> dependant = result.getExtendedDependant().getColumnIdentifiers();
    result.setDependantOccurrenceRatio(
        calculateOccurrenceRatio(dependant, result.getDependantTableName()));

    // calculate determinant occurrence ratio
    Set<ColumnIdentifier> determinant = result.getDeterminant().getColumnIdentifiers();
    result.setDeterminantOccurrenceRatio(
        calculateOccurrenceRatio(determinant, result.getDeterminantTableName()));
  }

  /**
   * Calculates the ratio of columns of the determinant/dependant side, which are almost unique, and
   * the column count of the determinant/dependant side. How many of the column in the
   * determinant/dependant side are almost unique?
   *
   * @param result the result
   */
  protected void calculateUniquenessRatios(FunctionalDependencyResult result) {
    float determinantUniqueRatio = calculateUniquenessRatio(
        this.tableInformationMap.get(result.getDeterminantTableName()),
        result.getDeterminant().getColumnIdentifiers());
    result.setDeterminantUniquenessRatio(determinantUniqueRatio);

    float dependantUniqueRatio = calculateUniquenessRatio(
        this.tableInformationMap.get(result.getDependantTableName()),
        result.getExtendedDependant().getColumnIdentifiers());
    result.setDependantUniquenessRatio(dependantUniqueRatio);
  }

}
