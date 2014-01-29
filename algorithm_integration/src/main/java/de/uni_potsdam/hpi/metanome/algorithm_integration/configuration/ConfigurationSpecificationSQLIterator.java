package de.uni_potsdam.hpi.metanome.algorithm_integration.configuration;

/**
 * Concrete {@link ConfigurationSpecification} sql iterator.
 * 
 * @see ConfigurationSpecification
 */
public class ConfigurationSpecificationSQLIterator extends ConfigurationSpecification {

	
	/**
	 * Construct a ConfigurationSpecificationSQLIterator.
	 * 
	 * @param a unique configuration parameter identifier
	 */
	public ConfigurationSpecificationSQLIterator(String identifier) {
		super(identifier);
	}
}
