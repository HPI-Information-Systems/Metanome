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

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


/**
 * A ConfigurationSettingDataSource represents those a setting, which store input data. Input data
 * can be a file input, a table input or a database connection.
 *
 * @author Claudia Exeler
 */
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = ConfigurationSettingFileInput.class, name = "ConfigurationSettingFileInput"),
  @JsonSubTypes.Type(value = ConfigurationSettingTableInput.class, name = "ConfigurationSettingTableInput"),
  @JsonSubTypes.Type(value = ConfigurationSettingDatabaseConnection.class, name = "ConfigurationSettingDatabaseConnection")
})
public abstract class ConfigurationSettingDataSource extends ConfigurationSetting {

  private static final long serialVersionUID = -52970308592095415L;

  public ConfigurationSettingDataSource() {
  }

  protected long id;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public abstract String getValueAsString();

}
