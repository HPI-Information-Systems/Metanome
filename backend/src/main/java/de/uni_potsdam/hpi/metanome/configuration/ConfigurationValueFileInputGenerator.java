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

package de.uni_potsdam.hpi.metanome.configuration;

import de.uni_potsdam.hpi.metanome.algorithm_integration.Algorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.FileInputParameterAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.FileInputGenerator;

import java.util.Set;

/**
 * Represents a file input configuration value for {@link de.uni_potsdam.hpi.metanome.algorithm_integration.Algorithm}s.
 *
 * @author Jakob Zwiener
 */
public class ConfigurationValueFileInputGenerator implements ConfigurationValue {

  protected final String identifier;
  protected final FileInputGenerator[] values;

  /**
   * Constructs a ConfigurationValueFileInputGenerator using the specification's identifier and the
   * {@link de.uni_potsdam.hpi.metanome.algorithm_integration.input.FileInputGenerator} values.
   *
   * @param identifier the configuration value's identifier
   * @param values     the values to set
   */
  public ConfigurationValueFileInputGenerator(String identifier, FileInputGenerator... values) {
    this.identifier = identifier;
    this.values = values;
  }

  @Override
  public void triggerSetValue(Algorithm algorithm, Set<Class<?>> algorithmInterfaces)
      throws AlgorithmConfigurationException {
    if (!algorithmInterfaces.contains(FileInputParameterAlgorithm.class)) {
      throw new AlgorithmConfigurationException(
          "Algorithm does not accept file input configuration values.");
    }
    FileInputParameterAlgorithm
        fileInputParameterAlgorithm =
        (FileInputParameterAlgorithm) algorithm;
    fileInputParameterAlgorithm.setFileInputConfigurationValue(identifier, values);
  }
}
