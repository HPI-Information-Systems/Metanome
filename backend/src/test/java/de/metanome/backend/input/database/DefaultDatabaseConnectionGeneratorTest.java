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

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.input.InputGenerationException;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link ResultSetIterator}
 *
 * @author Jakob Zwiener
 */
public class DefaultDatabaseConnectionGeneratorTest {

  /**
   * Test method for {@link DefaultDatabaseConnectionGenerator#DefaultDatabaseConnectionGenerator()}
   * and {@link DefaultDatabaseConnectionGenerator#DefaultDatabaseConnectionGenerator(String,
   * String, String, de.metanome.algorithm_integration.configuration.DbSystem)}
   * <p/>
   * After calling the constructors the default parameter should be set on the generator.
   */
  @Test
  public void testConstructor() {
    // Execute functionality
    DefaultDatabaseConnectionGenerator
      defaultDatabaseConnectionGenerator = new DefaultDatabaseConnectionGenerator();

    // Check result
    assertEquals(DefaultDatabaseConnectionGenerator.DEFAULT_FETCH_SIZE,
      defaultDatabaseConnectionGenerator
        .getFetchSize());
    assertEquals(DefaultDatabaseConnectionGenerator.DEFAULT_RESULT_SET_TYPE,
      defaultDatabaseConnectionGenerator.getResultSetType());
    assertEquals(DefaultDatabaseConnectionGenerator.DEFAULT_RESULT_SET_CONCURRENCY,
      defaultDatabaseConnectionGenerator.getResultSetConcurrency());
  }

  /**
   * Test method for {@link DefaultDatabaseConnectionGenerator#getFetchSize()} and {@link
   * DefaultDatabaseConnectionGenerator#setFetchSize(int)} <p/> The setter should return this, to
   * enable cascades.
   */
  @Test
  public void testGetSetFetchSize() {
    // Setup
    DefaultDatabaseConnectionGenerator
      defaultDatabaseConnectionGenerator = new DefaultDatabaseConnectionGenerator();
    // Expected values
    int expectedFetchSize = 420;

    // Check preconditions
    assertNotEquals(expectedFetchSize, defaultDatabaseConnectionGenerator.getFetchSize());

    // Execute functionality
    DefaultDatabaseConnectionGenerator
      actualDefaultDatabaseConnectionGenerator =
      defaultDatabaseConnectionGenerator.setFetchSize(expectedFetchSize);
    int actualFetchSize = defaultDatabaseConnectionGenerator.getFetchSize();

    // Check result
    assertSame(defaultDatabaseConnectionGenerator, actualDefaultDatabaseConnectionGenerator);
    assertEquals(expectedFetchSize, actualFetchSize);
  }

  /**
   * Test method for {@link DefaultDatabaseConnectionGenerator#getConnection()}
   * <p/>
   * The connection generator should return the current dbConnection.
   */
  @Test
  public void testGetConnection() {
    // Setup
    DefaultDatabaseConnectionGenerator
      connectionGenerator =
      new DefaultDatabaseConnectionGenerator();
    Connection expectedConnection = mock(Connection.class);
    connectionGenerator.dbConnection = expectedConnection;

    // Execute functionality
    Connection actualConnection = connectionGenerator.getConnection();

    // Check result
    assertSame(expectedConnection, actualConnection);
  }

  /**
   * Test method for{@link DefaultDatabaseConnectionGenerator#getResultSetType()} and {@link
   * DefaultDatabaseConnectionGenerator#setResultSetType(int)} <p/> The setter should return this,
   * to enable cascades.
   */
  @Test
  public void testGetSetResultSetType() {
    // Setup
    DefaultDatabaseConnectionGenerator
      defaultDatabaseConnectionGenerator = new DefaultDatabaseConnectionGenerator();
    // Expected values
    int expectedResultSetType = ResultSet.TYPE_SCROLL_SENSITIVE;

    // Check preconditions
    assertNotEquals(expectedResultSetType, defaultDatabaseConnectionGenerator.getResultSetType());

    // Execute functionality
    DefaultDatabaseConnectionGenerator
      actualDefaultDatabaseConnectionGenerator =
      defaultDatabaseConnectionGenerator.setResultSetType(expectedResultSetType);
    int actualResultSetType = defaultDatabaseConnectionGenerator.getResultSetType();

    // Check result
    assertSame(defaultDatabaseConnectionGenerator, actualDefaultDatabaseConnectionGenerator);
    assertEquals(expectedResultSetType, actualResultSetType);
  }

