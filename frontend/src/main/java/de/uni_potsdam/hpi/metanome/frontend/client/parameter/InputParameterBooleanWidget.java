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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingBoolean;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationBoolean;

public class InputParameterBooleanWidget extends VerticalPanel implements InputParameterWidget {

	protected ConfigurationSpecificationBoolean specification;
    protected List<CheckBox> widgets;

    public InputParameterBooleanWidget(
            ConfigurationSpecificationBoolean specification) {
        super();
        this.specification = specification;
        // TODO: implement arbitrary number of widgets
        widgets = new ArrayList<>(specification.getNumberOfValues());
        for (int i = 0; i < specification.getNumberOfValues(); i++) {
        	CheckBox checkbox = new CheckBox(specification.getIdentifier());
            widgets.add(checkbox);
            this.add(checkbox);
        }
    }

    @Override
    public ConfigurationSpecification getUpdatedSpecification() {
        // Build an array with the actual number of set values.
        ConfigurationSettingBoolean[] values = new ConfigurationSettingBoolean[widgets.size()];

        for (int i = 0; i < widgets.size(); i++) {
            values[i] = new ConfigurationSettingBoolean(widgets.get(i).getValue());
        }

        specification.setValues(values);

        return specification;
    }
}
