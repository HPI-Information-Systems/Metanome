package de.uni_potsdam.hpi.metanome.algorithm_integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
	 * Only two ColumnIdentifier with the same table and column identifiers should be equal.
	 */
	@Test
	public void testEquals() {
		// Setup
		String tableIdentifier1 = "table1";
		String columnIdentifier1 = "column1";
		ColumnIdentifier identifier1 = new ColumnIdentifier(tableIdentifier1, columnIdentifier1);
		ColumnIdentifier identifier2 = new ColumnIdentifier(tableIdentifier1, columnIdentifier1);
		String tableIdentifier2 = "table2";
		String columnIdentifier2 = "column2";
		ColumnIdentifier identifier3 = new ColumnIdentifier(tableIdentifier2, columnIdentifier2);		
		
		// Execute functionality
		// Check results
		assertTrue(identifier1.equals(identifier2));
		assertFalse(identifier1.equals(identifier3));
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
