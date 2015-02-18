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
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementBoolean;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingBoolean;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.input_fields.BooleanInput;
import de.metanome.frontend.client.input_fields.InputField;

import java.util.List;

public class InputParameterBooleanWidget extends InputParameterWidget {

  protected ConfigurationRequirementBoolean specification;
  protected List<BooleanInput> inputWidgets;
  protected TabWrapper messageReceiver;


  public InputParameterBooleanWidget(ConfigurationRequirementBoolean specification,
                                     TabWrapper wrapper) {
    super(specification, wrapper);
  }

  @Override
  protected void addInputField(boolean optional, boolean required, int settingIndex) {
    // Create the field with the default value, if one is set
    Boolean defaultValue = this.specification.getDefaultValue(settingIndex);
    BooleanInput field = new BooleanInput(optional, required);
    if (defaultValue != null) field.setValue(defaultValue);

    // Add the field at the correct position
    this.inputWidgets.add(field);
    int index = (this.getWidgetCount() < 1 ? 0 : this.getWidgetCount() - 1);
    this.insert(field, index);
  }

  @Override
  public ConfigurationRequirement getUpdatedSpecification() throws AlgorithmConfigurationException {
    // Build an array with the actual number of set values.
    ConfigurationSettingBoolean[] values = new ConfigurationSettingBoolean[inputWidgets.size()];

    for (int i = 0; i < inputWidgets.size(); i++) {
      values[i] = new ConfigurationSettingBoolean(inputWidgets.get(i).getValue());
    }
    specification.checkAndSetSettings(values);

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
  public ConfigurationRequirement getSpecification() {
    return this.specification;
  }

  @Override
  public void setSpecification(ConfigurationRequirement config) {
    this.specification = (ConfigurationRequirementBoolean) config;
  }

  @Override
  public void setMessageReceiver(TabWrapper messageReceiver) {
    this.messageReceiver = messageReceiver;
  }

}
