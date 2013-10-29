package de.uni_potsdam.hpi.metanome.input.sql;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.ResultSetMetaData;

public class ResultSetFixture {
	
	public ResultSet getTestData() throws SQLException {
		return getTestData(0);
	}

	public ResultSet getTestData(int numberOfColumns) throws SQLException {
		ResultSet resultSet = mock(ResultSet.class);
		ResultSetMetaData resultSetMetaData = mock(ResultSetMetaData.class);
		when(resultSetMetaData.getColumnCount())
			.thenReturn(numberOfColumns);
		when(resultSet.getMetaData())
			.thenReturn(resultSetMetaData);
		
		return resultSet;
	}
}
