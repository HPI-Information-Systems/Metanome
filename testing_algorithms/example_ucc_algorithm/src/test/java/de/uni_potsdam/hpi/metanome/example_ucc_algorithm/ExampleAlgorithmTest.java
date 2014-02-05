package de.uni_potsdam.hpi.metanome.example_ucc_algorithm;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_execution.ProgressReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationString;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.UniqueColumnCombinationResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.UniqueColumnCombination;

/**
 * Test for {@link ExampleAlgorithm}
 *
 * @author Jakob Zwiener
 */
public class ExampleAlgorithmTest {

	protected ExampleAlgorithm algorithm;
	protected String pathIdentifier;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		algorithm = new ExampleAlgorithm();
		pathIdentifier = "pathToInputFile";
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		
	}

	/**
	 * Test method for {@link ExampleAlgorithm#getConfigurationRequirements()}
	 * 
	 * The algorithm should return one configuration specification of string type
	 */
	@Test
	public void testGetConfigurationRequirements() {
		// Execute functionality
		List<ConfigurationSpecification> actualConfigurationRequirements = this.algorithm.getConfigurationRequirements();
		
		// Check result
		assertEquals(2, actualConfigurationRequirements.size());
		assertThat(actualConfigurationRequirements.get(0), instanceOf(ConfigurationSpecificationString.class));
	}

	/**
	 * Test method for {@link ExampleAlgorithm#setConfigurationValue(String, String...)}
	 * 
	 * The algorithm should store the path when it is supplied through setConfigurationValue.
	 * 
	 * @throws AlgorithmConfigurationException 
	 */
	@Test
	public void testSetConfigurationValue() throws AlgorithmConfigurationException {
		// Setup
		// Expected values
		String[] expectedConfigurationValues = {"test1", "test2"};
		
		// Execute functionality
		this.algorithm.setConfigurationValue(pathIdentifier, expectedConfigurationValues);
		
		// Check result
		assertEquals(expectedConfigurationValues[0], this.algorithm.path1);
		assertEquals(expectedConfigurationValues[1], this.algorithm.path2);
	}

	/**
	 * Test method for {@link ExampleAlgorithm#execute()}
	 * 
	 * When the algorithm is started after configuration a result should be received.
	 * 
	 * @throws AlgorithmConfigurationException 
	 * @throws CouldNotReceiveResultException 
	 */
	@Test
	public void testExecute() throws AlgorithmConfigurationException, CouldNotReceiveResultException {
		// Setup
		UniqueColumnCombinationResultReceiver resultReceiver = mock(UniqueColumnCombinationResultReceiver.class);
		ProgressReceiver progressCache = mock(ProgressReceiver.class);
		String[] configurationValues = {"something1", "something2"};
		this.algorithm.setConfigurationValue(pathIdentifier, configurationValues);
		
		// Execute functionality
		this.algorithm.setResultReceiver(resultReceiver);
		this.algorithm.setProgressReceiver(progressCache);
		this.algorithm.execute();
				
		// Check result
		verify(resultReceiver).receiveResult(isA(UniqueColumnCombination.class));
	}
}
