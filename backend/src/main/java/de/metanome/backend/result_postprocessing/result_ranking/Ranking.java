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

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.backend.result_postprocessing.helper.ColumnInformation;
import de.metanome.backend.result_postprocessing.helper.StringHelper;
import de.metanome.backend.result_postprocessing.helper.TableInformation;

import java.util.*;

public abstract class Ranking {

  protected static final float UNIQUENESS_THRESHOLD = 0.9f;

  protected Map<String, TableInformation> tableInformationMap;
  protected Map<String, Map<String, Integer>> occurrenceMap;


  public Ranking(Map<String, TableInformation> tableInformationMap) {
    this.tableInformationMap = tableInformationMap;
  }

  /**
   * Calculate data independent rankings.
   */
  public abstract void calculateDataIndependentRankings();

  /**
   * Calculate data dependent rankings.
   * @throws de.metanome.algorithm_integration.input.InputGenerationException if the input is not accessible
   * @throws de.metanome.algorithm_integration.input.InputIterationException if the input is not iterable
   * @throws de.metanome.algorithm_integration.AlgorithmConfigurationException if input generator could not be build
   */
  public abstract void calculateDataDependentRankings()
    throws InputGenerationException, InputIterationException, AlgorithmConfigurationException;

  /**
   * Initializes the occurrence list, so that each entry is present.
   */
  protected void initializeOccurrenceList() {
    for (String tableName : this.tableInformationMap.keySet()) {
      Map<String, Integer> subMap = new HashMap<>();
      for (String columnName : this.tableInformationMap.get(tableName).getColumnInformationMap()
        .keySet()) {
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
    String tableName = StringHelper.removeFileEnding(column.getTableIdentifier());

    Map<String, Integer> subMap = this.occurrenceMap.get(tableName);
    Integer oldValue = subMap.get(columnName);
    subMap.put(columnName, oldValue + 1);
    this.occurrenceMap.put(tableName, subMap);
  }

  /**
   * Calculate the ratio of the given number of columns and the overall occurrence of the columns.
   *
   * @param columns   the columns
   * @param tableName the table name
   * @return the ratio
   */
  protected float calculateOccurrenceRatio(List<ColumnIdentifier> columns,
                                           String tableName) {
    Integer occurrences = 0;
    for (ColumnIdentifier column : columns) {
      occurrences += this.occurrenceMap.get(tableName).get(
        column.getColumnIdentifier());
    }
    return (float) columns.size() / occurrences;
  }

  /**
   * Calculate the ratio of the given number of columns and the overall occurrence of the columns.
   *
   * @param columns   the columns
   * @param tableName the table name
   * @return the ratio
   */
  protected float calculateOccurrenceRatio(Set<ColumnIdentifier> columns,
                                           String tableName) {
    return calculateOccurrenceRatio(new ArrayList<>(columns), tableName);
  }

  /**
   * Calculate the ratio of the number of almost unique columns and all columns
   *
   * @param table   the table, the columns belong to
   * @param columns the columns
   * @return the ratio
   */
  protected float calculateUniquenessRatio(TableInformation table, List<ColumnIdentifier> columns) {
    Map<String, ColumnInformation> columnInformationList = table.getColumnInformationMap();
    Integer uniqueColumns = 0;

    for (ColumnIdentifier column : columns) {
      if (columnInformationList.get(column.getColumnIdentifier()).getUniquenessRate()
        >= UNIQUENESS_THRESHOLD) {
        uniqueColumns++;
      }
    }

    return (float) uniqueColumns / columns.size();
  }

  /**
   * Calculate the ratio of the number of almost unique columns and all columns
   *
   * @param table   the table, the columns belong to
   * @param columns the columns
   * @return the ratio
   */
  protected float calculateUniquenessRatio(TableInformation table, Set<ColumnIdentifier> columns) {
    return calculateUniquenessRatio(table, new ArrayList<>(columns));
  }

}
