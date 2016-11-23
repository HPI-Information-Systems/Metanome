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
import de.metanome.backend.result_postprocessing.results.InclusionDependencyResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Calculates the rankings for inclusion dependency results.
 */
public class InclusionDependencyRanking extends Ranking {

  protected List<InclusionDependencyResult> results;

  public InclusionDependencyRanking(List<InclusionDependencyResult> results,
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

    for (InclusionDependencyResult result : this.results) {
      for (ColumnIdentifier column : result.getReferenced().getColumnIdentifiers()) {
        updateOccurrenceList(column);
      }
      for (ColumnIdentifier column : result.getDependant().getColumnIdentifiers()) {
        updateOccurrenceList(column);
      }
    }
  }

  /**
   * Calculates the data independent ranking and stores them in the results.
   */
  @Override
  public void calculateDataIndependentRankings() {
    for (InclusionDependencyResult result : this.results) {
      calculateColumnRatios(result);
      calculateOccurrenceRatios(result);
      calculateGeneralCoverage(result);
    }
  }

  /**
   * Calculates the data dependent ranking and stores them in the results.
   */
  @Override
  public void calculateDataDependentRankings() {
    for (InclusionDependencyResult result : this.results) {
      calculateColumnRatios(result);
      calculateOccurrenceRatios(result);
      calculateGeneralCoverage(result);

      calculateUniquenessRatios(result);
    }
  }

  /**
   * Calculates the ratio of the reference/dependant column count and the overall occurrence of the
   * referenced/dependant columns in the result.
   *
   * @param result the inclusion dependency result
   */
  protected void calculateOccurrenceRatios(InclusionDependencyResult result) {
    // calculate referenced occurrence ratio
    List<ColumnIdentifier> referenced = result.getReferenced().getColumnIdentifiers();
    result.setReferencedOccurrenceRatio(
      calculateOccurrenceRatio(referenced, result.getReferencedTableName()));

    // calculate dependant occurrence ratio
    List<ColumnIdentifier> dependant = result.getDependant().getColumnIdentifiers();
    result.setDependantOccurrenceRatio(
      calculateOccurrenceRatio(dependant, result.getDependantTableName()));
  }

  /**
   * Calculates the ratio of the reference/dependant column count and the column count of the
   * corresponding table.
   *
   * @param result the result
   */
  protected void calculateColumnRatios(InclusionDependencyResult result) {
    Integer referencedColumnCount = result.getReferenced().getColumnIdentifiers().size();
    Integer dependantColumnCount = result.getDependant().getColumnIdentifiers().size();

    Integer
      referencedTableColumnCount =
      this.tableInformationMap.get(result.getReferencedTableName()).getColumnCount();
    Integer
      dependantTableColumnCount =
      this.tableInformationMap.get(result.getDependantTableName()).getColumnCount();

    result.setReferencedColumnRatio((float) referencedColumnCount / referencedTableColumnCount);
    result.setDependantColumnRatio((float) dependantColumnCount / dependantTableColumnCount);
  }

  /**
   * Calculates the relation between the total number of columns of the result and the number of
   * columns of tables, which are involved.
   *
   * @param result the inclusion dependency result
   */
  protected void calculateGeneralCoverage(InclusionDependencyResult result) {
    Integer referencedColumnCount = result.getReferenced().getColumnIdentifiers().size();
    Integer dependantColumnCount = result.getDependant().getColumnIdentifiers().size();

    int tableCount;
    if (result.getReferencedTableName().equals(result.getDependantTableName())) {
      tableCount = this.tableInformationMap.get(result.getReferencedTableName()).getColumnCount();
    } else {
      tableCount = this.tableInformationMap.get(result.getReferencedTableName()).getColumnCount() +
        this.tableInformationMap.get(result.getDependantTableName()).getColumnCount();
    }

    result.setGeneralCoverage(((float) referencedColumnCount + dependantColumnCount) / tableCount);
  }

  /**
   * Calculates the ratio of columns of the reference/dependant side, which are almost unique, and
   * the column count of the reference/dependant side. How many of the column in the
   * reference/dependant side are almost unique?
   *
   * @param result the inclusion dependency result
   */
  protected void calculateUniquenessRatios(InclusionDependencyResult result) {
    float referenceUniqueRatio = calculateUniquenessRatio(
      this.tableInformationMap.get(result.getReferencedTableName()),
      result.getReferenced().getColumnIdentifiers());
    result.setReferencedUniquenessRatio(referenceUniqueRatio);

    float dependantUniqueRatio = calculateUniquenessRatio(
      this.tableInformationMap.get(result.getDependantTableName()),
      result.getDependant().getColumnIdentifiers());
    result.setDependantUniquenessRatio(dependantUniqueRatio);
  }

}
