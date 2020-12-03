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

import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResultSetFixture {

  public ResultSet getTestData() throws SQLException {
    return getTestData(0);
  }

  public ResultSet getTestData(int numberOfColumns) throws SQLException {
    ResultSet resultSet = mock(ResultSet.class);
    ResultSetMetaData resultSetMetaData = mock(ResultSetMetaData.class);
    when(resultSetMetaData.getColumnCount())
      .thenReturn(numberOfColumns);
    when(resultSet.getMetaData())
      .thenReturn(resultSetMetaData);

    return resultSet;
  }
}
