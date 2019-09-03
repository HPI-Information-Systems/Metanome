/**
 * Copyright 2014-2016 by Metanome Project
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
package de.metanome.backend.configuration;

import de.metanome.algorithm_integration.Algorithm;
import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.algorithm_types.TableInputParameterAlgorithm;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementTableInput;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingTableInput;
import de.metanome.algorithm_integration.input.TableInputGenerator;
import de.metanome.backend.input.database.DefaultTableInputGenerator;
import de.metanome.backend.results_db.AlgorithmType;

import java.io.FileNotFoundException;
import java.util.Set;

/**
 * Represents {@link de.metanome.algorithm_integration.input.TableInputGenerator} configuration
 * values for {@link Algorithm}s.
 *
 * @author Jakob Zwiener
 */
public class ConfigurationValueTableInputGenerator
  extends ConfigurationValue<TableInputGenerator, ConfigurationRequirementTableInput> {

  protected ConfigurationValueTableInputGenerator() {
  }

  public ConfigurationValueTableInputGenerator(String identifier,
                                               TableInputGenerator... values) {
    super(identifier, values);
  }

  public ConfigurationValueTableInputGenerator(ConfigurationRequirementTableInput requirement)
    throws AlgorithmConfigurationException, FileNotFoundException {
    super(requirement);
  }

  @Override
  protected TableInputGenerator[] convertToValues(ConfigurationRequirementTableInput requirement)
    throws AlgorithmConfigurationException {
    ConfigurationSettingTableInput[] settings = requirement.getSettings();
    TableInputGenerator[] configValues = new TableInputGenerator[settings.length];
    for (int i = 0; i < settings.length; i++) {
      configValues[i] = new DefaultTableInputGenerator(settings[i]);
    }
    return configValues;
  }

  @Override
  public void triggerSetValue(Algorithm algorithm, Set<Class<?>> algorithmInterfaces)
    throws AlgorithmConfigurationException {
    if (!algorithmInterfaces.contains(AlgorithmType.TABLE_INPUT.getAlgorithmClass())) {
      throw new AlgorithmConfigurationException(
        "Algorithm does not accept table input configuration values.");
    }

    TableInputParameterAlgorithm tableInputParameterAlgorithm =
      (TableInputParameterAlgorithm) algorithm;
    tableInputParameterAlgorithm.setTableInputConfigurationValue(identifier, values);
  }

}
