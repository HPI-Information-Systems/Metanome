package de.uni_potsdam.hpi.metanome.algorithm_integration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import de.uni_potsdam.hpi.metanome.algorithm_integration.Algorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ConfigurationValue;

/**
 * Tests for {@link Algorithm}
 */
public class AlgorithmTest {

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
	 * The configure method should trigger the configuration through double dispatch on all the
	 * {@link ConfigurationValue}s.
	 */
	@Test
	public void testConfigure() {
		// Setup
		List<ConfigurationValue> configurationValues = new ArrayList<ConfigurationValue>();
		Algorithm algorithm = mock(Algorithm.class, Mockito.CALLS_REAL_METHODS);
		
		// Expected values
		for (int i = 0; i < 3; i++) {
			ConfigurationValue configValue = mock(ConfigurationValue.class);
			configurationValues.add(configValue);
		}
		
		// Execute functionality
		algorithm.configure(configurationValues);	
		
		// Check result
		for (ConfigurationValue configValue : configurationValues) {
			verify(configValue).triggerSetValue(algorithm);
		}
	}	
}
