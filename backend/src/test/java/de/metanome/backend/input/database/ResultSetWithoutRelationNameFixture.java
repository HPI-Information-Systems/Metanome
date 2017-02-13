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

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResultSetWithoutRelationNameFixture {

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
    // Simulate that JDBC driver is not properly reporting relation names (e.g., Redshift).
    when(resultSetMetaData.getTableName(1))
      .thenReturn(null);
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
      .thenReturn(false);

    return resultSet;
  }

  public String getExpectedRelationName() {
    return "some_table";
  }

  public ImmutableList<String> getExpectedColumnNames() {
    return ImmutableList.of("column1", "column2", "column3");
  }

  public int numberOfRows() {
    return 0;
  }

  public int numberOfColumns() {
    return 3;
  }


}
