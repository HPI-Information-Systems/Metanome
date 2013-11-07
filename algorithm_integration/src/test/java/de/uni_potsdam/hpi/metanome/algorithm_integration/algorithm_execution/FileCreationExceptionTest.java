/**
 * 
 */
package de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_execution;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Jakob Zwiener
 *
 * Test for {@link FileCreationException}
 */
public class FileCreationExceptionTest {

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
	 * Test method for {@link FileCreationException#FileCreationException()}
	 * 
	 * TODO docs
	 */
	@Test
	public void testFileCreationException() {
		// Setup
		// Expected values
		String expectedMessage = "some message";
		
		// Execute functionality
		String actualMessage;
		try {
			throw new FileCreationException(expectedMessage);
		} catch (FileCreationException e) {
			actualMessage = e.getMessage();
		}
		
		// Check result 
		assertEquals(expectedMessage, actualMessage);
	}

}
