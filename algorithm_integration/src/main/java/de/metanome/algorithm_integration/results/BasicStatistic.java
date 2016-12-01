/**
 * Copyright 2014-2016 by Metanome Project
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
package de.metanome.algorithm_integration.results;

import com.fasterxml.jackson.annotation.JsonTypeName;
import de.metanome.algorithm_integration.ColumnCombination;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.result_receiver.ColumnNameMismatchException;
import de.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;
import de.metanome.algorithm_integration.results.basic_statistic_values.BasicStatisticValue;

import javax.xml.bind.annotation.XmlTransient;
import java.util.HashMap;
import java.util.Map;


/**
 * Represents a basic statistic result.
 */
@JsonTypeName("BasicStatistic")
public class BasicStatistic implements Result {

  public static final String NAME_COLUMN_SEPARATOR = " - ";
  public static final String COLUMN_VALUE_SEPARATOR = ": ";
  public static final String STATISTIC_SEPARATOR = "; ";

  private static final long serialVersionUID = -8010850754433867718L;

  protected ColumnCombination columnCombination;
  protected Map<String, BasicStatisticValue> statisticMap;

  /**
   * Exists for serialization.
   */
  protected BasicStatistic() {
    this.columnCombination = new ColumnCombination();
    this.statisticMap = new HashMap<>();
  }

  /**
   * Creates a basic statistic result for the given columns.
   * @param columnIdentifier the column identifiers
   */
  public BasicStatistic(ColumnIdentifier... columnIdentifier) {
    this.columnCombination = new ColumnCombination(columnIdentifier);
    this.statisticMap = new HashMap<>();
  }

  /**
   * Creates a basic statistic results for the given columns with the given statistics.
   * @param statisticMap     the statistics
   * @param columnIdentifier the column identifiers
   */
  public BasicStatistic(Map<String, BasicStatisticValue> statisticMap, ColumnIdentifier... columnIdentifier) {
    this.columnCombination = new ColumnCombination(columnIdentifier);
    this.statisticMap = statisticMap;
  }

  /**
   * Adds a statistic to the result
   * @param statisticName  the name of the statistic
   * @param statisticValue the value of the statistic
   */
  public void addStatistic(String statisticName, BasicStatisticValue statisticValue) {
    this.statisticMap.put(statisticName, statisticValue);
  }

  /**
   * @return the columnCombination
   */
  public ColumnCombination getColumnCombination() {
    return columnCombination;
  }

  public void setColumnCombination(ColumnCombination columnCombination) {
    this.columnCombination = columnCombination;
  }

  public Map<String, BasicStatisticValue> getStatisticMap() {
    return statisticMap;
  }

  /**
   * Sets the statistics of the result.
   * @param statisticMap the statistic map containing all statistics
   */
    public void setStatisticMap(Map<String, BasicStatisticValue> statisticMap) {
    this.statisticMap = statisticMap;
  }

  @Override
  @XmlTransient
  public void sendResultTo(OmniscientResultReceiver resultReceiver)
    throws CouldNotReceiveResultException, ColumnNameMismatchException {
    resultReceiver.receiveResult(this);
  }

  @Override
  public String toString() {
    String str = columnCombination.toString() + COLUMN_VALUE_SEPARATOR;

    for (Map.Entry<String, BasicStatisticValue> entry : this.statisticMap.entrySet()) {
      str += entry.getKey() + NAME_COLUMN_SEPARATOR + entry.getValue() + STATISTIC_SEPARATOR;
    }

    return str;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((columnCombination == null) ? 0 : columnCombination.hashCode());
    result = prime * result
      + ((this.statisticMap.isEmpty()) ? 0 : this.statisticMap.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    BasicStatistic other = (BasicStatistic) obj;
    if (columnCombination == null) {
      if (other.columnCombination != null) {
        return false;
      }
    } else if (!columnCombination.equals(other.columnCombination)) {
      return false;
    }
    if (!this.statisticMap.equals(other.statisticMap)) {
      return false;
    }
    return true;
  }

}
