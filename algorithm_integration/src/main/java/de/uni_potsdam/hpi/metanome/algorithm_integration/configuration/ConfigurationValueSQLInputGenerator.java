package de.uni_potsdam.hpi.metanome.algorithm_integration.configuration;

import java.util.Set;

import de.uni_potsdam.hpi.metanome.algorithm_integration.Algorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.SqlInputParameterAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.SQLInputGenerator;

/**
 * Represents sql input configuration values for {@link Algorithm}s.
 * 
 * @author Jakob Zwiener
 */
public class ConfigurationValueSQLInputGenerator implements ConfigurationValue {

	protected final String identifier;
	protected final SQLInputGenerator value;
	
	/**
	 * Constructs a ConfigurationValueSQLInputGenerator using the specification's identifier and 
	 * a SQLInputGenerator as value.
	 * 
	 * @param identifier
	 * @param value
	 */
	public ConfigurationValueSQLInputGenerator(String identifier, SQLInputGenerator value) {
		this.identifier = identifier;
		this.value = value;
	}
	
	@Override
	public void triggerSetValue(Algorithm algorithm, Set<Class<?>> algorithmInterfaces) throws AlgorithmConfigurationException {
		if (!algorithmInterfaces.contains(SqlInputParameterAlgorithm.class)) {
			throw new AlgorithmConfigurationException("Algorithm does not accept sql input configuration values.");
		}
		
		SqlInputParameterAlgorithm sqlInputParameterAlgorithm = (SqlInputParameterAlgorithm) algorithm;
		sqlInputParameterAlgorithm.setConfigurationValue(identifier, value);		
	}
}
