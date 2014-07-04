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

public class ColumnIdentifier implements Comparable<ColumnIdentifier>, Serializable {

  private static final long serialVersionUID = 6071753577078585888L;

  protected String tableIdentifier;
  protected String columnIdentifier;

  /**
   * Exists for GWT serialization.
   */
  protected ColumnIdentifier() {
    this.tableIdentifier = "";
    this.columnIdentifier = "";
  }

  /**
   * @param tableIdentifier  table's identifer
   * @param columnIdentifier column's identifier
   */
  public ColumnIdentifier(String tableIdentifier, String columnIdentifier) {
    this.tableIdentifier = tableIdentifier;
    this.columnIdentifier = columnIdentifier;
  }

  @Override
  public String toString() {
    return tableIdentifier + "." + columnIdentifier;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime
             * result
             + ((columnIdentifier == null) ? 0 : columnIdentifier.hashCode());
    result = prime * result
             + ((tableIdentifier == null) ? 0 : tableIdentifier.hashCode());
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
    ColumnIdentifier other = (ColumnIdentifier) obj;
    if (columnIdentifier == null) {
      if (other.columnIdentifier != null) {
        return false;
      }
    } else if (!columnIdentifier.equals(other.columnIdentifier)) {
      return false;
    }
    if (tableIdentifier == null) {
      if (other.tableIdentifier != null) {
        return false;
      }
    } else if (!tableIdentifier.equals(other.tableIdentifier)) {
      return false;
    }
    return true;
  }

  @Override
  public int compareTo(ColumnIdentifier other) {
    int tableIdentifierComparison = tableIdentifier.compareTo(other.tableIdentifier);
    if (0 != tableIdentifierComparison) {
      return tableIdentifierComparison;
    } else {
      return columnIdentifier.compareTo(other.columnIdentifier);
    }
  }

}
