package de.metanome.cli;

import de.hpi.isg.profiledb.ProfileDB;
import de.hpi.isg.profiledb.store.model.Experiment;
import de.metanome.algorithm_integration.Algorithm;
import de.metanome.algorithm_integration.AlgorithmConfigurationException;

/**
 * An {@link Algorithm} that takes {@link ProfileDB} {@link Experiment} configuration values.
 */
public interface ExperimentParameterAlgorithm extends Algorithm {

    /**
     * Sets a {@link Experiment} configuration value on the algorithm.
     *
     * @param experiment     the configuration value
     * @throws AlgorithmConfigurationException if the algorithm cannot be correctly configured using the received
     *                                         configuration values
     */
    void setProfileDBExperiment(Experiment experiment) throws AlgorithmConfigurationException;

}
