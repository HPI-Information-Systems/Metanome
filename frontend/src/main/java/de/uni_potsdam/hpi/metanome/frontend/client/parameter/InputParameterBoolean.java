package de.uni_potsdam.hpi.metanome.frontend.client.parameter;


public class InputParameterBoolean extends InputParameter {
	private static final long serialVersionUID = 5161219640594711634L;
	
	private boolean value;

	public InputParameterBoolean(){
		super();
	}

	public InputParameterBoolean(String identifier) {
		super(identifier);
	}

	// **** GETTERS & SETTERS ****
	public Boolean getValue() {
		return value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}

	@Override
	public InputParameterWidget createWrappingWidget() {
		return new InputParameterBooleanWidget(this);
	}
}
