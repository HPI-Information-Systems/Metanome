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

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Represents the composite key of an {@link de.uni_potsdam.hpi.metanome.results_db.Execution} in
 * the database.
 *
 * @author Jakob Zwiener
 */
public class ExecutionId implements Serializable {

  protected Algorithm algorithm;
  protected Timestamp begin;

  /**
   * Exists for hibernate serialization
   */
  private ExecutionId() {

  }

  public ExecutionId(Algorithm algorithm, Timestamp begin) {
    this.algorithm = algorithm;
    this.begin = begin;
  }

  public Algorithm getAlgorithm() {
    return algorithm;
  }

  public void setAlgorithm(Algorithm algorithm) {
    this.algorithm = algorithm;
  }

  public Timestamp getBegin() {
    return begin;
  }

  public void setBegin(Timestamp begin) {
    this.begin = begin;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ExecutionId)) {
      return false;
    }

    ExecutionId that = (ExecutionId) o;

    if (!algorithm.equals(that.algorithm)) {
      return false;
    }
    if (!begin.equals(that.begin)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = algorithm.hashCode();
    result = 31 * result + begin.hashCode();
    return result;
  }
}
