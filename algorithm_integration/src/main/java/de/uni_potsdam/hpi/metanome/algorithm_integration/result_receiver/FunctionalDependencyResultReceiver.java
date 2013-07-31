package de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier;
import de.uni_potsdam.hpi.metanome.algorithm_integration.FunctionalDependencyAlgorithm;

/**
 * Receives the results of a {@link FunctionalDependencyAlgorithm}.
 */
public interface FunctionalDependencyResultReceiver {

	/**
	 * Receives a functional dependency as a determinant {@link ColumnCombination} and
	 * a dependent {@link ColumnIdentifier}.
	 * 
	 * @param determinant
	 * @param dependent
	 */
	void receiveResult(ColumnCombination determinant, ColumnIdentifier dependent);
}
