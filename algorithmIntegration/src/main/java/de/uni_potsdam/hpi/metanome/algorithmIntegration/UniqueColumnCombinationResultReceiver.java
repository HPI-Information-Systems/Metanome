package de.uni_potsdam.hpi.metanome.algorithmIntegration;

/**
 * Receives the results of a {@link UniqueColumnCombinationsAlgorithm}.
 */
public interface UniqueColumnCombinationResultReceiver {
	
	/**
	 * Receives an unique column combination from an {@link UniqueColumnCombinationsAlgorithm}.
	 * 
	 * @param columnCombination
	 */
	void receiveResult(ColumnCombination columnCombination);

}
