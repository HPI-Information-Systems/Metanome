package de.uni_potsdam.hpi.metanome.algorithm_integration.input;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;

public class CsvFileGenerator {

	protected File inputFile;
	protected char separator;
	protected char quotechar;
	protected char escape;
	protected int line;
	protected boolean strictQuotes;
	protected boolean ignoreLeadingWhiteSpace;
	
	public CsvFileGenerator(File inputFile) {
		this.inputFile = inputFile;
	}
	
	public CsvFileGenerator(File inputFile, char separator, char quotechar, char escape, int line, boolean strictQuotes, boolean ignoreLeadingWhiteSpace) {
		this.inputFile = inputFile;
		this.separator = separator;
		this.quotechar = quotechar;
		this.escape = escape;
		this.line = line;
		this.strictQuotes = strictQuotes;
		this.ignoreLeadingWhiteSpace = ignoreLeadingWhiteSpace;
		
	}

	public SimpleRelationalInput generateNewCsvFile() throws FileNotFoundException, IOException {
		// TODO: call more sofisticated constructor.
		return new CsvFile(new CSVReader(new FileReader(inputFile)));	
	}

}
