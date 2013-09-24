
package de.uni_potsdam.hpi.metanome.algorithm_loading;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uni_potsdam.hpi.metanome.algorithm_integration.UniqueColumnCombinationsAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_loading.AlgorithmFinder;

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
		Class<?> algorithmType = new AlgorithmFinder().getAlgorithmClass(file);
		
		// Check result
		assertNotNull(algorithmType);
		assertEquals(UniqueColumnCombinationsAlgorithm.class, algorithmType);	
	}
	
	@Test
	public void retrieveAllJarFiles() throws IOException, ClassNotFoundException {
		// Setup
		String jarFilePath = ClassLoader.getSystemResource("testjar.jar").getPath();
		jarFilePath = jarFilePath.substring(0, jarFilePath.lastIndexOf(File.separator));
		AlgorithmFinder algoFinder = new AlgorithmFinder();
		
		//Execute
		String[] algos = algoFinder.getAvailableAlgorithms(null);
		
		//Check
		assertTrue(algos.length > 0);
	}
	
	@Test
	public void retrieveUniqueColumnCombinationJarFiles() throws IOException, ClassNotFoundException {
		// Setup
		AlgorithmFinder algoFinder = new AlgorithmFinder();
		
		//Execute
		String[] algos = algoFinder.getAvailableAlgorithms(UniqueColumnCombinationsAlgorithm.class);
		
		//Check
		assertTrue(algos.length > 0);
	}
}
