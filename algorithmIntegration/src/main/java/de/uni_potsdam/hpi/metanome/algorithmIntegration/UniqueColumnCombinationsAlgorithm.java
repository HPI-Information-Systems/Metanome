package de.uni_potsdam.hpi.metanome.algorithmIntegration;

/**
 * An {@link Algorithm} that calculates unique {@link ColumnCombination}s.
 */
public abstract class UniqueColumnCombinationsAlgorithm extends Algorithm {

	/**
	 * Starts the execution of the {@link UniqueColumnCombinationsAlgorithm} and 
	 * supplies an {@link UniqueColumnCombinationResultReceiver} to send results to. 
	 * 
	 * @param resultReceiver
	 * @param configurationValues
	 */
	abstract public void start(UniqueColumnCombinationResultReceiver resultReceiver);
}
