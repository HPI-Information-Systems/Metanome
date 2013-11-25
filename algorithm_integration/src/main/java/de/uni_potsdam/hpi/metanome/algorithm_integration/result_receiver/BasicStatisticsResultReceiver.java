package de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver;

import java.io.Closeable;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;

/**
 * Receives the results of a {@link BasicStatisticsAlgorithm}.
 */
public interface BasicStatisticsResultReceiver extends Closeable {

	/**
	 * Receives a basic statistic, consisting of the concerned columns, a name and a value 
	 * of the statistic
	 
	 * @param columns		the concerned column, or @link{ColumnCombination} for multiple columns
	 * @param statisticName	a key or description of the statistic. Does not need to be unique but 
	 * 						should be meaningful to the user
	 * @param statisticValue	the calculated value of the statistic on the columns
	 */
	void receiveResult(ColumnCombination columns, String statisticName, Object statisticValue);
}
