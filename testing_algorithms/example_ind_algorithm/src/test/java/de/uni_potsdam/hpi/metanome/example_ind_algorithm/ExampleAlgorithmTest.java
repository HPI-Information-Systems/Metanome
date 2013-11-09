package de.uni_potsdam.hpi.metanome.example_ind_algorithm;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmExecutionException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.InclusionDependencyResultReceiver;

public class ExampleAlgorithmTest {

	protected ExampleAlgorithm algorithm;
	protected String tableIdentifier;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		algorithm = new ExampleAlgorithm();
		tableIdentifier = "tableName";
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
		this.algorithm.setConfigurationValue(tableIdentifier, expectedConfigurationValue);
		
		// Check result
		assertEquals(expectedConfigurationValue, this.algorithm.tableName);
	}

	/**
	 * When the algorithm is started after configuration a result should be received.
	 * 
	 * @throws AlgorithmExecutionException 
	 */
	@Test
	public void testStart() throws AlgorithmExecutionException {
		// Setup
		InclusionDependencyResultReceiver resultReceiver = mock(InclusionDependencyResultReceiver.class);
		this.algorithm.setConfigurationValue(tableIdentifier, "something");
		
		// Execute functionality
		this.algorithm.setResultReceiver(resultReceiver);
		this.algorithm.execute();
		
		// Check result
		verify(resultReceiver).receiveResult(isA(ColumnCombination.class), isA(ColumnCombination.class));
	}
}
