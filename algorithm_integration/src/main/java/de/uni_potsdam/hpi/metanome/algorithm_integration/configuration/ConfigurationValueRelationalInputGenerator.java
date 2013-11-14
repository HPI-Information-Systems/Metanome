package de.uni_potsdam.hpi.metanome.algorithm_integration.configuration;

import de.uni_potsdam.hpi.metanome.algorithm_integration.Algorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.RelationalInputGenerator;

/**
 * @author Jakob Zwiener
 * 
 * Represents csv file configuration values for {@link Algorithm}s.
 */
public class ConfigurationValueRelationalInputGenerator implements ConfigurationValue {

	protected final String identifier;
	protected final RelationalInputGenerator value;
	
	/**
	 * Constructs a ConfigurationValueRelationalInputGenerator using the specification's identifier and the boolean value.
	 * 
	 * @param identifier
	 * @param value
	 */
	public ConfigurationValueRelationalInputGenerator(String identifier, RelationalInputGenerator value) {
		this.identifier = identifier;
		this.value = value;
	}
	
	/**
	 * Sets it's own value on the algorithm (second call of double dispatch).
	 * 
	 * @param algorithm
	 * @throws AlgorithmConfigurationException 
	 */
	@Override
	public void triggerSetValue(Algorithm algorithm) throws AlgorithmConfigurationException {
		algorithm.setConfigurationValue(identifier, value);		
	}

}
