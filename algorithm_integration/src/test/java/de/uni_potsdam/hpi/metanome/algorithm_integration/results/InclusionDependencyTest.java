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
 * Test for {@link InclusionDependency}
 */
public class InclusionDependencyTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link InclusionDependency#sendResultTo(OmniscientResultReceiver)}
	 * 
	 * The {@link InclusionDependency} should be sendable to the {@link OmniscientResultReceiver}.
	 * 
	 * @throws CouldNotReceiveResultException 
	 */
	@Test
	public void testSendResultTo() throws CouldNotReceiveResultException {
		// Setup
		OmniscientResultReceiver resultReceiver = mock(OmniscientResultReceiver.class);
		InclusionDependency ind = new InclusionDependency();
		
		// Execute functionality 
		ind.sendResultTo(resultReceiver);
		
		// Check result
		verify(resultReceiver).receiveResult(ind);
	}

	/**
	 * Test method for {@link InclusionDependency#InclusionDependency(ColumnCombination, ColumnCombination)}
	 * 
	 * A {@link InclusionDependency} should store referenced and dependant.
	 */
	@Test
	public void testConstructor() {
		// Setup
		// Expected values
		ColumnCombination expectedDependant = new ColumnCombination(
				new ColumnIdentifier("table2", "column2"),
				new ColumnIdentifier("table2", "column27"));
		ColumnCombination expectedReferenced = new ColumnCombination(
				new ColumnIdentifier("table1", "column1"),
				new ColumnIdentifier("table1", "column4"));
		// Execute functionality
		InclusionDependency ind = new InclusionDependency(expectedDependant, expectedReferenced);
		
		// Check result
		assertEquals(expectedDependant, ind.getDependant());
		assertEquals(expectedReferenced, ind.getReferenced());
	}
	
	/**
	 * Test method for {@link InclusionDependency#toString()}
	 * 
	 * A {@link InclusionDependency} should return a human readable string representation.
	 */
	@Test
	public void testToString() {
		// Setup
		ColumnCombination expectedDependant = new ColumnCombination(
				new ColumnIdentifier("table2", "column2"),
				new ColumnIdentifier("table2", "column27"));
		ColumnCombination expectedReferenced = new ColumnCombination(
				new ColumnIdentifier("table1", "column1"),
				new ColumnIdentifier("table1", "column4"));
		InclusionDependency ind = new InclusionDependency(expectedDependant, expectedReferenced);
		// Expected values
		String expectedStringRepresentation = expectedDependant + InclusionDependency.IND_SEPARATOR + expectedReferenced;
		
		// Execute functionality
		// Check result
		assertEquals(expectedStringRepresentation, ind.toString());
	}
}
