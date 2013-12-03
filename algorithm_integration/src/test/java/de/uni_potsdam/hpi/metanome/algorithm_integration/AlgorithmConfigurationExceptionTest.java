package de.uni_potsdam.hpi.metanome.algorithm_integration;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uni_potsdam.hpi.metanome.test_helper.GwtSerializationTester;

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
	 * Test method for {@link AlgorithmConfigurationException#AlgorithmConfigurationException(String)}
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
	
	/**
	 * Tests that the instances of {@link AlgorithmConfigurationException} are serializable in GWT.
	 */
	@Test
	public void testGwtSerialization() {
		GwtSerializationTester.checkGwtSerializability(new AlgorithmConfigurationException(""));
	}

}
