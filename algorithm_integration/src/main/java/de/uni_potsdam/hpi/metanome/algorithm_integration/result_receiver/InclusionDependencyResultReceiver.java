package de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.InclusionDependencyAlgorithm;

/**
 * Receives the results of a {@link InclusionDependencyAlgorithm}.
 */
public interface InclusionDependencyResultReceiver {

	/**
	 * Receives two unique column combinations from an {@link InclusionDependencyAlgorithm}.
	 * 
	 * @param dependent
	 * @param referenced
	 * @throws CouldNotReceiveResultException 
	 */
	void receiveResult(ColumnCombination dependent, ColumnCombination referenced) throws CouldNotReceiveResultException;
}
