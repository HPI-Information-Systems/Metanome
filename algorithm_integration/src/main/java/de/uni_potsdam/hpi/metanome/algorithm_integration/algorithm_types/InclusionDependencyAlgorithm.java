package de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types;

import de.uni_potsdam.hpi.metanome.algorithm_integration.Algorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.InclusionDependencyResultReceiver;

/**
 * An {@link Algorithm} that discovers inclusion dependencies.
 */
public interface InclusionDependencyAlgorithm extends Algorithm {

	/**
	 * Sets a {@link InclusionDependencyResultReceiver} to send the results to.
	 * 
	 * @param resultReceiver
	 */
	void setResultReceiver(InclusionDependencyResultReceiver resultReceiver);
	
}
