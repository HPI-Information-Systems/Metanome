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

package de.metanome.backend.configuration;

import de.metanome.algorithm_integration.Algorithm;
import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.algorithm_types.FileInputParameterAlgorithm;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementFileInput;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingFileInput;
import de.metanome.algorithm_integration.input.FileInputGenerator;
import de.metanome.backend.input.file.DefaultFileInputGenerator;

import java.util.Set;

/**
 * Represents {@link de.metanome.algorithm_integration.input.FileInputGenerator} configuration
 * values for {@link Algorithm}s.
 *
 * @author Jakob Zwiener
 */
public class ConfigurationValueFileInputGenerator
    extends ConfigurationValue<FileInputGenerator, ConfigurationRequirementFileInput> {

  public ConfigurationValueFileInputGenerator(String identifier,
                                              FileInputGenerator... values) {
    super(identifier, values);
  }

  public ConfigurationValueFileInputGenerator(ConfigurationRequirementFileInput requirement)
      throws AlgorithmConfigurationException {
    super(requirement);
  }

  @Override
  protected FileInputGenerator[] convertToValues(
      ConfigurationRequirementFileInput requirement)
      throws AlgorithmConfigurationException {
    ConfigurationSettingFileInput[] settings = requirement.getSettings();

    FileInputGenerator[]
        fileInputGenerators =
        new FileInputGenerator[settings.length];

    for (int i = 0; i < settings.length; i++) {
      fileInputGenerators[i] = new DefaultFileInputGenerator(settings[i]);
    }

    return fileInputGenerators;
  }

  @Override
  public void triggerSetValue(Algorithm algorithm, Set<Class<?>> algorithmInterfaces)
      throws AlgorithmConfigurationException {
    if (!algorithmInterfaces.contains(FileInputParameterAlgorithm.class)) {
      throw new AlgorithmConfigurationException(
          "Algorithm does not accept file input configuration values.");
    }

    FileInputParameterAlgorithm fileInputParameterAlgorithm =
        (FileInputParameterAlgorithm) algorithm;
    fileInputParameterAlgorithm.setFileInputConfigurationValue(identifier, values);
  }
}
