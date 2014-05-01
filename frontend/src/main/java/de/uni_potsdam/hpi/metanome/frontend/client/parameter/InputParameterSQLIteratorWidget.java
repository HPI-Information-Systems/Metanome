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

import com.google.gwt.user.client.ui.VerticalPanel;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingDataSource;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingSQLIterator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationSQLIterator;

import java.util.ArrayList;
import java.util.List;

public class InputParameterSQLIteratorWidget extends VerticalPanel implements
        InputParameterDataSourceWidget {

    /**
     * Corresponding inputParameter, where the value is going to be written
     */
    private ConfigurationSpecificationSQLIterator specification;
    private List<SQLIteratorInput> widgets;


    public InputParameterSQLIteratorWidget(
            ConfigurationSpecificationSQLIterator config) {
        super();
        this.specification = config;
        // TODO: implement arbitrary number of widgets
        widgets = new ArrayList<>(specification.getNumberOfValues());
        for (int i = 0; i < specification.getNumberOfValues(); i++) {
            SQLIteratorInput input = new SQLIteratorInput();
            this.addWidget(input, i);
        }
    }

    public void addWidget(SQLIteratorInput widget, int row) {
        this.widgets.add(widget);
        this.add(widget);
    }

    @Override
    public ConfigurationSpecification getUpdatedSpecification() {
        // Build an array with the actual number of set values.
        ConfigurationSettingSQLIterator[] values = new ConfigurationSettingSQLIterator[widgets.size()];

        for (int i = 0; i < widgets.size(); i++) {
            values[i] = widgets.get(i).getValue();
        }

        specification.setValues(values);

        return this.specification;
    }

    @Override
    public boolean accepts(ConfigurationSettingDataSource setting) {
        return setting instanceof ConfigurationSettingSQLIterator;
    }

    @Override
    public boolean isDataSource() {
        return true;
    }

    @Override
    public void setDataSource(ConfigurationSettingDataSource dataSource) {
        if (dataSource instanceof ConfigurationSettingSQLIterator)
            this.widgets.get(0).setValues((ConfigurationSettingSQLIterator) dataSource);
        else
            ; //TODO throw some exception
    }

}
