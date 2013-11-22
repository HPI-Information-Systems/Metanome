package de.uni_potsdam.hpi.metanome.input.csv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.InputGenerationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.InputIterationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.RelationalInput;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.RelationalInputGenerator;

public class CsvFileGenerator implements RelationalInputGenerator {

	protected File inputFile;
	protected char separator = CSVParser.DEFAULT_SEPARATOR;
	protected char quotechar = CSVParser.DEFAULT_QUOTE_CHARACTER;
	protected char escape = CSVParser.DEFAULT_ESCAPE_CHARACTER;
	protected int skipLines = CSVReader.DEFAULT_SKIP_LINES;
	protected boolean strictQuotes = CSVParser.DEFAULT_STRICT_QUOTES;
	protected boolean ignoreLeadingWhiteSpace = CSVParser.DEFAULT_IGNORE_LEADING_WHITESPACE;
	protected boolean hasHeader = CsvFile.DEFAULT_HAS_HEADER;
	
	public CsvFileGenerator(File inputFile) throws FileNotFoundException {
		this.setInputFile(inputFile);
	}
	
	public CsvFileGenerator(File inputFile, char separator, char quotechar, char escape, int skipLines, 
			boolean strictQuotes, boolean ignoreLeadingWhiteSpace) throws FileNotFoundException {
		this.setInputFile(inputFile);
		this.separator = separator;
		this.quotechar = quotechar;
		this.escape = escape;
		this.skipLines = skipLines;
		this.strictQuotes = strictQuotes;
		this.ignoreLeadingWhiteSpace = ignoreLeadingWhiteSpace;		
	}
	
	private void setInputFile(File inputFile) throws FileNotFoundException{
		if (!inputFile.isFile()) {
			throw new FileNotFoundException();
		}
		this.inputFile = inputFile;
	}

	public RelationalInput generateNewCopy() throws InputGenerationException {
		try {
			return new CsvFile(inputFile.getName(), new FileReader(inputFile), separator, quotechar, escape, skipLines, strictQuotes, ignoreLeadingWhiteSpace, hasHeader);
		} catch (FileNotFoundException e) {
			throw new InputGenerationException("File not found.");
		} catch (InputIterationException e) {
			throw new InputGenerationException("Could not iterate over the first line of the csv file.");
		} 	
	}
}
