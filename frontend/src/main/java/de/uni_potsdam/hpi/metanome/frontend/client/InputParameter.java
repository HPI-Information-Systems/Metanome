package de.uni_potsdam.hpi.metanome.frontend.client;

import java.io.Serializable;


public class InputParameter implements Serializable{
	private static final long serialVersionUID = -2023547438585673486L;

	public enum Type {BOOL, INT, STRING}
	
	
	private String identifier;
	private Type type;
	
	public InputParameter(){
		
	}
	
	public InputParameter(String identifier, Type type){
		this.setIdentifier(identifier);
		this.setType(type);
	}

	
	// **** GETTERS & SETTERS ****
	
	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

}
