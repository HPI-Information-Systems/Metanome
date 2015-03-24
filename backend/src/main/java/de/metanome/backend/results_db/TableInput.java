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

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

/**
 * Represents a table input in the database.
 *
 * @author Jakob Zwiener
 */
@Entity
@GwtCompatible
public class TableInput extends Input implements Serializable {

  protected String tableName;
  protected DatabaseConnection databaseConnection;
  protected String comment;

  // Exists for Serialization
  public TableInput() {}

  public TableInput(String name) {
    super(name);
  }

  public TableInput(String tableName, DatabaseConnection databaseConnection) {
    super(tableName + "; " + databaseConnection.getIdentifier());

    this.tableName = tableName;
    this.databaseConnection = databaseConnection;
  }

  public String getTableName() {
    return tableName;
  }

  public TableInput setTableName(String tableName) {
    this.tableName = tableName;
    return this;
  }

  @ManyToOne(targetEntity = DatabaseConnection.class)
  public DatabaseConnection getDatabaseConnection() {
    return databaseConnection;
  }

  public TableInput setDatabaseConnection(DatabaseConnection databaseConnection) {
    this.databaseConnection = databaseConnection;
    return this;
  }

  public String getComment() {
    return comment;
  }

  public TableInput setComment(String comment) {
    this.comment = comment;

    return this;
  }

  @Override
  public TableInput setId(long id) {
    super.setId(id);

    return this;
  }

  @Override
  @Transient
  public String getIdentifier() {
    return tableName + "; " + databaseConnection.getIdentifier();
  }

}
