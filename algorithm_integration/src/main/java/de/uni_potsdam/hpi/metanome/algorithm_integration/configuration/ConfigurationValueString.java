package de.uni_potsdam.hpi.metanome.algorithm_integration.configuration;

import java.util.Set;

import de.uni_potsdam.hpi.metanome.algorithm_integration.Algorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.StringParameterAlgorithm;

/**
 * @author Jakob Zwiener
 * 
 * Represents string configuration values for {@link Algorithm}s.
 */
public class ConfigurationValueString implements ConfigurationValue {

	protected final String identifier;
	protected final String value;
	
	/**
	 * Constructs a ConfigurationValueString using the specification's identifier and the string value.
	 * 
	 * @param identifier
	 * @param value
	 */
	public ConfigurationValueString(String identifier, String value) {
		this.identifier = identifier;
		this.value = value;
	}
	
	@Override
	public void triggerSetValue(Algorithm algorithm, Set<Class<?>> algorithmInterfaces) throws AlgorithmConfigurationException {
		if (!algorithmInterfaces.contains(StringParameterAlgorithm.class)) {
			throw new AlgorithmConfigurationException("Algorithm does not accept string configuration values.");
		}
		
		StringParameterAlgorithm stringParameterAlgorithm = (StringParameterAlgorithm) algorithm;
		stringParameterAlgorithm.setConfigurationValue(identifier, value);
	}
}
