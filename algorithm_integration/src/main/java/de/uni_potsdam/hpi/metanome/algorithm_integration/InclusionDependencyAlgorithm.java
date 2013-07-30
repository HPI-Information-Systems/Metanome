package de.uni_potsdam.hpi.metanome.algorithm_integration;

import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.InclusionDependencyResultReceiver;


/**
 * An {@link Algorithm} that discovers inclusion dependencies.
 */
public abstract class InclusionDependencyAlgorithm extends Algorithm {

	/**
	 * Starts the execution of the algorithm and sends the results to 
	 * an {@link InclusionDependencyResultReceiver}.
	 * 
	 * @param resultReceiver
	 */
	abstract public void start(InclusionDependencyResultReceiver resultReceiver);
}
