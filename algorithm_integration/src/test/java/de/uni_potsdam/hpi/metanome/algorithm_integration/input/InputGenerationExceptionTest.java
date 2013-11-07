package de.uni_potsdam.hpi.metanome.algorithm_integration.input;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link InputGenerationException}
 */
public class InputGenerationExceptionTest {

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
	 * Test method for {@link de.uni_potsdam.hpi.metanome.algorithm_integration.input.InputGenerationException#InputGenerationException(java.lang.String)}.
	 * 
	 * The exception should store the message.
	 */
	@Test
	public void testInputGenerationException() {
		// Setup
		// Expected values
		String expectedMessage = "some message";
		
		// Execute functionality
		String actualMessage;
		try {
			throw new InputGenerationException(expectedMessage);
		} catch (InputGenerationException e) {
			actualMessage = e.getMessage();
		}
		
		// Check result 
		assertEquals(expectedMessage, actualMessage);
	}

}
