package de.uni_potsdam.hpi.metanome.algorithm_integration;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

/**
 * Represents column combinations.
 */
public class ColumnCombination implements Serializable {
	
	private static final long serialVersionUID = 5994284083803031188L;
	
	protected TreeSet<ColumnIdentifier> columnCombination;
	
	/**
	 * Exists for GWT serialization.
	 */
	protected ColumnCombination() {
		columnCombination = new TreeSet<ColumnIdentifier>();
	}
	
	/**
	 * Store string identifiers for columns to form a column combination.
	 * 
	 * @param columnIdentifier
	 */
	public ColumnCombination(ColumnIdentifier... columnIdentifier) {
		columnCombination = new TreeSet<ColumnIdentifier>(Arrays.asList(columnIdentifier));
	}
	
	/**
	 * Get column identifiers as set.
	 * 
	 * @return Set<ColumnIdentifier>
	 */
	public Set<ColumnIdentifier> getColumnIdentifiers() {
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
		ColumnCombination other = (ColumnCombination) obj;
		if (columnCombination == null) {
			if (other.columnCombination != null)
				return false;
		} else if (!columnCombination.equals(other.columnCombination))
			return false;
		return true;
	}
}
