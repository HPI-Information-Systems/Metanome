package de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types;

import de.uni_potsdam.hpi.metanome.algorithm_integration.Algorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.SQLInputGenerator;

/**
 * An {@link Algorithm} that takes {@link SQLInputGenerator} configuration values.
 * 
 * @author Jakob Zwiener
 */
public interface SqlInputParameterAlgorithm extends Algorithm {

	/**
	 * Sets a SQLInputGenerator configuration value on the algorithm.
	 * 
	 * @param identifier the parameter's identifier
	 * @param values one or multiple SQLInputGenerator values
	 */
	void setConfigurationValue(String identifier, SQLInputGenerator... values) throws AlgorithmConfigurationException;
	
}
