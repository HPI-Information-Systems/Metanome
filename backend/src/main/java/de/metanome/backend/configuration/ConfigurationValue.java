/**
 * Copyright 2015-2016 by Metanome Project
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


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.metanome.algorithm_integration.Algorithm;
import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;

import java.io.FileNotFoundException;
import java.util.Set;

@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = ConfigurationValueBoolean.class, name = "configurationValueBoolean"),
  @JsonSubTypes.Type(value = ConfigurationValueDatabaseConnectionGenerator.class, name = "configurationValueDatabaseConnectionGenerator"),
  @JsonSubTypes.Type(value = ConfigurationValueFileInputGenerator.class, name = "configurationValueFileInputGenerator"),
  @JsonSubTypes.Type(value = ConfigurationValueInteger.class, name = "configurationValueInteger"),
  @JsonSubTypes.Type(value = ConfigurationValueListBox.class, name = "configurationValueListBox"),
  @JsonSubTypes.Type(value = ConfigurationValueCheckBox.class, name = "configurationValueCheckBox"),
  @JsonSubTypes.Type(value = ConfigurationValueRelationalInputGenerator.class, name = "configurationValueRelationalInputGenerator"),
  @JsonSubTypes.Type(value = ConfigurationValueString.class, name = "configurationValueString"),
  @JsonSubTypes.Type(value = ConfigurationValueTableInputGenerator.class, name = "configurationValueTableInputGenerator")
})
public abstract class ConfigurationValue<T, R extends ConfigurationRequirement<?>> implements
  de.metanome.algorithm_integration.configuration.ConfigurationValue {

  protected final String identifier;
  protected final T[] values;

  //Dummy constructor
  protected ConfigurationValue() {
    this.identifier = null;
    this.values = null;
  }

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
   * @throws de.metanome.algorithm_integration.AlgorithmConfigurationException if requirement could not be converted into value
   */
  public ConfigurationValue(R requirement)
    throws AlgorithmConfigurationException{
    this.identifier = requirement.getIdentifier();
    this.values = convertToValues(requirement);
  }

  /**
   * Gets the values of a ConfigurationValue out of a {@link de.metanome.algorithm_integration.configuration.ConfigurationRequirement}
   *
   * @param requirement the configuration requirement
   * @return the values of type T
   * @throws de.metanome.algorithm_integration.AlgorithmConfigurationException if requirement could not be converted into value
   */
  protected abstract T[] convertToValues(R requirement)
          throws AlgorithmConfigurationException;

  /**
   * {@inheritDoc}
   */
  public abstract void triggerSetValue(Algorithm algorithm, Set<Class<?>> algorithmInterfaces)
    throws AlgorithmConfigurationException;

  public String getIdentifier() {
    return identifier;
  }

  public T[] getValues() {
    return values;
  }
}

