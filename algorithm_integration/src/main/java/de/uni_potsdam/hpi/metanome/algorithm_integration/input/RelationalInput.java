package de.uni_potsdam.hpi.metanome.algorithm_integration.input;

import com.google.common.collect.ImmutableList;

/**
 * @author Jakob Zwiener
 * 
 * Relational inputs can be iterated, but different than iterators may throw {@link InputIterationException}s when iterating
 *
 */
public interface RelationalInput {
	
	boolean hasNext() throws InputIterationException;
	
	ImmutableList<String> next() throws InputIterationException;
	
	void remove();
}
