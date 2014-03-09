/*
 * Copyright 2014 by the Metanome project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.uni_potsdam.hpi.metanome.frontend.client.parameter;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class InputParameterSQLIteratorWidget extends FlexTable implements
InputParameterDataSourceWidget {

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
	public InputParameterDataSource getInputParameter() {
		setCurrentValues(this.inputParameter);
		return this.inputParameter;
	}

	@Override
	public void setInputParameter(InputParameterDataSource parameter) {
		InputParameterSQLIterator sqlParameter = (InputParameterSQLIterator)parameter;
		this.dbUrlTextbox.setValue(sqlParameter.getDbUrl());
		this.passwordTextbox.setValue(sqlParameter.getPassword());
		this.usernameTextbox.setValue(sqlParameter.getUserName());
	}

}
