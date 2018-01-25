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

import com.google.common.base.Joiner;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Represents matching combinations.
 */
public class MatchingCombination implements Serializable, Comparable<Object> {

  public static final String COLUMN_CONNECTOR = ";";

  private static final long serialVersionUID = -1675606730574675390L;

  protected Set<MatchingIdentifier> matchingIdentifiers;

  /**
   * Creates an empty matching combination. Needed for serialization.
   */
  public MatchingCombination() {
    matchingIdentifiers = new TreeSet<>();
  }

  /**
   * Store matching identifiers for columns to form a matching combination.
   *
   * @param matchingIdentifiers the identifier in the MatchingCombination
   */
  public MatchingCombination(MatchingIdentifier... matchingIdentifiers) {
    this.matchingIdentifiers = new TreeSet<>(Arrays.asList(matchingIdentifiers));
  }

  /**
   * Creates a matching combination from the given string using the given mapping.
   *
   * @param tableMapping the table mapping
   * @param columnMapping the column mapping
   * @param str the string
   * @return a matching combination
   */
  public static MatchingCombination fromString(Map<String, String> tableMapping,
      Map<String, String> columnMapping, String str)
      throws NullPointerException, IndexOutOfBoundsException {
    if (str.isEmpty()) {
      return new MatchingCombination(new MatchingIdentifier[0]);
    }
    String[] parts = str.split(COLUMN_CONNECTOR);

    MatchingIdentifier[] identifiers = new MatchingIdentifier[parts.length];
    for (int i = 0; i < parts.length; i++) {
      identifiers[i] = MatchingIdentifier.fromString(tableMapping, columnMapping, parts[i].trim());
    }
    return new MatchingCombination(identifiers);
  }

  /**
   * Get matching identifiers as set.
   *
   * @return matchingIdentifiers
   */
  public Set<MatchingIdentifier> getMatchingIdentifiers() {
    return matchingIdentifiers;
  }

  public void setMatchingIdentifiers(Set<MatchingIdentifier> identifiers) {
    this.matchingIdentifiers = identifiers;
  }

  @Override
  public String toString() {
    return matchingIdentifiers.toString();
  }

  /**
   * Returns a compressed string representing this matching combination.
   *
   * @param tableMapping the table mapping
   * @param columnMapping the column mapping
   * @return the compressed string
   */
  public String toString(Map<String, String> tableMapping, Map<String, String> columnMapping)
      throws NullPointerException {
    List<String> mis = new ArrayList<>();
    for (MatchingIdentifier mi : this.matchingIdentifiers) {
      mis.add(mi.toString(tableMapping, columnMapping));
    }
    return Joiner.on(COLUMN_CONNECTOR).join(mis);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((matchingIdentifiers == null) ? 0 : matchingIdentifiers.hashCode());
    return result;
  }

  @Override
  public int compareTo(Object o) {
    if (o != null && o instanceof MatchingCombination) {
      MatchingCombination other = (MatchingCombination) o;

      int lengthComparison = this.matchingIdentifiers.size() - other.matchingIdentifiers.size();
      if (lengthComparison != 0) {
        return lengthComparison;

      } else {
        Iterator<MatchingIdentifier> otherIterator = other.matchingIdentifiers.iterator();
        int equalCount = 0;
        int negativeCount = 0;
        int positiveCount = 0;

        while (otherIterator.hasNext()) {
          MatchingIdentifier currentOther = otherIterator.next();
          // because the order of the single column values can differ,
          // you have to compare all permutations
          for (MatchingIdentifier currentThis : this.matchingIdentifiers) {
            int currentComparison = currentThis.compareTo(currentOther);
            if (currentComparison == 0) {
              equalCount++;
            } else if (currentComparison > 0) {
              positiveCount++;
            } else if (currentComparison < 0) {
              negativeCount++;
            }
          }
        }

        if (equalCount == this.matchingIdentifiers.size()) {
          return 0;
        } else if (positiveCount > negativeCount) {
          return 1;
        } else {
          return -1;
        }
      }
    } else {
      //and always last
      return 1;
    }
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
    MatchingCombination other = (MatchingCombination) obj;
    if (matchingIdentifiers == null) {
      if (other.matchingIdentifiers != null) {
        return false;
      }
    } else if (!matchingIdentifiers.equals(other.matchingIdentifiers)) {
      return false;
    }
    return true;
  }
}
