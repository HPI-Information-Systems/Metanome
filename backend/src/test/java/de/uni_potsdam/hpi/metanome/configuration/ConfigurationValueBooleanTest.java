/*
 * Copyright 2014 by the Metanome project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.uni_potsdam.hpi.metanome.configuration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.BooleanParameterAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationBoolean;

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
	 * Test method for {@link ConfigurationValueBoolean#ConfigurationValueBoolean(String, boolean...)}
	 * 
	 * Parameters should be set on the algorithm through triggerSetValue. This is the last call in a double 
	 * dispatch call to determine the parameters type.
	 * 
	 * @throws AlgorithmConfigurationException 
	 */
	@Test
	public void testTriggerSetValue() throws AlgorithmConfigurationException {
		// Setup
		BooleanParameterAlgorithm algorithm = mock(BooleanParameterAlgorithm.class);
		Set<Class<?>> interfaces = new HashSet<Class<?>>();
		interfaces.add(BooleanParameterAlgorithm.class);
		// Expected values
		String expectedIdentifier = "configId1";
		boolean[] expectedConfigurationValue = {true, false};
		
		// Execute functionality
		ConfigurationValueBoolean configValue = new ConfigurationValueBoolean(
				new ConfigurationSpecificationBoolean(expectedIdentifier).getIdentifier(), expectedConfigurationValue);
		configValue.triggerSetValue(algorithm, interfaces);
		
		// Check result
		verify(algorithm).setConfigurationValue(expectedIdentifier, expectedConfigurationValue);		
	}

}
