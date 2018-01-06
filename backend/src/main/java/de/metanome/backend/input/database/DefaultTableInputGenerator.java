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

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingTableInput;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.RelationalInput;
import de.metanome.algorithm_integration.input.TableInputGenerator;
import de.metanome.algorithm_integration.input.DatabaseConnectionGenerator;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Provides database tables as {@link RelationalInput} by executing select statements on an
 * underlying {@link DefaultDatabaseConnectionGenerator}.
 *
 * @author Jakob Zwiener
 * @see de.metanome.algorithm_integration.input.RelationalInput
 * @see DefaultDatabaseConnectionGenerator
 */
public class DefaultTableInputGenerator implements TableInputGenerator {

  protected static final String BASE_STATEMENT = "SELECT * FROM %s";
  protected static final String SORT_STATEMENT = "SELECT * FROM %s ORDER BY %s %s";
  protected static final String FILTER_STATEMENT = "SELECT * FROM %s WHERE %s";

  protected DefaultDatabaseConnectionGenerator defaultDatabaseConnectionGenerator;
  protected String table;

  protected DefaultTableInputGenerator() {
  }

  /**
   * Exists for tests.
   *
   * @param defaultDatabaseConnectionGenerator the default database connection generator
   * @param table                              the table
   */
  protected DefaultTableInputGenerator(
    DefaultDatabaseConnectionGenerator defaultDatabaseConnectionGenerator, String table) {
    this.defaultDatabaseConnectionGenerator = defaultDatabaseConnectionGenerator;
    this.table = table;
  }

  /**
   * @param setting the table input setting to construct the table input generator from
   * @throws AlgorithmConfigurationException is thrown if the underlying {@link DefaultDatabaseConnectionGenerator}
   *                                         cannot be instantiated.
   */
  public DefaultTableInputGenerator(ConfigurationSettingTableInput setting)
    throws AlgorithmConfigurationException {
    this.defaultDatabaseConnectionGenerator =
      new DefaultDatabaseConnectionGenerator(setting.getDatabaseConnection());
    this.table = setting.getTable();
  }

  /**
   * Generates a new {@link de.metanome.algorithm_integration.input.RelationalInput} to iterate over
   * the data in the table.
   *
   * @return the {@link de.metanome.algorithm_integration.input.RelationalInput}
   * @throws InputGenerationException if the database statement could not be executed
   */
  @Override
  public RelationalInput generateNewCopy() throws InputGenerationException, AlgorithmConfigurationException {
    String query = String.format(BASE_STATEMENT, table);
    return defaultDatabaseConnectionGenerator
      .generateRelationalInputFromSql(query, table);
  }

  @Override
  public ResultSet sortBy(String column, Boolean descending) throws InputGenerationException, AlgorithmConfigurationException {
	String query = String.format(SORT_STATEMENT, table, column, descending ? "DESC" : "ASC");
    return defaultDatabaseConnectionGenerator
      .generateResultSetFromSql(query);
  }

  @Override
  public ResultSet filter(String filterExpression) throws InputGenerationException, AlgorithmConfigurationException {
    String query = String.format(FILTER_STATEMENT, table, filterExpression);
    return defaultDatabaseConnectionGenerator
      .generateResultSetFromSql(query);
  }

  @Override
  public ResultSet select() throws InputGenerationException, AlgorithmConfigurationException {
	String query = String.format(BASE_STATEMENT, table);
    return defaultDatabaseConnectionGenerator
      .generateResultSetFromSql(query);
  }

  @Override
  @JsonIgnore
  public DatabaseConnectionGenerator getDatabaseConnectionGenerator(){
    return this.defaultDatabaseConnectionGenerator;
  }

  @Override
  public void close() throws Exception {
	defaultDatabaseConnectionGenerator.close();
  }
}
