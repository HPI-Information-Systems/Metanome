package de.uni_potsdam.hpi.metanome.frontend.client.parameter;

import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Widget;

public class InputParameterInteger extends InputParameter {
	private static final long serialVersionUID = 5161219640594711634L;
	
	private int value;

	public InputParameterInteger(){}

	public InputParameterInteger(String identifier) {
		super(identifier);
	}

	// **** GETTERS & SETTERS ****
	public Integer getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	@Override
	public void setValue(Object obj) {
		if (obj == null)
			this.value = 0;
		else
			this.value = (Integer) obj;
	}

	@Override
	public Widget getWidget() {
		return new IntegerBox();
	}
}
