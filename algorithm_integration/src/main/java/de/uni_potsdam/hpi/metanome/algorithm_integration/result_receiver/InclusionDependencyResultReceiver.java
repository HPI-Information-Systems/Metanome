package de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver;

import java.io.Closeable;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.InclusionDependencyAlgorithm;

/**
 * Receives the results of a {@link InclusionDependencyAlgorithm}.
 */
public interface InclusionDependencyResultReceiver extends Closeable {

	/**
	 * Receives two unique column combinations from an {@link InclusionDependencyAlgorithm}.
	 * 
	 * @param dependent
	 * @param referenced
	 * @throws CouldNotReceiveResultException 
	 */
	void receiveResult(ColumnCombination dependent, ColumnCombination referenced) throws CouldNotReceiveResultException;
}
