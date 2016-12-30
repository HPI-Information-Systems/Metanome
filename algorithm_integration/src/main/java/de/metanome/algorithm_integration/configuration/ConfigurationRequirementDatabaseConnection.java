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
package de.metanome.algorithm_integration.configuration;

import com.fasterxml.jackson.annotation.JsonTypeName;
import de.metanome.algorithm_integration.AlgorithmConfigurationException;

import javax.xml.bind.annotation.XmlTransient;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Concrete {@link ConfigurationRequirement} for database connections.
 *
 * @author Jakob Zwiener
 * @see ConfigurationRequirement
 */
@JsonTypeName("ConfigurationRequirementDatabaseConnection")
public class ConfigurationRequirementDatabaseConnection
  extends ConfigurationRequirement<ConfigurationSettingDatabaseConnection> {

  private static final long serialVersionUID = 1540216264474337285L;

  // Needed for restful serialization
  public String type = "ConfigurationRequirementDatabaseConnection";

  private List<String> acceptedDBSystems;

  public ConfigurationRequirementDatabaseConnection() {
  }

  public ConfigurationRequirementDatabaseConnection(String identifier) {
    super(identifier);
  }

  public ConfigurationRequirementDatabaseConnection(String identifier, int numberOfSettings) {
    super(identifier, numberOfSettings);
  }

  public ConfigurationRequirementDatabaseConnection(String identifier, int minNumberOfSetting,
                                                    int maxNumberOfSetting) {
    super(identifier, minNumberOfSetting, maxNumberOfSetting);
  }

  public List<String> getAcceptedDBSystems() {
    return acceptedDBSystems;
  }

  public void setAcceptedDBSystems(List<String> acceptedDBSystems) {
    this.acceptedDBSystems = acceptedDBSystems;
  }

  /**
   * {@inheritDoc}
   */
  @XmlTransient
  @Override
  public ConfigurationValue build(ConfigurationFactory factory)
          throws AlgorithmConfigurationException, FileNotFoundException {
    return factory.build(this);
  }

}
