package de.uni_potsdam.hpi.metanome.result_receiver;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.InclusionDependencyResultReceiver;

/**
 * TODO: docs
 */
public class InclusionDependencyFileWriter extends ResultFileWriter implements InclusionDependencyResultReceiver {

	public InclusionDependencyFileWriter(String fileName) {
		super(fileName);
	}

	@Override
	public void receiveResult(ColumnCombination dependent,
			ColumnCombination referenced) {
		appendToResultFile(dependent.toString() + " in " + referenced.toString());
	}

}
