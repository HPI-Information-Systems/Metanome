package de.uni_potsdam.hpi.metanome.algorithm_integration.input;

import com.google.common.collect.ImmutableList;

/**
 * @author Jakob Zwiener
 * 
 * Relational inputs can be iterated, but different than iterators may throw {@link InputIterationException}s when iterating
 *
 */
public interface RelationalInput {
	
	/**
	 * TODO docs
	 * 
	 * @return
	 * @throws InputIterationException
	 */
	boolean hasNext() throws InputIterationException;
	
	/**
	 * TODO docs
	 * 
	 * @return
	 * @throws InputIterationException
	 */
	ImmutableList<String> next() throws InputIterationException;
	
	/**
	 * TODO docs
	 */
	void remove();
	
	/**
	 * TODO docs
	 * 
	 * @return
	 */
	int numberOfColumns();
}
