package de.uni_potsdam.hpi.metanome.algorithm_integration.input;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link InputIterationException}
 */
public class InputIterationExceptionTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link de.uni_potsdam.hpi.metanome.algorithm_integration.input.InputIterationException#InputIterationException(java.lang.String)}.
	 * 
	 * The exception should store the message.
	 */
	@Test
	public void testInputIterationException() {
		// Setup
		// Expected values
		String expectedMessage = "some message";
		
		// Execute functionality
		String actualMessage;
		try {
			throw new InputIterationException(expectedMessage);
		} catch (InputIterationException e) {
			actualMessage = e.getMessage();
		}
		
		// Check result 
		assertEquals(expectedMessage, actualMessage);
	}

}
