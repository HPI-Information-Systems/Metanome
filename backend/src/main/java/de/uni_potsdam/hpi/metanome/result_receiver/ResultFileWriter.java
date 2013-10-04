package de.uni_potsdam.hpi.metanome.result_receiver;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ResultFileWriter {

	protected BufferedWriter fileWriter;

	public ResultFileWriter(String fileName) throws IOException {
		this.fileWriter = new BufferedWriter(new FileWriter(fileName, true));
	}	
	
	
	protected void appendToResultFile(String string) throws IOException {
		fileWriter.write(string);
		fileWriter.newLine();
		fileWriter.flush();
	}

}