/**
 * Copyright 2015-2016 by Metanome Project
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
package de.metanome.backend.input.database;

import com.google.common.collect.ImmutableList;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.input.RelationalInput;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

public class ResultSetIterator implements RelationalInput {

  /**
   * Surrogate for when the name of the iterated relation could not be retrieved.
   */
  public static final String UNKNOWN_RELATION_NAME = "unknown";

  protected ResultSet resultSet;
  protected int numberOfColumns;
  protected boolean nextCalled;
  protected boolean hasNext;
  protected String relationName;
  protected ImmutableList<String> columnNames;

  public ResultSetIterator(ResultSet resultSet) throws SQLException {
    this(resultSet, null);
  }

  public ResultSetIterator(ResultSet resultSet, String relationName) throws SQLException {
    this.resultSet = resultSet;
    ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
    this.numberOfColumns = resultSetMetaData.getColumnCount();
    this.nextCalled = false;
    this.relationName = resultSetMetaData.getTableName(1);
    if (this.relationName == null || this.relationName.isEmpty())
      this.relationName = relationName; // use as fallback only
    if (this.relationName == null || this.relationName.isEmpty())
      this.relationName = UNKNOWN_RELATION_NAME;
    this.columnNames = retrieveColumnNames(resultSetMetaData);
  }

  protected ImmutableList<String> retrieveColumnNames(ResultSetMetaData resultSetMetaData)
    throws SQLException {
    List<String> columnNames = new LinkedList<>();

    for (int i = 0; i < numberOfColumns; i++) {
      String columnLabel = resultSetMetaData.getColumnLabel(i + 1);
      columnNames.add(columnLabel == null ? String.format("column%03d", i + 1) : columnLabel);
    }

    return ImmutableList.copyOf(columnNames);
  }

  @Override
  public boolean hasNext() throws InputIterationException {
    if (!nextCalled) {
      try {
        hasNext = resultSet.next();
      } catch (SQLException e) {
        throw new InputIterationException("Could not retrieve next row", e);
      }
      nextCalled = true;
    }

    return hasNext;
  }

  @Override
  public List<String> next() throws InputIterationException {

    if (!nextCalled) {
      try {
        resultSet.next();
      } catch (SQLException e) {
        throw new InputIterationException("Could not retrieve next row", e);
      }
    }

    nextCalled = false;

    List<String> resultRow = new ArrayList<>();

    for (int columnIndex = 0; columnIndex < numberOfColumns; columnIndex++) {
      try {
        resultRow.add(resultSet.getString(columnIndex + 1));
      } catch (SQLException e) {
        throw new InputIterationException("Could not retrieve values from result set", e);
      }
    }

    return resultRow;
  }

  @Override
  public int numberOfColumns() {
    return numberOfColumns;
  }

  @Override
  public String relationName() {
    return relationName;
  }

  @Override
  public ImmutableList<String> columnNames() {
    return columnNames;
  }

  @Override
  public void close() throws Exception {
    if (!resultSet.isClosed()) {
      resultSet.close();
    }
    if (!resultSet.isClosed()) {
      resultSet.getStatement().close();
    }
  }
}
