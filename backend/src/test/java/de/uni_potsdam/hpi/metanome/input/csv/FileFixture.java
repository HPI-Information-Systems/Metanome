package de.uni_potsdam.hpi.metanome.input.csv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class FileFixture {

	protected String fileData;
	
	public FileFixture(String fileData) {
		this.fileData = fileData;
	}
	
	public File getTestData(String fileName) throws FileNotFoundException, UnsupportedEncodingException {
		String filePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		filePath += fileName;
		File file = new File(URLDecoder.decode(filePath, "utf-8"));
		// Mark files for deletion once vm exits.
		file.deleteOnExit();
		
		PrintWriter writer = new PrintWriter(file);
		
		writer.print(fileData);
		writer.close();
		
		return file;
	}
}
