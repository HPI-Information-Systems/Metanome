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
	 * Two ColumnIdentifier with the same table and column identifiers should be equal.
	 */
	@Test
	public void testEqualsEqual() {
		// Setup
		String tableIdentifier1 = "table1";
		String columnIdentifier1 = "column1";
		ColumnIdentifier identifier1 = new ColumnIdentifier(tableIdentifier1, columnIdentifier1);
		ColumnIdentifier identifier2 = new ColumnIdentifier(tableIdentifier1, columnIdentifier1);

		// Execute functionality
		// Check results
		assertEquals(identifier1, identifier2);
		assertEquals(identifier1.hashCode(), identifier2.hashCode());
	}
	
	/**
	 * Two ColumnIdentifiers with different values should not be equal.
	 */
	@Test
	public void testEqualsNotEqualValues() {
		// Setup
		String tableIdentifier1 = "table1";
		String columnIdentifier1 = "column1";
		ColumnIdentifier identifier1 = new ColumnIdentifier(tableIdentifier1, columnIdentifier1);
		String tableIdentifier2 = "table2";
		String columnIdentifier2 = "column2";
		ColumnIdentifier identifier3 = new ColumnIdentifier(tableIdentifier2, columnIdentifier2);
		
		// Execute functionality
		// Check results
		assertNotEquals(identifier1, identifier3);
		assertNotEquals(identifier1.hashCode(), identifier3.hashCode());
	}
	
	/**
	 * A ColumnIdentifier should not be equal to null.
	 */
	@Test
	public void testEqualsNotEqualNull() {
		// Setup
		String tableIdentifier1 = "table1";
		String columnIdentifier1 = "column1";
		ColumnIdentifier identifier1 = new ColumnIdentifier(tableIdentifier1, columnIdentifier1);
		
		// Execute functionality
		// Check results
		assertNotEquals(identifier1, null);
	}
	
	/**
	 * A ColumnIdentifier should not be equal to an instance of a different class.
	 */
	@Test
	public void testEqualsNotEqualDifferentClass() {
		// Setup
		String tableIdentifier1 = "table1";
		String columnIdentifier1 = "column1";
		ColumnIdentifier identifier1 = new ColumnIdentifier(tableIdentifier1, columnIdentifier1);
		
		// Execute functionality
		// Check results
		assertNotEquals(identifier1, "test");
	}
	
	/**
	 * If one of the ColumnIdentifiers contains a null value as sub identifier the ColumnIdentifiers are not equal.
	 * If the two ColumnIdentifiers have the null values in the same places, they are equal.
	 */
	@Test
	public void testEqualsContainingNull() {
		//Setup
		String tableIdentifier1 = "table1";
		String columnIdentifier1 = "column1";
		ColumnIdentifier identifier1 = new ColumnIdentifier(tableIdentifier1, columnIdentifier1);
		ColumnIdentifier identifierNull1 = new ColumnIdentifier(tableIdentifier1, null);
		ColumnIdentifier identifierNull2 = new ColumnIdentifier(null, columnIdentifier1);
		
		// Execute functionality
		// Check results
		assertNotEquals(identifierNull1, identifier1);
		assertNotEquals(identifierNull1.hashCode(), identifier1.hashCode());
		assertEquals(identifierNull1, identifierNull1);
		assertNotEquals(identifierNull2, identifier1);
		assertNotEquals(identifierNull2.hashCode(), identifier1.hashCode());
		assertEquals(identifierNull2, identifierNull2);
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
