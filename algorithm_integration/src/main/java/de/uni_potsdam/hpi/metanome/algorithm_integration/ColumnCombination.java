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
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

/**
 * Represents column combinations.
 */
public class ColumnCombination implements Serializable {

  private static final long serialVersionUID = 5994284083803031188L;

  protected TreeSet<ColumnIdentifier> columnCombination;

  /**
   * Exists for GWT serialization.
   */
  protected ColumnCombination() {
    columnCombination = new TreeSet<>();
  }

  /**
   * Store string identifiers for columns to form a column combination.
   *
   * @param columnIdentifier the identifier in the ColumnCombination
   */
  public ColumnCombination(ColumnIdentifier... columnIdentifier) {
    columnCombination = new TreeSet<>(Arrays.asList(columnIdentifier));
  }

  /**
   * Get column identifiers as set.
   *
   * @return columnCombination
   */
  public Set<ColumnIdentifier> getColumnIdentifiers() {
    return columnCombination;
  }

  @Override
  public String toString() {
    return columnCombination.toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime
             * result
             + ((columnCombination == null) ? 0 : columnCombination
        .hashCode());
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
    ColumnCombination other = (ColumnCombination) obj;
    if (columnCombination == null) {
      if (other.columnCombination != null) {
        return false;
      }
    } else if (!columnCombination.equals(other.columnCombination)) {
      return false;
    }
    return true;
  }
}
