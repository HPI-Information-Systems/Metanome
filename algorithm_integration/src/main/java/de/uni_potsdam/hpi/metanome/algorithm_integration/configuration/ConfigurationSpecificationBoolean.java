package de.uni_potsdam.hpi.metanome.algorithm_integration.configuration;

/**
 * Concrete {@link ConfigurationSpecification} for booleans.
 * 
 * @see ConfigurationSpecification
 * @author Jakob Zwiener
 */
public class ConfigurationSpecificationBoolean extends ConfigurationSpecification {

	/**
	 * Construct a {@link ConfigurationSpecificationBoolean}, requesting 1 value.
	 * 
	 * @param identifier
	 */
	public ConfigurationSpecificationBoolean(String identifier) {
		super(identifier);
	}

	/**
	 * Constructs a {@link ConfigurationSpecificationBoolean}, potentially requesting several values.
	 * 
	 * @param identifier
	 * @param numberOfValues
	 */
	public ConfigurationSpecificationBoolean(String identifier,
			int numberOfValues) {
		
		super(identifier, numberOfValues);
	}
}
