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
package de.metanome.algorithms.testing.example_sql_profiling_algorithm;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.AlgorithmExecutionException;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.algorithm_types.BasicStatisticsAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.DatabaseConnectionParameterAlgorithm;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementDatabaseConnection;
import de.metanome.algorithm_integration.input.DatabaseConnectionGenerator;
import de.metanome.algorithm_integration.input.RelationalInput;
import de.metanome.algorithm_integration.result_receiver.BasicStatisticsResultReceiver;
import de.metanome.algorithm_integration.results.BasicStatistic;
import de.metanome.algorithm_integration.results.basic_statistic_values.BasicStatisticValueStringList;

import java.util.ArrayList;
import java.util.List;


/**
 * Example Algorithm for sql profiling tasks (test purposes)
 *
 * @author Jakob Zwiener
 */
public class SqlProfilingAlgorithm implements DatabaseConnectionParameterAlgorithm, BasicStatisticsAlgorithm {

  public static final String DATABASE_IDENTIFIER = "database identifier";

  public static final String COLUMN_1 = "id";
  public static final String COLUMN_2 = "str_col";
  public static final String COLUMN_3 = "num_col";
  public static final String TABLE_NAME = "test_table";

  protected DatabaseConnectionGenerator inputGenerator;
  protected BasicStatisticsResultReceiver resultReceiver;

  @Override
  public void setDatabaseConnectionGeneratorConfigurationValue(String identifier,
                                                               DatabaseConnectionGenerator... values)
      throws AlgorithmConfigurationException {

    if (identifier.equals(DATABASE_IDENTIFIER)) {
      inputGenerator = values[0];
    }
  }

  @Override
  public ArrayList<ConfigurationRequirement<?>> getConfigurationRequirements() {
    ArrayList<ConfigurationRequirement<?>> configurationRequirements = new ArrayList<>();

    configurationRequirements.add(new ConfigurationRequirementDatabaseConnection(
        DATABASE_IDENTIFIER));

    return configurationRequirements;
  }

  @Override
  public void execute() throws AlgorithmExecutionException {
    String[] columnNames = {COLUMN_1, COLUMN_2, COLUMN_3};

    for (String columnName : columnNames) {
      RelationalInput input = inputGenerator.generateRelationalInputFromSql("SELECT " + columnName + " FROM " + TABLE_NAME, TABLE_NAME);

      List<String> columnValues = new ArrayList<>();
      while (input.hasNext()) {
        columnValues.add(input.next().get(0));
      }

      BasicStatistic result = new BasicStatistic(new ColumnIdentifier(TABLE_NAME, columnName));
      result.addStatistic(columnName, new BasicStatisticValueStringList(columnValues));

      this.resultReceiver.receiveResult(result);
    }
  }

  @Override
  public String getAuthors() {
    return "Metanome Team";
  }

  @Override
  public String getDescription() {
    return "Example algorithm for testing a database connection.";
  }

  @Override
  public void setResultReceiver(BasicStatisticsResultReceiver resultReceiver) {
    this.resultReceiver = resultReceiver;
  }
}
