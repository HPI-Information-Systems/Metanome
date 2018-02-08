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
package de.metanome.backend.results_db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.annotations.GwtCompatible;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingDatabaseConnection;
import de.metanome.algorithm_integration.configuration.DbSystem;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * Represents a database connection in the database.
 *
 * @author Jakob Zwiener
 */
@Entity
@GwtCompatible
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = DatabaseConnection.class, name = "databaseConnection")
})
public class DatabaseConnection extends Input implements Serializable {

  private static final long serialVersionUID = 4924078640889259327L;

  protected String url;
  protected String username;
  protected String password;
  protected DbSystem system;
  protected String comment;

  // Exists for Serialization
  public DatabaseConnection() {
  }

  public DatabaseConnection(String name) {
    super(name);
  }

  public DatabaseConnection(String url, String username, String password, DbSystem system) {
    super(ConfigurationSettingDatabaseConnection.getIdentifier(url, username, system));

    this.url = url;
    this.username = username;
    this.password = password;
    this.system = system;
  }

  @Override
  public DatabaseConnection setId(long id) {
    super.setId(id);

    return this;
  }

  public String getUrl() {
    return url;
  }

  public DatabaseConnection setUrl(String url) {
    this.url = url;

    return this;
  }

  public String getUsername() {
    return username;
  }

  public DatabaseConnection setUsername(String username) {
    this.username = username;

    return this;
  }

  public String getPassword() {
    return password;
  }

  public DatabaseConnection setPassword(String password) {
    this.password = password;

    return this;
  }

  public DbSystem getSystem() {
    return system;
  }

  public DatabaseConnection setSystem(DbSystem system) {
    this.system = system;

    return this;
  }

  public String getComment() {
    return comment;
  }

  public DatabaseConnection setComment(String comment) {
    this.comment = comment;

    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof DatabaseConnection)) {
      return false;
    }

    DatabaseConnection that = (DatabaseConnection) o;

    return id == that.id;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 31)
            .append(super.hashCode())
            .append(url)
            .append(username)
            .append(password)
            .append(system)
            .append(comment)
            .toHashCode();
  }

  @Override
  @Transient
  @JsonIgnore
  public String getIdentifier() {
    return ConfigurationSettingDatabaseConnection.getIdentifier(this.url, this.username, this.system);
  }

}
