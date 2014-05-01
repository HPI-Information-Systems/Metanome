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

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingCsvFile;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingDataSource;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationCsvFile;
import de.uni_potsdam.hpi.metanome.frontend.client.services.InputDataService;
import de.uni_potsdam.hpi.metanome.frontend.client.services.InputDataServiceAsync;

import java.util.ArrayList;
import java.util.List;

public class InputParameterCsvFileWidget extends VerticalPanel implements InputParameterDataSourceWidget {

    protected List<CsvFileInput> widgets;
    /**
     * Corresponding ConfigurationSpecification, where the value is going to be written
     */
    private ConfigurationSpecificationCsvFile specification;

    /**
     * Constructor.
     *
     * @param configSpec
     */
    public InputParameterCsvFileWidget(ConfigurationSpecificationCsvFile configSpec) {
        super();
        this.specification = configSpec;
        //TODO implement arbitrary number of values
        widgets = new ArrayList<>(specification.getNumberOfValues());
        for (int i = 0; i < specification.getNumberOfValues(); i++) {
            CsvFileInput input = new CsvFileInput();
            this.addWidget(input);
        }

        this.addAvailableCsvsToListbox(widgets);
    }

    private void addWidget(CsvFileInput widget) {
        this.widgets.add(widget);
        this.add(widget);
    }


    /**
     * Calls the InputDataService to retrieve available CSV files (specified by their
     * file paths) and adds them as entries to the given ListBox. Only the actual file
     * name (not the preceding directories) are displayed.
     *
     * @param listbox the ListBox to add the available files' names to
     */
    private void addAvailableCsvsToListbox(final List<CsvFileInput> widgets) {
        AsyncCallback<String[]> callback = new AsyncCallback<String[]>() {
            public void onFailure(Throwable caught) {
                // TODO: Do something with errors.
                caught.printStackTrace();
            }

            public void onSuccess(String[] result) {
                for (CsvFileInput widget : widgets) {
                    try {
                        widget.addToListbox(result);
                    } catch (AlgorithmConfigurationException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        };

        InputDataServiceAsync service = GWT.create(InputDataService.class);
        service.listCsvInputFiles(callback);
    }


    @Override
    public ConfigurationSpecificationCsvFile getUpdatedSpecification() {
        // Build an array with the actual number of set values.
        ConfigurationSettingCsvFile[] values = new ConfigurationSettingCsvFile[widgets.size()];

        for (int i = 0; i < widgets.size(); i++) {
            values[i] = widgets.get(i).getValuesAsSettings();
        }

        specification.setValues(values);

        return specification;
    }

    @Override
    public boolean accepts(ConfigurationSettingDataSource setting) {
        return setting instanceof ConfigurationSettingCsvFile;
    }

    @Override
    public boolean isDataSource() {
        return true;
    }

    @Override
    public void setDataSource(ConfigurationSettingDataSource dataSource) throws AlgorithmConfigurationException {
        this.widgets.get(0).selectDataSource(dataSource);
    }


}
