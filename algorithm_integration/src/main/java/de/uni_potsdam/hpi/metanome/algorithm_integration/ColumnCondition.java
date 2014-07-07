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

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

/**
 * A column with condition for conditional results e.g.{@link de.uni_potsdam.hpi.metanome.algorithm_integration.results.ConditionalUniqueColumnCombination}
 *
 * @author Jens Hildebrandt
 */
public class ColumnCondition implements Comparable<ColumnCondition>, Serializable {
  protected ColumnIdentifier column;
  protected TreeSet<String> columnValues;

  /**
   * Exists for Gwt serialization
   */
  protected ColumnCondition() {
    this.column = new ColumnIdentifier();
    this.columnValues = new TreeSet<>();
  }

  /**
   * Constructs a {@link de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCondition} from a
   * {@link de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier} and an array of
   * {@link java.lang.String}s.
   *
   * @param identifier   column of the condition
   * @param columnValues where the condition is true
   */
  public ColumnCondition(ColumnIdentifier identifier, String... columnValues) {
    this();
    this.column = identifier;
    for (String columnValue : columnValues) {
      this.columnValues.add(columnValue);
    }
  }

  /**
   * Constructs a {@link de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCondition} from a
   * {@link de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier} and a List of {@link
   * java.lang.String}s.
   *
   * @param identifier   column of the condition
   * @param columnValues where the condition is true
   */
  public ColumnCondition(ColumnIdentifier identifier, List<String> columnValues) {
    this(identifier, columnValues.toArray(new String[columnValues.size()]));
  }

  public TreeSet<String> getColumnValues() {
    return columnValues;
  }

  public ColumnIdentifier getColumn() {
    return column;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ColumnCondition that = (ColumnCondition) o;

    if (!column.equals(that.column)) {
      return false;
    }
    if (!columnValues.equals(that.columnValues)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = column.hashCode();
    result = 31 * result + columnValues.hashCode();
    return result;
  }

  @Override
  public int compareTo(ColumnCondition o) {
    int columnComparison = this.column.compareTo(o.column);
    if (0 != columnComparison) {
      return columnComparison;
    } else {
      int lengthComparison = this.columnValues.size() - o.columnValues.size();

      if (0 != lengthComparison) {
        return lengthComparison;
      } else {
        Iterator<String> thisIterator = this.columnValues.iterator();

        Iterator<String> thatIterator = o.columnValues.iterator();
        while (thisIterator.hasNext() && thatIterator.hasNext()) {
          String thisString = thisIterator.next();
          String thatString = thatIterator.next();

          int stringComparison = thisString.compareTo(thatString);
          if (0 != stringComparison) {
            return stringComparison;
          }
        }
      }
    }
    return 0;
  }

  @Override
  public String toString() {
    return this.column.toString() + ": " + this.columnValues.toString();
  }
}
