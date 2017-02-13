/**
 * Copyright 2014-2016 by Metanome Project
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
package de.metanome.algorithm_integration.input;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Generates new copies of {@link RelationalInput}s or {@link ResultSet}s.
 *
 * @author Jakob Zwiener
 */
public interface DatabaseConnectionGenerator extends AutoCloseable {

  /**
   * Creates a {@link RelationalInput} from an sql statement issued to a database.
   *
   * @param queryString the query string to generate the input
   * @param relationName fallback relation name whenever the relation name cannot be provided from the query result;
   *                     may be {@code null}
   * @return the {@link de.metanome.algorithm_integration.input.RelationalInput} containing the
   * query result
   * @throws InputGenerationException if the input cannot be generated
   * @throws AlgorithmConfigurationException if the configuration is not correct
   */
  RelationalInput generateRelationalInputFromSql(String queryString, String relationName)
    throws InputGenerationException, AlgorithmConfigurationException;

  /**
   * Creates a {@link ResultSet} from an sql statement issued to a database.
   *
   * @param queryString the query string to generate the input
   * @return the {@link ResultSet} containing the query result
   * @throws InputGenerationException if the result cannot be generated
   * @throws AlgorithmConfigurationException if the configuration is not correct
   */
  ResultSet generateResultSetFromSql(String queryString) throws InputGenerationException, AlgorithmConfigurationException;

  /**
   * Closes all executed statements.
   * @throws java.sql.SQLException if statements could not be closed
   */
  void closeAllStatements() throws SQLException;

  /**
   * @return the database connection
   */
  Connection getConnection();

}
