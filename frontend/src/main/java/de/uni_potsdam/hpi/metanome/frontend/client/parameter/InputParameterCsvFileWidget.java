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

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingCsvFile;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingDataSource;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationCsvFile;
import de.uni_potsdam.hpi.metanome.frontend.client.helpers.InputValidationException;
import de.uni_potsdam.hpi.metanome.frontend.client.services.InputDataService;
import de.uni_potsdam.hpi.metanome.frontend.client.services.InputDataServiceAsync;

import java.util.List;

public class InputParameterCsvFileWidget extends InputParameterDataSourceWidget {

  protected List<CsvFileInput> inputWidgets;
  /**
   * Corresponding ConfigurationSpecification, where the value is going to be written
   */
  private ConfigurationSpecificationCsvFile specification;

  public InputParameterCsvFileWidget(ConfigurationSpecificationCsvFile configSpec) {
    super(configSpec);

    this.addAvailableCsvsToListbox(inputWidgets);
  }

  /**
   * Calls the InputDataService to retrieve available CSV files (specified by their file paths) and
   * adds them as entries to the given ListBox. Only the actual file name (not the preceding
   * directories) are displayed.
   *
   * @param widgets the csv widgets to add to the list
   */
  private void addAvailableCsvsToListbox(final List<CsvFileInput> widgets) {
    AsyncCallback<String[]> callback = getCallback(widgets);

    InputDataServiceAsync service = GWT.create(InputDataService.class);
    service.listCsvInputFiles(callback);
  }


  protected AsyncCallback<String[]> getCallback(final List<CsvFileInput> widgets) {
    return new AsyncCallback<String[]>() {
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
  }

  @Override
  protected void addInputField(boolean optional) {

    CsvFileInput widget = new CsvFileInput(optional);
    this.inputWidgets.add(widget);
    int index = (this.getWidgetCount() < 1 ? 0 : this.getWidgetCount() - 1);
    this.insert(widget, index);
  }

  @Override
  public ConfigurationSpecificationCsvFile getUpdatedSpecification()
      throws InputValidationException {
    // Build an array with the actual number of set values.
    ConfigurationSettingCsvFile[] values = new ConfigurationSettingCsvFile[inputWidgets.size()];

    for (int i = 0; i < inputWidgets.size(); i++) {
      values[i] = inputWidgets.get(i).getValuesAsSettings();
    }

    specification.setValues(values);

    return specification;
  }


  @Override
  public void setDataSource(ConfigurationSettingDataSource dataSource)
      throws AlgorithmConfigurationException {
    this.inputWidgets.get(0).selectDataSource(dataSource);
  }

  @Override
  public boolean accepts(ConfigurationSettingDataSource setting) {
    return setting instanceof ConfigurationSettingCsvFile;
  }


  @Override
  public List<? extends InputField> getInputWidgets() {
    return this.inputWidgets;
  }

  @Override
  public void setInputWidgets(List<? extends InputField> inputWidgetsList) {
    this.inputWidgets = (List<CsvFileInput>) inputWidgetsList;
  }


  @Override
  public ConfigurationSpecification getSpecification() {
    return this.specification;
  }

  @Override
  public void setSpecification(ConfigurationSpecification config) {
    this.specification = (ConfigurationSpecificationCsvFile) config;
  }

}
