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
import de.metanome.algorithm_integration.algorithm_types.RelationalInputParameterAlgorithm;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementRelationalInput;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.backend.results_db.AlgorithmType;

import java.util.Set;

/**
 * Represents {@link de.metanome.algorithm_integration.input.RelationalInputGenerator} configuration
 * values for {@link Algorithm}s.
 *
 * @author Jakob Zwiener
 */
public class ConfigurationValueRelationalInputGenerator
  extends ConfigurationValue<RelationalInputGenerator, ConfigurationRequirementRelationalInput> {

  protected ConfigurationValueRelationalInputGenerator() {
  }

  public ConfigurationValueRelationalInputGenerator(String identifier,
                                                    RelationalInputGenerator... values) {
    super(identifier, values);
  }

  @Override
  protected RelationalInputGenerator[] convertToValues(
    ConfigurationRequirementRelationalInput requirement) throws AlgorithmConfigurationException {
    return null;
  }

  @Override
  public void triggerSetValue(Algorithm algorithm, Set<Class<?>> algorithmInterfaces)
    throws AlgorithmConfigurationException {
    if (!algorithmInterfaces.contains(AlgorithmType.RELATIONAL_INPUT.getAlgorithmClass())) {
      throw new AlgorithmConfigurationException(
        "Algorithm does not accept relational input configuration values.");
    }
    RelationalInputParameterAlgorithm
      relationalInputParameterAlgorithm =
      (RelationalInputParameterAlgorithm) algorithm;
    relationalInputParameterAlgorithm.setRelationalInputConfigurationValue(identifier, values);
  }
}
