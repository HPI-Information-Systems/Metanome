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

package de.uni_potsdam.hpi.metanome.algorithm_integration.configuration;


import com.google.common.base.Joiner;

public class ConfigurationSettingSqlIterator extends ConfigurationSettingDataSource {

  private static final long serialVersionUID = 3242593091096735218L;

  private String dbUrl;
  private String username;
  private String password;
  private DbSystem system;

  /**
   * Exists for GWT serialization.
   */
  protected ConfigurationSettingSqlIterator() {
  }

  public ConfigurationSettingSqlIterator(String dbUrl, String username, String password,
                                         DbSystem system) {
    this.dbUrl = dbUrl;
    this.username = username;
    this.password = password;
    this.system = system;
  }

  public String getDbUrl() {
    return dbUrl;
  }

  public void setDbUrl(String dbUrl) {
    this.dbUrl = dbUrl;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public DbSystem getSystem() {
    return system;
  }

  public void setSystem(DbSystem system) {
    this.system = system;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ConfigurationSettingSqlIterator that = (ConfigurationSettingSqlIterator) o;

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

  @Override
  public String getValueAsString() {
    return Joiner.on(';').join(this.dbUrl, this.username, this.password, this.system);
  }
}
