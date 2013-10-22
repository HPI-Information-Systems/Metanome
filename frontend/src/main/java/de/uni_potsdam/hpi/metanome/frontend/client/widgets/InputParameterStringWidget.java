package de.uni_potsdam.hpi.metanome.frontend.client.widgets;

import com.google.gwt.user.client.ui.TextBox;

import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameter;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterString;

public class InputParameterStringWidget extends TextBox implements InputParameterWidget {
	
	InputParameterString inputParameter;

	public InputParameterStringWidget(InputParameterString inputParameter) {
		super();
		this.inputParameter = inputParameter;
	}

	@Override
	public void setValue(String value){
		super.setValue(value);
		this.inputParameter.setValue(value);
	}
	
	@Override
	public InputParameter getInputParameter() {
		return inputParameter;
	}
	
}
