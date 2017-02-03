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

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a specific column.
 */
public class ColumnIdentifier implements Comparable<ColumnIdentifier>, Serializable {

  private static final long serialVersionUID = -3199299021265706919L;

  public static final String TABLE_COLUMN_CONCATENATOR = ".";
  public static final String TABLE_COLUMN_CONCATENATOR_ESC = "\\.";

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
    if (this.tableIdentifier.isEmpty() && this.columnIdentifier.isEmpty())
      return "";
    return tableIdentifier + TABLE_COLUMN_CONCATENATOR + columnIdentifier;
  }

  /**
   * Returns the encoded string for this column identifier.
   * The encoded string is determined by the given mappings.
   * @param tableMapping the table mapping
   * @param columnMapping the column mapping
   * @return the encoded string
   */
  public String toString(Map<String, String> tableMapping, Map<String, String> columnMapping) {
    String tableValue = tableMapping.get(this.tableIdentifier);
    String columnStr = tableValue + TABLE_COLUMN_CONCATENATOR + this.columnIdentifier;
    return columnMapping.get(columnStr);
  }

  /**
   * Creates a ColumnIdentifier from the given string using the given mappings.
   * @param tableMapping the table mapping
   * @param columnMapping the column mapping
   * @param str the string
   * @return a column identifier
   */
  public static ColumnIdentifier fromString(Map<String, String> tableMapping, Map<String, String> columnMapping, String str)
    throws NullPointerException, IndexOutOfBoundsException {
    if (str.isEmpty()) {
      return new ColumnIdentifier();
    }

    String[] parts = columnMapping.get(str).split(TABLE_COLUMN_CONCATENATOR_ESC, 2);
    String tableKey = parts[0];
    String columnName = parts[1];
    String tableName = tableMapping.get(tableKey);

    return new ColumnIdentifier(tableName, columnName);
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
    int tableIdentifierComparison;
    if (this.tableIdentifier == null) {
      if (other.tableIdentifier == null)
        tableIdentifierComparison = 0;
      else
        tableIdentifierComparison = 1;
    } else if (other.tableIdentifier == null)
      tableIdentifierComparison = -1;
    else
      tableIdentifierComparison = this.tableIdentifier.compareTo(other.tableIdentifier);

    if (0 != tableIdentifierComparison) {
      return tableIdentifierComparison;
    } else {
      return columnIdentifier.compareTo(other.columnIdentifier);
    }
  }

}
