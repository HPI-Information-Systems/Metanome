package de.uni_potsdam.hpi.metanome.result_receiver;

import java.io.FileNotFoundException;
import java.io.OutputStream;

import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.UniqueColumnCombinationResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.UniqueColumnCombination;

/**
 * TODO: docs
 */
public class UniqueColumnCombinationPrinter extends ResultPrinter implements UniqueColumnCombinationResultReceiver {

	public UniqueColumnCombinationPrinter(OutputStream outStream) {
		super(outStream);
	}

	public UniqueColumnCombinationPrinter(String fileName, String subdirectoryName) throws FileNotFoundException {
		super(fileName, subdirectoryName);
	}

	@Override
	public void receiveResult(UniqueColumnCombination uniqueColumnCombination) {
		this.outStream.println(uniqueColumnCombination);
		addResult(uniqueColumnCombination.toString());
	}
}
