package de.uni_potsdam.hpi.metanome.frontend.client.parameter;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingSQLIterator;

public class SQLIteratorInput extends InputField {
	
	private TextBox dbUrlTextbox;
	private TextBox usernameTextbox;
	private TextBox passwordTextbox;
	private FlexTable layoutTable;
	
	
	public SQLIteratorInput(boolean optional) {
		super(optional);
		
		this.layoutTable = new FlexTable();
		this.add(this.layoutTable);
		
		this.dbUrlTextbox = new TextBox();
		addRow(this.dbUrlTextbox, "Database URL", 0);
		
		this.usernameTextbox = new TextBox();
		addRow(this.usernameTextbox, "User Name", 1);
		
		this.passwordTextbox = new TextBox();
		addRow(this.passwordTextbox, "Password", 2);
		
	}
	
	protected void addRow(Widget inputWidget, String name, int row) {
		this.layoutTable.setText(row, 0, name);
		this.layoutTable.setWidget(row, 1, inputWidget);
	}
	
	public void setValues(ConfigurationSettingSQLIterator dataSource) {
		this.dbUrlTextbox.setValue(dataSource.getDbUrl());
		this.usernameTextbox.setValue(dataSource.getUsername());
		this.passwordTextbox.setValue(dataSource.getPassword());
	}

	public ConfigurationSettingSQLIterator getValue() {
		ConfigurationSettingSQLIterator setting = new ConfigurationSettingSQLIterator();
		
		setting.setDbUrl(this.dbUrlTextbox.getValue());
		setting.setUsername(this.usernameTextbox.getValue());
		setting.setPassword(this.passwordTextbox.getValue());
		
		return setting;
	}

}
