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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((columnIdentifier == null) ? 0 : columnIdentifier.hashCode());
		result = prime * result
				+ ((tableIdentifier == null) ? 0 : tableIdentifier.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ColumnIdentifier other = (ColumnIdentifier) obj;
		if (columnIdentifier == null) {
			if (other.columnIdentifier != null)
				return false;
		} else if (!columnIdentifier.equals(other.columnIdentifier))
			return false;
		if (tableIdentifier == null) {
			if (other.tableIdentifier != null)
				return false;
		} else if (!tableIdentifier.equals(other.tableIdentifier))
			return false;
		return true;
	}
	
	
}
