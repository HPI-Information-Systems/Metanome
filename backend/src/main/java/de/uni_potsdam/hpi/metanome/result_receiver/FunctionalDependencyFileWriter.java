package de.uni_potsdam.hpi.metanome.result_receiver;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.FunctionalDependencyResultReceiver;

public class FunctionalDependencyFileWriter extends ResultFileWriter implements
		FunctionalDependencyResultReceiver {

	public FunctionalDependencyFileWriter(String fileName) {
		super(fileName);
	}

	@Override
	public void receiveResult(ColumnCombination determinant,
			ColumnIdentifier dependent) {
		appendToResultFile(determinant.toString() + " -> " + dependent.toString());
	}

}
