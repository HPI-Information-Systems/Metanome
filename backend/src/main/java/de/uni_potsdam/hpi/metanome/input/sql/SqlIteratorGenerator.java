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

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.InputGenerationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.RelationalInput;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.SqlInputGenerator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

/**
 * Generates {@link de.uni_potsdam.hpi.metanome.input.sql.SqlIterator}s or {@link
 * java.sql.ResultSet}s for a given query.
 *
 * @author Jakob Zwiener
 * @see de.uni_potsdam.hpi.metanome.input.sql.SqlIterator
 * @see java.sql.ResultSet
 */
public class SqlIteratorGenerator implements SqlInputGenerator {

  public static final int DEFAULT_FETCH_SIZE = 100;
  private int fetchSize = DEFAULT_FETCH_SIZE;
  public static final int DEFAULT_RESULT_SET_TYPE = ResultSet.TYPE_FORWARD_ONLY;
  private int resultSetType = DEFAULT_RESULT_SET_TYPE;
  public static final int DEFAULT_RESULT_SET_CONCURRENCY = ResultSet.CONCUR_READ_ONLY;
  private int resultSetConcurrency = DEFAULT_RESULT_SET_CONCURRENCY;
  protected Connection dbConnection;
  private List<Statement> statements = new LinkedList<>();

  /**
   * Exists for tests.
   */
  protected SqlIteratorGenerator() {
  }

  public SqlIteratorGenerator(String dbUrl, String userName, String password)
      throws AlgorithmConfigurationException {
    try {
      this.dbConnection = DriverManager.getConnection(dbUrl, userName, password);
      this.dbConnection.setAutoCommit(false);
    } catch (SQLException e) {
      throw new AlgorithmConfigurationException("Failed to get Database Connection.");
    }
  }

  @Override
  public RelationalInput generateRelationalInputFromSql(String queryString)
      throws InputGenerationException {

    ResultSet resultSet = executeQuery(queryString);

    SqlIterator sqlIterator;
    try {
      sqlIterator = new SqlIterator(resultSet);
    } catch (SQLException e) {
      throw new InputGenerationException("Could not construct sql input.");
    }

    return sqlIterator;
  }

  /**
   * Executes the given query and returns the associated {@link ResultSet}.
   *
   * @param queryString the query string to execute
   * @return associated {@link ResultSet}
   */
  protected ResultSet executeQuery(String queryString) throws InputGenerationException {
    Statement sqlStatement;
    try {
      sqlStatement = dbConnection.createStatement(getResultSetType(), getResultSetConcurrency());
      sqlStatement.setFetchSize(getFetchSize());
      statements.add(sqlStatement);
    } catch (SQLException e) {
      throw new InputGenerationException("Could not create sql statement on connection.");
    }
    ResultSet resultSet;
    try {
      resultSet = sqlStatement.executeQuery(queryString);
    } catch (SQLException e) {
      throw new InputGenerationException("Could not execute sql statement.");
    }

    return resultSet;
  }

  @Override
  public ResultSet generateResultSetFromSql(String queryString) throws InputGenerationException {
    return executeQuery(queryString);
  }

  @Override
  public void closeAllStatements() throws SQLException {
    for (Statement statement : statements) {
      if (statement.isClosed()) {
        continue;
      }
      statement.close();
    }
  }

  @Override
  public void close() throws SQLException {
    if (!dbConnection.isClosed()) {
      dbConnection.close();
    }
  }

  public int getFetchSize() {
    return fetchSize;
  }

  public SqlIteratorGenerator setFetchSize(int fetchSize) {
    this.fetchSize = fetchSize;
    return this;
  }

  public int getResultSetType() {
    return resultSetType;
  }

  public SqlIteratorGenerator setResultSetType(int resultSetType) {
    this.resultSetType = resultSetType;
    return this;
  }

  public int getResultSetConcurrency() {
    return resultSetConcurrency;
  }

  public SqlIteratorGenerator setResultSetConcurrency(int resultSetConcurrency) {
    this.resultSetConcurrency = resultSetConcurrency;
    return this;
  }
}
