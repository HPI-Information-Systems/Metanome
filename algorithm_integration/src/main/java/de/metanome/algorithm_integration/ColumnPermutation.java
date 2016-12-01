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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Represents column permutations. In contrast to {@link ColumnCombination}s the order of the column
 * is not necessarily lexicographically.
 */
public class ColumnPermutation implements Serializable {

  private static final long serialVersionUID = -8843040353094470475L;

  protected List<ColumnIdentifier> columnIdentifiers;

  /**
   * Creates an empty column permutation.
   * Needed for serialization.
   */
  public ColumnPermutation() {
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
    return Joiner.on(",").join(cis);
  }

  /**
   * Creates a column combination from the given string using the given mapping.
   * @param str the string
   * @param tableMapping the table mapping
   * @param columnMapping the column mapping
   * @return a column combination
   */
  public static ColumnPermutation fromString(Map<String, String> tableMapping, Map<String, String> columnMapping, String str)
    throws NullPointerException, IndexOutOfBoundsException {
    String[] parts = str.split(",");

    ColumnIdentifier[] identifiers = new ColumnIdentifier[parts.length];
    for (int i = 0; i < parts.length; i++) {
      identifiers[i] = ColumnIdentifier.fromString(tableMapping, columnMapping, parts[i].trim());
    }
    return new ColumnPermutation(identifiers);
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
