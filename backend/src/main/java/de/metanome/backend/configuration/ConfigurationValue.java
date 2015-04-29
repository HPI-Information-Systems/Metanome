/*
 * Copyright 2015 by the Metanome project
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
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;

import java.util.Set;

public abstract class ConfigurationValue<T, R extends ConfigurationRequirement> implements
                                                                                de.metanome.algorithm_integration.configuration.ConfigurationValue {

  protected final String identifier;
  protected final T[] values;

  /**
   * Constructs a ConfigurationValue using an identifier and configuration values.
   *
   * @param identifier the configuration value identifier
   * @param values     the configuration values
   */
  public ConfigurationValue(String identifier, T... values) {
    this.identifier = identifier;
    this.values = values;
  }

  /**
   * Constructs a ConfigurationValue using a {@link de.metanome.algorithm_integration.configuration.ConfigurationRequirement}.
   *
   * @param requirement the requirement to generate the ConfigurationValue
   */
  public ConfigurationValue(R requirement)
      throws AlgorithmConfigurationException {
    this.identifier = requirement.getIdentifier();
    this.values = convertToValues(requirement);
  }

  /**
   * Gets the values of a ConfigurationValue out of a {@link de.metanome.algorithm_integration.configuration.ConfigurationRequirement}
   *
   * @param requirement the configuration requirement
   * @return the values of type T
   */
  protected abstract T[] convertToValues(R requirement)
      throws AlgorithmConfigurationException;

  /**
   * {@inheritDoc}
   */
  public abstract void triggerSetValue(Algorithm algorithm, Set<Class<?>> algorithmInterfaces)
      throws AlgorithmConfigurationException;

}
