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
		ColumnIdentifier identifier4 = new ColumnIdentifier(tableIdentifier2, null);
		
		// Execute functionality
		// Check results
		// equals
		assertTrue(identifier1.equals(identifier2));
		assertEquals(identifier1.hashCode(), identifier2.hashCode());
		// not equals
		assertFalse(identifier1.equals(identifier3));
		assertNotEquals(identifier1.hashCode(), identifier3.hashCode());
		// not equals (null)
		assertFalse(identifier1.equals(null));
		// hashCode contains null
		assertNotEquals(identifier1.hashCode(), identifier4.hashCode());
		
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
