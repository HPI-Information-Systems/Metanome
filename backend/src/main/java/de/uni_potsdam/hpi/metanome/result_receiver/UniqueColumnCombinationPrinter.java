package de.uni_potsdam.hpi.metanome.result_receiver;

import java.io.PrintStream;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.UniqueColumnCombinationResultReceiver;

/**
 * TODO: docs
 */
public class UniqueColumnCombinationPrinter implements UniqueColumnCombinationResultReceiver {

	protected PrintStream outStream;
	
	public UniqueColumnCombinationPrinter(PrintStream outStream) {
		this.outStream = outStream;
	}

	@Override
	public void receiveResult(ColumnCombination columnCombination) {
		this.outStream.println(columnCombination);
	}

}
