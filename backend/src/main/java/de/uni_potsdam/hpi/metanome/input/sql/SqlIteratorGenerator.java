package de.uni_potsdam.hpi.metanome.input.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.SimpleRelationalInput;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.InputGenerationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.SQLInputGenerator;

public class SqlIteratorGenerator implements SQLInputGenerator {

	protected Connection dbConnection;
	
	public SqlIteratorGenerator(String dbUrl, String userName, String password) throws AlgorithmConfigurationException{
		try {
			this.dbConnection = DriverManager.getConnection(dbUrl, userName, password);
		} catch (SQLException e) {
			throw new AlgorithmConfigurationException("Failed to get Database Connection");
		}
	}

	@Override
	public SimpleRelationalInput generateSimpleRelationalInputFromSql(String queryString) throws InputGenerationException {
		
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
	 * @param queryString
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
		} catch (SQLException e) {
			throw new InputGenerationException("Could not execute sql statement.");
		}
		
		return resultSet;
	}

	@Override
	public ResultSet generateResultSetFromSql(String queryString) throws InputGenerationException {
		return executeQuery(queryString);
	}

}
