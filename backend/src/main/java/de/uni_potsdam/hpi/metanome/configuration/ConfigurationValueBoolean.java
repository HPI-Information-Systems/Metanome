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

import java.util.Set;

import de.uni_potsdam.hpi.metanome.algorithm_integration.Algorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.BooleanParameterAlgorithm;

/**
 * Represents boolean configuration values for {@link Algorithm}s.
 */
public class ConfigurationValueBoolean implements ConfigurationValue {

	protected final String identifier;
	protected final boolean[] values;
	
	/**
	 * Constructs a ConfigurationValueBoolean using the specification's identifier and the boolean value.
	 * 
	 * @param identifier
	 * @param values
	 */
	public ConfigurationValueBoolean(String identifier, boolean... values) {
		this.identifier = identifier;
		this.values = values;
	}
	
	@Override
	public void triggerSetValue(Algorithm algorithm, Set<Class<?>> algorithmInterfaces) throws AlgorithmConfigurationException {
		if (!algorithmInterfaces.contains(BooleanParameterAlgorithm.class)) {
			throw new AlgorithmConfigurationException("Algorithm does not accept boolean configuration values.");
		}
		
		BooleanParameterAlgorithm booleanParameterAlgorithm = (BooleanParameterAlgorithm) algorithm;
		booleanParameterAlgorithm.setConfigurationValue(identifier, values);
	}
}
