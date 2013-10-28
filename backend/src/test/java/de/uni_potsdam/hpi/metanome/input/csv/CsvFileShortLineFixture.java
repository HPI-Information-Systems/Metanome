package de.uni_potsdam.hpi.metanome.input.csv;

import java.io.StringReader;

import au.com.bytecode.opencsv.CSVReader;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.InputIterationException;

public class CsvFileShortLineFixture {

	public CsvFile getTestData() throws InputIterationException {
		return new CsvFile(new CSVReader(new StringReader("'one','two','three'\n'four','five'\n'six','seven','eight'"), ',', '\''));
	}
	
}
