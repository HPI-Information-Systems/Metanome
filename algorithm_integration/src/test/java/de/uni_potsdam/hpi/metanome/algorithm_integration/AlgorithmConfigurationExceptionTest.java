package de.uni_potsdam.hpi.metanome.algorithm_integration;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link AlgorithmConfigurationException}
 */
public class AlgorithmConfigurationExceptionTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link AlgorithmExecutionException#AlgorithmExecutionException(String)}
	 * 
	 * The exception should store the message.
	 */
	@Test
	public void testAlgorithmConfigurationExceptionString() {
		// Setup
		// Expected values
		String expectedMessage = "some message";
		
		// Execute functionality
		String actualMessage;
		try {
			throw new AlgorithmConfigurationException(expectedMessage);
		} catch (AlgorithmConfigurationException e) {
			actualMessage = e.getMessage();
		}
		
		// Check result 
		assertEquals(expectedMessage, actualMessage);
	}

}
