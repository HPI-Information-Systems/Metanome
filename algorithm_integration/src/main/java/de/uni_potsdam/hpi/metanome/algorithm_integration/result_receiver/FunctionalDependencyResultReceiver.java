package de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver;

import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.FunctionalDependencyAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.FunctionalDependency;

/**
 * Receives the results of a {@link FunctionalDependencyAlgorithm}.
 */
public interface FunctionalDependencyResultReceiver {

	/**
	 * Receives a {@link FunctionalDependency} from a {@link FunctionalDependencyAlgorithm}.
	 * 
	 * @param functionalDependency
	 * @throws CouldNotReceiveResultException 
	 */
	void receiveResult(FunctionalDependency functionalDependency) throws CouldNotReceiveResultException;
}
