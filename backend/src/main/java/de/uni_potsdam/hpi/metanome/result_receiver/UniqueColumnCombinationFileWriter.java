package de.uni_potsdam.hpi.metanome.result_receiver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.UniqueColumnCombinationResultReceiver;

/**
 * TODO: docs
 */
public class UniqueColumnCombinationFileWriter implements UniqueColumnCombinationResultReceiver {

	protected File file;
	
	public UniqueColumnCombinationFileWriter(String fileName) {
		this.file = new File(fileName);
	}

	@Override
	public void receiveResult(ColumnCombination columnCombination) {
		try {
			FileWriter writer = new FileWriter(this.file, true);
			writer.write(columnCombination.toString() + "\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
