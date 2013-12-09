package de.uni_potsdam.hpi.metanome.frontend.client.parameter;

import java.io.Serializable;

import de.uni_potsdam.hpi.metanome.frontend.client.widgets.InputParameterWidget;

/**
 * InputParameters correspond to a ConfigurationSpecification and ConfigurationValue type. 
 * It is used for frontend input of the configuration value, so generally, a ConfigurationSpecification
 * will be converted to an InputParameter, which is used to get the user's value input, and then converted
 * to the ConfigurationValue handed back to the algorithm.
 * 
 * @author Claudia
 *
 */
public abstract class InputParameter implements Serializable{
	private static final long serialVersionUID = -2023547438585673486L;	
	
	/**
	 * The identifier is used to maintain the matching between ConfigurationSpecification
	 * and ConfigurationValue while retrieving the values.
	 */
	private String identifier;	
	
	public InputParameter(){}
	
	public InputParameter(String identifier){
		this.setIdentifier(identifier);
	}
	
	// **** GETTERS & SETTERS ****
	
	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * Every subclass must implement this and return a Widget which
	 * can retrieve the user input for its value.
	 * 
	 * @return a Widget suited for input of the specific subclass
	 */
	public abstract InputParameterWidget createWrappingWidget();
}
