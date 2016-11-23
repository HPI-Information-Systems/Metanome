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


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.xml.bind.annotation.XmlTransient;

/**
 * The setting of a {@link de.metanome.algorithm_integration.configuration.ConfigurationRequirementDatabaseConnection}
 *
 * @author Claudia Exeler
 */
@JsonTypeName("ConfigurationSettingDatabaseConnection")
public class ConfigurationSettingDatabaseConnection extends ConfigurationSettingDataSource {

  private static final long serialVersionUID = -7220041878087963L;

  private String dbUrl;
  private String username;
  private String password;
  private DbSystem system;

  // Needed for restful serialization
  public String type = "ConfigurationSettingDatabaseConnection";

  /**
   * Exists for serialization.
   */
  public ConfigurationSettingDatabaseConnection() {
  }

  public ConfigurationSettingDatabaseConnection(String dbUrl, String username, String password,
                                                DbSystem system) {
    this.dbUrl = dbUrl;
    this.username = username;
    this.password = password;
    this.system = system;
  }

  public String getDbUrl() {
    return dbUrl;
  }

  public ConfigurationSettingDatabaseConnection setDbUrl(String dbUrl) {
    this.dbUrl = dbUrl;
    return this;
  }

  public String getUsername() {
    return username;
  }

  public ConfigurationSettingDatabaseConnection setUsername(String username) {
    this.username = username;
    return this;
  }

  public String getPassword() {
    return password;
  }

  public ConfigurationSettingDatabaseConnection setPassword(String password) {
    this.password = password;
    return this;
  }

  public DbSystem getSystem() {
    return system;
  }

  public ConfigurationSettingDatabaseConnection setSystem(DbSystem system) {
    this.system = system;
    return this;
  }

  @Override
  @XmlTransient
  @JsonIgnore
  public String getValueAsString() {
    return ConfigurationSettingDatabaseConnection.getIdentifier(this.dbUrl, this.username, this.system);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ConfigurationSettingDatabaseConnection that = (ConfigurationSettingDatabaseConnection) o;

    if (!dbUrl.equals(that.dbUrl)) {
      return false;
    }
    if (!password.equals(that.password)) {
      return false;
    }
    if (system != that.system) {
      return false;
    }
    if (!username.equals(that.username)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = dbUrl.hashCode();
    result = 31 * result + username.hashCode();
    result = 31 * result + password.hashCode();
    result = 31 * result + system.hashCode();
    return result;
  }

  @XmlTransient
  @JsonIgnore
  public static String getIdentifier(String url, String username, DbSystem system) {
    return String.format("%s; %s; %s", url, username, system.name());
  }

}
