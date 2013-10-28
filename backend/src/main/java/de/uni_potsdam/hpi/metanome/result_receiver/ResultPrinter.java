package de.uni_potsdam.hpi.metanome.result_receiver;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class ResultPrinter implements Closeable{

	protected PrintStream outStream;
	
	public ResultPrinter(OutputStream outStream) {
		this.outStream = new PrintStream(outStream);
	}
	
	public ResultPrinter(String fileName, String subdirectoryName) throws FileNotFoundException {
		File directory = new File(subdirectoryName);
		if (!directory.exists())
			directory.mkdirs();
		
		this.outStream = new PrintStream(new FileOutputStream(new File(subdirectoryName + "/" + fileName), true));
	}
	
	@Override
	public void close() throws IOException {
		outStream.close();		
	}
	
}
