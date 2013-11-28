package de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver;

import java.io.Closeable;

import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.UniqueColumnCombinationsAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.UniqueColumnCombination;

/**
 * Receives the results of a {@link UniqueColumnCombinationsAlgorithm}.
 */
public interface UniqueColumnCombinationResultReceiver extends Closeable {
	
	/**
	 * Receives an unique column combination from an {@link UniqueColumnCombinationsAlgorithm}.
	 * 
	 * @param columnCombination
	 * @throws CouldNotReceiveResultException 
	 */
	void receiveResult(UniqueColumnCombination uniqueColumnCombination) throws CouldNotReceiveResultException;
}
