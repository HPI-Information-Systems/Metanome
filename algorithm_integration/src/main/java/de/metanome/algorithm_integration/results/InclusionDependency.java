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
package de.metanome.algorithm_integration.results;

import com.fasterxml.jackson.annotation.JsonTypeName;
import de.metanome.algorithm_integration.ColumnPermutation;
import de.metanome.algorithm_integration.result_receiver.ColumnNameMismatchException;
import de.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;

import javax.xml.bind.annotation.XmlTransient;
import java.util.Map;


/**
 * Represents an inclusion dependency.
 *
 * @author Jakob Zwiener
 */
@JsonTypeName("InclusionDependency")
public class InclusionDependency implements Result {

  public static final String IND_SEPARATOR = "[=";
  public static final String IND_SEPARATOR_ESC = "\\[=";

  private static final long serialVersionUID = -760072975848083178L;

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
    throws CouldNotReceiveResultException, ColumnNameMismatchException {
    resultReceiver.receiveResult(this);
  }

  @Override
  public String toString() {
    return dependant.toString() + IND_SEPARATOR + referenced.toString();
  }

  /**
   * Returns a compressed string representing this inclusion dependency.
   * @param tableMapping the table mapping
   * @param columnMapping the column mapping
   * @return the compressed string
   */
  public String toString(Map<String, String> tableMapping, Map<String, String> columnMapping) {
    return dependant.toString(tableMapping, columnMapping) + IND_SEPARATOR + referenced.toString(tableMapping, columnMapping);
  }

  /**
   * Creates a inclusion dependency from the given string using the given mappings.
   * @param tableMapping the table mapping
   * @param columnMapping the column mapping
   * @param str the string
   * @return a inclusion dependency
   */
  public static InclusionDependency fromString(Map<String, String> tableMapping, Map<String, String> columnMapping, String str)
    throws NullPointerException, IndexOutOfBoundsException {
    String[] parts = str.split(IND_SEPARATOR_ESC);
    ColumnPermutation dependant = ColumnPermutation.fromString(tableMapping, columnMapping, parts[0]);
    ColumnPermutation referenced = ColumnPermutation.fromString(tableMapping, columnMapping, parts[1]);

    return new InclusionDependency(dependant, referenced);
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
