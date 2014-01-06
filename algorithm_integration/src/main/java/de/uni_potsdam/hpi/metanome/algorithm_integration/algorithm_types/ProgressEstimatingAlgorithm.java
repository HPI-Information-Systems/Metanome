package de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types;

import de.uni_potsdam.hpi.metanome.algorithm_integration.Algorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_execution.ProgressReceiver;

/**
 * @author Jakob Zwiener
 *
 * An {@link Algorithm} that estimates it's own progress.
 */
public interface ProgressEstimatingAlgorithm extends Algorithm {
	
	/**
	 * @param progressReceiver
	 */
	void setProgressReceiver(ProgressReceiver progressReceiver);
}
