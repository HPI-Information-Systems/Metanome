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

package de.metanome.algorithm_integration.configuration;

import com.google.common.annotations.GwtIncompatible;

import com.fasterxml.jackson.annotation.JsonTypeName;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.input.RelationalInputGeneratorInitializer;

import javax.xml.bind.annotation.XmlTransient;


/**
 * The setting of a {@link de.metanome.algorithm_integration.configuration.ConfigurationRequirementTableInput}
 *
 * @author Tanja Bergmann
 */
@JsonTypeName("ConfigurationSettingTableInput")
public class ConfigurationSettingTableInput extends ConfigurationSettingRelationalInput {

  private String table;
  private ConfigurationSettingDatabaseConnection databaseConnection;

  // Needed for restful serialization
  public String type = "ConfigurationSettingTableInput";

  /**
   * Exists for serialization.
   */
  public ConfigurationSettingTableInput() {
  }

  public ConfigurationSettingTableInput(String table,
                                        ConfigurationSettingDatabaseConnection databaseConnection) {
    this.table = table;
    this.databaseConnection = databaseConnection;
  }

  public String getTable() {
    return this.table;
  }

  public void setTable(String table) {
    this.table = table;
  }

  public ConfigurationSettingDatabaseConnection getDatabaseConnection() {
    return this.databaseConnection;
  }

  public void setDatabaseConnection(ConfigurationSettingDatabaseConnection databaseConnection) {
    this.databaseConnection = databaseConnection;
  }

  @Override
  @XmlTransient
  public String getValueAsString() {
    return this.table + "; " + this.databaseConnection.getValueAsString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @XmlTransient
  @GwtIncompatible("Can only be called from backend.")
  public void generate(RelationalInputGeneratorInitializer initializer)
      throws AlgorithmConfigurationException {
    initializer.initialize(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ConfigurationSettingTableInput that = (ConfigurationSettingTableInput) o;

    if (!table.equals(that.table)) {
      return false;
    }
    if (!databaseConnection.equals(that.databaseConnection)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = table.hashCode();
    result = 31 * result + databaseConnection.hashCode();
    return result;
  }

}
