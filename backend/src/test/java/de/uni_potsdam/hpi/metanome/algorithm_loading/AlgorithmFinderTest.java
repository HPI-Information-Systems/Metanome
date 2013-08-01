
package de.uni_potsdam.hpi.metanome.algorithm_loading;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uni_potsdam.hpi.metanome.algorithm_integration.UniqueColumnCombinationsAlgorithm;

/**
 *
 */
public class AlgorithmFinderTest {

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
	 * A valid algorithm jar should be loadable and of correct class.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 */
	@Test
	public void getAlgorithmType() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException {
		// Setup
		String jarFilePath = ClassLoader.getSystemResource("testjar.jar").getFile();
		File file = new File(jarFilePath);
		
		// Execute functionality
		Type algorithmType = new AlgorithmFinder().getAlgorithmClass(file);
		
		// Check result
		assertNotNull(algorithmType);
		assertEquals(UniqueColumnCombinationsAlgorithm.class, algorithmType);	
	}
}
