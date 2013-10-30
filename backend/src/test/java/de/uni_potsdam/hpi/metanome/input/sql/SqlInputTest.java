package de.uni_potsdam.hpi.metanome.input.sql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

import de.uni_potsdam.hpi.metanome.algorithm_integration.input.InputIterationException;

public class SqlInputTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * hasNext should give the information supplied by the method next on the {@link ResultSet}. Next should only be called once, though
	 * on the {@link ResultSet} and the hasNext information should be stored.
	 * 
	 * @throws SQLException
	 * @throws InputIterationException 
	 */
	@Test
	public void testHasNext() throws SQLException, InputIterationException {
		// Setup 
		ResultSetFixture fixture = new ResultSetFixture();
		ResultSet resultSet = fixture.getTestData();
		SqlInput sqlInput = new SqlInput(resultSet);
		// Expected values
		boolean expectedFirstNext = true;
		boolean expectedSecondNext = false;
		when(resultSet.next())
			.thenReturn(expectedFirstNext)
			// The second call should not be made.
			.thenReturn(false);
		
		// Execute functionality
		// Check result
		assertEquals(expectedFirstNext, sqlInput.hasNext());
		// This should give the cached next result.
		assertEquals(expectedFirstNext, sqlInput.hasNext());
		// After having called next on the SqlInput the second next call should be made to the ResultSet
		sqlInput.next();
		assertEquals(expectedSecondNext, sqlInput.hasNext());
	}

	/**
	 * Next should return the values in the record set as an ImmutableList of Strings per row.
	 * 
	 * @throws SQLException
	 * @throws InputIterationException
	 */
	@Test
	public void testNext() throws SQLException, InputIterationException {
		// Setup
		ResultSetTwoLinesFixture fixture = new ResultSetTwoLinesFixture();
		ResultSet resultSet = fixture.getTestData();
		SqlInput sqlInput = new SqlInput(resultSet);
		
		// Check result
		List<Boolean> expectedNextValues = fixture.getExpectedNextValues();
		List<ImmutableList<String>> expectedRecords = fixture.getExpectedRecords();
		for (int i = 0; i < fixture.numberOfRows(); i++) {
			assertEquals(expectedNextValues.get(i), sqlInput.hasNext());
			assertEquals(expectedRecords.get(i), sqlInput.next());
		}
		assertEquals(expectedNextValues.get(fixture.numberOfRows()), sqlInput.hasNext());
		// Next should have been called.
		verify(resultSet, times(3)).next();
		
	}
	
	/**
	 * Next should return the correct rows and call next on the result set although hasNext has not been called.
	 * 
	 * @throws SQLException
	 * @throws InputIterationException
	 */
	@Test
	public void testNextWithoutHasNext() throws SQLException, InputIterationException {
		// Setup
		ResultSetTwoLinesFixture fixture = new ResultSetTwoLinesFixture();
		ResultSet resultSet = fixture.getTestData();
		SqlInput sqlInput = new SqlInput(resultSet);
		
		// Check result
		List<ImmutableList<String>> expectedRecords = fixture.getExpectedRecords();
		for (int i = 0; i < fixture.numberOfRows(); i++) {
			assertEquals(expectedRecords.get(i), sqlInput.next());
		}
		// Next should have been called (although hasNext has not been called on sqlInput).
		verify(resultSet, times(2)).next();
	}

	/**
	 * The remove method should always throw an {@link UnsupportedOperationException}.
	 * 
	 * @throws SQLException 
	 */
	@Test
	public void testRemove() throws SQLException {
		// Setup 
		ResultSetFixture fixture = new ResultSetFixture();
		ResultSet resultSet = fixture.getTestData();
		SqlInput sqlInput = new SqlInput(resultSet);

		// Check result
		try {
			sqlInput.remove();
			fail("Expected an UnsupportedOperationException to be thrown.");
		}
		catch (UnsupportedOperationException actualException) {
			// Intentionally left blank
		}
	}

}
