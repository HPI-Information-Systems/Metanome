package de.uni_potsdam.hpi.metanome.algorithm_execution;

import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_execution.ProgressReceiver;

/**
 * Stores received progress estimations.
 * 
 * @author Jakob Zwiener
 */
public class ProgressCache implements ProgressReceiver {	
	
	protected float progress;

	public ProgressCache() {
		progress = 0;
	}
	
	/**
	 * @return progress
	 */
	public float getProgress() {
		return progress;
	}
	
	@Override
	public boolean updateProgress(float progress) {
		// Progress should be between 0 and 1 including bounds.
		if ((progress < 0) || (progress > 1)) {
			return false;
		}
				
		this.progress = progress;
		
		return true;
	}

}
