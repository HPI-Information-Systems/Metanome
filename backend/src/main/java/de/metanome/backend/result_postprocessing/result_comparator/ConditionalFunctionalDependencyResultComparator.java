/**
 * Copyright 2015-2016 by Metanome Project
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
package de.metanome.backend.result_postprocessing.result_comparator;

import de.metanome.backend.result_postprocessing.results.ConditionalFunctionalDependencyResult;

/**
 * Defines a conditional functional dependency comparator based on a predefined sort property and sort
 * direction order.
 */
public class ConditionalFunctionalDependencyResultComparator
  extends ResultComparator<ConditionalFunctionalDependencyResult> {

  public static final String DEPENDANT_COLUMN = "dependant";
  public static final String DETERMINANT_COLUMN = "determinant";
  public static final String PATTERN_TABLEAU_COLUMN = "tableau";

  /**
   * Creates a conditional functional dependency result comparator for given property and direction
   *
   * @param sortProperty Sort property
   * @param isAscending  Sort direction
   */
  public ConditionalFunctionalDependencyResultComparator(String sortProperty, boolean isAscending) {
    super(sortProperty, isAscending);
  }

  /**
   * Compares two given conditional functional dependency results depending on given sort property
   *
   * @param cfd1         conditional functional dependency result
   * @param cfd2         other conditional functional dependency result
   * @param sortProperty Sort property
   * @return Returns 1 if cfd1 is greater than cfd2, 0 if both are equal, -1 otherwise
   */
  @Override
  protected int compare(ConditionalFunctionalDependencyResult cfd1, ConditionalFunctionalDependencyResult cfd2,
                        String sortProperty) {
    if (DEPENDANT_COLUMN.equals(sortProperty)) {
      return cfd1.getDependant().toString().compareTo(cfd2.getDependant().toString());
    }
    if (DETERMINANT_COLUMN.equals(sortProperty)) {
      return cfd1.getDeterminant().toString().compareTo(cfd2.getDeterminant().toString());
    }
    if (PATTERN_TABLEAU_COLUMN.equals(sortProperty)) {
      return cfd1.getPatternTableau().toString().compareTo(cfd2.getPatternTableau().toString());
    }

    return 0;
  }


}
