package de.uni_potsdam.hpi.metanome.result_receiver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.uni_potsdam.hpi.metanome.algorithm_integration.results.BasicStatistic;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.FunctionalDependency;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.InclusionDependency;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.Result;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.UniqueColumnCombination;

/**
 * Stores all received Results in a list and returns the new results on call to {@link ResultsCache#getNewResults()}.
 * 
 * @author Jakob Zwiener
 */
public class ResultsCache implements CloseableOmniscientResultReceiver {

	protected List<Result> results = new LinkedList<Result>();
	
	@Override
	public void receiveResult(BasicStatistic statistic) {
		results.add(statistic);
	}

	@Override
	public void receiveResult(FunctionalDependency functionalDependency) {
		results.add(functionalDependency);
	}

	@Override
	public void receiveResult(InclusionDependency inclusionDependency) {
		results.add(inclusionDependency);
	}

	@Override
	public void receiveResult(UniqueColumnCombination uniqueColumnCombination) {
		results.add(uniqueColumnCombination);
	}

	/**
	 * Should return all results once. After receiving the list it should be cleared and only filled by new results.
	 * 
	 * @return new results
	 */
	public ArrayList<Result> getNewResults() {
		ArrayList<Result> currentResults = new ArrayList<Result>(results);
		results.clear();
		return currentResults;
	}

	@Override
	public void close() throws IOException {
		
	}
	
}
