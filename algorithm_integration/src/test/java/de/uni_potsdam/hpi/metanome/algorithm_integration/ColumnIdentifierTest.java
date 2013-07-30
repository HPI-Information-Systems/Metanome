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
	 * A ColumnIdentifier should be equal to itself.
	 */
	@Test
	public void testEqualsReflexivity() {
		// Setup
		String tableIdentifier1 = "table1";
		String columnIdentifier1 = "column1";
		ColumnIdentifier identifier1 = new ColumnIdentifier(tableIdentifier1, columnIdentifier1);
		
		// Execute functionality
		// Check results
		assertEquals(identifier1, identifier1);
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
		ColumnIdentifier identifierNull1 = new ColumnIdentifier(tableIdentifier1, null);
		ColumnIdentifier identifierNull2 = new ColumnIdentifier(null, columnIdentifier1);
		
		// Execute functionality
		// Check results
		// equals
		assertEquals(identifier1, identifier2);
		assertEquals(identifier1.hashCode(), identifier2.hashCode());
		// not equals
		assertNotEquals(identifier1, identifier3);
		assertNotEquals(identifier1.hashCode(), identifier3.hashCode());
		// not equals (null)
		assertNotEquals(identifier1, null);
		// not equals (different class)
		assertNotEquals(identifier1, "test");
		// identifier contain null
		assertNotEquals(identifierNull1, identifier1);
		assertNotEquals(identifierNull1.hashCode(), identifier1.hashCode());
		assertNotEquals(identifierNull2, identifier1);
		assertNotEquals(identifierNull2.hashCode(), identifier1.hashCode());		
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
