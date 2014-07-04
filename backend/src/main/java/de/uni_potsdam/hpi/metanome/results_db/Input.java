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

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * Represents superclass inputs in the database.
 *
 * @author Jakob Zwiener
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Input {

  protected long id;

  /**
   * Retrieves an Input from the database.
   *
   * @param id the Input's id
   * @return the input
   */
  public static Input retrieve(long id) throws EntityStorageException {
    return (Input) HibernateUtil.retrieve(Input.class, id);
  }

  /**
   * Retrieves all inputs and subclasses stored in the database.
   *
   * @return a list of all inputs
   */
  public static List<Input> retrieveAll() throws EntityStorageException {
    return HibernateUtil.queryCriteria(Input.class);
  }

  /**
   * Stores the Input in the database.
   *
   * @return the Input
   */
  public Input store() throws EntityStorageException {
    HibernateUtil.store(this);

    return this;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Input)) {
      return false;
    }

    Input input = (Input) o;

    if (id != input.id) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return (int) (id ^ (id >>> 32));
  }


}
