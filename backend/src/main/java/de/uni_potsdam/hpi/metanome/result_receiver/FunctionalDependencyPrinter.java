package de.uni_potsdam.hpi.metanome.result_receiver;

import java.io.FileNotFoundException;
import java.io.OutputStream;

import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.FunctionalDependencyResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.FunctionalDependency;

public class FunctionalDependencyPrinter extends ResultPrinter implements FunctionalDependencyResultReceiver {

	protected static final String FD_SEPARATOR = " --> ";
	
	public FunctionalDependencyPrinter(OutputStream outStream) {
		super(outStream);
	}

	public FunctionalDependencyPrinter(String fileName, String subdirectoryName) throws FileNotFoundException {
		super(fileName, subdirectoryName);
	}
	
	@Override
	public void receiveResult(FunctionalDependency functionalDependency) throws CouldNotReceiveResultException {
		outStream.println(functionalDependency);
		addResult(functionalDependency.toString());
	}
	
}
