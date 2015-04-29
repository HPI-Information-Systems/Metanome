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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents column permutations. In contrast to {@link ColumnCombination}s the order of the column
 * is not necessarily lexicographically.
 */
public class ColumnPermutation implements Serializable {

  protected List<ColumnIdentifier> columnIdentifiers;

  /**
   * Exists for GWT serialization.
   */
  protected ColumnPermutation() {
    columnIdentifiers = new ArrayList<>();
  }

  /**
   * Store string identifiers for columns to form a column combination.
   *
   * @param columnIdentifier the identifier in the ColumnCombination
   */
  public ColumnPermutation(ColumnIdentifier... columnIdentifier) {
    columnIdentifiers = Arrays.asList(columnIdentifier);
  }

  /**
   * Get column identifiers as set.
   *
   * @return columnIdentifiers
   */
  public List<ColumnIdentifier> getColumnIdentifiers() {
    return columnIdentifiers;
  }

  public void setColumnIdentifiers(List<ColumnIdentifier> identifiers) {
    this.columnIdentifiers = identifiers;
  }

  @Override
  public String toString() {
    return columnIdentifiers.toString();
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
    ColumnPermutation other = (ColumnPermutation) obj;
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
