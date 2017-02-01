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

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents superclass inputs in the database.
 *
 * @author Jakob Zwiener
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@GwtCompatible
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = FileInput.class, name = "fileInput"),
  @JsonSubTypes.Type(value = TableInput.class, name = "tableInput"),
  @JsonSubTypes.Type(value = DatabaseConnection.class, name = "databaseConnection")
})

public class Input implements Serializable {

  private static final long serialVersionUID = -7086702450298405009L;

  protected long id;
  protected String name;
  protected List<Execution> executions = new ArrayList<>();

  // Exists for Serialization
  public Input() {
  }

  public Input(String name) {
    this.name = name;
  }

  @Id
  @GeneratedValue
  public long getId() {
    return id;
  }

  public Input setId(long id) {
    this.id = id;

    return this;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @XmlTransient
  @JsonIgnore
  @ManyToMany(fetch = FetchType.EAGER, mappedBy = "inputs", cascade = CascadeType.ALL)
  public List<Execution> getExecutions() {
    return executions;
  }

  @XmlTransient
  @JsonIgnore
  public void setExecutions(List<Execution> executions) {
    this.executions = executions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Input)) {
      return false;
    }

    Input input = (Input) o;

    return id == input.id;

  }

  @Override
  public int hashCode() {
    return (int) (id ^ (id >>> 32));
  }

  @Transient
  public String getIdentifier() {
    return String.valueOf(id);
  }
}
