package de.uni_potsdam.hpi.metanome.algorithm_integration;

import static org.junit.Assert.assertEquals;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uni_potsdam.hpi.metanome.test_helper.GwtSerializationTester;

/**
 * Test for {@link AlgorithmExecutionException}
 */
public class AlgorithmExecutionExceptionTest {

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
	public void testAlgorithmExecutionException() {
		// Setup
		// Expected values
		String expectedMessage = "some message";
		
		// Execute functionality
		String actualMessage;
		try {
			throw new AlgorithmExecutionException(expectedMessage);
		} catch (AlgorithmExecutionException e) {
			actualMessage = e.getMessage();
		}
		
		// Check result 
		assertEquals(expectedMessage, actualMessage);
	}

	/**
	 * Tests that the instances of {@link AlgorithmExecutionException} are serializable in GWT.
	 */
	@Test
	public void testGwtSerialization() {
		GwtSerializationTester.checkGwtSerializability(new AlgorithmExecutionException(""));
	}
}
