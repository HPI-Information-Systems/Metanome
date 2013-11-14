package de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types;

import de.uni_potsdam.hpi.metanome.algorithm_integration.Algorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.FunctionalDependencyResultReceiver;

/**
 * An {@link Algorithm} that discovers functional dependencies.
 */
public interface FunctionalDependencyAlgorithm extends Algorithm {

	/**
	 * Sets a {@link FunctionalDependencyResultReceiver} to send the results to.
	 * 
	 * @param resultReceiver
	 */
	void setResultReceiver(FunctionalDependencyResultReceiver resultReceiver);
	
}