  /**
   * Test method for {@link DefaultDatabaseConnectionGenerator#getResultSetConcurrency()} and {@link
   * DefaultDatabaseConnectionGenerator#setResultSetConcurrency(int)} <p/> The setter should return
   * this, to enable cascades.
   */
  @Test
  public void testGetSetResultSetConcurrency() {
    // Setup
    DefaultDatabaseConnectionGenerator
      defaultDatabaseConnectionGenerator = new DefaultDatabaseConnectionGenerator();
    // Expected values
    int expectedResultSetConcurrency = ResultSet.CONCUR_UPDATABLE;

    // Check preconditions
    assertNotEquals(expectedResultSetConcurrency,
      defaultDatabaseConnectionGenerator.getResultSetConcurrency());

    // Execute functionality
    DefaultDatabaseConnectionGenerator
      actualDefaultDatabaseConnectionGenerator =
      defaultDatabaseConnectionGenerator.setResultSetConcurrency(expectedResultSetConcurrency);
    int actualResultSetConcurrency = defaultDatabaseConnectionGenerator.getResultSetConcurrency();

    // Check result
    assertSame(defaultDatabaseConnectionGenerator, actualDefaultDatabaseConnectionGenerator);
    assertEquals(expectedResultSetConcurrency, actualResultSetConcurrency);
  }

  /**
   * Test method for {@link DefaultDatabaseConnectionGenerator#close()} <p/> The database connection
   * generator should be closeable. After closing the database iterator generator the underlying db
   * connection should be closed.
   */
  @Test
  public void testClose() throws SQLException {
    // Setup
    DefaultDatabaseConnectionGenerator
      defaultDatabaseConnectionGenerator = new DefaultDatabaseConnectionGenerator();
    Connection connection = mock(Connection.class);
    defaultDatabaseConnectionGenerator.dbConnection = connection;

    // Execute functionality
    defaultDatabaseConnectionGenerator.close();

    // Check result
    verify(connection).close();
  }

  /**
   * Test method for {@link DefaultDatabaseConnectionGenerator#close()}
   * <p/>
   * If the db connection is already it should not be closed again.
   */
  @Test
  public void testCloseConnectionAlreadyClosed() throws SQLException {
    // Setup
    DefaultDatabaseConnectionGenerator
      defaultDatabaseConnectionGenerator = new DefaultDatabaseConnectionGenerator();
    Connection connection = mock(Connection.class);
    when(connection.isClosed()).thenReturn(true);
    defaultDatabaseConnectionGenerator.dbConnection = connection;

    // Execute functionality
    defaultDatabaseConnectionGenerator.close();

    // Check result
    verify(connection, never()).close();
  }

  /**
   * Test method for {@link DefaultDatabaseConnectionGenerator#executeQuery(String)} and {@link
   * DefaultDatabaseConnectionGenerator#closeAllStatements()} <p/> All executed statements should be
   * stored and closed on closeAllStatements. Already closed statements should not be closed again.
   */
  @Test
  public void testCloseAllStatements() throws SQLException, InputGenerationException, AlgorithmConfigurationException {
    // Setup
    DefaultDatabaseConnectionGenerator
      defaultDatabaseConnectionGenerator = new DefaultDatabaseConnectionGenerator();
    Connection connection = mock(Connection.class);
    defaultDatabaseConnectionGenerator.dbConnection = connection;

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
    defaultDatabaseConnectionGenerator.executeQuery("some query 1");
    defaultDatabaseConnectionGenerator.executeQuery("some query 2");
    defaultDatabaseConnectionGenerator.executeQuery("some query 3");
    defaultDatabaseConnectionGenerator.closeAllStatements();

    // Check result
    verify(statementMock1).close();
    verify(statementMock2).close();
    verify(statementMock3, never()).close();
  }
}
