package de.uni_potsdam.hpi.metanome.algorithm_integration;

import static org.junit.Assert.*;

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
	 * A {@link ColumnCombination} should return the ordered column identifiers as string representation.
	 * E.g. "[column1, column2]".
	 */
	@Test
	public void testToString() {
		// Setup
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
