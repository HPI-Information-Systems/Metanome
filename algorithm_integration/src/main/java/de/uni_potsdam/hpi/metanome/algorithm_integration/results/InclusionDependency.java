package de.uni_potsdam.hpi.metanome.algorithm_integration.results;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;

/**
 * @author Jakob Zwiener
 * 
 * Represents an inclusion dependency.
 */
public class InclusionDependency implements Result {

	private static final long serialVersionUID = 1217247222227000634L;
	
	public static final String IND_SEPARATOR = "[=";

	protected ColumnCombination dependant;
	protected ColumnCombination referenced;
	
	/**
	 * Exists for GWT serialization.
	 */
	public InclusionDependency() {
		this.referenced = new ColumnCombination();
		this.dependant = new ColumnCombination();
	}

	public InclusionDependency(ColumnCombination dependant, 
			ColumnCombination referenced) {
		this.dependant = dependant;
		this.referenced = referenced;		
	}
	
	/**
	 * @return dependant
	 */
	public ColumnCombination getDependant() {
		return dependant;
	}
	
	/**
	 * @return referenced
	 */
	public ColumnCombination getReferenced() {
		return referenced;
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
			.append(dependant)
			.append(IND_SEPARATOR)
			.append(referenced);
		
		return builder.toString();
	}
	
}
