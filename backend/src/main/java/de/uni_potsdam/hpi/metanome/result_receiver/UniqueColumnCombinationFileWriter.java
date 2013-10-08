package de.uni_potsdam.hpi.metanome.result_receiver;

import java.io.IOException;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.UniqueColumnCombinationResultReceiver;

/**
 * TODO: docs
 */
public class UniqueColumnCombinationFileWriter extends ResultFileWriter implements UniqueColumnCombinationResultReceiver {

	public UniqueColumnCombinationFileWriter(String fileName, String subdirectoryName) throws IOException {
		super(fileName, subdirectoryName);
	}

	@Override
	public void receiveResult(ColumnCombination columnCombination) throws CouldNotReceiveResultException {
		try {
			appendToResultFile(columnCombination.toString());
		} catch (IOException e) {
			throw new CouldNotReceiveResultException("Could not write to result to file.");
		}
	}

}
