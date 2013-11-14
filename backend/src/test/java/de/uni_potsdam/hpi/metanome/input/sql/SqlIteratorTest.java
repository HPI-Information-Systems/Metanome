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

/**
 * @author Jakob Zwiener
 * 
 * Test for {@link SqlIterator}
 */
public class SqlIteratorTest {

	protected ResultSetFixture minimalResultSetFixture;
	protected ResultSetTwoLinesFixture twoLinesResultSetFixture;
	
	@Before
	public void setUp() throws Exception {
		// TODO initialise fixtures here
		
		minimalResultSetFixture = new ResultSetFixture();
		twoLinesResultSetFixture = new ResultSetTwoLinesFixture();
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
		ResultSet resultSet = minimalResultSetFixture.getTestData();
		SqlIterator sqlIterator = new SqlIterator(resultSet);
		// Expected values
		boolean expectedFirstNext = true;
		boolean expectedSecondNext = false;
		when(resultSet.next())
			.thenReturn(expectedFirstNext)
			// The second call should not be made.
			.thenReturn(false);
		
		// Execute functionality
		// Check result
		assertEquals(expectedFirstNext, sqlIterator.hasNext());
		// This should give the cached next result.
		assertEquals(expectedFirstNext, sqlIterator.hasNext());
		// After having called next on the SqlIterator the second next call should be made to the ResultSet
		sqlIterator.next();
		assertEquals(expectedSecondNext, sqlIterator.hasNext());
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
		ResultSet resultSet = twoLinesResultSetFixture.getTestData();
		SqlIterator sqlIterator = new SqlIterator(resultSet);
		
		// Check result
		List<Boolean> expectedNextValues = twoLinesResultSetFixture.getExpectedNextValues();
		List<ImmutableList<String>> expectedRecords = twoLinesResultSetFixture.getExpectedRecords();
		for (int i = 0; i < twoLinesResultSetFixture.numberOfRows(); i++) {
			assertEquals(expectedNextValues.get(i), sqlIterator.hasNext());
			assertEquals(expectedRecords.get(i), sqlIterator.next());
		}
		assertEquals(expectedNextValues.get(twoLinesResultSetFixture.numberOfRows()), sqlIterator.hasNext());
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
		ResultSet resultSet = twoLinesResultSetFixture.getTestData();
		SqlIterator sqlIterator = new SqlIterator(resultSet);
		
		// Check result
		List<ImmutableList<String>> expectedRecords = twoLinesResultSetFixture.getExpectedRecords();
		for (int i = 0; i < twoLinesResultSetFixture.numberOfRows(); i++) {
			assertEquals(expectedRecords.get(i), sqlIterator.next());
		}
		// Next should have been called (although hasNext has not been called on sqlIterator).
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
		SqlIterator sqlIterator = new SqlIterator(minimalResultSetFixture.getTestData());

		// Check result
		try {
			sqlIterator.remove();
			fail("Expected an UnsupportedOperationException to be thrown.");
		}
		catch (UnsupportedOperationException actualException) {
			// Intentionally left blank
		}
	}
	
	/**
	 * Test method for {@link SqlIterator#numberOfColumns()}
	 * 
	 * A {@link SqlIterator} should return the correct number of columns of the result.
	 * 
	 * @throws SQLException
	 */
	@Test
	public void testNumberOfColumns() throws SQLException {
		// Setup
		SqlIterator sqlIterator = new SqlIterator(twoLinesResultSetFixture.getTestData());
		
		// Check result
		assertEquals(twoLinesResultSetFixture.numberOfColumns(), sqlIterator.numberOfColumns());
	}
	
	/**
	 * Test method for {@link SqlIterator#columnNames()}
	 * 
	 * A {@link SqlIterator} should return the column names from the meta data.
	 * 
	 * @throws SQLException 
	 */
	@Test 
	public void testColumnNames() throws SQLException {
		// Setup
		SqlIterator sqlIterator = new SqlIterator(twoLinesResultSetFixture.getTestData());
		
		// Check result
		assertEquals(twoLinesResultSetFixture.getExpectedColumnNames(), sqlIterator.columnNames());
	}

}
