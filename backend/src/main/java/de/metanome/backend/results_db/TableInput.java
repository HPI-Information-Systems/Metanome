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
import de.metanome.algorithm_integration.configuration.ConfigurationSettingTableInput;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * Represents a table input in the database.
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
  @JsonSubTypes.Type(value = TableInput.class, name = "tableInput")
})
public class TableInput extends Input implements Serializable {

  private static final long serialVersionUID = -4091112658105793585L;

  protected String tableName;
  protected DatabaseConnection databaseConnection;
  protected String comment;

  // Exists for Serialization
  public TableInput() {
  }

  public TableInput(String name) {
    super(name);
  }

  public TableInput(String tableName, DatabaseConnection databaseConnection) {
    super(ConfigurationSettingTableInput.getIdentifier(tableName, databaseConnection.getUrl(), databaseConnection.getUsername(), databaseConnection.getSystem()));

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
  @JsonIgnore
  public String getIdentifier() {
    return ConfigurationSettingTableInput.getIdentifier(this.tableName, this.databaseConnection.getUrl(), this.databaseConnection.getUsername(), this.databaseConnection.getSystem());
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 31)
            .append(super.hashCode())
            .append(tableName)
            .append(databaseConnection)
            .append(comment)
            .toHashCode();
  }
}