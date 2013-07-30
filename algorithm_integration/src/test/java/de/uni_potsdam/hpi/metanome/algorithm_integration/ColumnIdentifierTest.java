package de.uni_potsdam.hpi.metanome.algorithm_integration;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link ColumnIdentifier}
 */
public class ColumnIdentifierTest {

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
	 * Two ColumnIdentifier with the same values should be equal.
	 */
	@Test
	public void testEquals() {
		// Setup
		// TODO: implement
	}
	
	/**
	 * A ColumnIdentifier should return the table and column identifier joined by a ".".
	 */
	@Test 
	public void testToString() {
		// Setup
		String tableIdentifier = "table1";
		String columnIdentifier = "column1";
		ColumnIdentifier identifier = new ColumnIdentifier(tableIdentifier, columnIdentifier);
		// Expected values
		String expectedIdentifier = tableIdentifier + "." + columnIdentifier;
		
		// Execute functionality
		String actualIdentifier = identifier.toString();
		
		// Check result
		assertEquals(expectedIdentifier, actualIdentifier);		
	}

}
