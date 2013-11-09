package de.uni_potsdam.hpi.metanome.algorithm_integration.configuration;

/**
 * Concrete {@link ConfigurationSpecification} for csv files.
 * 
 * @see ConfigurationSpecification
 */
public class ConfigurationSpecificationSQLIterator extends ConfigurationSpecification {

	
	/**
	 * Construct a ConfigurationSepcificationCsvFile.
	 * 
	 * @param a unique configuration parameter identifier
	 */
	public ConfigurationSpecificationSQLIterator(String identifier) {
		super(identifier);
	}
}
