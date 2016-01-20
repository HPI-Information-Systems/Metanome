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
import java.util.Map;

public class ColumnIdentifier implements Comparable<ColumnIdentifier>, Serializable {

  private static final long serialVersionUID = -3199299021265706919L;

  protected String tableIdentifier;
  protected String columnIdentifier;

  public ColumnIdentifier() {
    this.tableIdentifier = "";
    this.columnIdentifier = "";
  }

  /**
   * @param tableIdentifier  table's identifier
   * @param columnIdentifier column's identifier
   */
  public ColumnIdentifier(String tableIdentifier, String columnIdentifier) {
    this.tableIdentifier = tableIdentifier;
    this.columnIdentifier = columnIdentifier;
  }

  public String getTableIdentifier() {
    return tableIdentifier;
  }

  public void setTableIdentifier(String tableIdentifier) {
    this.tableIdentifier = tableIdentifier;
  }

  public String getColumnIdentifier() {
    return columnIdentifier;
  }

  public void setColumnIdentifier(String columnIdentifier) {
    this.columnIdentifier = columnIdentifier;
  }

  @Override
  public String toString() {
    return tableIdentifier + "." + columnIdentifier;
  }

  /**
   * Returns the encoded string for this column identifier.
   * The encoded string is determined by the given mapping.
   * @param mapping the mapping
   * @return the encoded string
   */
  public String toString(Map<String, String> mapping) {
    return mapping.get(this.toString());
  }

  /**
   * Creates a ColumnIdentifier from the given string using the given mapping.
   * @param mapping the mapping
   * @param str the string
   * @return a column identifier
   */
  public static ColumnIdentifier fromString(Map<String, String> mapping, String str) {
    String originalStr = mapping.get(str);
    String[] parts = originalStr.split("\\.");
    if (parts.length > 2) {
      String column = parts[parts.length - 1];
      String table = parts[0];
      for (int i = 1; i < parts.length - 1; i++)
        table += "." + parts[i];
        return new ColumnIdentifier(table, column);
    } else {
      return new ColumnIdentifier(parts[0], parts[1]);
    }
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
