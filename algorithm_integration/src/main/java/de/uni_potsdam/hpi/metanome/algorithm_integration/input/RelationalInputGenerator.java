package de.uni_potsdam.hpi.metanome.algorithm_integration.input;

/**
 * Generates new copies of a {@link RelationalInput}.
 * 
 * @author Jakob Zwiener
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
