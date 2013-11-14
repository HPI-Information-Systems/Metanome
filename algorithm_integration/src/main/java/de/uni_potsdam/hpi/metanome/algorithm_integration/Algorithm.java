package de.uni_potsdam.hpi.metanome.algorithm_integration;

import java.util.List;

import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification;

/**
 * An algorithm should supply the configuration requirements, should initiate double dispatch with incoming
 * configurations and receive the different configuration types.
 * Subclassing of this class will not lead to a functioning algorithm, as only more concrete algorithms can be started.
 */
public interface Algorithm {
	
	/**
	 * Algorithms should supply a list of needed configuration parameters.
	 * 
	 * @return a list of ConfigurationSpecifications
	 */
	List<ConfigurationSpecification> getConfigurationRequirements();
	
	/**
	 * Starts the execution of the algorithm.
	 */
	void execute() throws AlgorithmExecutionException;

}
