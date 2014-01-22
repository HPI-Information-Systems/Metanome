package de.uni_potsdam.hpi.metanome.algorithm_integration.configuration;

/**
 * Concrete {@link ConfigurationSpecification} for strings.
 * 
 * @see ConfigurationSpecification
 * @author Jakob Zwiener
 */
public class ConfigurationSpecificationString extends ConfigurationSpecification {

	/**
	 * Construct a ConfigurationSepcificationString, requesting 1 value.
	 * 
	 * @param identifier
	 */
	public ConfigurationSpecificationString(String identifier) {
		super(identifier);
	}

	/**
	 * Constructs a {@link ConfigurationSpecificationString}, potentially requesting several values.
	 * 
	 * @param identifier
	 * @param numberOfValues
	 */
	public ConfigurationSpecificationString(String identifier,
			int numberOfValues) {
		
		super(identifier, numberOfValues);
	}

}
