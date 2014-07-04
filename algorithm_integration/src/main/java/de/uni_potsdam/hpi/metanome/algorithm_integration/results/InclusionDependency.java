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

package de.uni_potsdam.hpi.metanome.algorithm_integration.results;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;

/**
 * Represents an inclusion dependency.
 *
 * @author Jakob Zwiener
 */
public class InclusionDependency implements Result {

  public static final String IND_SEPARATOR = "[=";
  private static final long serialVersionUID = 1217247222227000634L;
  protected ColumnCombination dependant;
  protected ColumnCombination referenced;

  /**
   * Exists for GWT serialization.
   */
  protected InclusionDependency() {
    this.referenced = new ColumnCombination();
    this.dependant = new ColumnCombination();
  }

  public InclusionDependency(ColumnCombination dependant,
                             ColumnCombination referenced) {
    this.dependant = dependant;
    this.referenced = referenced;
  }

  /**
   * @return dependant
   */
  public ColumnCombination getDependant() {
    return dependant;
  }

  /**
   * @return referenced
   */
  public ColumnCombination getReferenced() {
    return referenced;
  }

  @Override
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
