package de.uni_potsdam.hpi.metanome.result_receiver;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.UniqueColumnCombinationResultReceiver;

/**
 * TODO: docs
 */
public class UniqueColumnCombinationPrinter implements UniqueColumnCombinationResultReceiver {

	@Override
	public void receiveResult(ColumnCombination columnCombination) {
		System.out.println(columnCombination);
	}

}
