package de.uni_potsdam.hpi.metanome.result_receiver;

import java.util.HashSet;
import java.util.Set;

import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.BasicStatistic;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.FunctionalDependency;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.InclusionDependency;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.UniqueColumnCombination;

public class ResultsHub implements OmniscientResultReceiver {
	
	protected Set<OmniscientResultReceiver> subscriber = new HashSet<OmniscientResultReceiver>();

	/**
	 * Adds an {@link OmniscientResultReceiver} to the set of subscribers.
	 * 
	 * @param resultReceiver
	 */
	public void addSubscriber(OmniscientResultReceiver resultReceiver) {
		subscriber.add(resultReceiver);	
	}

	/**
	 * @return subscriber
	 */
	public Set<OmniscientResultReceiver> getSubscriber() {
		return subscriber;
	}

	/**
	 * Removes an {@link OmniscientResultReceiver} from the subscriber set.
	 * 
	 * @param resultReceiver
	 */
	public void removeSubscriber(OmniscientResultReceiver resultReceiver) {
		subscriber.remove(resultReceiver);
	}

	@Override
	public void receiveResult(BasicStatistic statistic) 
			throws CouldNotReceiveResultException {
		for (OmniscientResultReceiver resultReceiver : subscriber) {
			resultReceiver.receiveResult(statistic);
		}
	}

	@Override
	public void receiveResult(FunctionalDependency functionalDependency)
			throws CouldNotReceiveResultException {
		for (OmniscientResultReceiver resultReceiver : subscriber) {
			resultReceiver.receiveResult(functionalDependency);
		}
	}

	@Override
	public void receiveResult(InclusionDependency inclusionDependency)
			throws CouldNotReceiveResultException {
		for (OmniscientResultReceiver resultReceiver : subscriber) {
			resultReceiver.receiveResult(inclusionDependency);
		}
		
	}

	@Override
	public void receiveResult(UniqueColumnCombination uniqueColumnCombination)
			throws CouldNotReceiveResultException {
		for (OmniscientResultReceiver resultReceiver : subscriber) {
			resultReceiver.receiveResult(uniqueColumnCombination);
		}		
	}
}
