/**
 * 
 */
package de.uni_potsdam.hpi.metanome.algorithmIntegration;

import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link ConfigurationValueString}
 */
public class ConfigurationValueStringTest {

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
	 * Parameters should be set on the algorithm through triggerSetValue. This is the last call in a double 
	 * dispatch call to determine the parameters type.
	 */
	@Test
	public void testTriggerSetValue() {
		// Setup
		Algorithm algorithm = mock(Algorithm.class);
		// Expected values
		String expectedIdentifier = "configId1";
		String expectedConfigurationValue = "value1";
		
		// Execute functionality
		ConfigurationValueString configValue = new ConfigurationValueString(
				new ConfigurationSpecificationString(expectedIdentifier), expectedConfigurationValue);
		configValue.triggerSetValue(algorithm);
		
		// Check result
		verify(algorithm).setConfigurationValue(expectedIdentifier, expectedConfigurationValue);		
	}
}
