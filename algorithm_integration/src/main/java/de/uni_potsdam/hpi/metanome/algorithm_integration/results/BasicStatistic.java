package de.uni_potsdam.hpi.metanome.algorithm_integration.results;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;

public class BasicStatistic implements Result {

	private static final long serialVersionUID = 3334423877877174177L;

	public static final String NAME_COLUMN_SEPARATOR = " of ";
	public static final String COLUMN_VALUE_SEPARATOR = ": ";
	
	ColumnCombination columns;
	String statisticName;
	Object statisticValue;
	
	/**
	 * Exists for GWT serialization.
	 */
	public BasicStatistic() {
		this.columns = new ColumnCombination();
		this.statisticName = "";
	}
	
	public BasicStatistic(String statisticName, Object statisticValue, ColumnIdentifier... columnIdentifier) {
		this.columns = new ColumnCombination(columnIdentifier);
		this.statisticName = statisticName;
		this.statisticValue = statisticValue;
	}
	
	
	@Override
	public void sendResultTo(OmniscientResultReceiver resultReceiver)
			throws CouldNotReceiveResultException {
		resultReceiver.receiveResult(this);		
	}
	
	/**
	 * @return the columns
	 */
	public ColumnCombination getColumnCombination() {
		return columns;
	}
	
	/**
	 * @return the name of the statistic
	 */
	public String getStatisticName() {
		return statisticName;
	}
	
	/**
	 * @return the value of the statistic on the columns
	 */
	public Object getStatisticValue() {
		return statisticValue;
	}

	@Override
	public String toString() {
		return statisticName + NAME_COLUMN_SEPARATOR + columns.toString() + COLUMN_VALUE_SEPARATOR + statisticValue.toString();
	}

}
