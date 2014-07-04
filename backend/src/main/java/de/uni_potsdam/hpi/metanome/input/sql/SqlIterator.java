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

package de.uni_potsdam.hpi.metanome.input.sql;

import com.google.common.collect.ImmutableList;

import de.uni_potsdam.hpi.metanome.algorithm_integration.input.InputIterationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.RelationalInput;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class SqlIterator implements RelationalInput {

  protected ResultSet resultSet;
  protected int numberOfColumns;
  protected boolean nextCalled;
  protected boolean hasNext;
  protected String relationName;
  protected ImmutableList<String> columnNames;

  public SqlIterator(ResultSet resultSet) throws SQLException {
    this.resultSet = resultSet;
    ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
    this.numberOfColumns = resultSetMetaData.getColumnCount();
    this.nextCalled = false;
    this.relationName = resultSetMetaData.getTableName(1);
    this.columnNames = retrieveColumnNames(resultSetMetaData);
  }

  protected ImmutableList<String> retrieveColumnNames(ResultSetMetaData resultSetMetaData)
      throws SQLException {
    List<String> columnNames = new LinkedList<>();

    for (int i = 0; i < numberOfColumns; i++) {
      columnNames.add(resultSetMetaData.getColumnLabel(i + 1));
    }

    return ImmutableList.copyOf(columnNames);
  }

  @Override
  public boolean hasNext() throws InputIterationException {
    if (!nextCalled) {
      try {
        hasNext = resultSet.next();
      } catch (SQLException e) {
        throw new InputIterationException("Could not retrieve next row.");
      }
      nextCalled = true;
    }

    return hasNext;
  }

  @Override
  public ImmutableList<String> next() throws InputIterationException {

    if (!nextCalled) {
      try {
        resultSet.next();
      } catch (SQLException e) {
        throw new InputIterationException("Could not retrieve next row.");
      }
    }

    nextCalled = false;

    String[] resultRow = new String[numberOfColumns];

    for (int columnIndex = 0; columnIndex < numberOfColumns; columnIndex++) {
      try {
        resultRow[columnIndex] = resultSet.getString(columnIndex + 1);
      } catch (SQLException e) {
        throw new InputIterationException("Could not retrieve values from result set.");
      }
    }

    return ImmutableList.copyOf(resultRow);
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
