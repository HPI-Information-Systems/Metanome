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

package de.metanome.backend.results_db;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;

import de.metanome.algorithm_integration.configuration.DbSystem;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * Represents a database connection in the database.
 *
 * @author Jakob Zwiener
 */
@Entity
@GwtCompatible
public class DatabaseConnection implements Serializable {

  protected long id;
  protected String url;
  protected String username;
  protected String password;
  protected DbSystem system;
  protected String comment;

  @Id
  @GeneratedValue
  public long getId() {
    return id;
  }

  public DatabaseConnection setId(long id) {
    this.id = id;

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

    if (id != that.id) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return (int) (id ^ (id >>> 32));
  }

  @Transient
  public String getIdentifier() {
    return url + "; " + username + "; " + system.name();
  }

}
