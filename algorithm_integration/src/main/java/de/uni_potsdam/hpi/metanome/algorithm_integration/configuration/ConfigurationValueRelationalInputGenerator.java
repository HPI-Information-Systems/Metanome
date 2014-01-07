package de.uni_potsdam.hpi.metanome.algorithm_integration.configuration;

import java.util.Set;

import de.uni_potsdam.hpi.metanome.algorithm_integration.Algorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.RelationalInputParameterAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.RelationalInputGenerator;

/**
 * Represents csv file configuration values for {@link Algorithm}s.
 * 
 * @author Jakob Zwiener
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
	
	@Override
	public void triggerSetValue(Algorithm algorithm, Set<Class<?>> algorithmInterfaces) throws AlgorithmConfigurationException {
		if (!algorithmInterfaces.contains(RelationalInputParameterAlgorithm.class)) {
			throw new AlgorithmConfigurationException("Algorithm does not accept relational input configuration values.");
		}
		RelationalInputParameterAlgorithm relationalInputParameterAlgorithm = (RelationalInputParameterAlgorithm) algorithm;
		relationalInputParameterAlgorithm.setConfigurationValue(identifier, value);		
	}	
}
