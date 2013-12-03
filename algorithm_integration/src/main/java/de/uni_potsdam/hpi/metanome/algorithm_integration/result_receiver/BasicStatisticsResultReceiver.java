package de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver;

import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.BasicStatisticsAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.BasicStatistic;

/**
 * Receives the results of a {@link BasicStatisticsAlgorithm}.
 */
public interface BasicStatisticsResultReceiver {

	/**
	 * Receives a basic statistic, consisting of the concerned columns, a name and a value 
	 * of the statistic
	 
	 * @param statisticValue	the calculated value of the statistic on the columns
	 * 
	 * @throws CouldNotReceiveResultException
	 */
	void receiveResult(BasicStatistic statistic) throws CouldNotReceiveResultException;
}
