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
package de.metanome.algorithm_integration;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.*;

/**
 * Logical "and" used in the composite pattern to represent column condition. Contains subcondition
 * that are concatenated by "and".
 *
 * @author Jens Ehrlich
 */
@JsonTypeName("ColumnConditionAnd")
public class ColumnConditionAnd implements ColumnCondition {

  private static final long serialVersionUID = 1773856119866618862L;

  protected boolean isNegated = false;
  protected float coverage = Float.NaN;
  protected Set<ColumnCondition> columnValues;

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
    Collections.addAll(this.columnValues, conditions);
  }


  public Set<ColumnCondition> getColumnValues() {
    return columnValues;
  }

  public void setColumnValues(Set<ColumnCondition> columnValues) {
    this.columnValues = columnValues;
  }

  public boolean isNegated() {
    return isNegated;
  }

  public void setNegated(boolean isNegated) {
    this.isNegated = isNegated;
  }

  @Override
  public float getCoverage() {
    if (Float.isNaN(this.coverage)) {
      float coverage = Float.MAX_VALUE;
      for (ColumnCondition subCondition : this.columnValues) {
        if (coverage > subCondition.getCoverage()) {
          coverage = subCondition.getCoverage();
        }
      }
      return coverage;
    } else {
      return this.coverage;
    }
  }

  @Override
  public void setCoverage(float coverage) {
    this.coverage = coverage;
  }

  @Override
  public ColumnCondition add(ColumnCondition condition) {
    this.columnValues.add(condition);
    return this;
  }

  @Override
  @JsonIgnore
  public TreeSet<ColumnIdentifier> getContainedColumns() {
    TreeSet<ColumnIdentifier> result = new TreeSet<>();
    for (ColumnCondition subElement : this.columnValues) {
      result.addAll(subElement.getContainedColumns());
    }
    return result;
  }

  @Override
  @JsonIgnore
  public List<Map<ColumnIdentifier, String>> getPatternConditions() {
    List<Map<ColumnIdentifier, String>> result = new LinkedList<>();
    Map<ColumnIdentifier, String> condition = new TreeMap<>();
    for (ColumnCondition columnCondition : this.columnValues) {
      condition.putAll(columnCondition.getPatternConditions().get(0));
    }
    result.add(condition);
    return result;
  }

  @Override
  public int compareTo(ColumnCondition o) {
    if (o instanceof ColumnConditionAnd) {
      ColumnConditionAnd other = (ColumnConditionAnd) o;
      int lengthComparison = this.columnValues.size() - other.columnValues.size();
      if (lengthComparison != 0) {
        return lengthComparison;
      } else {
        Iterator<ColumnCondition> otherIterator = other.columnValues.iterator();
        int equalCount = 0;

        while (otherIterator.hasNext()) {
          ColumnCondition currentOther = otherIterator.next();
          // because the order of the single column values can differ,
          // you have to compare all permutations
          for (ColumnCondition currentThis : this.columnValues) {
            int currentComparison = currentThis.compareTo(currentOther);
            if (currentComparison == 0) {
              equalCount++;
            }
          }
        }

        if (equalCount == this.columnValues.size()) {
          return 0;
        } else {
          return 1;
        }
      }
    } else {
      //and always last
      return 1;
    }
  }

  @Override
  public String toString() {
    String delimiter = " " + AND + " ";
    StringBuilder builder = new StringBuilder();
    if (isNegated) {
      builder.append(NOT);
    }
    builder.append(OPEN_BRACKET);
    for (ColumnCondition value : this.columnValues) {
      builder.append(value.toString());
      builder.append(delimiter);
    }
    return builder.substring(0, builder.length() - delimiter.length())
      .concat(CLOSE_BRACKET);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ColumnConditionAnd that = (ColumnConditionAnd) o;

    if (isNegated != that.isNegated) {
      return false;
    }
    if (columnValues != null ? !columnValues.equals(that.columnValues)
      : that.columnValues != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = (isNegated ? 1 : 0);
    result = 31 * result + (columnValues != null ? columnValues.hashCode() : 0);
    return result;
  }
}
