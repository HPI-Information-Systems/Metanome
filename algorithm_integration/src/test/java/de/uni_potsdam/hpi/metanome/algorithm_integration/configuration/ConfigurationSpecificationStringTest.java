package de.uni_potsdam.hpi.metanome.algorithm_integration.configuration;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationString;

/**
 * Tests for {@link ConfigurationSpecificationString}
 */
public class ConfigurationSpecificationStringTest {

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
	 * Test method for {@link ConfigurationSpecificationString#ConfigurationSpecificationString(String)}
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
		ConfigurationSpecificationString configSpec = new ConfigurationSpecificationString(expectedIdentifier);
		
		// Execute functionality
		String actualIdentifier = configSpec.getIdentifier();
		int actualNumberOfValues = configSpec.getNumberOfValues();
		
		// Check result
		assertEquals(expectedIdentifier, actualIdentifier);
		assertEquals(expectedNumberOfValues, actualNumberOfValues);
	}
	
	/**
	 * Test method for {@link ConfigurationSpecificationString#ConfigurationSpecificationString(String, int)}
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
		ConfigurationSpecificationString configSpec = new ConfigurationSpecificationString(expectedIdentifier, expectedNumberOfValues);
		
		// Execute functionality
		String actualIdentifier = configSpec.getIdentifier();
		int actualNumberOfValues = configSpec.getNumberOfValues();
		
		// Check result
		assertEquals(expectedIdentifier, actualIdentifier);
		assertEquals(expectedNumberOfValues, actualNumberOfValues);
	}
}
