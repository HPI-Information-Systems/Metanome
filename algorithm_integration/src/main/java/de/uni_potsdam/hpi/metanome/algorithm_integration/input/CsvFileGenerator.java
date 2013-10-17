package de.uni_potsdam.hpi.metanome.algorithm_integration.input;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;

public class CsvFileGenerator {

	protected String inputFilePath;
	
	public CsvFileGenerator(String inputFilePath) {
		this.inputFilePath = inputFilePath;
	}

	public SimpleRelationalInput generateNewCsvFile() throws FileNotFoundException, IOException {
		return new CsvFile(new CSVReader(new FileReader(inputFilePath)));	
	}

}
