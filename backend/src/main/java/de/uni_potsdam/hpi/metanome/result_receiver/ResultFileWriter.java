package de.uni_potsdam.hpi.metanome.result_receiver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ResultFileWriter {

	protected BufferedWriter fileWriter;

	public ResultFileWriter(String fileName, String subdirectoryName) throws IOException {
		File directory = new File(subdirectoryName);
		if (!directory.exists())
			directory.mkdirs();
		this.fileWriter = new BufferedWriter(new FileWriter(
				subdirectoryName + "/" + fileName, true));
	}	
	
	
	protected void appendToResultFile(String string) throws IOException {
		fileWriter.write(string);
		fileWriter.newLine();
		fileWriter.flush();
	}

}