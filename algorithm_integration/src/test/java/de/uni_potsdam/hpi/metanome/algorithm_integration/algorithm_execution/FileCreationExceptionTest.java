/**
 * 
 */
package de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_execution;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uni_potsdam.hpi.metanome.test_helper.GwtSerializationTester;

/**
 * Test for {@link FileCreationException}
 * 
 * @author Jakob Zwiener
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
	 * The exception should store the message.
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
	
	/**
	 * Tests that the instances of {@link FileCreationException} are serializable in GWT.
	 */
	@Test
	public void testGwtSerialization() {
		GwtSerializationTester.checkGwtSerializability(new FileCreationException(""));
	}

}
