package de.uni_potsdam.hpi.metanome.configuration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.SqlInputParameterAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationSQLIterator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.SQLInputGenerator;

/**
 * Tests for {@link ConfigurationValueSQLInputGenerator}
 * 
 * @author Jakob Zwiener
 */
public class ConfigurationValueSQLInputGeneratorTest {
	
	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
		
	}

	/**
	 * Test method for {@link ConfigurationValueSQLInputGenerator#triggerSetValue(de.uni_potsdam.hpi.metanome.algorithm_integration.Algorithm, Set)}
	 * 
	 * Parameters should be set on the algorithm through triggerSetValue. This is the last call in a double 
	 * dispatch call to determine the parameters type.
	 * 
	 * @throws AlgorithmConfigurationException 
	 */
	@Test
	public void testTriggerSetValue() throws AlgorithmConfigurationException {
		// Setup
		SqlInputParameterAlgorithm algorithm = mock(SqlInputParameterAlgorithm.class);
		Set<Class<?>> interfaces = new HashSet<Class<?>>();
		interfaces.add(SqlInputParameterAlgorithm.class);
		// Expected values
		String expectedIdentifier = "configId1";
		SQLInputGenerator[] expectedConfigurationValue = {mock(SQLInputGenerator.class), mock(SQLInputGenerator.class)};
		
		// Execute functionality
		ConfigurationValueSQLInputGenerator configValue = new ConfigurationValueSQLInputGenerator(
				new ConfigurationSpecificationSQLIterator(expectedIdentifier).getIdentifier(), expectedConfigurationValue);
		configValue.triggerSetValue(algorithm ,interfaces);
		
		// Check result
		verify(algorithm).setConfigurationValue(expectedIdentifier, expectedConfigurationValue);		
	}
}
