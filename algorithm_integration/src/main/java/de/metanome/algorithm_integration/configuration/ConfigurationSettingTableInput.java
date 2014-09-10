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

import com.google.common.base.Joiner;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.input.RelationalInputGeneratorInitializer;

public class ConfigurationSettingTableInput
    implements ConfigurationSettingDataSource, ConfigurationSettingRelationalInput {


  private static final long serialVersionUID = 3242593091096735218L;

  private String table;
  private ConfigurationSettingDatabaseConnection databaseConnection;

  /**
   * Exists for GWT serialization.
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

  @Override
  public String getValueAsString() {
    return Joiner.on(';').join(this.table, this.databaseConnection.getValueAsString());
  }

  @Override
  public void generate(RelationalInputGeneratorInitializer generator)
      throws AlgorithmConfigurationException {
    generator.initialize(this);
  }
}
