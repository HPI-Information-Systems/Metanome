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
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementInteger;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingInteger;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.helpers.InputValidationException;
import de.metanome.frontend.client.input_fields.InputField;
import de.metanome.frontend.client.input_fields.IntegerInput;

import java.util.ArrayList;
import java.util.List;

public class InputParameterIntegerWidget extends InputParameterWidget {

  protected ConfigurationRequirementInteger specification;
  protected List<IntegerInput> inputWidgets;
  protected TabWrapper messageReceiver;

  public InputParameterIntegerWidget(ConfigurationRequirementInteger config, TabWrapper wrapper) {
    super(config, wrapper);
  }

  @Override
  protected void addInputField(boolean optional, boolean required, int settingIndex) {
    // Create the field with the default value, if one is set
    Integer defaultValue = this.specification.getDefaultValue(settingIndex);
    IntegerInput field = new IntegerInput(optional, required);
    if (defaultValue != null) {
      field.setValue(defaultValue);
    }

    // Add the field at the correct position
    this.inputWidgets.add(field);
    int index = (this.getWidgetCount() < 1 ? 0 : this.getWidgetCount() - 1);
    this.insert(field, index);
  }

  @Override
  public ConfigurationRequirementInteger getUpdatedSpecification()
      throws InputValidationException, AlgorithmConfigurationException {
    this.specification.checkAndSetSettings(this.getConfigurationSettings());
    return this.specification;
  }

  protected ConfigurationSettingInteger[] getConfigurationSettings()
      throws InputValidationException {
    List<ConfigurationSettingInteger> values = new ArrayList<>();

    for (IntegerInput ii : this.inputWidgets) {
      int current = ii.getValue();
      if (current != -1) {
        values.add(new ConfigurationSettingInteger(current));
      }
    }

    return values.toArray(new ConfigurationSettingInteger[values.size()]);
  }


  @Override
  public List<? extends InputField> getInputWidgets() {
    return this.inputWidgets;
  }

  @Override
  public void setInputWidgets(List<? extends InputField> inputWidgetsList) {
    this.inputWidgets = (List<IntegerInput>) inputWidgetsList;
  }

  @Override
  public ConfigurationRequirement getSpecification() {
    return this.specification;
  }

  @Override
  public void setSpecification(ConfigurationRequirement config) {
    this.specification = (ConfigurationRequirementInteger) config;
  }

  @Override
  public void setMessageReceiver(TabWrapper messageReceiver) {
    this.messageReceiver = messageReceiver;
  }

}
