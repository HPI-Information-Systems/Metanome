package de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types;

import de.uni_potsdam.hpi.metanome.algorithm_integration.Algorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;

/**
 * An {@link Algorithm} that takes string configuration values.
 * 
 * @author Jakob Zwiener
 */
public interface StringParameterAlgorithm extends Algorithm {

	/**
	 * Sets a string configuration value on the algorithm.
	 * 
	 * @param identifier
	 * @param values
	 */
	void setConfigurationValue(String identifier, String... values) throws AlgorithmConfigurationException;
	
}
