package de.metanome.cli;

import de.metanome.algorithm_integration.Algorithm;
import de.metanome.algorithm_integration.AlgorithmConfigurationException;

/**
 * An {@link de.metanome.algorithm_integration.Algorithm} that takes {@link
 * HdfsInputGenerator} configuration values.
 */
public interface HdfsInputParameterAlgorithm extends Algorithm {

    /**
     * Sets a {@link HdfsInputGenerator} configuration value on the algorithm.
     *
     * @param identifier the value's identifier
     * @param values     the configuration values
     * @throws de.metanome.algorithm_integration.AlgorithmConfigurationException if the algorithm
     *                                                                           cannot be correctly
     *                                                                           configured using the
     *                                                                           received configuration
     *                                                                           values
     */
    void setHdfsInputConfigurationValue(String identifier, HdfsInputGenerator... values)
            throws AlgorithmConfigurationException;

}
