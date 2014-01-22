package de.uni_potsdam.hpi.metanome.algorithm_integration.configuration;

import de.uni_potsdam.hpi.metanome.algorithm_integration.Algorithm;

/**
 * Represents a configuration parameter an {@link Algorithm} needs to be properly configured.
 * The ConfigurationSpecification is then used to construct a configuration value that
 * is sent to the {@link Algorithm} for configuration.
 * Only type concrete ConfigurationSpecification subclasses should be used to specify
 * configuration parameters.
 * 
 * @author Jakob Zwiener
 */
public abstract class ConfigurationSpecification {
	
	public static final int ARBITRARY_NUMBER_OF_VALUES = -1;
	
	protected final String identifier;
	protected final int numberOfValues;
	
	/**
	 * Construct a configuration specification.
	 * A string identifier is stored to identify configuration parameter.
	 * The identifier should be unique among all parameters of one algorithm.
	 * The number of requested values defaults to 1.
	 * 
	 * @param identifier
	 */
	public ConfigurationSpecification(String identifier) {
		this(identifier, 1);
	}
	
	/**
	 * Construct a configuration specification.
	 * A string identifier is stored to identify configuration parameter.
	 * The identifier should be unique among all parameters of one algorithm.
	 * The number of requested values is set. Use ARBITRARY_NUMBER_OF_VALUES to request 
	 * arbitrary number of values.
	 * 
	 * @param identifier
	 * @param numberOfValues
	 */
	public ConfigurationSpecification(String identifier, int numberOfValues) {
		this.identifier = identifier; 
		this.numberOfValues = numberOfValues;
	}

	/**
	 * @return identifier
	 */
	public String getIdentifier() {
		return identifier;
	}
	
	/**
	 * @return numberOfValues
	 */
	public int getNumberOfValues() {
		return numberOfValues;
	}
	
}
