package de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types;

import de.uni_potsdam.hpi.metanome.algorithm_integration.Algorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.UniqueColumnCombinationResultReceiver;

/**
 * An {@link Algorithm} that discovers unique {@link ColumnCombination}s.
 */
public interface UniqueColumnCombinationsAlgorithm extends Algorithm {

	/**
	 * Sets a {@link UniqueColumnCombinationResultReceiver} to send the results to.
	 * 
	 * @param resultReceiver
	 */
	void setResultReceiver(UniqueColumnCombinationResultReceiver resultReceiver);
	
}
