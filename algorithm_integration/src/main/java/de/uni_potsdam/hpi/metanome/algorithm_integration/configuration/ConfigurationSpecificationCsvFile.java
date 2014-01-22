package de.uni_potsdam.hpi.metanome.algorithm_integration.configuration;

/**
 * Concrete {@link ConfigurationSpecification} for csv files.
 * 
 * @see ConfigurationSpecification
 * @author Jakob Zwiener
 */
public class ConfigurationSpecificationCsvFile extends ConfigurationSpecification {
	
	/**
	 * Constructs a {@link ConfigurationSpecificationCsvFile}, requesting 1 value.
	 * 
	 * @param identifier
	 */
	public ConfigurationSpecificationCsvFile(String identifier) {
		super(identifier);
	}

	/**
	 * Constructs a {@link ConfigurationSpecificationCsvFile}, potentially requesting several values.
	 * 
	 * @param identifier
	 * @param numberOfValues
	 */
	public ConfigurationSpecificationCsvFile(String identifier,
			int numberOfValues) {
		
		super(identifier, numberOfValues);
	}
}
