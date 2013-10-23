package de.uni_potsdam.hpi.metanome.input.csv;

import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;

import com.google.common.collect.ImmutableList;

import de.uni_potsdam.hpi.metanome.algorithm_integration.input.SimpleRelationalInput;

/**
 * {@link CsvFile}s are Iterators over lines in a csv file.
 */
class CsvFile implements SimpleRelationalInput {
	
	protected CSVReader csvReader;
	protected ImmutableList<String> nextLine;
	
	public CsvFile(CSVReader csvReader) throws IOException {
		this.csvReader = csvReader;
		this.nextLine = readNextLine();
	}

	@Override
	public boolean hasNext() throws IOException {
		if (this.nextLine == null) {
			this.csvReader.close();
			return false;
		}
		else {
			return true;
		}
	}

	@Override
	public ImmutableList<String> next() throws IOException {
		ImmutableList<String> currentLine = this.nextLine;
		this.nextLine = readNextLine();
		return currentLine;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	protected ImmutableList<String> readNextLine() throws IOException {
		String[] lineArray = this.csvReader.readNext();
		if (lineArray == null) {
			return null;
		}
		else {
			return ImmutableList.copyOf(lineArray);
		}
	}
}