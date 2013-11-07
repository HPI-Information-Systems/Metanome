package de.uni_potsdam.hpi.metanome.input.sql;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.google.common.collect.ImmutableList;

import de.uni_potsdam.hpi.metanome.algorithm_integration.input.InputIterationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.SimpleRelationalInput;

public class SqlIterator implements SimpleRelationalInput {

	protected ResultSet resultSet;
	protected int numberOfColumns;
	protected boolean nextCalled;
	protected boolean hasNext;
	
	public SqlIterator(ResultSet resultSet) throws SQLException {
		this.resultSet = resultSet;
		ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
		this.numberOfColumns = resultSetMetaData.getColumnCount();
		this.nextCalled = false;
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
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
