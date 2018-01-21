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

import java.sql.ResultSet;

/**
 * Generates new copies of a database table based {@link de.metanome.algorithm_integration.input.RelationalInput}.
 *
 * @author Jakob Zwiener
 */
public interface TableInputGenerator extends RelationalInputGenerator {

  /**
   * Sort the table by a given column in the given way (descending or ascending).
   *
   * @param column     the column, after which the table is sort
   * @param descending the way of ordering, descending or ascending
   * @return the sorted result set
   * @throws InputGenerationException if the table could not be sorted
   * @throws AlgorithmConfigurationException if the configuration is not correct
   */
  public ResultSet sortBy(String column, Boolean descending) throws InputGenerationException, AlgorithmConfigurationException;

  /**
   * Filter the table by the given expression.
   *
   * @param filterExpression the expression by which the table is filtered
   * @return the filtered result set
   * @throws InputGenerationException if the table could not be filtered
   * @throws AlgorithmConfigurationException if the configuration is not correct
   */
  public ResultSet filter(String filterExpression) throws InputGenerationException, AlgorithmConfigurationException;

  /**
   * Select everything from the table.
   *
   * @return the result set
   * @throws InputGenerationException if the table could not be selected
   * @throws AlgorithmConfigurationException if the configuration is not correct
   */
  public ResultSet select() throws InputGenerationException, AlgorithmConfigurationException;

  public DatabaseConnectionGenerator getDatabaseConnectionGenerator();

}
