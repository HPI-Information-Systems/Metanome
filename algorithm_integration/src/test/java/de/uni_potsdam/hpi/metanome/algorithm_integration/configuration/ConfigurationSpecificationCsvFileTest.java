package de.uni_potsdam.hpi.metanome.algorithm_integration.configuration;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link ConfigurationSpecificationCsvFile}
 */
public class ConfigurationSpecificationCsvFileTest {

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
	 * Test method for {@link ConfigurationSpecificationCsvFile#ConfigurationSpecificationCsvFile(String)}
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
		ConfigurationSpecificationCsvFile configSpec = new ConfigurationSpecificationCsvFile(expectedIdentifier);
		
		// Execute functionality
		String actualIdentifier = configSpec.getIdentifier();
		int actualNumberOfValues = configSpec.getNumberOfValues();
		
		// Check result
		assertEquals(expectedIdentifier, actualIdentifier);
		assertEquals(expectedNumberOfValues, actualNumberOfValues);
	}
	
	/**
	 * Test method for {@link ConfigurationSpecificationCsvFile#ConfigurationSpecificationCsvFile(String, int)}
	 * 
	 * The identifier should be set in the constructor and be retrievable through getIdentifier.
	 * The numberOfValues should be set to 2.
	 */
	@Test
	public void testConstructorGetTwo() {
		// Setup
		// Expected values
		String expectedIdentifier = "parameter1";
		int expectedNumberOFValues = 2;
		ConfigurationSpecificationCsvFile configSpec = new ConfigurationSpecificationCsvFile(expectedIdentifier, expectedNumberOFValues);
		
		// Execute functionality
		String actualIdentifier = configSpec.getIdentifier();
		int actualNumberOfValues = configSpec.getNumberOfValues(); 
		
		// Check result
		assertEquals(expectedIdentifier, actualIdentifier);
		assertEquals(expectedNumberOFValues, actualNumberOfValues);
	}
}
