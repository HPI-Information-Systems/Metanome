package de.uni_potsdam.hpi.metanome.algorithm_integration;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Represents column combinations.
 */
public class ColumnCombination {
	
	protected Set<ColumnIdentifier> columnCombination;
	
	/**
	 * Store string identifiers for columns to form a column combination.
	 * 
	 * @param columnIdentifier
	 */
	public ColumnCombination(ColumnIdentifier... columnIdentifier) {
		columnCombination = new LinkedHashSet<ColumnIdentifier>(Arrays.asList(columnIdentifier));
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
}
