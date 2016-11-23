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

import de.metanome.backend.result_postprocessing.results.InclusionDependencyResult;

/**
 * Defines an inclusion dependency comparator based on a predefined sort property and sort direction
 * order.
 */
public class InclusionDependencyResultComparator
  extends ResultComparator<InclusionDependencyResult> {

  public static final String DEPENDANT_COLUMN = "dependant";
  public static final String REFERENCED_COLUMN = "referenced";
  public static final String DEPENDANT_COLUMN_RATIO = "dependant_column_ratio";
  public static final String REFERENCED_COLUMN_RATIO = "referenced_column_ratio";
  public static final String DEPENDANT_OCCURRENCE_RATIO = "dependant_occurrence_ratio";
  public static final String REFERENCED_OCCURRENCE_RATIO = "referenced_occurrence_ratio";
  public static final String DEPENDANT_UNIQUENESS_RATIO = "dependant_uniqueness_ratio";
  public static final String REFERENCED_UNIQUENESS_RATIO = "referenced_uniqueness_ratio";
  public static final String COVERAGE = "coverage";

  /**
   * Creates an inclusion dependency result comparator for given property and direction
   *
   * @param sortProperty Sort property
   * @param isAscending  Sort direction
   */
  public InclusionDependencyResultComparator(String sortProperty, boolean isAscending) {
    super(sortProperty, isAscending);
  }

  /**
   * Compares two given inclusion dependency results depending on given sort property
   *
   * @param ind1         inclusion dependency result
   * @param ind2         other inclusion dependency result
   * @param sortProperty Sort property
   * @return Returns 1 if ind1 is greater than ind2, 0 if both are equal, -1 otherwise
   */
  @Override
  protected int compare(InclusionDependencyResult ind1, InclusionDependencyResult ind2,
                        String sortProperty) {
    if (DEPENDANT_COLUMN.equals(sortProperty)) {
      return ind1.getDependant().toString().compareTo(ind2.getDependant().toString());
    }
    if (REFERENCED_COLUMN.equals(sortProperty)) {
      return ind1.getReferenced().toString().compareTo(ind2.getReferenced().toString());
    }
    if (DEPENDANT_COLUMN_RATIO.equals(sortProperty)) {
      return Float.compare(ind1.getDependantColumnRatio(), ind2.getDependantColumnRatio());
    }
    if (REFERENCED_COLUMN_RATIO.equals(sortProperty)) {
      return Float.compare(ind1.getReferencedColumnRatio(), ind2.getReferencedColumnRatio());
    }
    if (DEPENDANT_OCCURRENCE_RATIO.equals(sortProperty)) {
      return Float.compare(ind1.getDependantOccurrenceRatio(), ind2.getDependantOccurrenceRatio());
    }
    if (REFERENCED_OCCURRENCE_RATIO.equals(sortProperty)) {
      return Float
        .compare(ind1.getReferencedOccurrenceRatio(), ind2.getReferencedOccurrenceRatio());
    }
    if (DEPENDANT_UNIQUENESS_RATIO.equals(sortProperty)) {
      return Float.compare(ind1.getDependantUniquenessRatio(), ind2.getDependantUniquenessRatio());
    }
    if (REFERENCED_UNIQUENESS_RATIO.equals(sortProperty)) {
      return Float
        .compare(ind1.getReferencedUniquenessRatio(), ind2.getReferencedUniquenessRatio());
    }
    if (COVERAGE.equals(sortProperty)) {
      return Float
        .compare(ind1.getGeneralCoverage(), ind2.getGeneralCoverage());
    }

    return 0;
  }


}
