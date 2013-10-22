package de.uni_potsdam.hpi.metanome.frontend.client.parameter;

import java.io.Serializable;

import de.uni_potsdam.hpi.metanome.frontend.client.widgets.InputParameterWidget;


public abstract class InputParameter implements Serializable{
	private static final long serialVersionUID = -2023547438585673486L;	
	
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

	public abstract Object getValue();

	public abstract InputParameterWidget createWrappingWidget();
}
