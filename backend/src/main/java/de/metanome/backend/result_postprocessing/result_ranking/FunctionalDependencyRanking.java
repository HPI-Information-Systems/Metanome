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

import de.metanome.algorithm_helper.data_structures.PLIBuilder;
import de.metanome.algorithm_helper.data_structures.PositionListIndex;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.backend.result_postprocessing.helper.ColumnInformation;
import de.metanome.backend.result_postprocessing.helper.TableInformation;
import de.metanome.backend.result_postprocessing.results.FunctionalDependencyResult;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
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
  public void calculateDataDependentRankings()
      throws InputGenerationException, InputIterationException {
    for (FunctionalDependencyResult result : this.results) {
      calculateColumnRatios(result);
      calculateGeneralCoverage(result);
      calculateOccurrenceRatios(result);
      calculateUniquenessRatios(result);

      // The pollution rank and information gain are
      // only defined on one table
      if (this.tableInformationMap.size() == 1) {
        TableInformation tableInformation = this.tableInformationMap.values().iterator().next();
        Map<Integer, PositionListIndex> PLIs = createPLIs(tableInformation);
        tableInformation.setPLIs(PLIs);

        calculatePollution(result, tableInformation, PLIs);
        calculateInformationGainCells(result, tableInformation, PLIs);
        calculateInformationGainBytes(result, tableInformation, PLIs);
      }
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
   * Calculates the ratio of the determinant/dependant column count and the overall occurrence of
   * the determinant/dependant columns in the result.
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

  /**
   * Calculates the pollution rank for a given functional dependency result. The pollution rank is
   * defined as the minimum number of tuples which need to be changed so that a new column can be
   * added to the dependant.
   *
   * @param result the functional dependency result
   */
  protected void calculatePollution(FunctionalDependencyResult result,
                                    TableInformation tableInformation,
                                    Map<Integer, PositionListIndex> PLIs)
      throws InputGenerationException, InputIterationException {
    // Ignore functional dependencies which are already determine all columns
    if (result.getGeneralCoverage() == 1.0) {
      result.setPollution(0.0f);
      result.setPollutionColumn("");
      return;
    }

    // Determine all columns which should be checked as possible dependants
    BitSet candidateSet = new BitSet(tableInformation.getColumnCount());
    candidateSet.or(result.getExtendedDependantAsBitSet());
    candidateSet.or(result.getDeterminantAsBitSet());
    candidateSet.flip(0, tableInformation.getColumnCount());

    // Get the minimal key error
    float minKeyError = -1;
    for (int columnIndex = candidateSet.nextSetBit(0); columnIndex != -1;
         columnIndex = candidateSet.nextSetBit(columnIndex + 1)) {
      // Get the test columns as bit set
      BitSet testColumn = new BitSet();
      testColumn.set(columnIndex);
      BitSet columnsToTest = (BitSet) result.getDeterminantAsBitSet().clone();
      columnsToTest.or(testColumn);
      // Calculate the key error
      float keyError =
          Math.abs(calculateKeyError(columnsToTest, PLIs) -
                   calculateKeyError(result.getDeterminantAsBitSet(), PLIs));
      minKeyError = (minKeyError == -1) ? keyError : Math.min(minKeyError, keyError);
      // Set the name of the minimal polluted column
      if (minKeyError == keyError) {
        result.setPollutionColumn(tableInformation.getColumn(columnIndex).getColumnName());
      }
    }

    // Calculate and store the pollution rank
    result.setPollution(minKeyError / (float) tableInformation.getRowCount());
  }

  /**
   * Calculates the key error for the given columns using the given PLIs. The key error is equal to
   * the number of entries, which has to be removed, so that the columns become unique.
   *
   * @param columns the columns as BitSet
   * @param PLIs    the position list indexes
   * @return the key error
   */
  protected long calculateKeyError(BitSet columns, Map<Integer, PositionListIndex> PLIs) {
    List<Integer> indexes = new ArrayList<>();
    for (int i = columns.nextSetBit(0); i != -1; i = columns.nextSetBit(i + 1)) {
      indexes.add(i);
    }

    if (indexes.isEmpty()) {
      return 0L;
    }

    PositionListIndex pli = PLIs.get(indexes.get(0));
    for (int i = 1; i < indexes.size(); i++) {
      pli = pli.intersect(PLIs.get(indexes.get(i)));
    }

    return pli.getRawKeyError();
  }

  /**
   * Calculates the information gain of the given functional dependency in cells. How many cells can
   * be removed, when using the functional dependency for normalization?
   *
   * @param result           the result
   * @param tableInformation the table
   * @param PLIs             the position list indices for each column
   */
  protected void calculateInformationGainCells(FunctionalDependencyResult result,
                                             TableInformation tableInformation,
                                             Map<Integer, PositionListIndex> PLIs) {
    long rowCount = tableInformation.getRowCount();
    long columnCount = tableInformation.getColumnCount();
    int determinantColumnCount = result.getDeterminant().getColumnIdentifiers().size();
    int dependantColumnCount = result.getExtendedDependant().getColumnIdentifiers().size();

    // Calculate the cell count of the original table
    long cellCountTable = columnCount * rowCount;

    // Normalizing the table using the given functional dependency:
    // The original table lose the dependant columns
    long cellCountWithoutDependant = (columnCount - dependantColumnCount) * rowCount;

    // Calculate the number of cells in the new "determinant-dependant" table
    long uniqueRowsCount = rowCount - calculateKeyError(result.getDeterminantAsBitSet(), PLIs);
    long cellCountNewTable = (dependantColumnCount + determinantColumnCount) * uniqueRowsCount;

    // Store the information gain in the ranking structure
    result
        .setInformationGainCells(cellCountTable - (cellCountWithoutDependant + cellCountNewTable));
  }

  /**
   * Calculates the information gain of the given functional dependency in bytes. How many bytes can
   * be saved, when using the functional dependency for normalization?
   *
   * @param result           the result
   * @param tableInformation the table
   * @param PLIs             the position list indices for each column
   */
  protected void calculateInformationGainBytes(FunctionalDependencyResult result,
                                             TableInformation tableInformation,
                                             Map<Integer, PositionListIndex> PLIs) {
    long rowCount = tableInformation.getRowCount();

    // Get the information content of the whole table in bytes
    long informationGainTable = tableInformation.getInformationContent();

    Map<String, ColumnInformation>
        columnInformationMap =
        tableInformation.getColumnInformationMap();
    Set<String> dependantColumnNames = new HashSet<>();
    for (ColumnIdentifier column : result.getExtendedDependant().getColumnIdentifiers()) {
      dependantColumnNames.add(column.getColumnIdentifier());
    }

    // Normalizing the table using the given functional dependency:
    // The original table lose the dependant columns
    // Get the information gain for this new table
    long informationGainTableWithoutDependant = 0L;
    for (String columnName : columnInformationMap.keySet()) {
      if (!dependantColumnNames.contains(columnName)) {
        informationGainTableWithoutDependant +=
            columnInformationMap.get(columnName).getInformationContent(rowCount);
      }
    }

    // Calculate the information gain in the new "determinant-dependant" table
    long uniqueRowsCount = rowCount - calculateKeyError(result.getDeterminantAsBitSet(), PLIs);
    long informationGainNewTable = 0L;
    for (ColumnIdentifier column : result.getDeterminant().getColumnIdentifiers()) {
      informationGainNewTable +=
          columnInformationMap.get(column.getColumnIdentifier())
              .getInformationContent(uniqueRowsCount);
    }
    for (ColumnIdentifier column : result.getExtendedDependant().getColumnIdentifiers()) {
      informationGainNewTable +=
          columnInformationMap.get(column.getColumnIdentifier())
              .getInformationContent(uniqueRowsCount);
    }

    // Store the information gain in the result
    result.setInformationGainBytes(informationGainTable -
                                   (informationGainTableWithoutDependant
                                    + informationGainNewTable));
  }

  /**
   * Creates the position list indices for the given table.
   *
   * @param tableInformation the table
   * @return a map containing for each column its position list index
   */
  protected Map<Integer, PositionListIndex> createPLIs(TableInformation tableInformation)
      throws InputGenerationException, InputIterationException {
    int index = 0;
    Map<Integer, PositionListIndex> pliList = new HashMap<>();

    PLIBuilder pliBuilder =
        new PLIBuilder(tableInformation.getRelationalInputGenerator().generateNewCopy());
    List<PositionListIndex> PLIs = pliBuilder.getPLIList();

    for (PositionListIndex PLI : PLIs) {
      pliList.put(index, PLI);
      index++;
    }

    return pliList;

  }

}
