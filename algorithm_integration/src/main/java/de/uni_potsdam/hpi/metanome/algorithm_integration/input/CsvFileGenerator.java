package de.uni_potsdam.hpi.metanome.algorithm_integration.input;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;

public class CsvFileGenerator {

	protected File inputFile;
	protected char separator = CSVParser.DEFAULT_SEPARATOR;
	protected char quotechar = CSVParser.DEFAULT_QUOTE_CHARACTER;
	protected char escape = CSVParser.DEFAULT_ESCAPE_CHARACTER;
	protected int line = CSVParser.INITIAL_READ_SIZE;
	protected boolean strictQuotes = CSVParser.DEFAULT_STRICT_QUOTES;
	protected boolean ignoreLeadingWhiteSpace = CSVParser.DEFAULT_IGNORE_LEADING_WHITESPACE;
	
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
		return new CsvFile(new CSVReader(new FileReader(inputFile), separator, quotechar, escape, line, strictQuotes, ignoreLeadingWhiteSpace));	
	}

}
