package de.uni_potsdam.hpi.metanome.algorithm_integration.results;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;

/**
 * @author Jakob Zwiener
 * 
 * Represents a functional dependency.
 */
public class FunctionalDependency implements Result {

	private static final long serialVersionUID = -8530894352049893955L;

	public static final String FD_SEPARATOR = "-->";
	
	protected ColumnCombination determinant;
	protected ColumnIdentifier dependant;

	/**
	 * Exists for GWT serialization.
	 */
	public FunctionalDependency() {
		this.determinant = new ColumnCombination();
		this.dependant = new ColumnIdentifier("", "");
	}
	
	public FunctionalDependency(ColumnCombination determinant,
			ColumnIdentifier dependant) {
		this.determinant = determinant;
		this.dependant = dependant;
	}
	
	/**
	 * @return determinant
	 */
	public ColumnCombination getDeterminant() {
		return determinant;
	}

	/**
	 * @return dependant
	 */
	public ColumnIdentifier getDependant() {
		return dependant;
	}

	@Override
	public void sendResultTo(OmniscientResultReceiver resultReceiver)
			throws CouldNotReceiveResultException {
		resultReceiver.receiveResult(this);		
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder
			.append(determinant)
			.append(FD_SEPARATOR)
			.append(dependant);
		
		return builder.toString();
	}

}
