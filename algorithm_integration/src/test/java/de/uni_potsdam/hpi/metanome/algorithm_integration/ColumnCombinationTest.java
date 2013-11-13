package de.uni_potsdam.hpi.metanome.algorithm_integration;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;

/**
 * Tests for {@link ColumnCombination}
 */
public class ColumnCombinationTest {

	/**
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		
	}

	/**
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		
	}

	/**
	 * A {@link ColumnCombination} should store all it's identifiers. Identifiers can only appear once in the {@link ColumnCombination}.
	 */
	@Test
	public void testConstructor() {
		// Setup
		// Expected values
		ColumnIdentifier expectedColumn1 = new ColumnIdentifier("table1", "column1");
		ColumnIdentifier expectedColumn2 = new ColumnIdentifier("table2", "column2");
		// Execute functionality
		ColumnCombination columnCombination = new ColumnCombination(expectedColumn1, expectedColumn2, expectedColumn2);
		
		// Check result
		assertEquals(2, columnCombination.columnCombination.size());
		assertTrue(columnCombination.columnCombination.contains(expectedColumn1));
		assertTrue(columnCombination.columnCombination.contains(expectedColumn2));
	}
	
	/**
	 * Test method for {@link ColumnCombination#getColumnIdentifiers()}
	 * 
	 * A {@link ColumnCombination} should return its columnIdentifiers as a set.
	 */
	@Test
	public void testGetColumnIdentifiers() {
		// Setup
		// Expected values
		ColumnIdentifier expectedColumn1 = new ColumnIdentifier("table1", "column1");
		ColumnIdentifier expectedColumn2 = new ColumnIdentifier("table2", "column2");
		ColumnIdentifier expectedColumn3 = new ColumnIdentifier("table3", "column3");
		ColumnCombination columnCombination = new ColumnCombination(expectedColumn2, expectedColumn3, expectedColumn1);
		
		// Execute functionality
		Set<ColumnIdentifier> actualColumnIdentifiers = columnCombination.getColumnIdentifiers();
		
		// Check result
		assertTrue(3 == actualColumnIdentifiers.size());
		assertTrue(actualColumnIdentifiers.contains(expectedColumn1));
		assertTrue(actualColumnIdentifiers.contains(expectedColumn2));
		assertTrue(actualColumnIdentifiers.contains(expectedColumn3));
	}
	
	/**
	 * Test method for {@link ColumnCombination#toString()}
	 * 
	 * A {@link ColumnCombination} should return the ordered column identifiers as string representation.
	 * E.g. "[column1, column2]".
	 */
	@Test
	public void testToString() {
		// Setup
		// FIXME
		// switch "table1" to "table56" and the test will fail, because the order of column identifiers is not guaranteed
		ColumnIdentifier expectedColumn1 = new ColumnIdentifier("table1", "column1");
		ColumnIdentifier expectedColumn2 = new ColumnIdentifier("table2", "column2");
		ColumnCombination columnCombination = new ColumnCombination(expectedColumn2, expectedColumn1);
		// Expected values
		String expectedStringRepresentation = "[" + expectedColumn1.toString() + ", " + expectedColumn2.toString() + "]";
		
		// Execute functionality
		String actualStringRepresentation = columnCombination.toString();
		
		// Check result
		assertEquals(expectedStringRepresentation, actualStringRepresentation);
	}

}
