package de.uni_potsdam.hpi.metanome.result_receiver;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.UniqueColumnCombinationResultReceiver;

/**
 * TODO: docs
 */
public class UniqueColumnCombinationFileWriter extends ResultFileWriter implements UniqueColumnCombinationResultReceiver {

	public UniqueColumnCombinationFileWriter(String fileName) {
		super(fileName);
	}

	@Override
	public void receiveResult(ColumnCombination columnCombination) {
		appendToResultFile(columnCombination.toString());
	}

}
