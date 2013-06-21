package de.uni_potsdam.hpi.metanome.algorithmIntegration;

/**
 * Represents a configuration parameter an {@link Algorithm} needs to be properly configured.
 * The ConfigurationSpecification is then used to construct a {@link ConfigurationValue} that
 * is sent to the {@link Algorithm} for configuration.
 * Only type concrete ConfigurationSpecification subclasses should be used to specify
 * configuration parameters.
 */
public abstract class ConfigurationSpecification {
	
	protected final String identifier;
	
	/**
	 * Construct a configuration specification.
	 * An string identifier is stored to identify configuration parameter.
	 * The identifier should be unique among all parameters of one algorithm.
	 * 
	 * @param identifier
	 */
	public ConfigurationSpecification(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * @return the identifier
	 */
	public String getIdentifier() {
		return identifier;
	}
	
}
