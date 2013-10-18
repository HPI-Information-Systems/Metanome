package de.uni_potsdam.hpi.metanome.algorithm_integration.input;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;

public class CsvFileFixture {

	protected static final char QUOTE_CHAR = '"';
	protected static final char SEPARATOR = ',';
	
	protected FileFixture fileFixture;
	
	public CsvFileFixture() {
		this.fileFixture = new FileFixture(getCsvFileData());
	}
	
	public String getTestDataPath(String fileName) throws FileNotFoundException, UnsupportedEncodingException {
		return fileFixture.getTestDataPath(fileName);
	}
	
	protected String getCsvFileData() {
		StringBuilder fileDataBuilder = new StringBuilder();
		List<String> quotedStrings = new LinkedList<String>();
		
		for (String unquotedString : expectedFirstLine()) {
			quotedStrings.add(QUOTE_CHAR + unquotedString + QUOTE_CHAR);
		}
		
		fileDataBuilder.append(Joiner.on(SEPARATOR).join(quotedStrings));
		fileDataBuilder.append(System.getProperty("line.separator"));
		
		quotedStrings.clear();
		for (String unquotedString : expectedSecondLine()) {
			quotedStrings.add(QUOTE_CHAR + unquotedString + QUOTE_CHAR);
		}
		
		fileDataBuilder.append(Joiner.on(SEPARATOR).join(quotedStrings));
		
		return fileDataBuilder.toString();
	}
	
	public ImmutableList<String> expectedFirstLine() {
		return ImmutableList.of("3","4","5");
	}
	
	public ImmutableList<String> expectedSecondLine() {
		return ImmutableList.of("6","7","8");
	}
	
}
