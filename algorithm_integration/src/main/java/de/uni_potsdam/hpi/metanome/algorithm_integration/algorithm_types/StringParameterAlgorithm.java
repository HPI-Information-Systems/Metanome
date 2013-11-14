package de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types;

import de.uni_potsdam.hpi.metanome.algorithm_integration.Algorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;

/**
 * @author Jakob Zwiener
 *
 * An {@link Algorithm} that takes string configuration values.
 */
public interface StringParameterAlgorithm extends Algorithm {

	/**
	 * Sets a string configuration value on the algorithm.
	 * 
	 * @param identifier
	 * @param value
	 */
	void setConfigurationValue(String identifier, String value) throws AlgorithmConfigurationException;
	
}
