package de.uni_potsdam.hpi.metanome.frontend.client.parameter;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingSQLIterator;

public class SQLIteratorInput extends FlexTable {
	
	private TextBox dbUrlTextbox;
	private TextBox usernameTextbox;
	private TextBox passwordTextbox;
	
	public SQLIteratorInput() {
		super();
		
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
	
	protected void setCurrentValues(ConfigurationSettingSQLIterator setting){
		setting.setDbUrl(this.dbUrlTextbox.getValue());
		setting.setUsername(this.usernameTextbox.getValue());
		setting.setPassword(this.passwordTextbox.getValue());
	}

}
