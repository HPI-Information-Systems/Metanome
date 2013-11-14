package de.uni_potsdam.hpi.metanome.input.csv;

import java.io.StringReader;

import de.uni_potsdam.hpi.metanome.algorithm_integration.input.InputGenerationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.InputIterationException;

public class CsvFileShortLineFixture {

	public CsvFile getTestData() throws InputIterationException, InputGenerationException {
		return new CsvFile(new StringReader("'one','two','three'\n'four','five'\n'six','seven','eight'"), ',', '\'');
	}
	
}
