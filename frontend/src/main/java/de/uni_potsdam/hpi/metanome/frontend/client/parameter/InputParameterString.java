package de.uni_potsdam.hpi.metanome.frontend.client.parameter;



public class InputParameterString extends InputParameter {
	private static final long serialVersionUID = 7089599177559324612L;

	private String[] values;

	public InputParameterString(){
		super();
	}
	
	public InputParameterString(String identifier) {
		super(identifier);
	}

	// **** GETTERS & SETTERS ****
	
	public String[] getValues() {
		return values;
	}

	public void setValues(String... values) {
		this.values = values;
	}

	@Override
	public InputParameterWidget createWrappingWidget() {
		return new InputParameterStringWidget(this);
	}
}
