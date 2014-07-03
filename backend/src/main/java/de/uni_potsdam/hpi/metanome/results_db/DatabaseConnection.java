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

package de.uni_potsdam.hpi.metanome.results_db;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Represents a database connection in the database.
 *
 * @author Jakob Zwiener
 */
@Entity
public class DatabaseConnection {

  protected long id;
  protected String url;
  protected String username;
  protected String password;

  /**
   * Stores a DatabaseConnection in the database.
   *
   * @param databaseConnection the DataConnection to store
   */
  public static void store(DatabaseConnection databaseConnection) throws EntityStorageException {
    HibernateUtil.store(databaseConnection);
  }

  /**
   * Retrieves a DatabaseConnection from the database.
   *
   * @param id the DatabaseConnection's id
   * @return the databaseConnection
   */
  public static DatabaseConnection retrieve(long id) throws EntityStorageException {
    return (DatabaseConnection) HibernateUtil.retrieve(DatabaseConnection.class, id);
  }

  @Id
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
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
}
