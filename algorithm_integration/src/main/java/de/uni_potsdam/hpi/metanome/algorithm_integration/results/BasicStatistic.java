/*
 * Copyright 2014 by the Metanome project
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

package de.uni_potsdam.hpi.metanome.algorithm_integration.results;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;

public class BasicStatistic implements Result {

  public static final String NAME_COLUMN_SEPARATOR = " of ";
  public static final String COLUMN_VALUE_SEPARATOR = ": ";
  private static final long serialVersionUID = 3334423877877174177L;
  ColumnCombination columns;
  String statisticName;
  Object statisticValue;

  /**
   * Exists for GWT serialization.
   */
  protected BasicStatistic() {
    this.columns = new ColumnCombination();
    this.statisticName = "";
  }

  public BasicStatistic(String statisticName, Object statisticValue,
                        ColumnIdentifier... columnIdentifier) {
    this.columns = new ColumnCombination(columnIdentifier);
    this.statisticName = statisticName;
    this.statisticValue = statisticValue;
  }


  @Override
  public void sendResultTo(OmniscientResultReceiver resultReceiver)
      throws CouldNotReceiveResultException {
    resultReceiver.receiveResult(this);
  }

  /**
   * @return the columns
   */
  public ColumnCombination getColumnCombination() {
    return columns;
  }

  /**
   * @return the name of the statistic
   */
  public String getStatisticName() {
    return statisticName;
  }

  /**
   * @return the value of the statistic on the columns
   */
  public Object getStatisticValue() {
    return statisticValue;
  }

  @Override
  public String toString() {
    return statisticName + NAME_COLUMN_SEPARATOR + columns.toString() + COLUMN_VALUE_SEPARATOR
           + statisticValue.toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((columns == null) ? 0 : columns.hashCode());
    result = prime * result
             + ((statisticName == null) ? 0 : statisticName.hashCode());
    result = prime * result
             + ((statisticValue == null) ? 0 : statisticValue.hashCode());
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
    if (columns == null) {
      if (other.columns != null) {
        return false;
      }
    } else if (!columns.equals(other.columns)) {
      return false;
    }
    if (statisticName == null) {
      if (other.statisticName != null) {
        return false;
      }
    } else if (!statisticName.equals(other.statisticName)) {
      return false;
    }
    if (statisticValue == null) {
      if (other.statisticValue != null) {
        return false;
      }
    } else if (!statisticValue.equals(other.statisticValue)) {
      return false;
    }
    return true;
  }

}
