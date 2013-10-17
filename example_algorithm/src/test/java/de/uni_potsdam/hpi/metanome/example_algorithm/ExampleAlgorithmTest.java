package de.uni_potsdam.hpi.metanome.example_algorithm;

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

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationString;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.UniqueColumnCombinationResultReceiver;

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
	 * The algorithm should store the path when it is supplied through setConfigurationValue.
	 */
	@Test
	public void testSetConfigurationValue() {
		// Setup
		// Expected values
		String expectedConfigurationValue = "test";
		
		// Execute functionality
		this.algorithm.setConfigurationValue(pathIdentifier, expectedConfigurationValue);
		
		// Check result
		assertEquals(expectedConfigurationValue, this.algorithm.path);
	}

	/**
	 * When the algorithm is started after configuration a result should be received.
	 * @throws CouldNotReceiveResultException 
	 */
	@Test
	public void testStart() throws CouldNotReceiveResultException {
		// Setup
		UniqueColumnCombinationResultReceiver resultReceiver = mock(UniqueColumnCombinationResultReceiver.class);
		this.algorithm.setConfigurationValue(pathIdentifier, "something");
		
		// Execute functionality
		this.algorithm.start(resultReceiver);
		
		// Check result
		verify(resultReceiver).receiveResult(isA(ColumnCombination.class));
		}
}
