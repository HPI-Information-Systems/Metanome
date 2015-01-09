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

package de.metanome.algorithm_integration.results;

import com.fasterxml.jackson.annotation.JsonTypeName;

import de.metanome.algorithm_integration.ColumnPermutation;
import de.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;

import javax.xml.bind.annotation.XmlTransient;


/**
 * Represents an inclusion dependency.
 *
 * @author Jakob Zwiener
 */
@JsonTypeName("InclusionDependency")
public class InclusionDependency implements Result {

  public static final String IND_SEPARATOR = "[=";

  protected ColumnPermutation dependant;
  protected ColumnPermutation referenced;

  /**
   * Exists for serialization.
   */
  protected InclusionDependency() {
    this.referenced = new ColumnPermutation();
    this.dependant = new ColumnPermutation();
  }

  public InclusionDependency(ColumnPermutation dependant,
		  ColumnPermutation referenced) {
    this.dependant = dependant;
    this.referenced = referenced;
  }

  /**
   * @return dependant
   */
  public ColumnPermutation getDependant() {
    return dependant;
  }

  public void setDependant(ColumnPermutation dependant) {
    this.dependant = dependant;
  }

  /**
   * @return referenced
   */
  public ColumnPermutation getReferenced() {
    return referenced;
  }

  public void setReferenced(ColumnPermutation referenced) {
    this.referenced = referenced;
  }

  @Override
  @XmlTransient
  public void sendResultTo(OmniscientResultReceiver resultReceiver)
      throws CouldNotReceiveResultException {
    resultReceiver.receiveResult(this);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();

    builder
        .append(dependant)
        .append(IND_SEPARATOR)
        .append(referenced);

    return builder.toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
             + ((dependant == null) ? 0 : dependant.hashCode());
    result = prime * result
             + ((referenced == null) ? 0 : referenced.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    InclusionDependency other = (InclusionDependency) obj;
    if (dependant == null) {
      if (other.dependant != null) {
        return false;
      }
    } else if (!dependant.equals(other.dependant)) {
      return false;
    }
    if (referenced == null) {
      if (other.referenced != null) {
        return false;
      }
    } else if (!referenced.equals(other.referenced)) {
      return false;
    }
    return true;
  }

}
