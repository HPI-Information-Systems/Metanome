package de.uni_potsdam.hpi.metanome.algorithm_integration.input;

/**
 * @author Jakob Zwiener
 * 
 * Generates new copies of a {@link RelationalInput}.
 *
 */
public interface RelationalInputGenerator {
	
	/**
	 * Generates a new copy of the relational input that can be iterated from the beginning.
	 * 
	 * @return new copy of the relational input 
	 * @throws InputGenerationException
	 */
	public RelationalInput generateNewCopy() throws InputGenerationException;
}
