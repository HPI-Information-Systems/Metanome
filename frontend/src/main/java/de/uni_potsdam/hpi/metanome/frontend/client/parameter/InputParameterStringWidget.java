package de.uni_potsdam.hpi.metanome.frontend.client.parameter;

import com.google.gwt.user.client.ui.TextBox;

public class InputParameterStringWidget extends TextBox implements InputParameterWidget {
	
	InputParameterString inputParameter;

	public InputParameterStringWidget(InputParameterString inputParameter) {
		super();
		this.inputParameter = inputParameter;
	}
	
	@Override
	public InputParameter getInputParameter() {
		this.inputParameter.setValue(this.getValue());
		return inputParameter;
	}
	
}
