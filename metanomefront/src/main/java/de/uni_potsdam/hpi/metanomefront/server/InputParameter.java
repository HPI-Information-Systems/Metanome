package de.uni_potsdam.hpi.metanomefront.server;


public class InputParameter {
	public enum Type {BOOL, INT, STRING}

	private String identifier;
	private Type type;
	
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
