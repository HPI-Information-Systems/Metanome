package de.uni_potsdam.hpi.metanome.algorithm_integration.configuration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uni_potsdam.hpi.metanome.algorithm_integration.Algorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.RelationalInputGenerator;

/**
 * @author Jakob Zwiener
 * 
 * Tests for {@link ConfigurationValueRelationalInputGenerator}
 */
public class ConfigurationValueRelationalInputGeneratorTest {
	
	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
		
	}

	/**
	 * Parameters should be set on the algorithm through triggerSetValue. This is the last call in a double 
	 * dispatch call to determine the parameters type.
	 * 
	 * @throws AlgorithmConfigurationException 
	 */
	@Test
	public void testTriggerSetValue() throws AlgorithmConfigurationException {
		// Setup
		Algorithm algorithm = mock(Algorithm.class);
		// Expected values
		String expectedIdentifier = "configId1";
		RelationalInputGenerator expectedConfigurationValue = mock(RelationalInputGenerator.class);
		
		// Execute functionality
		ConfigurationValueRelationalInputGenerator configValue = new ConfigurationValueRelationalInputGenerator(
				new ConfigurationSpecificationCsvFile(expectedIdentifier).getIdentifier(), expectedConfigurationValue);
		configValue.triggerSetValue(algorithm);
		
		// Check result
		verify(algorithm).setConfigurationValue(expectedIdentifier, expectedConfigurationValue);		
	}
}
