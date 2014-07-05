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

import de.uni_potsdam.hpi.metanome.algorithm_integration.input.InputGenerationException;

import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link de.uni_potsdam.hpi.metanome.input.sql.SqlIterator}
 *
 * @author Jakob Zwiener
 */
public class SqlIteratorGeneratorTest {

  /**
   * Test method for {@link SqlIteratorGenerator#SqlIteratorGenerator()} and {@link
   * de.uni_potsdam.hpi.metanome.input.sql.SqlIteratorGenerator#SqlIteratorGenerator(String, String,
   * String)}
   *
   * After calling the constructors the default parameter should be set on the generator.
   */
  @Test
  public void testConstructor() {
    // Execute functionality
    SqlIteratorGenerator sqlIteratorGenerator = new SqlIteratorGenerator();

    // Check result
    assertEquals(SqlIteratorGenerator.DEFAULT_FETCH_SIZE, sqlIteratorGenerator.getFetchSize());
    assertEquals(SqlIteratorGenerator.DEFAULT_RESULT_SET_TYPE,
                 sqlIteratorGenerator.getResultSetType());
    assertEquals(SqlIteratorGenerator.DEFAULT_RESULT_SET_CONCURRENCY,
                 sqlIteratorGenerator.getResultSetConcurrency());
  }

  /**
   * Test method for {@link SqlIteratorGenerator#getFetchSize()} and {@link
   * de.uni_potsdam.hpi.metanome.input.sql.SqlIteratorGenerator#setFetchSize(int)} <p/> The setter
   * should return this, to enable cascades.
   */
  @Test
  public void testGetSetFetchSize() {
    // Setup
    SqlIteratorGenerator sqlIteratorGenerator = new SqlIteratorGenerator();
    // Expected values
    int expectedFetchSize = 420;

    // Check preconditions
    assertNotEquals(expectedFetchSize, sqlIteratorGenerator.getFetchSize());

    // Execute functionality
    SqlIteratorGenerator
        actualSqlIteratorGenerator =
        sqlIteratorGenerator.setFetchSize(expectedFetchSize);
    int actualFetchSize = sqlIteratorGenerator.getFetchSize();

    // Check result
    assertSame(sqlIteratorGenerator, actualSqlIteratorGenerator);
    assertEquals(expectedFetchSize, actualFetchSize);
  }

  /**
   * Test method for{@link SqlIteratorGenerator#getResultSetType()} and {@link
   * de.uni_potsdam.hpi.metanome.input.sql.SqlIteratorGenerator#setResultSetType(int)} <p/> The
   * setter should return this, to enable cascades.
   */
  @Test
  public void testGetSetResultSetType() {
    // Setup
    SqlIteratorGenerator sqlIteratorGenerator = new SqlIteratorGenerator();
    // Expected values
    int expectedResultSetType = ResultSet.TYPE_SCROLL_SENSITIVE;

    // Check preconditions
    assertNotEquals(expectedResultSetType, sqlIteratorGenerator.getResultSetType());

    // Execute functionality
    SqlIteratorGenerator
        actualSqlIteratorGenerator =
        sqlIteratorGenerator.setResultSetType(expectedResultSetType);
    int actualResultSetType = sqlIteratorGenerator.getResultSetType();

    // Check result
    assertSame(sqlIteratorGenerator, actualSqlIteratorGenerator);
    assertEquals(expectedResultSetType, actualResultSetType);
  }

  /**
   * Test method for {@link SqlIteratorGenerator#getResultSetConcurrency()} and {@link
   * de.uni_potsdam.hpi.metanome.input.sql.SqlIteratorGenerator#setResultSetConcurrency(int)} <p/>
   * The setter should return this, to enable cascades.
   */
  @Test
  public void testGetSetResultSetConcurrency() {
    // Setup
    SqlIteratorGenerator sqlIteratorGenerator = new SqlIteratorGenerator();
    // Expected values
    int expectedResultSetConcurrency = ResultSet.CONCUR_UPDATABLE;

    // Check preconditions
    assertNotEquals(expectedResultSetConcurrency, sqlIteratorGenerator.getResultSetConcurrency());

    // Execute functionality
    SqlIteratorGenerator
        actualSqlIteratorGenerator =
        sqlIteratorGenerator.setResultSetConcurrency(expectedResultSetConcurrency);
    int actualResultSetConcurrency = sqlIteratorGenerator.getResultSetConcurrency();

    // Check result
    assertSame(sqlIteratorGenerator, actualSqlIteratorGenerator);
    assertEquals(expectedResultSetConcurrency, actualResultSetConcurrency);
  }

  /**
   * Test method for {@link SqlIteratorGenerator#close()} <p/> The sql iterator generator should be
   * closeable. After closing the sql iterator generator the underlying db connection should be
   * closed.
   */
  @Test
  public void testClose() throws SQLException {
    // Setup
    SqlIteratorGenerator sqlIteratorGenerator = new SqlIteratorGenerator();
    Connection connection = mock(Connection.class);
    sqlIteratorGenerator.dbConnection = connection;

    // Execute functionality
    sqlIteratorGenerator.close();

    // Check result
    verify(connection).close();
  }

  /**
   * Test method for {@link SqlIteratorGenerator#close()}
   *
   * If the db connection is already it should not be closed again.
   */
  @Test
  public void testCloseConnectionAlreadyClosed() throws SQLException {
    // Setup
    SqlIteratorGenerator sqlIteratorGenerator = new SqlIteratorGenerator();
    Connection connection = mock(Connection.class);
    when(connection.isClosed()).thenReturn(true);
    sqlIteratorGenerator.dbConnection = connection;

    // Execute functionality
    sqlIteratorGenerator.close();

    // Check result
    verify(connection, never()).close();
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.input.sql.SqlIteratorGenerator#executeQuery(String)}
   * and {@link SqlIteratorGenerator#closeAllStatements()} <p/> All executed statements should be
   * stored and closed on closeAllStatements. Already closed statements should not be closed again.
   */
  @Test
  public void testCloseAllStatements() throws SQLException, InputGenerationException {
    // Setup
    SqlIteratorGenerator sqlIteratorGenerator = new SqlIteratorGenerator();
    Connection connection = mock(Connection.class);
    sqlIteratorGenerator.dbConnection = connection;

    Statement statementMock1 = mock(Statement.class);
    when(statementMock1.executeQuery(anyString())).thenReturn(mock(ResultSet.class));
    Statement statementMock2 = mock(Statement.class);
    when(statementMock2.executeQuery(anyString())).thenReturn(mock(ResultSet.class));
    Statement statementMock3 = mock(Statement.class);
    when(statementMock3.isClosed()).thenReturn(true);
    when(statementMock3.executeQuery(anyString())).thenReturn(mock(ResultSet.class));

    when(connection.createStatement(anyInt(), anyInt()))
        .thenReturn(statementMock1, statementMock2, statementMock3);

    // Execute functionality
    sqlIteratorGenerator.executeQuery("some query 1");
    sqlIteratorGenerator.executeQuery("some query 2");
    sqlIteratorGenerator.executeQuery("some query 3");
    sqlIteratorGenerator.closeAllStatements();

    // Check result
    verify(statementMock1).close();
    verify(statementMock2).close();
    verify(statementMock3, never()).close();
  }
}
