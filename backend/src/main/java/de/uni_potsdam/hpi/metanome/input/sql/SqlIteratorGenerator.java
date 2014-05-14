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

import java.sql.*;

public class SqlIteratorGenerator implements SqlInputGenerator {

    protected Connection dbConnection;

    /**
     * Exists for tests.
     */
    protected SqlIteratorGenerator() {
    }

    public SqlIteratorGenerator(String dbUrl, String userName, String password) throws AlgorithmConfigurationException {
        try {
            this.dbConnection = DriverManager.getConnection(dbUrl, userName, password);
        } catch (SQLException e) {
            throw new AlgorithmConfigurationException("Failed to get Database Connection");
        }
    }

    @Override
    public RelationalInput generateRelationalInputFromSql(String queryString) throws InputGenerationException {

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
     * @throws InputGenerationException
     */
    protected ResultSet executeQuery(String queryString) throws InputGenerationException {
        Statement sqlStatement;
        try {
            sqlStatement = dbConnection.createStatement();
        } catch (SQLException e) {
            throw new InputGenerationException("Could not create sql statement on connection.");
        }
        ResultSet resultSet;
        try {
            resultSet = sqlStatement.executeQuery(queryString);
            sqlStatement.closeOnCompletion();
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
    public void close() throws SQLException {
        dbConnection.close();
    }
}
