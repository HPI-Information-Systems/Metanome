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
