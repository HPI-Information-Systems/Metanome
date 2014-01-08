package de.uni_potsdam.hpi.metanome.result_receiver;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.BasicStatistic;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.FunctionalDependency;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.InclusionDependency;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.UniqueColumnCombination;

public class ResultsHub implements CloseableOmniscientResultReceiver {
	
	protected Set<CloseableOmniscientResultReceiver> subscriber = new HashSet<CloseableOmniscientResultReceiver>();

	/**
	 * Adds an {@link CloseableOmniscientResultReceiver} to the set of subscribers.
	 * 
	 * @param resultReceiver
	 */
	public void addSubscriber(CloseableOmniscientResultReceiver resultReceiver) {
		subscriber.add(resultReceiver);	
	}

	/**
	 * @return subscriber
	 */
	public Set<CloseableOmniscientResultReceiver> getSubscriber() {
		return subscriber;
	}

	/**
	 * Removes an {@link CloseableOmniscientResultReceiver} from the subscriber set.
	 * 
	 * @param resultReceiver
	 */
	public void removeSubscriber(CloseableOmniscientResultReceiver resultReceiver) {
		subscriber.remove(resultReceiver);
	}

	@Override
	public void receiveResult(BasicStatistic statistic) 
			throws CouldNotReceiveResultException {
		for (CloseableOmniscientResultReceiver resultReceiver : subscriber) {
			resultReceiver.receiveResult(statistic);
		}
	}

	@Override
	public void receiveResult(FunctionalDependency functionalDependency)
			throws CouldNotReceiveResultException {
		for (CloseableOmniscientResultReceiver resultReceiver : subscriber) {
			resultReceiver.receiveResult(functionalDependency);
		}
	}

	@Override
	public void receiveResult(InclusionDependency inclusionDependency)
			throws CouldNotReceiveResultException {
		for (CloseableOmniscientResultReceiver resultReceiver : subscriber) {
			resultReceiver.receiveResult(inclusionDependency);
		}
		
	}

	@Override
	public void receiveResult(UniqueColumnCombination uniqueColumnCombination)
			throws CouldNotReceiveResultException {
		for (CloseableOmniscientResultReceiver resultReceiver : subscriber) {
			resultReceiver.receiveResult(uniqueColumnCombination);
		}		
	}

	@Override
	public void close() throws IOException {
		for (CloseableOmniscientResultReceiver resultReceiver : subscriber) {
			resultReceiver.close();
		}
	}

}
