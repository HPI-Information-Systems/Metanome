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

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingDataSource;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper;


public abstract class InputParameterDataSourceWidget extends InputParameterWidget {

  public InputParameterDataSourceWidget(ConfigurationSpecification config, TabWrapper wrapper) {
    super(config, wrapper);
  }

  /**
   * Specifies which configuration setting is supported by this input widget
   * @param setting the configuration setting
   * @return true, if the configuration setting is supported, false otherwise
   */
  public abstract boolean accepts(ConfigurationSettingDataSource setting);

  @Override
  public boolean isDataSource() {
    return true;
  }

  /**
   * Sets the given data source on the input field.
   * @param dataSource the data source, which should be set.
   * @throws AlgorithmConfigurationException
   */
  public abstract void setDataSource(ConfigurationSettingDataSource dataSource)
      throws AlgorithmConfigurationException;

}
