package de.uni_potsdam.hpi.metanome.result_receiver;

import java.io.FileNotFoundException;
import java.io.OutputStream;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.InclusionDependencyResultReceiver;

public class InclusionDependencyPrinter extends ResultPrinter implements InclusionDependencyResultReceiver {

	protected static final String IND_SEPARATOR = " - ";
	
	public InclusionDependencyPrinter(OutputStream outStream) {
		super(outStream);
	}

	public InclusionDependencyPrinter(String fileName, String subdirectoryName) throws FileNotFoundException {
		super(fileName, subdirectoryName);
	}
	
	@Override
	public void receiveResult(ColumnCombination dependent, ColumnCombination referenced) throws CouldNotReceiveResultException {
		outStream.println(dependent + IND_SEPARATOR + referenced);
		addResult(dependent + IND_SEPARATOR + referenced);
	}
}
