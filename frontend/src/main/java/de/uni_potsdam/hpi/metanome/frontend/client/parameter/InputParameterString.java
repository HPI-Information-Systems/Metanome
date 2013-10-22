package de.uni_potsdam.hpi.metanome.frontend.client.parameter;

import de.uni_potsdam.hpi.metanome.frontend.client.widgets.InputParameterStringWidget;
import de.uni_potsdam.hpi.metanome.frontend.client.widgets.InputParameterWidget;


public class InputParameterString extends InputParameter {
	private static final long serialVersionUID = 7089599177559324612L;

	private String value;

	public InputParameterString(){
		super();
	}
	
	public InputParameterString(String identifier) {
		super(identifier);
	}

	// **** GETTERS & SETTERS ****
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public InputParameterWidget createWrappingWidget() {
		return new InputParameterStringWidget(this);
	}
}
