package de.uni_potsdam.hpi.metanome.algorithm_integration;

import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.BasicStatisticsResultReceiver;


/**
 * An {@link Algorithm} that discovers "simple" statistics such as
 * min/max values, data types etc.
 */
public abstract class BasicStatisticsAlgorithm extends Algorithm {

	/**
	 * Starts the execution of the algorithm and sends the results to 
	 * an {@link BasicStatisticsResultReceiver}.
	 * 
	 * @param resultReceiver
	 */
	abstract public void start(BasicStatisticsResultReceiver resultReceiver);
}
