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

import de.metanome.backend.result_postprocessing.results.ConditionalInclusionDependencyResult;

/**
 * Defines a conditional inclusion dependency comparator based on a predefined sort property and sort
 * direction order.
 */
public class ConditionalInclusionDependencyResultComparator extends ResultComparator<ConditionalInclusionDependencyResult> {

  public static final String DEPENDANT_COLUMN = "dependant";
  public static final String DETERMINANT_COLUMN = "determinant";
  public static final String EXTENDED_DEPENDANT_COLUMN = "extended_dependant";

  /**
   * Creates a conditional inclusion dependency result comparator for given property and direction
   *
   * @param sortProperty Sort property
   * @param isAscending  Sort direction
   */
  public ConditionalInclusionDependencyResultComparator(String sortProperty, boolean isAscending) {
    super(sortProperty, isAscending);
  }

  /**
   * Compares two given conditional inclusion dependency results depending on given sort property
   *
   * @param cid1          conditional inclusion dependency result
   * @param cid2          other conditional inclusion dependency result
   * @param sortProperty Sort property
   * @return Returns 1 if cid1 is greater than cid2, 0 if both are equal, -1 otherwise
   */
  @Override
  protected int compare(ConditionalInclusionDependencyResult cid1, ConditionalInclusionDependencyResult cid2, String sortProperty) {
    if (DEPENDANT_COLUMN.equals(sortProperty)) {
      return cid1.getDependant().toString().compareTo(cid2.getDependant().toString());
    }
    if (DETERMINANT_COLUMN.equals(sortProperty)) {
      return cid1.getDeterminant().toString().compareTo(cid2.getDeterminant().toString());
    }
    if (EXTENDED_DEPENDANT_COLUMN.equals(sortProperty)) {
      return cid1.getExtendedDependant().toString().compareTo(cid2.getExtendedDependant().toString());
    }
    return 0;
  }
}
