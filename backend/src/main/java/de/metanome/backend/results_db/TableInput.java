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
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.List;

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
public class TableInput extends Input implements IsSerializable {

  private static final long serialVersionUID = 506811774527044153L;

  protected String tableName;
  protected DatabaseConnection databaseConnection;
  protected String comment;

  /**
   * Retrieves a TableInput from the database.
   *
   * @param id the TableInput's id
   * @return the tableInput
   */
  @GwtIncompatible("HibernateUtil is not gwt compatible.")
  public static TableInput retrieve(long id) throws EntityStorageException {
    return (TableInput) HibernateUtil.retrieve(TableInput.class, id);
  }

  /**
   * Retrieves all table inputs stored in the database.
   *
   * @return a list of all table inputs
   */
  @GwtIncompatible("HibernateUtil is not gwt compatible.")
  public static List<Input> retrieveAll() throws EntityStorageException {
    return HibernateUtil.queryCriteria(TableInput.class);
  }

  /**
   * Stores the TableInput in the database.
   *
   * @return the TableInput
   */
  @Override
  @GwtIncompatible("HibernateUtil is not gwt compatible.")
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
