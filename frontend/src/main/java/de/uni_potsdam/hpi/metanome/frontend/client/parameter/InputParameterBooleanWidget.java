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

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingBoolean;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationBoolean;

public class InputParameterBooleanWidget extends VerticalPanel implements InputParameterWidget {
	
	private List<CheckBox> checkBoxes;
	private ConfigurationSpecificationBoolean configSpec;

	public InputParameterBooleanWidget(
			ConfigurationSpecificationBoolean config) {
		super();
		this.configSpec = config;
		this.checkBoxes = new LinkedList<CheckBox>();
		for (int i=0; i<config.getNumberOfValues(); i++) {
			CheckBox checkBox = new CheckBox();
			this.checkBoxes.add(checkBox);
			this.add(checkBox);
		}
	}

	@Override
	public ConfigurationSpecificationBoolean getConfigurationSpecificationWithValues(){
		this.configSpec.setValues(this.getConfigurationSettings());
		return this.configSpec;
	}
	
	
	protected ConfigurationSettingBoolean[] getConfigurationSettings() {
		ConfigurationSettingBoolean[] values = new ConfigurationSettingBoolean[this.checkBoxes.size()];
		int i=0;
		for (CheckBox c : checkBoxes){
			values[i] = new ConfigurationSettingBoolean(c.getValue());
			i++;
		}
		return values;
	}

}
