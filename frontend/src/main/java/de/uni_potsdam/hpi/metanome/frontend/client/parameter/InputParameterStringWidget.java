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

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingString;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationString;

public class InputParameterStringWidget extends VerticalPanel implements InputParameterWidget {
	
	// FIXME implement several input values
	
	private ConfigurationSpecificationString specification;
	private List<TextBox> widgets;
	
	public InputParameterStringWidget(ConfigurationSpecificationString config) {
		super();
		this.specification = config;
		this.widgets = new LinkedList<TextBox>();
		for (int i=0; i<config.getNumberOfValues(); i++) {
			TextBox textBox = new TextBox();
			this.widgets.add(textBox);
			this.add(textBox);
		}
	}
	
	@Override
	public ConfigurationSpecificationString getUpdatedSpecification(){
		this.specification.setValues(this.getConfigurationSettings());
		return this.specification;
	}
	
	
	protected ConfigurationSettingString[] getConfigurationSettings() {
		ConfigurationSettingString[] values = new ConfigurationSettingString[this.widgets.size()];
		int i=0;
		for (TextBox b : this.widgets){
			values[i] = new ConfigurationSettingString(b.getValue());
			i++;
		}
		return values;
	}

	@Override
	public boolean isDataSource() {
		return false;
	}

}
