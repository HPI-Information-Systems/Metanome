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
 * Logical "or" used in the composite pattern to represent column condition. Contains subcondition
 * that are concatenated by "or".
 *
 * @author Jens Ehrlich
 */
@JsonTypeName("ColumnConditionOr")
public class ColumnConditionOr implements ColumnCondition {

  private static final long serialVersionUID = 4484094838499696950L;

  protected boolean isNegated = false;
  protected Set<ColumnCondition> columnValues;
  protected float coverage = Float.NaN;

  protected ColumnConditionOr() {
    this.columnValues = new TreeSet<>();
  }

  public ColumnConditionOr(Map<ColumnIdentifier, String> conditionMap) {
    this();
    for (ColumnIdentifier column : conditionMap.keySet()) {
      columnValues.add(new ColumnConditionValue(column, conditionMap.get(column)));
    }
  }

  public ColumnConditionOr(TreeSet<ColumnCondition> treeSet) {
    this.columnValues = new TreeSet<>(treeSet);
  }

  public ColumnConditionOr(ColumnCondition... conditions) {
    this();
    Collections.addAll(this.columnValues, conditions);
  }

  public ColumnConditionOr(ColumnIdentifier identifier, String... values) {
    this();
    for (String value : values) {
      columnValues.add(new ColumnConditionValue(identifier, value));
    }
  }

  public boolean isNegated() {
    return isNegated;
  }

  public void setNegated(boolean isNegated) {
    this.isNegated = isNegated;
  }

  public Set<ColumnCondition> getColumnValues() {
    return columnValues;
  }

  public void setColumnValues(Set<ColumnCondition> columnValues) {
    this.columnValues = columnValues;
  }

  @Override
  public float getCoverage() {
    if (Float.isNaN(this.coverage)) {
      float coverage = 0;
      for (ColumnCondition subCondition : this.columnValues) {
        coverage += subCondition.getCoverage();
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
    for (ColumnCondition columnCondition : this.columnValues) {
      result.addAll(columnCondition.getPatternConditions());
    }
    return result;
  }

  @Override
  public int compareTo(ColumnCondition o) {
    if (o instanceof ColumnConditionOr) {
      ColumnConditionOr other = (ColumnConditionOr) o;
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
      //or between "simple" and "and"
      if (o instanceof ColumnConditionValue) {
        return 1;
      } else {
        return -1;
      }
    }
  }

  @Override
  public String toString() {
    String delimiter = " " + OR + " ";
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

    ColumnConditionOr that = (ColumnConditionOr) o;

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
