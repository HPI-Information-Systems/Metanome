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

import de.metanome.algorithm_integration.configuration.ConfigurationSettingString;
import de.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.metanome.algorithm_integration.configuration.ConfigurationSpecificationString;
import de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper;
import de.uni_potsdam.hpi.metanome.frontend.client.input_fields.InputField;
import de.uni_potsdam.hpi.metanome.frontend.client.input_fields.StringInput;

import java.util.List;

public class InputParameterStringWidget extends InputParameterWidget {

  protected ConfigurationSpecificationString specification;
  protected List<StringInput> inputWidgets;
  protected TabWrapper messageReceiver;

  public InputParameterStringWidget(ConfigurationSpecificationString config, TabWrapper wrapper) {
    super(config, wrapper);
  }

  @Override
  protected void addInputField(boolean optional) {
    StringInput field = new StringInput(optional);
    this.inputWidgets.add(field);
    int index = (this.getWidgetCount() < 1 ? 0 : this.getWidgetCount() - 1);
    this.insert(field, index);
  }

  @Override
  public ConfigurationSpecificationString getUpdatedSpecification() {
    this.specification.setSettings(this.getConfigurationSettings());
    return this.specification;
  }

  protected ConfigurationSettingString[] getConfigurationSettings() {
    ConfigurationSettingString[] values = new ConfigurationSettingString[this.inputWidgets.size()];
    int i = 0;
    for (StringInput si : this.inputWidgets) {
      values[i] = new ConfigurationSettingString(si.getValue());
      i++;
    }
    return values;
  }


  @Override
  public List<? extends InputField> getInputWidgets() {
    return this.inputWidgets;
  }

  @Override
  public void setInputWidgets(List<? extends InputField> inputWidgetsList) {
    this.inputWidgets = (List<StringInput>) inputWidgetsList;
  }


  @Override
  public ConfigurationSpecification getSpecification() {
    return this.specification;
  }

  @Override
  public void setSpecification(ConfigurationSpecification config) {
    this.specification = (ConfigurationSpecificationString) config;
  }

  @Override
  public void setMessageReceiver(TabWrapper messageReceiver) {
    this.messageReceiver = messageReceiver;
  }
}
