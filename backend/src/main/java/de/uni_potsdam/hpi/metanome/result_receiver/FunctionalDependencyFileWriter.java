package de.uni_potsdam.hpi.metanome.result_receiver;

import java.io.IOException;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.FunctionalDependencyResultReceiver;

public class FunctionalDependencyFileWriter extends ResultFileWriter implements
		FunctionalDependencyResultReceiver {

	public FunctionalDependencyFileWriter(String fileName, String subdirectoryName) throws IOException {
		super(fileName, subdirectoryName);
	}

	@Override
	public void receiveResult(ColumnCombination determinant,
			ColumnIdentifier dependent) throws CouldNotReceiveResultException {
		try {
			appendToResultFile(determinant.toString() + " -> " + dependent.toString());
		} catch (IOException e) {
			throw new CouldNotReceiveResultException("Could not write to result to file.");
		}
	}

}
