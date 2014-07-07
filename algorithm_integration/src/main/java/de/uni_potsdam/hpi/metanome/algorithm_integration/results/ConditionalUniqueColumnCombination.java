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
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCondition;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Represents a conditional unique column combination
 *
 * @author Jens Hildebrandt
 */
public class ConditionalUniqueColumnCombination implements Result {

  public static final String CUCC_SEPARATOR = " | ";
  private static final long serialVersionUID = 6946896625820917113L;
  protected ColumnCombination columnCombination;
  protected TreeSet<ColumnCondition> conditionList;


  /**
   * Exists for GWT serialization.
   */
  protected ConditionalUniqueColumnCombination() {
    this.columnCombination = new ColumnCombination();
    this.conditionList = new TreeSet<>();
  }

  /**
   * Constructs a {@link de.uni_potsdam.hpi.metanome.algorithm_integration.results.ConditionalUniqueColumnCombination}
   * from a {@link ColumnCombination} and an array of {@link de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCondition}s.
   *
   * @param columnCombination a supposedly unique column combination
   * @param columnConditions  array of conditions for the CUCC
   */
  public ConditionalUniqueColumnCombination(ColumnCombination columnCombination,
                                            ColumnCondition... columnConditions) {
    this();
    this.columnCombination = columnCombination;
    for (ColumnCondition columnCondition : columnConditions) {
      this.conditionList.add(columnCondition);
    }
  }

  /**
   * Constructs a {@link de.uni_potsdam.hpi.metanome.algorithm_integration.results.ConditionalUniqueColumnCombination}
   * from a {@link ColumnCombination} and a list of {@link de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCondition}s.
   *
   * @param columnCombination a supposedly unique column combination
   * @param columnConditions  list of conditions for the CUCC
   */
  public ConditionalUniqueColumnCombination(ColumnCombination columnCombination,
                                            List<ColumnCondition> columnConditions) {
    this(columnCombination, columnConditions.toArray(new ColumnCondition[columnConditions.size()]));
  }


  @Override
  public void sendResultTo(OmniscientResultReceiver resultReceiver)
      throws CouldNotReceiveResultException {
    resultReceiver.receiveResult(this);
  }

  /**
   * @return the column combination
   */
  public ColumnCombination getColumnCombination() {
    return this.columnCombination;
  }

  /**
   * @return the condition list
   */
  public Set<ColumnCondition> getConditions() {
    return this.conditionList;
  }

  @Override
  public String toString() {
    return columnCombination.toString() + conditionList.toString();

  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ConditionalUniqueColumnCombination that = (ConditionalUniqueColumnCombination) o;

    if (columnCombination != null ? !columnCombination.equals(that.columnCombination)
                                  : that.columnCombination != null) {
      return false;
    }
    if (conditionList != null ? !conditionList.equals(that.conditionList)
                              : that.conditionList != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = columnCombination != null ? columnCombination.hashCode() : 0;
    result = 31 * result + (conditionList != null ? conditionList.hashCode() : 0);
    return result;
  }
}
