package de.uni_potsdam.hpi.metanome.algorithm_integration.results;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;

/**
 * @author Jakob Zwiener
 * 
 * Represents a unique column combination.
 */
public class UniqueColumnCombination implements Result {

	private static final long serialVersionUID = 6744030409666817339L;
	
	protected ColumnCombination columnCombination;
	
	public UniqueColumnCombination(ColumnIdentifier... columnIdentifier) {
		this.columnCombination = new ColumnCombination(columnIdentifier);
	}
	
	@Override
	public void sendResultTo(OmniscientResultReceiver resultReceiver) throws CouldNotReceiveResultException {
		resultReceiver.receiveResult(this);
	}
	
	/**
	 * @return the column combination
	 */
	public ColumnCombination getColumnCombination() {
		return columnCombination;
	}

	@Override
	public String toString() {
		return columnCombination.toString();
	}
}
