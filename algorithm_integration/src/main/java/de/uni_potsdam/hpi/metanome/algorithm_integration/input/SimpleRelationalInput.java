package de.uni_potsdam.hpi.metanome.algorithm_integration.input;

import com.google.common.collect.ImmutableList;

// TODO rename
public interface SimpleRelationalInput{
	
	boolean hasNext() throws InputIterationException;
	
	ImmutableList<String> next() throws InputIterationException;
	
	void remove();
}
