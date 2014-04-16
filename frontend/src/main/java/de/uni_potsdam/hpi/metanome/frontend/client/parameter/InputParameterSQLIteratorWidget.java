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

import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingDataSource;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationSQLIterator;

public class InputParameterSQLIteratorWidget extends FlexTable implements
InputParameterDataSourceWidget {

	/** Corresponding inputParameter, where the value is going to be written */
	private ConfigurationSpecificationSQLIterator configSpec;
	
	private SQLIteratorInput inputFields; 


	public InputParameterSQLIteratorWidget(
			ConfigurationSpecificationSQLIterator inputParameter) {
		super();
		this.configSpec = inputParameter;
		
	}

	@Override
	public ConfigurationSpecification getConfigurationSpecificationWithValues() {
		// TODO Auto-generated method stub: 		setCurrentValues(this.configSpec);
		return this.configSpec;
	}

	@Override
	public void setDataSource(ConfigurationSettingDataSource dataSource) {
		// TODO Auto-generated method stub
//		this.dbUrlTextbox.setDataSource(sqlParameter.getDbUrl());
//		this.passwordTextbox.setDataSource(sqlParameter.getPassword());
//		this.usernameTextbox.setDataSource(sqlParameter.getUserName());
	}

}
