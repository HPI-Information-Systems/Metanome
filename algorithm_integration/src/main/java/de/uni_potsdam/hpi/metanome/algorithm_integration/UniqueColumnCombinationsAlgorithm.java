package de.uni_potsdam.hpi.metanome.algorithm_integration;

import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.UniqueColumnCombinationResultReceiver;

/**
 * An {@link Algorithm} that discovers unique {@link ColumnCombination}s.
 */
public abstract class UniqueColumnCombinationsAlgorithm extends Algorithm {

	/**
	 * Starts the execution of the algorithm and sends the results to
	 * an {@link UniqueColumnCombinationResultReceiver}. 
	 * 
	 * @param resultReceiver
	 */
	abstract public void start(UniqueColumnCombinationResultReceiver resultReceiver);
}
