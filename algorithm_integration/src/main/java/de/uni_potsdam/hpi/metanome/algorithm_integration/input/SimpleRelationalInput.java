package de.uni_potsdam.hpi.metanome.algorithm_integration.input;

import java.io.IOException;

import com.google.common.collect.ImmutableList;

public interface SimpleRelationalInput{
	
	boolean hasNext() throws IOException;
	
	ImmutableList<String> next() throws IOException;
	
	void remove();
}
