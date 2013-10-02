package de.uni_potsdam.hpi.metanome.result_receiver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ResultFileWriter {

	protected File file;

	public ResultFileWriter(String fileName) {
		super();
		this.file = new File(fileName);
	}
	
	protected void appendToResultFile(String string) {
		try {
			FileWriter writer = new FileWriter(this.file, true);
			writer.write(string + "\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}