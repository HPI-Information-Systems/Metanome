package de.uni_potsdam.hpi.metanome.input.sql;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mysql.jdbc.ResultSetMetaData;

public class ResultSetTwoLinesFixture {

	public ResultSet getTestData() throws SQLException {
		ResultSet resultSet = mock(ResultSet.class);
		ResultSetMetaData resultSetMetaData = mock(ResultSetMetaData.class);
		// Expected values
		// Expected column count
		when(resultSetMetaData.getColumnCount())
			.thenReturn(numberOfColumns());
		// Expected values when calling getMetaData
		when(resultSet.getMetaData())
			.thenReturn(resultSetMetaData);
		// Expected values when calling next
		when(resultSet.next())
			.thenReturn(getFirstExpectedNextValue(), getExpectedNextValuesExceptFirstAsArray());
		List<ImmutableList<String>> expectedRecords = getExpectedRecords();
		// Expected values when calling getString
		for (int columnIndex = 0; columnIndex < numberOfColumns(); columnIndex++) {
			when(resultSet.getString(columnIndex))
				.thenReturn(expectedRecords.get(0).get(columnIndex))
				.thenReturn(expectedRecords.get(1).get(columnIndex));
		}
		
		return resultSet;
	}
	
	protected boolean getFirstExpectedNextValue() {
		return getExpectedNextValues().get(0);
	}
	
	protected Boolean[] getExpectedNextValuesExceptFirstAsArray() {
		List<Boolean> expectedNextValues = getExpectedNextValues();
		expectedNextValues.remove(0);
		return expectedNextValues.toArray(new Boolean[expectedNextValues.size()]);
	}

	public List<Boolean> getExpectedNextValues() {
		List<Boolean> expectedNextValues = new ArrayList<Boolean>();
		
		expectedNextValues.add(true);
		expectedNextValues.add(true);
		expectedNextValues.add(false);
		
		return expectedNextValues;
	}
	
	public List<ImmutableList<String>> getExpectedRecords() {
		List<ImmutableList<String>> expectedRecords = new ArrayList<ImmutableList<String>>();
				
		expectedRecords.add(ImmutableList.of("cell1", "cell2", "cell3"));
		expectedRecords.add(ImmutableList.of("cell4", "cell5", "cell6"));
		
		return expectedRecords;
	}

	public int numberOfRows() {
		return 2;
	}
	
	protected int numberOfColumns() {
		return 3;
	}

	
}
