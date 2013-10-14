package de.uni_potsdam.hpi.metanome.algorithm_integration.configuration;

import de.uni_potsdam.hpi.metanome.algorithm_integration.Algorithm;

/**
 * Represents boolean configuration values for {@link Algorithm}s.
 */
public class ConfigurationValueBoolean implements ConfigurationValue {

	protected final String identifier;
	protected final boolean value;
	
	/**
	 * Constructs a ConfigurationValueString using the specification's identifier and the string value.
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
	 */
	@Override
	public void triggerSetValue(Algorithm algorithm) {
		algorithm.setConfigurationValue(identifier, value);		
	}

}
