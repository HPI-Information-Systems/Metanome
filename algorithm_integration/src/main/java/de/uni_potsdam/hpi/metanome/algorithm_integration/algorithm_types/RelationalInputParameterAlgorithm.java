package de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types;

import de.uni_potsdam.hpi.metanome.algorithm_integration.Algorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.RelationalInputGenerator;

/**
 * @author Jakob Zwiener
 * 
 * An {@link Algorithm} that takes {@link RelationalInputGenerator} configuration values.
 */
public interface RelationalInputParameterAlgorithm extends Algorithm {
	
	/**
	 * Sets a CsvFileGenerator configuration value on the algorithm.
	 * 
	 * @param identifier
	 * @param value
	 */
	void setConfigurationValue(String identifier, RelationalInputGenerator value) throws AlgorithmConfigurationException;

}
