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
 * Represents a conditional functional dependency.
 *
 * @author Maximilian Grundke
 */
@JsonTypeName("ConditionalFunctionalDependency")
public class ConditionalFunctionalDependency implements Result {

  public static final String FD_SEPARATOR = "->";
  public static final String TABLEAU_SEPARATOR = "#";

  private static final long serialVersionUID = 7625466610666776666L;

  protected ColumnCombination determinant;
  protected ColumnIdentifier dependant;
  protected String patternTableau;

  public ConditionalFunctionalDependency() {
    this.dependant = new ColumnIdentifier();
    this.determinant = new ColumnCombination();
    this.patternTableau = "";
  }

  public ConditionalFunctionalDependency(ColumnCombination determinant,
                                         ColumnIdentifier dependant,
                                         String patternTableau) {
    this.determinant = determinant;
    this.dependant = dependant;
    this.patternTableau = patternTableau;
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

  public String getPatternTableau() {
    return patternTableau;
  }

  public void setPatternTableau(String patternTableau) {
    this.patternTableau = patternTableau;
  }

  @Override
  @XmlTransient
  public void sendResultTo(OmniscientResultReceiver resultReceiver)
    throws CouldNotReceiveResultException, ColumnNameMismatchException {
    resultReceiver.receiveResult(this);
  }

  @Override
  public String toString() {
    return determinant.toString() + FD_SEPARATOR + dependant.toString() + TABLEAU_SEPARATOR + patternTableau;
  }

  /**
   * Encodes the conditional functional dependency as string with the given mappings.
   * @param tableMapping the table mapping
   * @param columnMapping the column mapping
   * @return the string
   */
  public String toString(Map<String, String> tableMapping, Map<String, String> columnMapping) {
    return determinant.toString(tableMapping, columnMapping) + FD_SEPARATOR +
            dependant.toString(tableMapping, columnMapping) + TABLEAU_SEPARATOR + patternTableau;
  }

  /**
   * Creates a conditional functional dependency from the given string using the given mapping.
   * @param tableMapping the table mapping
   * @param columnMapping the column mapping
   * @param str the string
   * @return a functional dependency
   */
  public static ConditionalFunctionalDependency fromString(Map<String, String> tableMapping, Map<String, String> columnMapping, String str)
    throws NullPointerException, IndexOutOfBoundsException {
    String[] parts = str.split(FD_SEPARATOR);
    ColumnCombination determinant = ColumnCombination.fromString(tableMapping, columnMapping, parts[0]);
    parts = parts[1].split(TABLEAU_SEPARATOR);
    ColumnIdentifier dependant = ColumnIdentifier.fromString(tableMapping, columnMapping, parts[0]);
    String patternTableau = parts[1];

    return new ConditionalFunctionalDependency(determinant, dependant, patternTableau);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ConditionalFunctionalDependency that = (ConditionalFunctionalDependency) o;

    if (determinant != null ? !determinant.equals(that.determinant) : that.determinant != null) return false;
    if (dependant != null ? !dependant.equals(that.dependant) : that.dependant != null) return false;
    return patternTableau != null ? patternTableau.equals(that.patternTableau) : that.patternTableau == null;
  }

  @Override
  public int hashCode() {
    int result = determinant != null ? determinant.hashCode() : 0;
    result = 31 * result + (dependant != null ? dependant.hashCode() : 0);
    result = 31 * result + (patternTableau != null ? patternTableau.hashCode() : 0);
    return result;
  }
}
