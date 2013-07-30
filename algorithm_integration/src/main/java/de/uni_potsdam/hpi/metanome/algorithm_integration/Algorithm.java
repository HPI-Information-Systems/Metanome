package de.uni_potsdam.hpi.metanome.algorithm_integration;

import java.util.List;

import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationValue;

/**
 * An algorithm should supply the configuration requirements, should initiate double dispatch with incoming
 * configurations and receive the different configuration types.
 * Subclassing of this class will not lead to a functioning algorithm, as only more concrete algorithms can be started.
 */
public abstract class Algorithm {
	
	/**
	 * Algorithms should supply a list of needed configuration parameters.
	 * 
	 * @return a list of ConfigurationSpecifications
	 */
	abstract public List<ConfigurationSpecification> getConfigurationRequirements();
	
	/**
	 * When receiving a list of {@link ConfigurationValue} the setting of these values should be initiated.
	 * Double dispatch is used to determine the type and have the appropriate setConfigurationValue method 
	 * called.
	 * 
	 * @param configurationValues
	 */
	public void configure(List<ConfigurationValue> configurationValues) {
		for (ConfigurationValue configurationValue : configurationValues) {
			configurationValue.triggerSetValue(this);
		}
	}
	
	/**
	 * Sets a string configuration value on the algorithm.
	 * 
	 * @param identifier
	 * @param value
	 */
	abstract public void setConfigurationValue(String identifier, String value);	
}
