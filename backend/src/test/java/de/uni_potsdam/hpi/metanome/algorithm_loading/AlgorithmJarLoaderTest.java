
package de.uni_potsdam.hpi.metanome.algorithm_loading;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uni_potsdam.hpi.metanome.algorithm_integration.Algorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.UniqueColumnCombinationsAlgorithm;

/**

 *
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
	 */
	@Test
	public void loadAlgorithm() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		// Setup
		AlgorithmJarLoader<UniqueColumnCombinationsAlgorithm> loader = 
				new AlgorithmJarLoader<UniqueColumnCombinationsAlgorithm>(UniqueColumnCombinationsAlgorithm.class);
		String jarFilePath = ClassLoader.getSystemResource("testjar.jar").getFile();
		
		// Execute functionality
		Algorithm algorithm = loader.loadAlgorithm(jarFilePath);
		
		// Check result
		assertNotNull(algorithm);
		assertTrue(algorithm instanceof UniqueColumnCombinationsAlgorithm);		
	}

}
