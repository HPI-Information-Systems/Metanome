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
package de.metanome.algorithm_integration;

import java.io.Serializable;
import java.util.Map;

/**
 * Represents a pair of columns connected with a similarity measure.
 */
public class MatchingIdentifier implements Comparable<MatchingIdentifier>, Serializable {

  public static final String IDENTIFIER_SEPARATOR = ",";
  public static final String SIMILARITY_SEPARATOR = "|";
  public static final String THRESHOLD_SEPARATOR = "@";

  private ColumnIdentifier left;
  private ColumnIdentifier right;
  private String similarityMeasure;
  private double threshold;

  public MatchingIdentifier() {
  }

  /**
   * @param leftIdentifier
   * @param rightIdentifier
   * @param similarityMeasure
   * @param threshold
   */
  public MatchingIdentifier(ColumnIdentifier leftIdentifier, ColumnIdentifier rightIdentifier,
      String similarityMeasure, double threshold) {
    this.left = leftIdentifier;
    this.right = rightIdentifier;
    this.similarityMeasure = similarityMeasure;
    this.threshold = threshold;
  }

  /**
   * Creates a MatchingIdentifier from the given string using the given mappings.
   *
   * @param tableMapping the table mapping
   * @param columnMapping the column mapping
   * @param str the string
   * @return a column identifier
   */
  public static MatchingIdentifier fromString(Map<String, String> tableMapping,
      Map<String, String> columnMapping, String str)
      throws NullPointerException, IndexOutOfBoundsException {
    if (str.isEmpty()) {
      throw new IllegalArgumentException(
          "Attempting to parse MatchingIdentifier from empty string");
    }

    int rightIdentifierStart = str.indexOf(IDENTIFIER_SEPARATOR) + 1;
    int simStart = str.indexOf(SIMILARITY_SEPARATOR) + 1;
    int thresholdStart = str.indexOf(THRESHOLD_SEPARATOR) + 1;
    String leftStr = str.substring(0, rightIdentifierStart);
    String rightStr = str.substring(rightIdentifierStart, simStart);
    String simStr = str.substring(simStart, thresholdStart);
    String thresholdStr = str.substring(thresholdStart);

    ColumnIdentifier leftIdentifier = ColumnIdentifier
        .fromString(tableMapping, columnMapping, leftStr);
    ColumnIdentifier rightIdentifier = ColumnIdentifier
        .fromString(tableMapping, columnMapping, rightStr);
    double threshold = Double.parseDouble(thresholdStr);
    return new MatchingIdentifier(leftIdentifier, rightIdentifier, simStr, threshold);
  }

  public ColumnIdentifier getLeft() {
    return left;
  }

  public void setLeft(ColumnIdentifier left) {
    this.left = left;
  }

  public ColumnIdentifier getRight() {
    return right;
  }

  public void setRight(ColumnIdentifier right) {
    this.right = right;
  }

  public String getSimilarityMeasure() {
    return similarityMeasure;
  }

  public void setSimilarityMeasure(String similarityMeasure) {
    this.similarityMeasure = similarityMeasure;
  }

  public double getThreshold() {
    return threshold;
  }

  public void setThreshold(double threshold) {
    this.threshold = threshold;
  }

  @Override
  public String toString() {
    return left + IDENTIFIER_SEPARATOR + right + SIMILARITY_SEPARATOR + similarityMeasure
        + THRESHOLD_SEPARATOR + threshold;
  }

  /**
   * Returns the encoded string for this matching identifier. The encoded string is determined by the
   * given mappings.
   *
   * @param tableMapping the table mapping
   * @param columnMapping the column mapping
   * @return the encoded string
   */
  public String toString(Map<String, String> tableMapping, Map<String, String> columnMapping) {
    return left.toString(tableMapping, columnMapping) + IDENTIFIER_SEPARATOR + right
        .toString(tableMapping, columnMapping) + SIMILARITY_SEPARATOR + similarityMeasure
        + THRESHOLD_SEPARATOR + threshold;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime
        * result
        + ((left == null) ? 0 : left.hashCode());
    result = prime * result
        + ((right == null) ? 0 : right.hashCode());
    result = prime * result
        + ((similarityMeasure == null) ? 0 : similarityMeasure.hashCode());
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
    MatchingIdentifier other = (MatchingIdentifier) obj;
    if (left == null) {
      if (other.left != null) {
        return false;
      }
    } else if (!left.equals(other.left)) {
      return false;
    }
    if (right == null) {
      if (other.right != null) {
        return false;
      }
    } else if (!right.equals(other.right)) {
      return false;
    }
    if (similarityMeasure == null) {
      if (other.similarityMeasure != null) {
        return false;
      }
    } else if (!similarityMeasure.equals(other.similarityMeasure)) {
      return false;
    }
    return !(threshold != other.threshold);
  }

  @Override
  public int compareTo(MatchingIdentifier other) {
    int thresholdComparison = -Double.compare(threshold, other.threshold);
    if (0 != thresholdComparison) {
      return thresholdComparison;
    } else {
      int tableIdentifierComparison;
      if (this.left == null) {
        if (other.left == null) {
          tableIdentifierComparison = 0;
        } else {
          tableIdentifierComparison = 1;
        }
      } else if (other.left == null) {
        tableIdentifierComparison = -1;
      } else {
        tableIdentifierComparison = this.left.compareTo(other.left);
      }

      if (0 != tableIdentifierComparison) {
        return tableIdentifierComparison;
      } else {
        return right.compareTo(other.right);
      }
    }
  }

}
