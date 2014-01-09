package de.uni_potsdam.hpi.metanome.algorithm_integration.results;

import java.io.Serializable;

import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;

/**
 * All Results need to be sendable to an {@link OmniscientResultReceiver}.
 * 
 * @author Jakob Zwiener
 */
public interface Result extends Serializable {

	/**
	 * Sends a result to an {@link OmniscientResultReceiver}.
	 * 
	 * @param resultReceiver
	 * 
	 * @throws CouldNotReceiveResultException 
	 */
	public void sendResultTo(OmniscientResultReceiver resultReceiver) throws CouldNotReceiveResultException;
	
}
