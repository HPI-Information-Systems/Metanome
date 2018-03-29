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

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingDatabaseConnection;
import de.metanome.algorithm_integration.configuration.DbSystem;
import de.metanome.algorithm_integration.input.DatabaseConnectionGenerator;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.RelationalInput;

import javax.persistence.Transient;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Generates {@link ResultSetIterator}s or {@link java.sql.ResultSet}s for a given query.
 *
 * @author Jakob Zwiener
 * @see ResultSetIterator
 * @see java.sql.ResultSet
 */
public class DefaultDatabaseConnectionGenerator implements DatabaseConnectionGenerator {

  public static final int DEFAULT_FETCH_SIZE = 100;
  private int fetchSize = DEFAULT_FETCH_SIZE;
  public static final int DEFAULT_RESULT_SET_TYPE = ResultSet.TYPE_FORWARD_ONLY;
  private int resultSetType = DEFAULT_RESULT_SET_TYPE;
  public static final int DEFAULT_RESULT_SET_CONCURRENCY = ResultSet.CONCUR_READ_ONLY;
  private int resultSetConcurrency = DEFAULT_RESULT_SET_CONCURRENCY;

  @Transient
  @JsonIgnore
  protected transient Connection dbConnection;

  protected DbSystem system;
  protected String dbUrl;
  protected String userName;
  protected String password;
  private List<Statement> statements = new LinkedList<>();

  /**
   * Exists for tests.
   */
  protected DefaultDatabaseConnectionGenerator() {
  }

  public DefaultDatabaseConnectionGenerator(String dbUrl, String userName, String password,
                                            DbSystem system)
    throws AlgorithmConfigurationException {
    this.dbUrl = dbUrl;
    this.userName = userName;
    this.password = password;
    this.system = system;
  }

  public DefaultDatabaseConnectionGenerator(ConfigurationSettingDatabaseConnection setting)
    throws AlgorithmConfigurationException {
    this(setting.getDbUrl(), setting.getUsername(), setting.getPassword(), setting.getSystem());
  }

  private void connect() throws AlgorithmConfigurationException {
    try {
      this.dbConnection = DriverManager.getConnection(this.dbUrl, this.userName, this.password);
      this.dbConnection.setAutoCommit(false);
    } catch (SQLException e) {
      throw new AlgorithmConfigurationException("Failed to get Database Connection", e);
    }
  }

  @Override
  public RelationalInput generateRelationalInputFromSql(String queryString, String relationName)
    throws InputGenerationException, AlgorithmConfigurationException {

    ResultSet resultSet = executeQuery(queryString);

    ResultSetIterator resultSetIterator;
    try {
      resultSetIterator = new ResultSetIterator(resultSet, relationName);
    } catch (SQLException e) {
      throw new InputGenerationException("Could not construct database input", e);
    }

    return resultSetIterator;
  }

  /**
   * Executes the given query and returns the associated {@link ResultSet}.
   *
   * @param queryString the query string to execute
   * @return associated {@link ResultSet}
   * @throws de.metanome.algorithm_integration.input.InputGenerationException if sql statement could not be created or executed
   * @throws de.metanome.algorithm_integration.AlgorithmConfigurationException if configuration is not correct
   */
  protected ResultSet executeQuery(String queryString) throws InputGenerationException, AlgorithmConfigurationException {
    if (this.dbConnection == null) {
      this.connect();
    }

    Statement sqlStatement;
    try {
      sqlStatement = this.dbConnection.createStatement(getResultSetType(), getResultSetConcurrency());
      sqlStatement.setFetchSize(getFetchSize());
      this.statements.add(sqlStatement);
    } catch (SQLException e) {
      throw new InputGenerationException("Could not create sql statement on connection", e);
    }
    ResultSet resultSet;
    try {
      resultSet = sqlStatement.executeQuery(queryString);
    } catch (SQLException e) {
      throw new InputGenerationException("Could not execute sql statement", e);
    }

    return resultSet;
  }

  @Override
  public ResultSet generateResultSetFromSql(String queryString) throws InputGenerationException, AlgorithmConfigurationException {
    return executeQuery(queryString);
  }

  @Override
  public void closeAllStatements() throws SQLException {
    for (Statement statement : this.statements) {
      if (statement.isClosed()) {
        continue;
      }
      statement.close();
    }
  }

  /**
   * @return the dbConnection
   */
  @Override
  @Transient
  @JsonIgnore
  public Connection getConnection() {
    if (this.dbConnection == null) {
      try {
        this.connect();
      } catch (AlgorithmConfigurationException algorithmConfigurationException) {
        algorithmConfigurationException.printStackTrace();
      }
    }
    return this.dbConnection;
  }

  @Override
  public void close() throws SQLException {
    if ((this.dbConnection == null) || this.dbConnection.isClosed()) {
      return;
    }
    if (!this.dbConnection.getAutoCommit()) {
      this.dbConnection.commit();
    }
    this.dbConnection.close();
    this.dbConnection = null;
  }

  @Transient
  @JsonIgnore
  public boolean isClosed() throws SQLException {
    return this.dbConnection == null || this.dbConnection.isClosed();
  }

  public int getFetchSize() {
    return this.fetchSize;
  }

  public DefaultDatabaseConnectionGenerator setFetchSize(int fetchSize) {
    this.fetchSize = fetchSize;
    return this;
  }

  public int getResultSetType() {
    return this.resultSetType;
  }

  public DefaultDatabaseConnectionGenerator setResultSetType(int resultSetType) {
    this.resultSetType = resultSetType;
    return this;
  }

  public int getResultSetConcurrency() {
    return this.resultSetConcurrency;
  }

  public DefaultDatabaseConnectionGenerator setResultSetConcurrency(int resultSetConcurrency) {
    this.resultSetConcurrency = resultSetConcurrency;
    return this;
  }

  public DbSystem getSystem() {
    return this.system;
  }

  public void setSystem(DbSystem system) {
    this.system = system;
  }

  public String getDbUrl() {
    return this.dbUrl;
  }

  public void setDbUrl(String dbUrl) {
    this.dbUrl = dbUrl;
  }

  public String getUserName() {
    return this.userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Transient
  @JsonIgnore
  public boolean isConnected() {
    return this.dbConnection != null;
  }
}
