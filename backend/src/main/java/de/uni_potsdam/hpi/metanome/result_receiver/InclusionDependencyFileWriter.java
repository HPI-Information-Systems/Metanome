package de.uni_potsdam.hpi.metanome.result_receiver;

import java.io.IOException;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.InclusionDependencyResultReceiver;

/**
 * TODO: docs
 */
public class InclusionDependencyFileWriter extends ResultFileWriter implements InclusionDependencyResultReceiver {

	public InclusionDependencyFileWriter(String fileName) throws IOException {
		super(fileName);
	}

	@Override
	public void receiveResult(ColumnCombination dependent,
			ColumnCombination referenced) throws CouldNotReceiveResultException {
		try {
			appendToResultFile(dependent.toString() + " in " + referenced.toString());
		} catch (IOException e) {
			throw new CouldNotReceiveResultException("Could not write to result to file.");
		}
	}

}
