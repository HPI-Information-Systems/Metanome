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

package de.uni_potsdam.hpi.metanome.algorithm_integration.input;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Generates new copies of {@link RelationalInput}s or {@link ResultSet}s.
 *
 * @author Jakob Zwiener
 */
public interface SqlInputGenerator extends AutoCloseable {

  /**
   * Creates a {@link RelationalInput} from an sql statement issued to a database.
   *
   * @param queryString the query string to generate the input
   * @return the {@link de.uni_potsdam.hpi.metanome.algorithm_integration.input.RelationalInput}
   * containing the query result
   * @throws InputGenerationException if the input cannot be generated
   */
  RelationalInput generateRelationalInputFromSql(String queryString)
      throws InputGenerationException;

  /**
   * Creates a {@link ResultSet} from an sql statement issued to a database.
   *
   * @param queryString the query string to generate the input
   * @return the {@link ResultSet} containing the query result
   * @throws InputGenerationException if the result cannot be generated
   */
  ResultSet generateResultSetFromSql(String queryString) throws InputGenerationException;

  /**
   * Closes all executed statements.
   */
  void closeAllStatements() throws SQLException;
}
