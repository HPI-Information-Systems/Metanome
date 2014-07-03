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


public class ConfigurationSettingSqlIterator extends ConfigurationSettingDataSource {

  private static final long serialVersionUID = 3242593091096735218L;

  private String dbUrl;
  private String username;
  private String password;

  /**
   * Exists for GWT serialization.
   */
  public ConfigurationSettingSqlIterator() {
  }

  public ConfigurationSettingSqlIterator(String dbUrl, String username, String password) {
    this.dbUrl = dbUrl;
    this.username = username;
    this.password = password;
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

  @Override
  public String getValueAsString() {
    return this.dbUrl + ";" + this.username + ";" + this.password;
  }
}
