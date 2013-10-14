package de.uni_potsdam.hpi.metanome.input.csv;

import java.io.IOException;
import java.io.StringReader;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;

public class CsvFileFixtureOneLine {

	public CsvFile getTestData() throws IOException {
		return new CsvFile(new StringReader(getCsvInputString()));
	}
	
	protected String getCsvInputString() {		
		return Joiner.on(",").join(getExpectedStrings());
	}
	
	public ImmutableList<String> getExpectedStrings() {
		return ImmutableList.of("'value1'", "'value2'", "value3");
	}
	
}
