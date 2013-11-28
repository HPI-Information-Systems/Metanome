package de.uni_potsdam.hpi.metanome.result_receiver;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

public class ResultPrinter implements Closeable {

	protected PrintStream outStream;
	protected List<String> newResults = new LinkedList<String>();
	
	public ResultPrinter(OutputStream outStream) {
		this.outStream = new PrintStream(outStream);
	}
	
	public ResultPrinter(String fileName, String subdirectoryName) throws FileNotFoundException {
		File directory = new File(subdirectoryName);
		if (!directory.exists())
			directory.mkdirs();
		
		this.outStream = new PrintStream(new FileOutputStream(new File(subdirectoryName + "/" + fileName), true));
	}
	
	protected void addResult(String newResult) {
		newResults.add(newResult);
	}
	
	public List<String> getNewResults() {
		List<String> returnResults = new LinkedList<String>();
		returnResults.addAll(newResults);
		newResults.clear();
		outStream.flush();
		return returnResults;
	}
	
	@Override
	public void close() throws IOException {
		outStream.close();		
	}
	
}
