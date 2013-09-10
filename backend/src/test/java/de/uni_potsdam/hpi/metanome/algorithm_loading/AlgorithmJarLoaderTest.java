
package de.uni_potsdam.hpi.metanome.algorithm_loading;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uni_potsdam.hpi.metanome.algorithm_integration.Algorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.UniqueColumnCombinationsAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_loading.AlgorithmJarLoader;
import de.uni_potsdam.hpi.metanome.result_receiver.UniqueColumnCombinationPrinter;

/**
 * Test for {@link AlgorithmJarLoader}
 */
public class AlgorithmJarLoaderTest {

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
	public void loadAlgorithm() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException {
		// Setup
		AlgorithmJarLoader<UniqueColumnCombinationsAlgorithm> loader = 
				new AlgorithmJarLoader<UniqueColumnCombinationsAlgorithm>(UniqueColumnCombinationsAlgorithm.class);
		String jarFilePath = ClassLoader.getSystemResource("testjar.jar").getFile();
		
		// Execute functionality
		Algorithm algorithm = loader.loadAlgorithm(jarFilePath);
		
		// Check result
		assertNotNull(algorithm);
		assertTrue(algorithm instanceof UniqueColumnCombinationsAlgorithm);	
		
		UniqueColumnCombinationsAlgorithm uccAlgorithm = (UniqueColumnCombinationsAlgorithm) algorithm;
		uccAlgorithm.start(new UniqueColumnCombinationPrinter(new PrintStream(new ByteArrayOutputStream())));
	}
}
