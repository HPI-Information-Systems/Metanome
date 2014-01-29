package de.uni_potsdam.hpi.metanome.algorithm_integration.results;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;

/**
 * Represents a unique column combination.
 * 
 * @author Jakob Zwiener
 */
public class UniqueColumnCombination implements Result {

	private static final long serialVersionUID = 6744030409666817339L;
	
	protected ColumnCombination columnCombination;
	
	/**
	 * Exists for GWT serialization.
	 */
	protected UniqueColumnCombination() {
		this.columnCombination = new ColumnCombination();
	}
	
	/**
	 * Constructs a {@link UniqueColumnCombination} from a number of {@link ColumnIdentifier}s.
	 * 
	 * @param columnIdentifier
	 */
	public UniqueColumnCombination(ColumnIdentifier... columnIdentifier) {
		this.columnCombination = new ColumnCombination(columnIdentifier);
	}
	
	/**
	 * Constructs a {@link UniqueColumnCombination} from a {@link ColumnCombination}.
	 * 
	 * @param columnCombination
	 */
	public UniqueColumnCombination(ColumnCombination columnCombination) {
		this.columnCombination = columnCombination;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((columnCombination == null) ? 0 : columnCombination
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UniqueColumnCombination other = (UniqueColumnCombination) obj;
		if (columnCombination == null) {
			if (other.columnCombination != null)
				return false;
		} else if (!columnCombination.equals(other.columnCombination))
			return false;
		return true;
	}	
}
