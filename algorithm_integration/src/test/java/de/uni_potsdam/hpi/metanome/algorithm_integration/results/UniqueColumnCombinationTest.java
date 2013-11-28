/**
 * 
 */
package de.uni_potsdam.hpi.metanome.algorithm_integration.results;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;

/**
 * @author Jakob Zwiener
 * 
 * Test for {@link UniqueColumnCombination}
 */
public class UniqueColumnCombinationTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * The {@link UniqueColumnCombination} should be sendable to the {@link OmniscientResultReceiver}.
	 * 
	 * @throws CouldNotReceiveResultException 
	 */
	@Test
	public void testSendResultTo() throws CouldNotReceiveResultException {
		// Setup
		OmniscientResultReceiver resultReceiver = mock(OmniscientResultReceiver.class);
		UniqueColumnCombination ucc = new UniqueColumnCombination();
		
		// Execute functionality 
		ucc.sendResultTo(resultReceiver);
		
		// Check result
		verify(resultReceiver).receiveResult(ucc);
	}
	
	/**
	 * A {@link UniqueColumnCombination} should store all it's identifiers. Identifiers can only appear once in the {@link UniqueColumnCombination}.
	 */
	@Test
	public void testConstructor() {
		// Setup
		// Expected values
		ColumnIdentifier expectedColumn1 = new ColumnIdentifier("table1", "column1");
		ColumnIdentifier expectedColumn2 = new ColumnIdentifier("table2", "column2");
		// Execute functionality
		UniqueColumnCombination uniqueColumnCombination = new UniqueColumnCombination(expectedColumn1, expectedColumn2, expectedColumn2);
		
		// Check result
		assertEquals(new ColumnCombination(expectedColumn1, expectedColumn2, expectedColumn2), uniqueColumnCombination.getColumnCombination());
	}
	
	/**
	 * A {@link UniqueColumnCombination} should return the contained {@link ColumnCombination}s string representation.
	 */
	@Test
	public void testToString() {
		// Setup
		ColumnIdentifier expectedColumn1 = new ColumnIdentifier("table1", "column1");
		ColumnIdentifier expectedColumn2 = new ColumnIdentifier("table2", "column2");
		UniqueColumnCombination uniqueColumnCombination = new UniqueColumnCombination(expectedColumn1, expectedColumn2);
		// Expected values
		String expectedStringRepresentation = new ColumnCombination(expectedColumn1, expectedColumn2).toString();
		
		// Execute functionality
		// Check result
		assertEquals(expectedStringRepresentation, uniqueColumnCombination.toString());
	}
}
