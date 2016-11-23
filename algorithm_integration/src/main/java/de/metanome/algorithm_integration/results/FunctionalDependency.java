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
import de.metanome.algorithm_integration.ColumnCombination;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.result_receiver.ColumnNameMismatchException;
import de.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;

import javax.xml.bind.annotation.XmlTransient;
import java.util.Map;

/**
 * Represents a functional dependency.
 *
 * @author Jakob Zwiener
 */
@JsonTypeName("FunctionalDependency")
public class FunctionalDependency implements Result {

  public static final String FD_SEPARATOR = "->";

  private static final long serialVersionUID = 7625471410289776666L;

  protected ColumnCombination determinant;
  protected ColumnIdentifier dependant;

  public FunctionalDependency() {
    this.dependant = new ColumnIdentifier();
    this.determinant = new ColumnCombination();
  }

  public FunctionalDependency(ColumnCombination determinant,
                              ColumnIdentifier dependant) {
    this.determinant = determinant;
    this.dependant = dependant;
  }

  /**
   * @return determinant
   */
  public ColumnCombination getDeterminant() {
    return determinant;
  }

  public void setDependant(ColumnIdentifier dependant) {
    this.dependant = dependant;
  }

  /**
   * @return dependant
   */
  public ColumnIdentifier getDependant() {
    return dependant;
  }

  public void setDeterminant(ColumnCombination determinant) {
    this.determinant = determinant;
  }

  @Override
  @XmlTransient
  public void sendResultTo(OmniscientResultReceiver resultReceiver)
    throws CouldNotReceiveResultException, ColumnNameMismatchException {
    resultReceiver.receiveResult(this);
  }

  @Override
  public String toString() {
    return determinant.toString() + FD_SEPARATOR + dependant.toString();
  }

  /**
   * Encodes the functional dependency as string with the given mappings.
   * @param tableMapping the table mapping
   * @param columnMapping the column mapping
   * @return the string
   */
  public String toString(Map<String, String> tableMapping, Map<String, String> columnMapping) {
    return determinant.toString(tableMapping, columnMapping) + FD_SEPARATOR + dependant.toString(tableMapping, columnMapping);
  }

  /**
   * Creates a functional dependency from the given string using the given mapping.
   * @param tableMapping the table mapping
   * @param columnMapping the column mapping
   * @param str the string
   * @return a functional dependency
   */
  public static FunctionalDependency fromString(Map<String, String> tableMapping, Map<String, String> columnMapping, String str)
    throws NullPointerException, IndexOutOfBoundsException {
    String[] parts = str.split(FD_SEPARATOR);
    ColumnCombination determinant = ColumnCombination.fromString(tableMapping, columnMapping, parts[0]);
    ColumnIdentifier dependant = ColumnIdentifier.fromString(tableMapping, columnMapping, parts[1]);

    return new FunctionalDependency(determinant, dependant);
  }


  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
      + ((dependant == null) ? 0 : dependant.hashCode());
    result = prime * result
      + ((determinant == null) ? 0 : determinant.hashCode());
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
    FunctionalDependency other = (FunctionalDependency) obj;
    if (dependant == null) {
      if (other.dependant != null) {
        return false;
      }
    } else if (!dependant.equals(other.dependant)) {
      return false;
    }
    if (determinant == null) {
      if (other.determinant != null) {
        return false;
      }
    } else if (!determinant.equals(other.determinant)) {
      return false;
    }
    return true;
  }

}
