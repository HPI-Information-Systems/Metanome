package de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types;

import de.uni_potsdam.hpi.metanome.algorithm_integration.Algorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.BasicStatisticsResultReceiver;

/**
 * An {@link Algorithm} that discovers "simple" statistics such as
 * min/max values, data types etc.
 */
public interface BasicStatisticsAlgorithm extends Algorithm {

	/**
	 * Sets a {@link BasicStatisticsResultReceiver} to send the results to.
	 * 
	 * @param resultReceiver
	 */
	void setResultReceiver(BasicStatisticsResultReceiver resultReceiver);
}
