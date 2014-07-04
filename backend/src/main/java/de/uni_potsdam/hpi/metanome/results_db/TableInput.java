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
import javax.persistence.ManyToOne;

/**
 * Represents a table input in the database.
 *
 * @author Jakob Zwiener
 */
@Entity
public class TableInput extends Input {

  protected String tableName;
  protected DatabaseConnection databaseConnection;

  /**
   * Retrieves a TableInput from the database.
   *
   * @param id the TableInput's id
   * @return the tableInput
   */
  public static TableInput retrieve(long id) throws EntityStorageException {
    return (TableInput) HibernateUtil.retrieve(TableInput.class, id);
  }

  /**
   * Stores the TableInput in the database.
   *
   * @return the TableInput
   */
  public TableInput store() throws EntityStorageException {
    HibernateUtil.store(this);

    return this;
  }

  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  @ManyToOne(targetEntity = DatabaseConnection.class)
  public DatabaseConnection getDatabaseConnection() {
    return databaseConnection;
  }

  public void setDatabaseConnection(DatabaseConnection databaseConnection) {
    this.databaseConnection = databaseConnection;
  }

  @Override
  public TableInput setId(long id) {
    super.setId(id);

    return this;
  }
}
