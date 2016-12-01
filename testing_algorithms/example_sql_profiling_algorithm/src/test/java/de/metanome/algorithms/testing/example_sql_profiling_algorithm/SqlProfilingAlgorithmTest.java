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
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementDatabaseConnection;
import de.metanome.algorithm_integration.configuration.DbSystem;
import de.metanome.algorithm_integration.input.DatabaseConnectionGenerator;
import de.metanome.algorithm_integration.result_receiver.BasicStatisticsResultReceiver;
import de.metanome.algorithm_integration.results.BasicStatistic;
import de.metanome.algorithm_integration.results.basic_statistic_values.BasicStatisticValueStringList;
import de.metanome.backend.input.database.DefaultDatabaseConnectionGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link de.metanome.algorithms.testing.example_sql_profiling_algorithm.SqlProfilingAlgorithm}
 *
 * @author Jakob Zwiener
 */
public class SqlProfilingAlgorithmTest {

  private static final String DB_URL = "jdbc:hsqldb:mem:sql_algorithm";
  private static final String USER = "sa";
  private static final String PASSWORD = "";

  private static final String COLUMN_1 = SqlProfilingAlgorithm.COLUMN_1;
  private static final String COLUMN_2 = SqlProfilingAlgorithm.COLUMN_2;
  private static final String COLUMN_3 = SqlProfilingAlgorithm.COLUMN_3;
  private static final String TABLE_NAME = SqlProfilingAlgorithm.TABLE_NAME;


  protected SqlProfilingAlgorithm algorithm;
  protected Connection connection;

  @Before
  public void setUp() throws Exception {
    algorithm = new SqlProfilingAlgorithm();

    // Load the HSQL Database Engine JDBC driver
    Class.forName("org.hsqldb.jdbcDriver");

    connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);

    Statement st = connection.createStatement();

    // Create test table
    st.executeQuery("CREATE TABLE " + TABLE_NAME + " ( " + COLUMN_1 + " INTEGER IDENTITY, " + COLUMN_2 + " VARCHAR(256), " + COLUMN_3 + " INTEGER)");
    // Insert data
    st.executeQuery("INSERT INTO " + TABLE_NAME + " (" + COLUMN_2 + "," + COLUMN_3 + ") VALUES('A', 1)");
    st.executeQuery("INSERT INTO " + TABLE_NAME + " (" + COLUMN_2 + "," + COLUMN_3 + ") VALUES('B', 2)");
    st.executeQuery("INSERT INTO " + TABLE_NAME + " (" + COLUMN_2 + "," + COLUMN_3 + ") VALUES('C', 3)");

    st.close();
  }

  @After
  public void tearDown() throws SQLException {
    Statement st = connection.createStatement();
    st.execute("SHUTDOWN");
    connection.close();
  }

  /**
   * Test method for {@link SqlProfilingAlgorithm#getConfigurationRequirements()}
   */
  @Test
  public void testGetConfigurationRequirements() {
    // Execute functionality
      ArrayList<ConfigurationRequirement<?>>
        actualConfigurationRequirements =
        algorithm.getConfigurationRequirements();

    // Check result
    assertThat(actualConfigurationRequirements.get(0),
               instanceOf(ConfigurationRequirementDatabaseConnection.class));
  }

  /**
   * Test method for {@link de.metanome.algorithms.testing.example_sql_profiling_algorithm.SqlProfilingAlgorithm#setDatabaseConnectionGeneratorConfigurationValue(String,
   * de.metanome.algorithm_integration.input.DatabaseConnectionGenerator...)}
   */
  @Test
  public void testSetSqlInputConfigurationValue() throws AlgorithmConfigurationException {
    // Setup
    // Expected values
    DatabaseConnectionGenerator expectedInputGenerator = mock(DatabaseConnectionGenerator.class);

    // Execute functionality
    algorithm
        .setDatabaseConnectionGeneratorConfigurationValue(SqlProfilingAlgorithm.DATABASE_IDENTIFIER,
                                                          expectedInputGenerator);

    // Check result
    assertSame(expectedInputGenerator, algorithm.inputGenerator);
  }

  /**
   * Test connection to database.
   */
  @Test
  public void testExecute() throws AlgorithmExecutionException {
    // Setup
    DatabaseConnectionGenerator inputGenerator = new DefaultDatabaseConnectionGenerator(DB_URL, USER, PASSWORD, DbSystem.MySQL);
    BasicStatisticsResultReceiver resultReceiver = mock(BasicStatisticsResultReceiver.class);

    algorithm
        .setDatabaseConnectionGeneratorConfigurationValue(SqlProfilingAlgorithm.DATABASE_IDENTIFIER,
                                                          inputGenerator);
    algorithm.setResultReceiver(resultReceiver);

    // Expected values
    BasicStatistic result = new BasicStatistic(new ColumnIdentifier(TABLE_NAME, COLUMN_1));
    List<String> list = new ArrayList<>();
    list.add("0");
    list.add("1");
    list.add("2");
    result.addStatistic(COLUMN_1, new BasicStatisticValueStringList(list));
    BasicStatistic result1 = new BasicStatistic(new ColumnIdentifier(TABLE_NAME, COLUMN_2));
    List<String> list1 = new ArrayList<>();
    list1.add("A");
    list1.add("B");
    list1.add("C");
    result1.addStatistic(COLUMN_2, new BasicStatisticValueStringList(list1));
    BasicStatistic result2 = new BasicStatistic(new ColumnIdentifier(TABLE_NAME, COLUMN_3));
    List<String> list2 = new ArrayList<>();
    list2.add("1");
    list2.add("2");
    list2.add("3");
    result2.addStatistic(COLUMN_3, new BasicStatisticValueStringList(list2));

    // Execute functionality
    algorithm.execute();

    // Check results
    verify(resultReceiver).receiveResult(result);
    verify(resultReceiver).receiveResult(result1);
    verify(resultReceiver).receiveResult(result2);
  }

}
