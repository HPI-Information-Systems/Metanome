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

import java.util.HashMap;
import java.util.Map;

public abstract class Ranking {

  protected Map<String, TableInformation> tableInformationMap;
  protected Map<String, Map<String, Integer>> occurrenceMap;

  public Ranking(Map<String, TableInformation> tableInformationMap) {
    this.tableInformationMap = tableInformationMap;
  }

  /**
   *
   */
  public abstract void calculateDataIndependentRankings();

  public abstract void calculateDataDependentRankings();

  /**
   * Initializes the occurrence list, so that each entry is present.
   */
  protected void initializeOccurrenceList() {
    for (String tableName : this.tableInformationMap.keySet()) {
      Map<String, Integer> subMap = new HashMap<>();
      for (String columnName : this.tableInformationMap.get(tableName).getColumnInformationList()
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
    String tableName = column.getTableIdentifier();

    Map<String, Integer> subMap = this.occurrenceMap.get(tableName);
    Integer oldValue = subMap.get(columnName);
    subMap.put(columnName, oldValue + 1);
    this.occurrenceMap.put(tableName, subMap);
  }


}
