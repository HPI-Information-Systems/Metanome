package de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types;

import de.uni_potsdam.hpi.metanome.algorithm_integration.Algorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;

/**
 * @author Jakob Zwiener
 * 
 * An {@link Algorithm} that takes boolean configuration values.
 */
public interface BooleanParameterAlgorithm extends Algorithm {

	/**
	 * Sets a boolean configuration value on the algorithm.
	 * 
	 * @param identifier
	 * @param value
	 */
	void setConfigurationValue(String identifier, boolean value) throws AlgorithmConfigurationException;
	
}
