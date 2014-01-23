
package de.uni_potsdam.hpi.metanome.algorithm_loading;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uni_potsdam.hpi.metanome.algorithm_integration.Algorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmExecutionException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_execution.ProgressReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.ProgressEstimatingAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.UniqueColumnCombinationsAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;

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
	 * @throws AlgorithmExecutionException 
	 */
	@Test
	public void loadAlgorithm() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException, AlgorithmExecutionException {
		// Setup
		AlgorithmJarLoader loader = new AlgorithmJarLoader();
		
		// Execute functionality
		Algorithm algorithm = loader.loadAlgorithm("example_ucc_algorithm.jar");
		
		// Check result
		assertNotNull(algorithm);
		assertTrue(algorithm instanceof UniqueColumnCombinationsAlgorithm);	
		
		UniqueColumnCombinationsAlgorithm uccAlgorithm = (UniqueColumnCombinationsAlgorithm) algorithm;
		uccAlgorithm.setResultReceiver(mock(OmniscientResultReceiver.class));
		
		ProgressEstimatingAlgorithm progressAlgorithm = (ProgressEstimatingAlgorithm) algorithm;
		progressAlgorithm.setProgressReceiver(mock(ProgressReceiver.class));
		
		algorithm.execute();
	}
}
