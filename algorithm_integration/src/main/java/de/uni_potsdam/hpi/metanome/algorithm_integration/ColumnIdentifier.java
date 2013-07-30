package de.uni_potsdam.hpi.metanome.algorithm_integration;

public class ColumnIdentifier {

	protected final String tableIdentifier;
	protected final String columnIdentifier;
	
	/**
	 * @param tableIdentifier
	 * @param columnIdentifier
	 */
	public ColumnIdentifier(String tableIdentifier, String columnIdentifier) {
		this.tableIdentifier = tableIdentifier;
		this.columnIdentifier = columnIdentifier;
	}

	@Override
	public String toString() {
		return tableIdentifier + "." + columnIdentifier;
	}
	
	
}
