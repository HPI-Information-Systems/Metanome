package de.uni_potsdam.hpi.metanome.algorithm_integration.results;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;
import de.uni_potsdam.hpi.metanome.test_helper.GwtSerializationTester;

/**
 * @author Jakob Zwiener
 * 
 * Test for {@link FunctionalDependency}
 */
public class FunctionalDependencyTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link FunctionalDependency#sendResultTo(OmniscientResultReceiver)}
	 * 
	 * The {@link FunctionalDependency} should be sendable to the {@link OmniscientResultReceiver}.
	 * 
	 * @throws CouldNotReceiveResultException 
	 */
	@Test
	public void testSendResultTo() throws CouldNotReceiveResultException {
		// Setup
		OmniscientResultReceiver resultReceiver = mock(OmniscientResultReceiver.class);
		FunctionalDependency fd = new FunctionalDependency(mock(ColumnCombination.class), mock(ColumnIdentifier.class));
		
		// Execute functionality 
		fd.sendResultTo(resultReceiver);
		
		// Check result
		verify(resultReceiver).receiveResult(fd);
	}
	
	/**
	 * Test method for {@link FunctionalDependency#FunctionalDependency(ColumnCombination, ColumnIdentifier)}
	 * 
	 * A {@link FunctionalDependency} should store the determinant {@link ColumnCombination} and the dependant {@link ColumnIdentifier}.
	 */
	@Test
	public void testConstructor() {
		// Setup
		// Expected values
		ColumnCombination expectedDeterminant = new ColumnCombination(new ColumnIdentifier("table1", "column1"), new ColumnIdentifier("table2", "column2"));
		ColumnIdentifier expectedDependant = new ColumnIdentifier("table1", "column7");
		// Execute functionality
		FunctionalDependency functionalDependency = new FunctionalDependency(expectedDeterminant, expectedDependant);
		
		// Check result
		assertEquals(expectedDeterminant, functionalDependency.getDeterminant());
		assertEquals(expectedDependant, functionalDependency.getDependant());
	}
	
	/**
	 * Test method for {@link FunctionalDependency#toString()}
	 * 
	 * A {@link FunctionalDependency} should return a human readable string representation.
	 */
	@Test
	public void testToString() {
		// Setup
		ColumnCombination expectedDeterminant = new ColumnCombination(new ColumnIdentifier("table1", "column1"), new ColumnIdentifier("table2", "column2"));
		ColumnIdentifier expectedDependant = new ColumnIdentifier("table1", "column7");
		FunctionalDependency functionalDependency = new FunctionalDependency(expectedDeterminant, expectedDependant);
		// Expected values
		String expectedStringRepresentation = expectedDeterminant + FunctionalDependency.FD_SEPARATOR + expectedDependant;
		
		// Execute functionality
		// Check result
		assertEquals(expectedStringRepresentation, functionalDependency.toString());
	}
	
	/**
	 * Test method for {@link FunctionalDependency#equals(Object)} and {@link FunctionalDependency#hashCode()} 
	 * 
	 * {@link FunctionalDependency}s with equal determinant and dependants should be equal.
	 */
	@Test
	public void testEqualsHashCode() {
		// Setup
		FunctionalDependency expectedFd = new FunctionalDependency(
				new ColumnCombination(
						new ColumnIdentifier("table1", "column2")),
				new ColumnIdentifier("table1", "column47"));
		FunctionalDependency expectedEqualFd = new FunctionalDependency(
				new ColumnCombination(
						new ColumnIdentifier("table1", "column2")),
				new ColumnIdentifier("table1", "column47")); 
		FunctionalDependency expectedNotEqualDeterminantFd = new FunctionalDependency(
				new ColumnCombination(
						new ColumnIdentifier("table1", "column4")),
				new ColumnIdentifier("table1", "column47"));
		FunctionalDependency expectedNotEqualDependantFd = new FunctionalDependency(
				new ColumnCombination(
						new ColumnIdentifier("table1", "column2")),
				new ColumnIdentifier("table1", "column57"));
		
		// Execute functionality
		// Check result
		assertEquals(expectedFd, expectedFd);
		assertEquals(expectedFd.hashCode(), expectedFd.hashCode());
		assertNotSame(expectedFd, expectedEqualFd);
		assertEquals(expectedFd, expectedEqualFd);
		assertEquals(expectedFd.hashCode(), expectedEqualFd.hashCode());
		assertNotEquals(expectedFd, expectedNotEqualDeterminantFd);
		assertNotEquals(expectedFd.hashCode(), expectedNotEqualDeterminantFd.hashCode());
		assertNotEquals(expectedFd, expectedNotEqualDependantFd);
		assertNotEquals(expectedFd.hashCode(), expectedNotEqualDependantFd.hashCode());
	}
	
	/**
	 * Tests that the instances of {@link FunctionalDependency} are serializable in GWT.
	 */
	@Test
	public void testGwtSerialization() {
		GwtSerializationTester.checkGwtSerializability(new FunctionalDependency(mock(ColumnCombination.class), mock(ColumnIdentifier.class)));
	}
	
}
