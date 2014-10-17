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
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementDatabaseConnection;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingDataSource;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingDatabaseConnection;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.helpers.InputValidationException;
import de.metanome.frontend.client.input_fields.DatabaseConnectionInput;
import de.metanome.frontend.client.input_fields.InputField;

import java.util.List;

public class InputParameterDatabaseConnectionWidget extends InputParameterDataSourceWidget {

  protected List<DatabaseConnectionInput> inputWidgets;
  protected TabWrapper messageReceiver;
  /**
   * Corresponding inputParameter, where the value is going to be written
   */
  private ConfigurationRequirementDatabaseConnection specification;

  public InputParameterDatabaseConnectionWidget(ConfigurationRequirementDatabaseConnection config,
                                                TabWrapper wrapper) {
    super(config, wrapper);

  }

  @Override
  protected void addInputField(boolean optional) {
    DatabaseConnectionInput widget = new DatabaseConnectionInput(
        optional, messageReceiver, this.specification.getAcceptedDBSystems());
    this.inputWidgets.add(widget);
    this.add(widget);
  }

  @Override
  public ConfigurationRequirement getUpdatedSpecification() throws InputValidationException {
    // Build an array with the actual number of set values.
    ConfigurationSettingDatabaseConnection[]
        values =
        new ConfigurationSettingDatabaseConnection[inputWidgets.size()];

    for (int i = 0; i < inputWidgets.size(); i++) {
      values[i] = inputWidgets.get(i).getValues();
    }

    specification.setSettings(values);

    return this.specification;
  }

  @Override
  public void setDataSource(ConfigurationSettingDataSource dataSource)
      throws AlgorithmConfigurationException {
    this.inputWidgets.get(0).setValues((ConfigurationSettingDatabaseConnection) dataSource);
  }

  @Override
  public void update() {
    for (int i = 0; i < inputWidgets.size(); i++) {
      inputWidgets.get(i).updateListBox();
    }
  }

  @Override
  public boolean accepts(ConfigurationSettingDataSource setting) {
    return setting instanceof ConfigurationSettingDatabaseConnection;
  }

  @Override
  public List<DatabaseConnectionInput> getInputWidgets() {
    return this.inputWidgets;
  }

  @Override
  public void setInputWidgets(List<? extends InputField> inputWidgetsList) {
    this.inputWidgets = (List<DatabaseConnectionInput>) inputWidgetsList;
  }

  @Override
  public ConfigurationRequirement getSpecification() {
    return this.specification;
  }

  @Override
  public void setSpecification(ConfigurationRequirement config) {
    this.specification = (ConfigurationRequirementDatabaseConnection) config;
  }

  @Override
  public void setMessageReceiver(TabWrapper messageReceiver) {
    this.messageReceiver = messageReceiver;
  }

}
