package de.uni_potsdam.hpi.metanome.result_receiver;

import java.io.FileNotFoundException;
import java.io.OutputStream;

import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.InclusionDependencyResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.InclusionDependency;

public class InclusionDependencyPrinter extends ResultPrinter implements InclusionDependencyResultReceiver {
	
	public InclusionDependencyPrinter(OutputStream outStream) {
		super(outStream);
	}

	public InclusionDependencyPrinter(String fileName, String subdirectoryName) throws FileNotFoundException {
		super(fileName, subdirectoryName);
	}
	
	@Override
	public void receiveResult(InclusionDependency inclusionDependency) throws CouldNotReceiveResultException {
		outStream.println(inclusionDependency);
		addResult(inclusionDependency.toString());
	}
}
