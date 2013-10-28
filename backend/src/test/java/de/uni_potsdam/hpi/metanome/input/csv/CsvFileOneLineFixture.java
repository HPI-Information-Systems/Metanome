package de.uni_potsdam.hpi.metanome.input.csv;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;

import de.uni_potsdam.hpi.metanome.input.csv.CsvFile;

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

	public CsvFile getTestData() throws IOException {
		return new CsvFile(new CSVReader(new StringReader(getCsvInputString()), this.separator, this.quoteChar));
	}
	
	protected String getCsvInputString() {
		List<String> quotedStrings = new LinkedList<String>();
		
		for (String unquotedString : getExpectedStrings()) {
			quotedStrings.add(this.quoteChar + unquotedString + this.quoteChar);
		}
		
		return Joiner.on(this.separator).join(quotedStrings);
	}
	
	public ImmutableList<String> getExpectedStrings() {
		return ImmutableList.of("value1", "value2", "value3");
	}
	
}
