package de.uni_potsdam.hpi.metanome.algorithm_integration.configuration;

import de.uni_potsdam.hpi.metanome.algorithm_integration.Algorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;

/**
 * Represents boolean configuration values for {@link Algorithm}s.
 */
public class ConfigurationValueBoolean implements ConfigurationValue {

	protected final String identifier;
	protected final boolean value;
	
	/**
	 * Constructs a ConfigurationValueBoolean using the specification's identifier and the boolean value.
	 * 
	 * @param identifier
	 * @param value
	 */
	public ConfigurationValueBoolean(String identifier, boolean value) {
		this.identifier = identifier;
		this.value = value;
	}
	
	/**
	 * Sets it's own value on the algorithm (second call of double dispatch).
	 * 
	 * @param algorithm
	 * 
	 * @throws AlgorithmConfigurationException 
	 */
	@Override
	public void triggerSetValue(Algorithm algorithm) throws AlgorithmConfigurationException {
		algorithm.setConfigurationValue(identifier, value);		
	}

}
