package de.uni_potsdam.hpi.metanome.algorithm_integration.configuration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uni_potsdam.hpi.metanome.algorithm_integration.Algorithm;

/**
 * Tests for {@link ConfigurationValueBoolean}
 */
public class ConfigurationValueBooleanTest {

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
		
	}

	/**
	 * Parameters should be set on the algorithm through triggerSetValue. This is the last call in a double 
	 * dispatch call to determine the parameters type.
	 */
	@Test
	public void testTriggerSetValue() {
		// Setup
		Algorithm algorithm = mock(Algorithm.class);
		// Expected values
		String expectedIdentifier = "configId1";
		boolean expectedConfigurationValue = true;
		
		// Execute functionality
		ConfigurationValueBoolean configValue = new ConfigurationValueBoolean(
				new ConfigurationSpecificationBoolean(expectedIdentifier).getIdentifier(), expectedConfigurationValue);
		configValue.triggerSetValue(algorithm);
		
		// Check result
		verify(algorithm).setConfigurationValue(expectedIdentifier, expectedConfigurationValue);		
	}

}
