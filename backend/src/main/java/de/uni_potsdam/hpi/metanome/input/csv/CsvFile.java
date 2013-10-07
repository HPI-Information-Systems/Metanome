package de.uni_potsdam.hpi.metanome.input.csv;

import com.google.common.collect.ImmutableList;

import de.uni_potsdam.hpi.metanome.algorithm_integration.input.SimpleRelationalInput;

/**
 * {@link CsvFile}s are Iterators over lines in a csv file.
 */
public class CsvFile implements SimpleRelationalInput {

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ImmutableList<String> next() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
