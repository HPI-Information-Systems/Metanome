package de.uni_potsdam.hpi.metanome.algorithm_integration.results;

import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;

public interface Result {

	public void sendResultTo(OmniscientResultReceiver resultReceiver);
	
}
