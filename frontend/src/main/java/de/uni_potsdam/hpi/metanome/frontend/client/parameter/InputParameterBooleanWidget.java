package de.uni_potsdam.hpi.metanome.frontend.client.parameter;

import com.google.gwt.user.client.ui.CheckBox;

public class InputParameterBooleanWidget extends CheckBox implements InputParameterWidget {
	
	private InputParameterBoolean inputParameter;

	public InputParameterBooleanWidget(
			InputParameterBoolean inputParameter) {
		super();
		this.inputParameter = inputParameter;
	}

	@Override
	public InputParameter getInputParameter() {
		this.inputParameter.setValue(this.getValue());
		return inputParameter;
	}

}
