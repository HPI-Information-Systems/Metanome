/**
 * Copyright 2014-2016 by Metanome Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package de.metanome.algorithm_integration.results;

import com.fasterxml.jackson.annotation.JsonTypeName;
import de.metanome.algorithm_integration.MatchingCombination;
import de.metanome.algorithm_integration.MatchingIdentifier;
import de.metanome.algorithm_integration.result_receiver.ColumnNameMismatchException;
import de.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;
import java.util.Map;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Represents a matching dependency.
 *
 * @author Philipp Schirmer
 */
@JsonTypeName("MatchingDependency")
public class MatchingDependency implements Result {

  static final String MD_SEPARATOR = "->";
  static final String SUPPORT_SEPARATOR = "§";

  private static final long serialVersionUID = 7625471410289776666L;

  private MatchingCombination determinant;
  private MatchingIdentifier dependant;
  private long support;

  public MatchingDependency() {
  }

  public MatchingDependency(MatchingCombination determinant,
      MatchingIdentifier dependant, long support) {
    this.determinant = determinant;
    this.dependant = dependant;
    this.support = support;
  }

  /**
   * Creates a matching dependency from the given string using the given mapping.
   *
   * @param tableMapping the table mapping
   * @param columnMapping the column mapping
   * @param str the string
   * @return a matching dependency
   */
  public static MatchingDependency fromString(Map<String, String> tableMapping,
      Map<String, String> columnMapping, String str)
      throws NullPointerException, IndexOutOfBoundsException {
    String[] supportParts = str.split(SUPPORT_SEPARATOR);
    long support = Long.parseLong(supportParts[1]);
    String[] parts = supportParts[0].split(MD_SEPARATOR);
    MatchingCombination determinant = MatchingCombination
        .fromString(tableMapping, columnMapping, parts[0]);
    MatchingIdentifier dependant = MatchingIdentifier
        .fromString(tableMapping, columnMapping, parts[1]);

    return new MatchingDependency(determinant, dependant, support);
  }

  public long getSupport() {
    return support;
  }

  public void setSupport(long support) {
    this.support = support;
  }

  /**
   * @return determinant
   */
  public MatchingCombination getDeterminant() {
    return determinant;
  }

  public void setDeterminant(MatchingCombination determinant) {
    this.determinant = determinant;
  }

  /**
   * @return dependant
   */
  public MatchingIdentifier getDependant() {
    return dependant;
  }

  public void setDependant(MatchingIdentifier dependant) {
    this.dependant = dependant;
  }

  @Override
  @XmlTransient
  public void sendResultTo(OmniscientResultReceiver resultReceiver)
      throws CouldNotReceiveResultException, ColumnNameMismatchException {
    resultReceiver.receiveResult(this);
  }

  @Override
  public String toString() {
    return determinant.toString() + MD_SEPARATOR + dependant.toString() + SUPPORT_SEPARATOR
        + support;
  }

  /**
   * Encodes the matching dependency as string with the given mappings.
   *
   * @param tableMapping the table mapping
   * @param columnMapping the column mapping
   * @return the string
   */
  public String toString(Map<String, String> tableMapping, Map<String, String> columnMapping) {
    return determinant.toString(tableMapping, columnMapping) + MD_SEPARATOR + dependant
        .toString(tableMapping, columnMapping) + SUPPORT_SEPARATOR
        + support;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((dependant == null) ? 0 : dependant.hashCode());
    result = prime * result
        + ((determinant == null) ? 0 : determinant.hashCode());
    result = prime * result
        + Long.hashCode(support);
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
    MatchingDependency other = (MatchingDependency) obj;
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
    return support == other.support;
  }

}
