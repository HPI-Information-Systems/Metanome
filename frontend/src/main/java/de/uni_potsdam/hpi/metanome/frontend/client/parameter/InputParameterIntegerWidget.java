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

import de.metanome.algorithm_integration.configuration.ConfigurationSettingInteger;
import de.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.metanome.algorithm_integration.configuration.ConfigurationSpecificationInteger;
import de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper;
import de.uni_potsdam.hpi.metanome.frontend.client.helpers.InputValidationException;
import de.uni_potsdam.hpi.metanome.frontend.client.input_fields.InputField;
import de.uni_potsdam.hpi.metanome.frontend.client.input_fields.IntegerInput;

import java.util.List;

public class InputParameterIntegerWidget extends InputParameterWidget {

  protected ConfigurationSpecificationInteger specification;
  protected List<IntegerInput> inputWidgets;
  protected TabWrapper messageReceiver;

  public InputParameterIntegerWidget(ConfigurationSpecificationInteger config, TabWrapper wrapper) {
    super(config, wrapper);
  }

  @Override
  protected void addInputField(boolean optional) {

    IntegerInput field = new IntegerInput(optional);
    this.inputWidgets.add(field);
    int index = (this.getWidgetCount() < 1 ? 0 : this.getWidgetCount() - 1);
    this.insert(field, index);
  }

  @Override
  public ConfigurationSpecificationInteger getUpdatedSpecification()
      throws InputValidationException {
    this.specification.setSettings(this.getConfigurationSettings());
    return this.specification;
  }

  protected ConfigurationSettingInteger[] getConfigurationSettings()
      throws InputValidationException {
    ConfigurationSettingInteger[]
        values =
        new ConfigurationSettingInteger[this.inputWidgets.size()];
    int i = 0;
    for (IntegerInput ii : this.inputWidgets) {
      values[i] = new ConfigurationSettingInteger(ii.getValue());
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
    this.inputWidgets = (List<IntegerInput>) inputWidgetsList;
  }

  @Override
  public ConfigurationSpecification getSpecification() {
    return this.specification;
  }

  @Override
  public void setSpecification(ConfigurationSpecification config) {
    this.specification = (ConfigurationSpecificationInteger) config;
  }

  @Override
  public void setMessageReceiver(TabWrapper messageReceiver) {
    this.messageReceiver = messageReceiver;
  }

}
