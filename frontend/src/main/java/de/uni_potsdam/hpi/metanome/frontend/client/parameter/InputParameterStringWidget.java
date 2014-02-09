package de.uni_potsdam.hpi.metanome.frontend.client.parameter;

import com.google.gwt.user.client.ui.TextBox;

public class InputParameterStringWidget extends TextBox implements InputParameterWidget {
	
	// FIXME implement several input values
	
	InputParameterString inputParameter;
	
	public InputParameterStringWidget(InputParameterString inputParameter) {
		super();
		this.inputParameter = inputParameter;
	}
	
	@Override
	public InputParameter getInputParameter() {
		this.inputParameter.setValues(this.getValue());
		return inputParameter;
	}
	
}
