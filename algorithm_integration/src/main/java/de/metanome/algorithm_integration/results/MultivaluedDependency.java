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
import de.metanome.algorithm_integration.ColumnCombination;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.result_receiver.ColumnNameMismatchException;
import de.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;

import javax.xml.bind.annotation.XmlTransient;
import java.util.Map;

/**
 * Represents a multivalued dependency.
 *
 * @author Tim Draeger
 */
@JsonTypeName("MultivaluedDependency")
public class MultivaluedDependency implements Result {

  public static final String MVD_SEPARATOR = "->>";

  //TODO
  private static final long serialVersionUID = 7625471410289776655L;

  protected ColumnCombination determinant;
  protected ColumnCombination dependant;

  public MultivaluedDependency() {
    this.dependant = new ColumnCombination();
    this.determinant = new ColumnCombination();
  }

  public MultivaluedDependency(ColumnCombination determinant,
		  					ColumnCombination dependant) {
    this.determinant = determinant;
    this.dependant = dependant;
  }

  /**
   * @return determinant
   */
  public ColumnCombination getDeterminant() {
    return determinant;
  }

  public void setDependant(ColumnCombination dependant) {
    this.dependant = dependant;
  }

  /**
   * @return dependant
   */
  public ColumnCombination getDependant() {
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
    return determinant.toString() + MVD_SEPARATOR + dependant.toString();
  }

  /**
   * Encodes the functional dependency as string with the given mappings.
   * @param tableMapping the table mapping
   * @param columnMapping the column mapping
   * @return the string
   */
  public String toString(Map<String, String> tableMapping, Map<String, String> columnMapping) {
    return determinant.toString(tableMapping, columnMapping) + MVD_SEPARATOR + dependant.toString(tableMapping, columnMapping);
  }

  /**
   * Creates a functional dependency from the given string using the given mapping.
   * @param tableMapping the table mapping
   * @param columnMapping the column mapping
   * @param str the string
   * @return a functional dependency
   */
  public static MultivaluedDependency fromString(Map<String, String> tableMapping, Map<String, String> columnMapping, String str)
    throws NullPointerException, IndexOutOfBoundsException {
    String[] parts = str.split(MVD_SEPARATOR);
    ColumnCombination determinant = ColumnCombination.fromString(tableMapping, columnMapping, parts[0]);
    ColumnCombination dependant = ColumnCombination.fromString(tableMapping, columnMapping, parts[1]);
    
    //TODO
    System.out.println("\n" + determinant.toString() + "->>" + dependant.toString() + "\n");

    return new MultivaluedDependency(determinant, dependant);
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
    MultivaluedDependency other = (MultivaluedDependency) obj;
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
