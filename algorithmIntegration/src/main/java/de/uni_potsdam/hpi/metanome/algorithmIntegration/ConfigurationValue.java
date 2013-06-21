package de.uni_potsdam.hpi.metanome.algorithmIntegration;

/**
 * Represents configuration values for {@link Algorithm}s. Sets it's own value on an {@link Algorithm}
 * through double dispatch (second call).
 */
interface ConfigurationValue {
	
	/**
	 * Sets the configuration value on the algorithm. 
	 * The type of the configuration value is resolved in the implementations. 
	 * 
	 * @param algorithm
	 */
	abstract void triggerSetValue(Algorithm algorithm);
	
}
