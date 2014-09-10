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

package de.metanome.algorithm_integration;

/**
 * This is the leaf node class for the {@link ColumnCondition} using the compsite pattern.
 *
 * @author Jens Ehrlich
 */
public class ColumnConditionValue implements ColumnCondition {

  protected ColumnIdentifier columnIdentifier;
  protected String columnValue;
  protected boolean isNegated;
  protected float coverage = 0;

  /**
   * Exists for Gwt serialization
   */
  protected ColumnConditionValue() {
    this.columnIdentifier = new ColumnIdentifier();
    this.columnValue = new String();
    this.isNegated = false;
  }

  /**
   * Constructs a {@link ColumnConditionValue} using a {@link ColumnIdentifier} and a {@link
   * java.lang.String }. By default the condition is not negated.
   */
  public ColumnConditionValue(ColumnIdentifier columnIdentifier, String columnValue) {
    this.columnIdentifier = columnIdentifier;
    this.columnValue = columnValue;
    this.isNegated = false;
  }

  /**
   * Constructs a {@link ColumnConditionValue} using a {@link ColumnIdentifier}, a {@link
   * java.lang.String }, and a boolean which indicates the negation of the condition.
   */
  public ColumnConditionValue(ColumnIdentifier columnIdentifier, String columnValue,
                              boolean isNegated) {
    this(columnIdentifier, columnValue);
    this.isNegated = isNegated;
  }

  @Override
  public float getCoverage() {
    return coverage;
  }

  public void setCoverage(float coverage) {
    this.coverage = coverage;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(this.columnIdentifier.toString());
    builder.append(": ");
    if (this.isNegated) {
      builder.append(NOT);
    }
    builder.append(this.columnValue);
    return builder.toString();
  }

  @Override
  public ColumnCondition add(ColumnCondition value) {
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ColumnConditionValue that = (ColumnConditionValue) o;

    if (isNegated != that.isNegated) {
      return false;
    }
    if (!columnIdentifier.equals(that.columnIdentifier)) {
      return false;
    }
    if (!columnValue.equals(that.columnValue)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int result = columnIdentifier.hashCode();
    result = 31 * result + columnValue.hashCode();
    result = 31 * result + (isNegated ? 1 : 0);
    return result;
  }

  @Override
  public int compareTo(ColumnCondition o) {
    if (o instanceof ColumnConditionValue) {
      ColumnConditionValue other = (ColumnConditionValue) o;
      if (other.isNegated == this.isNegated) {
        int columnComparison = this.columnIdentifier.compareTo(other.columnIdentifier);
        if (columnComparison != 0) {
          return columnComparison;
        } else {
          return this.columnValue.compareTo(other.columnValue);
        }
      } else {
        if (this.isNegated) {
          return 1;
        } else {
          return -1;
        }
      }
    }
    return -1;
  }

}
