package de.uni_potsdam.hpi.metanome.frontend.client.widgets;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameter;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterSQLIterator;

public class InputParameterSQLIteratorWidget extends FlexTable implements
InputParameterWidget {

	/** Corresponding inputParameter, where the value is going to be written */
	private InputParameterSQLIterator inputParameter;
	
	private TextBox dbUrlTextbox;
	private TextBox usernameTextbox;
	private TextBox passwordTextbox;

	public InputParameterSQLIteratorWidget(
			InputParameterSQLIterator inputParameter) {
		super();
		this.inputParameter = inputParameter;
		
		dbUrlTextbox = new TextBox();
		addRow(dbUrlTextbox, "Database URL", 0);
		
		usernameTextbox = new TextBox();
		addRow(usernameTextbox, "User Name", 1);
		
		passwordTextbox = new TextBox();
		addRow(passwordTextbox, "Password", 2);
		
	}

	protected void addRow(Widget inputWidget, String name, int row) {
		this.setText(row, 0, name);
		this.setWidget(row, 1, inputWidget);
	}
	
	protected void setCurrentValues(InputParameterSQLIterator inputParameter){
		inputParameter.setDbUrl(this.dbUrlTextbox.getValue());
		inputParameter.setUserName(this.usernameTextbox.getValue());
		inputParameter.setPassword(this.passwordTextbox.getValue());
	}
	
	@Override
	public InputParameter getInputParameter() {
		setCurrentValues(this.inputParameter);
		return this.inputParameter;
	}

}
