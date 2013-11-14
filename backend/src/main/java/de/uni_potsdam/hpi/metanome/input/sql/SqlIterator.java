package de.uni_potsdam.hpi.metanome.input.sql;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import de.uni_potsdam.hpi.metanome.algorithm_integration.input.InputIterationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.RelationalInput;

public class SqlIterator implements RelationalInput {

	protected ResultSet resultSet;
	protected int numberOfColumns;
	protected boolean nextCalled;
	protected boolean hasNext;
	protected String relationName;
	protected ImmutableList<String> columnNames;
	
	public SqlIterator(ResultSet resultSet) throws SQLException {
		this.resultSet = resultSet;
		ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
		this.numberOfColumns = resultSetMetaData.getColumnCount();
		this.nextCalled = false;
		this.relationName = resultSetMetaData.getTableName(0);
		this.columnNames = retrieveColumnNames(resultSetMetaData);
	}
	
	protected ImmutableList<String> retrieveColumnNames(ResultSetMetaData resultSetMetaData) throws SQLException {
		List<String> columnNames = new LinkedList<String>();
		
		for (int i = 0; i < numberOfColumns; i++) {
			columnNames.add(resultSetMetaData.getColumnLabel(i));
		}
		
		return ImmutableList.copyOf(columnNames);
	}

	@Override
	public boolean hasNext() throws InputIterationException {		
		if (!nextCalled) {
			try {
				hasNext = resultSet.next();
			} catch (SQLException e) {
				throw new InputIterationException("Could not retrieve next row.");
			}
			nextCalled = true;
		}
		
		return hasNext;
	}

	@Override
	public ImmutableList<String> next() throws InputIterationException {
		
		if (!nextCalled) {
			try {
				resultSet.next();
			} catch (SQLException e) {
				throw new InputIterationException("Could not retrieve next row.");
			}
		}
		
		nextCalled = false;
		
		String[] resultRow = new String[numberOfColumns];
		
		for (int columnIndex = 0; columnIndex < numberOfColumns; columnIndex++) {
			try {
				resultRow[columnIndex] = resultSet.getString(columnIndex);
			} catch (SQLException e) {
				throw new InputIterationException("Could not retrieve values from result set.");
			}
		}
		
		return ImmutableList.copyOf(resultRow);
	}
	
	@Override
	public int numberOfColumns() {
		return numberOfColumns;
	}
	
	@Override
	public String relationName() {
		return relationName;
	}
	
	@Override
	public ImmutableList<String> columnNames() {
		return columnNames;
	}

}
