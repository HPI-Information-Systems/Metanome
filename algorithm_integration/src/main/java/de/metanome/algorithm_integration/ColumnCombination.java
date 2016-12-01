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

import com.google.common.base.Joiner;

import java.io.Serializable;
import java.util.*;

/**
 * Represents column combinations.
 */
public class ColumnCombination implements Serializable, Comparable<Object> {

  public static final String COLUMN_CONNECTOR = ",";

  private static final long serialVersionUID = -1675606730574675390L;

  protected Set<ColumnIdentifier> columnIdentifiers;

  /**
   * Creates an empty column combination.
   * Needed for serialization.
   */
  public ColumnCombination() {
    columnIdentifiers = new TreeSet<>();
  }

  /**
   * Store string identifiers for columns to form a column combination.
   *
   * @param columnIdentifier the identifier in the ColumnCombination
   */
  public ColumnCombination(ColumnIdentifier... columnIdentifier) {
    columnIdentifiers = new TreeSet<>(Arrays.asList(columnIdentifier));
  }

  /**
   * Get column identifiers as set.
   *
   * @return columnIdentifiers
   */
  public Set<ColumnIdentifier> getColumnIdentifiers() {
    return columnIdentifiers;
  }

  public void setColumnIdentifiers(Set<ColumnIdentifier> identifiers) {
    this.columnIdentifiers = identifiers;
  }

  @Override
  public String toString() {
    return columnIdentifiers.toString();
  }

  /**
   * Returns a compressed string representing this column combination.
   * @param tableMapping the table mapping
   * @param columnMapping the column mapping
   * @return the compressed string
   */
  public String toString(Map<String, String> tableMapping, Map<String, String> columnMapping) throws NullPointerException {
    List<String> cis = new ArrayList<>();
    for (ColumnIdentifier ci : this.columnIdentifiers) {
      cis.add(ci.toString(tableMapping, columnMapping));
    }
    return Joiner.on(COLUMN_CONNECTOR).join(cis);
  }

  /**
   * Creates a column combination from the given string using the given mapping.
   * @param tableMapping the table mapping
   * @param columnMapping the column mapping
   * @param str the string
   * @return a column combination
   */
  public static ColumnCombination fromString(Map<String, String> tableMapping, Map<String, String> columnMapping, String str)
    throws NullPointerException, IndexOutOfBoundsException {
    String[] parts = str.split(COLUMN_CONNECTOR);

    ColumnIdentifier[] identifiers = new ColumnIdentifier[parts.length];
    for (int i = 0; i < parts.length; i++) {
      identifiers[i] = ColumnIdentifier.fromString(tableMapping, columnMapping, parts[i].trim());
    }
    return new ColumnCombination(identifiers);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime
      * result
      + ((columnIdentifiers == null) ? 0 : columnIdentifiers
      .hashCode());
    return result;
  }

  @Override
  public int compareTo(Object o) {
    if (o != null && o instanceof ColumnCombination) {
      ColumnCombination other =  (ColumnCombination) o;

      int lengthComparison = this.columnIdentifiers.size() - other.columnIdentifiers.size();
      if (lengthComparison != 0) {
        return lengthComparison;

      } else {
        Iterator<ColumnIdentifier> otherIterator = other.columnIdentifiers.iterator();
        int equalCount = 0;
        int negativeCount = 0;
        int positiveCount = 0;

        while (otherIterator.hasNext()) {
          ColumnIdentifier currentOther = otherIterator.next();
          // because the order of the single column values can differ,
          // you have to compare all permutations
          for (ColumnIdentifier currentThis : this.columnIdentifiers) {
            int currentComparison = currentThis.compareTo(currentOther);
            if (currentComparison == 0) {
              equalCount++;
            } else if (currentComparison > 0) {
              positiveCount++;
            } else if (currentComparison < 0) {
              negativeCount++;
            }
          }
        }

        if (equalCount == this.columnIdentifiers.size()) {
          return 0;
        } else if (positiveCount > negativeCount) {
          return 1;
        } else {
          return -1;
        }
      }
    } else {
      //and always last
      return 1;
    }
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
    ColumnCombination other = (ColumnCombination) obj;
    if (columnIdentifiers == null) {
      if (other.columnIdentifiers != null) {
        return false;
      }
    } else if (!columnIdentifiers.equals(other.columnIdentifiers)) {
      return false;
    }
    return true;
  }
}
