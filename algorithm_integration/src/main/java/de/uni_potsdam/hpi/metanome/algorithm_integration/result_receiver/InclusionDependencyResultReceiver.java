package de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver;

import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.InclusionDependencyAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.InclusionDependency;

/**
 * Receives the results of a {@link InclusionDependencyAlgorithm}.
 */
public interface InclusionDependencyResultReceiver {

	/**
	 * Receives an {@link InclusionDependency} from an {@link InclusionDependencyAlgorithm}.
	 * 
	 * @param inclusionDependency
	 * @throws CouldNotReceiveResultException 
	 */
	void receiveResult(InclusionDependency inclusionDependency) throws CouldNotReceiveResultException;
}
