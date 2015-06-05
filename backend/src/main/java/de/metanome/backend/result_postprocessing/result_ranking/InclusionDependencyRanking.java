package de.metanome.backend.result_postprocessing.result_ranking;

import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.ColumnPermutation;
import de.metanome.backend.result_postprocessing.helper.ColumnInformation;
import de.metanome.backend.result_postprocessing.helper.TableInformation;
import de.metanome.backend.result_postprocessing.results.InclusionDependencyResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Calculates the rankings for inclusion dependency results.
 */
public class InclusionDependencyRanking {

  protected static final float UNIQUENESS_THRESHOLD = 0.1f;

  protected List<InclusionDependencyResult> inclusionDependencyResults;
  protected Map<String, TableInformation> tableInformationMap;
  protected Map<String, Map<String, Integer>> occurrenceMap;


  public InclusionDependencyRanking(List<InclusionDependencyResult> inclusionDependencyResults,
                                    Map<String, TableInformation> tableInformationMap) {
    this.inclusionDependencyResults = inclusionDependencyResults;
    this.tableInformationMap = tableInformationMap;
    this.occurrenceMap = new HashMap<>();

    createOccurrenceList();
  }

  /**
   * The occurrence list stores how often a column occurs in the results.
   */
  protected void createOccurrenceList() {
    initializeOccurrenceList();
    for (InclusionDependencyResult result : this.inclusionDependencyResults) {
      for (ColumnIdentifier column : result.getReferenced().getColumnIdentifiers()) {
        updateOccurrenceList(column);
      }
      for (ColumnIdentifier column : result.getDependant().getColumnIdentifiers()) {
        updateOccurrenceList(column);
      }
    }
  }

  private void initializeOccurrenceList() {
    for (String tableName : tableInformationMap.keySet()) {
      Map<String, Integer> subMap = new HashMap<>();
      for (String columnName : tableInformationMap.get(tableName).getColumnInformationList().keySet()) {
        subMap.put(columnName, 0);
      }
      this.occurrenceMap.put(tableName, subMap);
    }
  }

  /**
   * Increases the occurrence of the given column in the given table.
   *
   * @param column the column identifier
   */
  protected void updateOccurrenceList(ColumnIdentifier column) {
    String columnName = column.getColumnIdentifier();
    String tableName = column.getTableIdentifier();

    Map<String, Integer> subMap = this.occurrenceMap.get(tableName);
    Integer oldValue = subMap.get(columnName);
    subMap.put(columnName, oldValue + 1);
    this.occurrenceMap.put(tableName, subMap);
  }

  /**
   * Calculates the data independent ranking and stores them in the results.
   */
  public void calculateDataIndependentRankings() {
    for (InclusionDependencyResult result : this.inclusionDependencyResults) {
      calculateSizeRatios(result);
      calculateColumnCountOccurrenceRatios(result);
    }
  }

  /**
   * Calculates the data dependent ranking and stores them in the results.
   */
  public void calculateDatDependentRankings() {
    for (InclusionDependencyResult result : this.inclusionDependencyResults) {
      calculateUniquenessRatios(result);
    }
  }

  /**
   * Calculates the ratio of the reference/dependant column count and the overall occurrence of the
   * referenced/dependant columns in the result.
   *
   * @param result the inclusion dependency result
   */
  protected void calculateColumnCountOccurrenceRatios(InclusionDependencyResult result) {
    // calculate referenced occurrence ratio
    ColumnPermutation referenced = result.getReferenced();
    result.setReferencedColumnOccurrence(
        calculateColumnCountOccurrenceRatio(referenced, result.getReferencedTableName()));

    // calculate dependant occurrence ratio
    ColumnPermutation dependant = result.getDependant();
    result.setDependantColumnOccurrence(
        calculateColumnCountOccurrenceRatio(dependant, result.getDependantTableName()));
  }

  /**
   * Calculate the ratio of the given column permutation count and the overall occurrence of the
   * columns in that column permutation.
   *
   * @param columnPermutation the column permutation
   * @param tableName         the table name
   * @return the ratio
   */
  protected float calculateColumnCountOccurrenceRatio(ColumnPermutation columnPermutation,
                                                      String tableName) {
    Integer referencedOccurrences = 0;
    for (ColumnIdentifier column : columnPermutation.getColumnIdentifiers()) {
      referencedOccurrences += this.occurrenceMap.get(tableName).get(column.getColumnIdentifier());
    }
    return (float) columnPermutation.getColumnIdentifiers().size() / referencedOccurrences;
  }

  /**
   * Calculates the ratio of the reference/dependant column count and the column count of the
   * corresponding table.
   *
   * @param result the result
   */
  protected void calculateSizeRatios(InclusionDependencyResult result) {
    Integer referencedColumnCount = result.getReferenced().getColumnIdentifiers().size();
    Integer dependantColumnCount = result.getDependant().getColumnIdentifiers().size();

    Integer
        referencedTableColumnCount =
        this.tableInformationMap.get(result.getReferencedTableName()).getColumnCount();
    Integer
        dependantTableColumnCount =
        this.tableInformationMap.get(result.getDependantTableName()).getColumnCount();

    result.setReferencedSizeRatio((float) referencedColumnCount / referencedTableColumnCount);
    result.setDependantSizeRatio((float) dependantColumnCount / dependantTableColumnCount);
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
    result.setReferencedUniqueRatio(referenceUniqueRatio);

    float dependantUniqueRatio = calculateUniquenessRatio(
        this.tableInformationMap.get(result.getDependantTableName()),
        result.getDependant().getColumnIdentifiers());
    result.setDependantUniqueRatio(dependantUniqueRatio);
  }

  protected float calculateUniquenessRatio(TableInformation table, List<ColumnIdentifier> columns) {
    Map<String, ColumnInformation> columnInformationList = table.getColumnInformationList();
    Integer uniqueColumns = 0;

    for (ColumnIdentifier column : columns) {
      if (columnInformationList.get(column.getColumnIdentifier()).getUniquenessRate()
          < UNIQUENESS_THRESHOLD) {
        uniqueColumns++;
      }
    }

    return (float) uniqueColumns / columns.size();
  }

}
