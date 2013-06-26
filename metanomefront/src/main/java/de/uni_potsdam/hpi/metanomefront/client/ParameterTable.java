package de.uni_potsdam.hpi.metanomefront.client;

import java.util.List;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import de.uni_potsdam.hpi.metanomefront.server.InputParameter;
import de.uni_potsdam.hpi.metanomefront.server.InputParameter.Type;

public class ParameterTable extends FlexTable {

	public ParameterTable(List<InputParameter> requiredParams) {
		super();
		
		int i = 0;
		for (InputParameter param : requiredParams) {
			this.setText(i, 0, param.getIdentifier());
			this.setWidget(i, 1, getWidgetForType(param.getType()));
			i++;
		}
	}

	private Widget getWidgetForType(Type type) {
		switch(type) {
		case STRING: return new TextBox();
		case BOOL: return new CheckBox();
		case INT: return new IntegerBox();
		}
		return null;
	}

}
