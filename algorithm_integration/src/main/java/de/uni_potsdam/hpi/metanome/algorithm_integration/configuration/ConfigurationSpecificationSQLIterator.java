package de.uni_potsdam.hpi.metanome.algorithm_integration.configuration;

/**
 * Concrete {@link ConfigurationSpecification} sql iterator.
 * 
 * @see ConfigurationSpecification
 * @author Jakob Zwiener
 */
public class ConfigurationSpecificationSQLIterator extends ConfigurationSpecification {

	
	/**
	 * Construct a {@link ConfigurationSpecificationSQLIterator}, requesting 1 value.
	 * 
	 * @param identifier
	 */
	public ConfigurationSpecificationSQLIterator(String identifier) {
		super(identifier);
	}

	/**
	 * Construcats a {@link ConfigurationSpecificationSQLIterator}, potentially requesting several values.
	 * 
	 * @param identifier
	 * @param numberOfValues
	 */
	public ConfigurationSpecificationSQLIterator(String identifier,
			int numberOfValues) {
		
		super(identifier, numberOfValues);
	}
}
