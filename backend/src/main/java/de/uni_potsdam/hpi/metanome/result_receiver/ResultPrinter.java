package de.uni_potsdam.hpi.metanome.result_receiver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.BasicStatistic;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.FunctionalDependency;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.InclusionDependency;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.UniqueColumnCombination;

public class ResultPrinter implements OmniscientResultReceiver {

	protected PrintStream statStream;
	protected PrintStream fdStream;
	protected PrintStream uccStream;
	protected PrintStream indStream;
	
	protected String algorithmExecutionIdentifier;
	protected String directoryName;
	
	public ResultPrinter(String algorithmExecutionIdentifier, String directoryName) throws FileNotFoundException {
		File directory = new File(directoryName);
		if (!directory.exists())
			directory.mkdirs();
		
		this.algorithmExecutionIdentifier = algorithmExecutionIdentifier;
		this.directoryName = directoryName;
	}

	@Override
	public void receiveResult(BasicStatistic statistic)
			throws CouldNotReceiveResultException {
		getStatStream().println(statistic.toString());		
	}

	@Override
	public void receiveResult(FunctionalDependency functionalDependency)
			throws CouldNotReceiveResultException {
		getFdStream().println(functionalDependency.toString());		
	}

	@Override
	public void receiveResult(InclusionDependency inclusionDependency)
			throws CouldNotReceiveResultException {
		getIndStream().println(inclusionDependency.toString());		
	}

	@Override
	public void receiveResult(UniqueColumnCombination uniqueColumnCombination)
			throws CouldNotReceiveResultException {
		getUccStream().println(uniqueColumnCombination.toString());		
	}

	protected PrintStream getStatStream() throws CouldNotReceiveResultException {
		if (statStream == null) {
			statStream = openStream("_stats");
		}
		
		return statStream;
	}
	
	protected PrintStream getFdStream() throws CouldNotReceiveResultException {
		if (fdStream == null) {
			fdStream = openStream("_fds");
		}
		
		return fdStream;
	}
	
	protected PrintStream getIndStream() throws CouldNotReceiveResultException {
		if (indStream == null) {
			indStream = openStream("_inds");
		}
		
		return indStream;
	}
	
	protected PrintStream getUccStream() throws CouldNotReceiveResultException {
		if (uccStream == null) {
			uccStream = openStream("_uccs");
		}
		
		return uccStream;		
	}

	protected PrintStream openStream(String fileSuffix) throws CouldNotReceiveResultException {
		try {
			return new PrintStream(new FileOutputStream(getOutputFilePathPrefix() + fileSuffix), true);
		} catch (FileNotFoundException e) {
			throw new CouldNotReceiveResultException("Could not open result file for writing.");
		}
	}
	
	protected String getOutputFilePathPrefix() {
		return directoryName + "/" + algorithmExecutionIdentifier;
	}
	
	@Override
	public void close() throws IOException {
		if (statStream != null) {
			statStream.close();
		}
		if (fdStream != null) {
			fdStream.close();
		}
		if (indStream != null) {
			indStream.close();
		}
		if (uccStream != null) {
			uccStream.close();
		}
	}
	
}
