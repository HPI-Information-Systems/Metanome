package de.uni_potsdam.hpi.metanome.input.csv;

import java.io.IOException;
import java.io.StringReader;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;

public class CsvFileFixtureOneLine {
	
	protected char separator;
	
	public CsvFileFixtureOneLine() {
		this(',');
	}
	
	public CsvFileFixtureOneLine(char separator) {
		this.separator = separator;
	}

	public CsvFile getTestData() throws IOException {
		return new CsvFile(new StringReader(getCsvInputString()), this.separator);
	}
	
	protected String getCsvInputString() {		
		return Joiner.on(this.separator).join(getExpectedStrings());
	}
	
	public ImmutableList<String> getExpectedStrings() {
		return ImmutableList.of("'value1'", "'value2'", "value3");
	}
	
}
