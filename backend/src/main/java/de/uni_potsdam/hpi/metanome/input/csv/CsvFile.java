package de.uni_potsdam.hpi.metanome.input.csv;

import java.io.Closeable;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;

import com.google.common.collect.ImmutableList;

import de.uni_potsdam.hpi.metanome.algorithm_integration.input.InputIterationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.RelationalInput;

/**
 * {@link CsvFile}s are Iterators over lines in a csv file.
 */
public class CsvFile implements RelationalInput, Closeable {
	
	protected CSVReader csvReader;
	protected ImmutableList<String> nextLine;
	protected int numberOfColumns;
	
	public CsvFile(CSVReader csvReader) throws InputIterationException {
		this.csvReader = csvReader;
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

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
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
}