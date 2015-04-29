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

package de.metanome.frontend.client.parameter;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementTableInput;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingDataSource;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingTableInput;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.helpers.InputValidationException;
import de.metanome.frontend.client.input_fields.InputField;
import de.metanome.frontend.client.input_fields.TableInputInput;

import java.util.ArrayList;
import java.util.List;

public class InputParameterTableInputWidget extends InputParameterDataSourceWidget {

  protected List<TableInputInput> inputWidgets;
  protected TabWrapper messageReceiver;

  /**
   * Corresponding ConfigurationSpecification, where the value is going to be written
   */
  private ConfigurationRequirementTableInput specification;

  public InputParameterTableInputWidget(ConfigurationRequirementTableInput configSpec,
                                        TabWrapper messageReceiver) {
    super(configSpec, messageReceiver);
  }

  @Override
  protected void addInputField(boolean optional, boolean required, int settingIndex) {
    TableInputInput widget = new TableInputInput(optional, required, messageReceiver);
    this.inputWidgets.add(widget);
    int index = (this.getWidgetCount() < 1 ? 0 : this.getWidgetCount() - 1);
    this.insert(widget, index);
  }

  @Override
  public ConfigurationRequirementTableInput getUpdatedSpecification()
      throws InputValidationException, AlgorithmConfigurationException {
    // Build an array with the actual number of set values.
    List<ConfigurationSettingTableInput> values = new ArrayList<>();

    for (TableInputInput inputWidget : inputWidgets) {
      ConfigurationSettingTableInput current = inputWidget.getValue();
      if (current != null) {
        values.add(current);
      }
    }

    specification
        .checkAndSetSettings(values.toArray(new ConfigurationSettingTableInput[values.size()]));

    return specification;
  }

  @Override
  public void setDataSource(ConfigurationSettingDataSource dataSource)
      throws AlgorithmConfigurationException {
    this.inputWidgets.get(0).selectDataSource(dataSource);
  }

  @Override
  public void update() {
    for (int i = 0; i < inputWidgets.size(); i++) {
      inputWidgets.get(i).updateListBox();
    }
  }

  @Override
  public boolean accepts(ConfigurationSettingDataSource setting) {
    return setting instanceof ConfigurationSettingTableInput;
  }

  @Override
  public List<? extends InputField> getInputWidgets() {
    return this.inputWidgets;
  }

  @Override
  public void setInputWidgets(List<? extends InputField> inputWidgetsList) {
    this.inputWidgets = (List<TableInputInput>) inputWidgetsList;
  }

  @Override
  public ConfigurationRequirement getSpecification() {
    return this.specification;
  }

  @Override
  public void setSpecification(ConfigurationRequirement config) {
    this.specification = (ConfigurationRequirementTableInput) config;
  }

  @Override
  public void setMessageReceiver(TabWrapper messageReceiver) {
    this.messageReceiver = messageReceiver;
  }
}
