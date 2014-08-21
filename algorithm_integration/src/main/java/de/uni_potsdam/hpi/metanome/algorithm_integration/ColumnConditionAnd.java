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

package de.uni_potsdam.hpi.metanome.algorithm_integration;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

/**
 * Logical "and" used in the composite pattern to represent column condition. Contains subcondition
 * that are concatenated by "and".
 *
 * @author Jens Hildebrandt
 */
public class ColumnConditionAnd implements ColumnCondition {

  protected boolean isNegated = false;
  protected TreeSet<ColumnCondition> columnValues;

  /**
   * Exists for Gwt serialization
   */
  protected ColumnConditionAnd() {
    this.columnValues = new TreeSet<>();
  }

  public ColumnConditionAnd(Map<ColumnIdentifier, String> conditionMap) {
    this();
    for (ColumnIdentifier column : conditionMap.keySet()) {

      columnValues.add(new ColumnConditionValue(column, conditionMap.get(column)));
    }
  }

  public ColumnConditionAnd(TreeSet<ColumnCondition> treeSet) {
    this.columnValues = new TreeSet<>(treeSet);
  }

  public ColumnConditionAnd(ColumnCondition... conditions) {
    this();
    for (ColumnCondition condition : conditions) {
      this.columnValues.add(condition);
    }
  }

  public void setIsNegated(boolean isNegated) {
    this.isNegated = isNegated;
  }

  @Override
  public ColumnCondition add(ColumnCondition condition) {
    this.columnValues.add(condition);
    return this;
  }

  public TreeSet<ColumnCondition> getColumnValues() {
    return columnValues;
  }

  @Override
  public int compareTo(ColumnCondition o) {
    if (o instanceof ColumnConditionAnd) {
      ColumnConditionAnd other = (ColumnConditionAnd) o;
      int lengthComparison = this.columnValues.size() - other.columnValues.size();
      if (lengthComparison != 0) {
        return lengthComparison;
      } else {
        Iterator<ColumnCondition> thisIterator = this.columnValues.iterator();
        Iterator<ColumnCondition> otherIterator = other.columnValues.iterator();
        while (thisIterator.hasNext() && otherIterator.hasNext()) {
          ColumnCondition currentThis = thisIterator.next();
          ColumnCondition currentOther = otherIterator.next();

          int currentComparison = currentThis.compareTo(currentOther);
          if (currentComparison != 0) {
            return currentComparison;
          }
        }
        //must be equal
        return 0;
      }
    } else {
      //and always last
      return 1;
    }
  }

  @Override
  public String toString() {
    String delimiter = " " + ColumnCondition.AND + " ";
    StringBuilder builder = new StringBuilder();
    if (isNegated) {
      builder.append(ColumnCondition.NOT);
    }
    builder.append(ColumnCondition.OPEN_BRACKET);
    for (ColumnCondition value : this.columnValues) {
      builder.append(value.toString());
      builder.append(delimiter);
    }
    return builder.substring(0, builder.length() - delimiter.length())
        .concat(ColumnCondition.CLOSE_BRACKET);
  }
}
