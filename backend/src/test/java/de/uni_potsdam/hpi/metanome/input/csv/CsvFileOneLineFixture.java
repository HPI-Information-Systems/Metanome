package de.uni_potsdam.hpi.metanome.input.csv;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import au.com.bytecode.opencsv.CSVParser;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;

import de.uni_potsdam.hpi.metanome.algorithm_integration.input.InputGenerationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.InputIterationException;

public class CsvFileOneLineFixture {

	protected char separator;
	protected char quoteChar;

	public CsvFileOneLineFixture() {
		this(CSVParser.DEFAULT_SEPARATOR);
	}
	
	public CsvFileOneLineFixture(char separator) {
		this(separator, CSVParser.DEFAULT_QUOTE_CHARACTER);
	}
	
	public CsvFileOneLineFixture(char separator, char quoteChar) {
		this.separator = separator;
		this.quoteChar = quoteChar;
	}

	public CsvFile getTestData() throws InputIterationException, InputGenerationException {
		return new CsvFile(getExpectedRelationName(), new StringReader(getCsvInputString()), this.separator, this.quoteChar, 0, true);
	}

	public CsvFile getTestDataWithoutHeader() throws InputIterationException, InputGenerationException {
		return new CsvFile(getExpectedRelationName(), new StringReader(getCsvInputString()), this.separator, this.quoteChar, 0, false);
	}
	
	protected String getCsvInputString() {
		List<String> quotedHeader = quoteStrings(getExpectedColumnNames());
		List<String> quotedLine = quoteStrings(getExpectedStrings());
		
		StringBuilder csvBuilder = new StringBuilder();
		
		csvBuilder.append(buildLineString(quotedHeader));
		csvBuilder.append("\n");
		csvBuilder.append(buildLineString(quotedLine));
		
		return csvBuilder.toString();
	}

	protected String buildLineString(List<String> line) {
		return Joiner.on(this.separator).join(line);
	}
	
	/**
	 * Puts the input strings into quotes.
	 * 
	 * @param unquotedStrings
	 * @return quoted strings
	 */
	protected List<String> quoteStrings(List<String> unquotedStrings) {
		List<String> quotedStrings = new LinkedList<String>();
		
		for (String unquotedString : unquotedStrings) {
			quotedStrings.add(this.quoteChar + unquotedString + this.quoteChar);
		}
		
		return quotedStrings;
	}
	
	public ImmutableList<String> getExpectedStrings() {
		return ImmutableList.of("value1", "value2", "value3");
	}
	
	public String getExpectedRelationName() {
		return "some_relation";
	}
	
	public ImmutableList<String> getExpectedColumnNames() {
		return ImmutableList.of("column1", "column2", "column3");
	}
	
	public ImmutableList<String> getExpectedDefaultColumnNames() {
		return ImmutableList.of("Column #1", "Column #2", "Column #3");
	}
	
	public int getExpectedNumberOfColumns() {
		return getExpectedStrings().size();
	}
	
}
