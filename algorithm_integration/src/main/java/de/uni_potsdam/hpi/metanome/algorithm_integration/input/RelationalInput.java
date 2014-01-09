package de.uni_potsdam.hpi.metanome.algorithm_integration.input;

import com.google.common.collect.ImmutableList;

/**
 * Relational inputs can be iterated, but iterators may throw {@link InputIterationException}s when iterating.
 * 
 * @author Jakob Zwiener
 */
public interface RelationalInput {
	
	/**
	 * If the {@link RelationalInput} has another row this method returns true.
	 * 
	 * @return the {@link RelationalInput} has another row
	 * @throws InputIterationException
	 */
	boolean hasNext() throws InputIterationException;
	
	/**
	 * Retrieves the next row.
	 * 
	 * @return the next row
	 * @throws InputIterationException
	 */
	ImmutableList<String> next() throws InputIterationException;
	
	/**
	 * Returns the number of columns.
	 * 
	 * @return the number of columns.
	 */
	int numberOfColumns();
	
	/**
	 * Returns the relation's name
	 * 
	 * @return the relation's name
	 */
	String relationName();
	
	/**
	 * Returns the column names. 
	 * 
	 * @return the column names.
	 */
	ImmutableList<String> columnNames();
}
