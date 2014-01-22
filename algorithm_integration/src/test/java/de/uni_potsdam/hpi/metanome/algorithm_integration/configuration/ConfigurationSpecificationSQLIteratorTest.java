package de.uni_potsdam.hpi.metanome.algorithm_integration.configuration;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link ConfigurationSpecificationSQLIterator}
 */
public class ConfigurationSpecificationSQLIteratorTest {

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
	 * Test method for {@link ConfigurationSpecificationSQLIterator#ConfigurationSpecificationSQLIterator(String)}
	 * 
	 * The identifier should be set in the constructor and be retrievable through getIdentifier.
	 * The numberOfValues should be set to 1.
	 */
	@Test
	public void testConstructorGetOne() {
		// Setup
		// Expected values
		String expectedIdentifier = "parameter1";
		int expectedNumberOfValues = 1;
		ConfigurationSpecificationSQLIterator configSpec = new ConfigurationSpecificationSQLIterator(expectedIdentifier);
		
		// Execute functionality
		String actualIdentifier = configSpec.getIdentifier();
		int actualNumberOfValues = configSpec.getNumberOfValues();
		
		// Check result
		assertEquals(expectedIdentifier, actualIdentifier);
		assertEquals(expectedNumberOfValues, actualNumberOfValues);
	}
	
	/**
	 * Test method for {@link ConfigurationSpecificationSQLIterator#ConfigurationSpecificationSQLIterator(String)}
	 * 
	 * The identifier should be set in the constructor and be retrievable through getIdentifier.
	 * The numberOfValues should be set to 2.
	 */
	@Test
	public void testConstructorGetTwo() {
		// Setup
		// Expected values
		String expectedIdentifier = "parameter1";
		int expectedNumberOfValues = 2;
		ConfigurationSpecificationSQLIterator configSpec = new ConfigurationSpecificationSQLIterator(expectedIdentifier, expectedNumberOfValues);
		
		// Execute functionality
		String actualIdentifier = configSpec.getIdentifier();
		int actualNumberOfValues = configSpec.getNumberOfValues();
		
		// Check result
		assertEquals(expectedIdentifier, actualIdentifier);
		assertEquals(expectedNumberOfValues, actualNumberOfValues);
	}
}
