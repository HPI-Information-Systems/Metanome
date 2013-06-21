package de.uni_potsdam.hpi.metanome.algorithmIntegration;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
		String expectedColumn1 = "column1";
		String expectedColumn2 = "column2";
		// Execute functionality
		ColumnCombination columnCombination = new ColumnCombination(expectedColumn1, expectedColumn2, expectedColumn2);
		
		// Check result
		assertEquals(2, columnCombination.columnCombination.size());
		assertTrue(columnCombination.columnCombination.contains(expectedColumn1));
		assertTrue(columnCombination.columnCombination.contains(expectedColumn2));
	}

}
