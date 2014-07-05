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

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResultSetTwoLinesFixture {

  public ResultSet getTestData() throws SQLException {
    ResultSet resultSet = mock(ResultSet.class);
    ResultSetMetaData resultSetMetaData = mock(ResultSetMetaData.class);
    // Expected values
    // Expected column count
    when(resultSetMetaData.getColumnCount())
        .thenReturn(numberOfColumns());
    // Simulate SQLException when starting count at 0.
    when(resultSetMetaData.getTableName(0))
        .thenThrow(new SQLException());
    when(resultSetMetaData.getTableName(1))
        .thenReturn(getExpectedRelationName());
    ImmutableList<String> expectedColumnNames = getExpectedColumnNames();
    // Simulate SQLException when starting count at 0.
    when(resultSetMetaData.getColumnName(0))
        .thenThrow(new SQLException());
    for (int i = 0; i < expectedColumnNames.size(); i++) {
      when(resultSetMetaData.getColumnLabel(i + 1))
          .thenReturn(expectedColumnNames.get(i));
    }
    // Expected values when calling getMetaData
    when(resultSet.getMetaData())
        .thenReturn(resultSetMetaData);
    // Expected values when calling next
    when(resultSet.next())
        .thenReturn(getFirstExpectedNextValue(), getExpectedNextValuesExceptFirstAsArray());
    List<ImmutableList<String>> expectedRecords = getExpectedRecords();
    // Simulate SQLException when starting count at 0.
    when(resultSet.getString(0))
        .thenThrow(new SQLException());
    // Expected values when calling getString
    for (int columnIndex = 0; columnIndex < numberOfColumns(); columnIndex++) {
      when(resultSet.getString(columnIndex + 1))
          .thenReturn(expectedRecords.get(0).get(columnIndex))
          .thenReturn(expectedRecords.get(1).get(columnIndex));
    }

    return resultSet;
  }

  protected boolean getFirstExpectedNextValue() {
    return getExpectedNextValues().get(0);
  }

  protected Boolean[] getExpectedNextValuesExceptFirstAsArray() {
    List<Boolean> expectedNextValues = getExpectedNextValues();
    expectedNextValues.remove(0);
    return expectedNextValues.toArray(new Boolean[expectedNextValues.size()]);
  }

  public List<Boolean> getExpectedNextValues() {
    List<Boolean> expectedNextValues = new ArrayList<>();

    expectedNextValues.add(true);
    expectedNextValues.add(true);
    expectedNextValues.add(false);

    return expectedNextValues;
  }

  public List<ImmutableList<String>> getExpectedRecords() {
    List<ImmutableList<String>> expectedRecords = new ArrayList<>();

    expectedRecords.add(ImmutableList.of("cell1", "cell2", "cell3"));
    expectedRecords.add(ImmutableList.of("cell4", "cell5", "cell6"));

    return expectedRecords;
  }

  public String getExpectedRelationName() {
    return "some_table";
  }

  public ImmutableList<String> getExpectedColumnNames() {
    return ImmutableList.of("column1", "column2", "column3");
  }

  public int numberOfRows() {
    return 2;
  }

  public int numberOfColumns() {
    return 3;
  }


}
