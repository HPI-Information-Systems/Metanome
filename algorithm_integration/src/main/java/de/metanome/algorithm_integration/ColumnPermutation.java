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
 * Represents column permutations.
 */
public class ColumnPermutation implements Serializable {

  private static final long serialVersionUID = 5066664198735742462L;
  protected List<ColumnIdentifier> columnPermutation;

  /**
   * Exists for GWT serialization.
   */
  protected ColumnPermutation() {
    columnPermutation = new ArrayList<>();
  }

  /**
   * Store string identifiers for columns to form a column permutation.
   *
   * @param columnIdentifier the identifier in the ColumnPermutation
   */
  public ColumnPermutation(ColumnIdentifier... columnIdentifier) {
    columnPermutation = new ArrayList<>(Arrays.asList(columnIdentifier));
  }

  /**
   * Get column identifiers as a list.
   *
   * @return columnPermutation
   */
  public List<ColumnIdentifier> getColumnIdentifiers() {
    return columnPermutation;
  }

  @Override
  public String toString() {
    return columnPermutation.toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((columnPermutation == null) ? 0 : columnPermutation.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ColumnPermutation other = (ColumnPermutation) obj;
    if (columnPermutation == null) {
      if (other.columnPermutation != null)
        return false;
    } else if (!columnPermutation.equals(other.columnPermutation))
      return false;
    return true;
  }


}
