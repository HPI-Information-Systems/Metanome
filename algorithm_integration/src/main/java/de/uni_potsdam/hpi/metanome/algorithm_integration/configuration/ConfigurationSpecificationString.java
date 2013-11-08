package de.uni_potsdam.hpi.metanome.algorithm_integration.configuration;

import java.io.Serializable;

/**
 * Concrete {@link ConfigurationSpecification} for strings.
 * 
 * @see ConfigurationSpecification
 */
public class ConfigurationSpecificationString extends ConfigurationSpecification implements Serializable{

	private static final long serialVersionUID = 6081887975370271315L;

	/**
	 * Construct a ConfigurationSepcificationString.
	 * 
	 * @param a unique configuration parameter identifier
	 */
	public ConfigurationSpecificationString(String identifier) {
		super(identifier);
	}

}
