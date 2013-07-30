package de.uni_potsdam.hpi.metanome.algorithm_integration;

import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.FunctionalDependencyResultReceiver;


/**
 * An {@link Algorithm} that discovers functional dependencies.
 */
public abstract class FunctionalDependencyAlgorithm extends Algorithm {

	/**
	 * Starts the execution of the algorithm and sends the results to 
	 * an {@link FunctionalDependencyResultReceiver}.
	 * 
	 * @param resultReceiver
	 */
	abstract public void start(FunctionalDependencyResultReceiver resultReceiver);
}
