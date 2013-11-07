package de.uni_potsdam.hpi.metanome.algorithm_integration;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

}
