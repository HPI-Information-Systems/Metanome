package de.uni_potsdam.hpi.metanome.input.csv;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;

import com.google.common.collect.ImmutableList;

import de.uni_potsdam.hpi.metanome.algorithm_integration.input.InputIterationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.RelationalInput;

/**
 * {@link CsvFile}s are Iterators over lines in a csv file.
 */
public class CsvFile implements RelationalInput, Closeable {
	
	protected static final boolean DEFAULT_HAS_HEADER = false;
	
	protected CSVReader csvReader;
	protected ImmutableList<String> headerLine;
	protected ImmutableList<String> nextLine;
	protected int numberOfColumns;
	
	public CsvFile(Reader reader, char separator, char quotechar) throws InputIterationException {
		this(reader, separator, quotechar, CSVReader.DEFAULT_SKIP_LINES);
	}
	
	public CsvFile(Reader reader, char separator, char quoteChar, int skipLines) throws InputIterationException {
		this(reader, separator, quoteChar, skipLines, DEFAULT_HAS_HEADER);
	}
	
	public CsvFile(Reader reader, char separator, char quotechar, int skipLines, boolean hasHeader) throws InputIterationException {
		this(reader, separator, quotechar, CSVParser.DEFAULT_ESCAPE_CHARACTER, skipLines, CSVParser.DEFAULT_STRICT_QUOTES, CSVParser.DEFAULT_IGNORE_LEADING_WHITESPACE, hasHeader);
	}

	public CsvFile(Reader reader, char separator, char quotechar, char escape, int skipLines, boolean strictQuotes, boolean ignoreLeadingWhiteSpace, boolean hasHeader) throws InputIterationException {
		this.csvReader = new CSVReader(reader, separator, quotechar, escape, skipLines, strictQuotes, ignoreLeadingWhiteSpace);
		if (hasHeader) {
			this.headerLine = readNextLine();
		}
		this.nextLine = readNextLine();
		this.numberOfColumns = this.nextLine.size();
	}

	@Override
	public boolean hasNext() {
		return !(this.nextLine == null);
	}

	@Override
	public ImmutableList<String> next() throws InputIterationException {
		ImmutableList<String> currentLine = this.nextLine;
		this.nextLine = readNextLine();
		if ((hasNext()) && (this.nextLine.size() != currentLine.size())) {
			throw new InputIterationException("Csv line length did not match.");
		}
		return currentLine;
	}
	
	protected ImmutableList<String> readNextLine() throws InputIterationException {
		String[] lineArray;
		try {
			lineArray = this.csvReader.readNext();
		} catch (IOException e) {
			throw new InputIterationException("Could not read next line in csv file.");
		}
		if (lineArray == null) {
			return null;
		}
		else {
			return ImmutableList.copyOf(lineArray);
		}
	}

	@Override
	public void close() throws IOException {
		csvReader.close();
	}
	
	@Override
	public int numberOfColumns() {
		return numberOfColumns;
	}
	
	@Override
	public String relationName() {
		return null;
	}
	
	@Override
	public ImmutableList<String> columnNames() {		
		return headerLine;
	}
}