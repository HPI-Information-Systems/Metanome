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

import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingBoolean;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationBoolean;

import java.util.List;

public class InputParameterBooleanWidget extends InputParameterWidget {

	protected ConfigurationSpecificationBoolean specification;
	protected List<BooleanInput> inputWidgets;


	public InputParameterBooleanWidget(ConfigurationSpecificationBoolean specification) {
		super(specification);
	}

	@Override
	protected void addInputField(boolean optional) {
		BooleanInput field = new BooleanInput(optional);
		this.inputWidgets.add(field);
		int index = (this.getWidgetCount() < 1 ? 0 : this.getWidgetCount() - 1);
		this.insert(field, index);
	}

	@Override
	public ConfigurationSpecification getUpdatedSpecification() {
		// Build an array with the actual number of set values.
		ConfigurationSettingBoolean[] values = new ConfigurationSettingBoolean[inputWidgets.size()];

		for (int i = 0; i < inputWidgets.size(); i++) {
			values[i] = new ConfigurationSettingBoolean(inputWidgets.get(i).getValue());
		}
		specification.setValues(values);

		return specification;
	}

	@Override
	public List<? extends InputField> getInputWidgets() {
		return this.inputWidgets;
	}

	@Override
	public void setInputWidgets(List<? extends InputField> inputWidgetsList) {
		this.inputWidgets = (List<BooleanInput>) inputWidgetsList;
	}

	@Override
	public ConfigurationSpecification getSpecification() {
		return this.specification;
	}

	@Override
	public void setSpecification(ConfigurationSpecification config) {
		this.specification = (ConfigurationSpecificationBoolean) config;
	}

}
