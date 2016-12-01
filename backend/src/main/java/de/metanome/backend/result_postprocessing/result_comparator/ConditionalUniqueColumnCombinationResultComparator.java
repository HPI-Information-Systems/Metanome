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

import de.metanome.backend.result_postprocessing.results.ConditionalUniqueColumnCombinationResult;

/**
 * Defines a conditional unique column combination comparator based on a predefined sort property
 * and sort direction order.
 */
public class ConditionalUniqueColumnCombinationResultComparator
  extends ResultComparator<ConditionalUniqueColumnCombinationResult> {

  public static final String COLUMN_COMBINATION_COLUMN = "column_combination";
  public static final String CONDITION_COLUMN = "condition";
  public static final String COVERAGE_COLUMN = "coverage";
  public static final String COLUMN_RATIO = "column_ratio";
  public static final String OCCURRENCE_RATIO = "occurrence_ratio";
  public static final String UNIQUENESS_RATIO = "uniqueness_ratio";

  /**
   * Creates a conditional unique column combination result comparator for given property and
   * direction
   *
   * @param sortProperty Sort property
   * @param isAscending  Sort direction
   */
  public ConditionalUniqueColumnCombinationResultComparator(String sortProperty,
                                                            boolean isAscending) {
    super(sortProperty, isAscending);
  }

  /**
   * Compares two given conditional unique column combination results depending on given sort
   * property
   *
   * @param cucc1        conditional unique column combination result
   * @param cucc2        other conditional unique column combination result
   * @param sortProperty Sort property
   * @return Returns 1 if cucc1 is greater than cucc2, 0 if both are equal, -1 otherwise
   */
  @Override
  protected int compare(ConditionalUniqueColumnCombinationResult cucc1,
                        ConditionalUniqueColumnCombinationResult cucc2, String sortProperty) {
    if (COLUMN_COMBINATION_COLUMN.equals(sortProperty)) {
      return cucc1.getColumnCombination().toString()
        .compareTo(cucc2.getColumnCombination().toString());
    }
    if (CONDITION_COLUMN.equals(sortProperty)) {
      return cucc1.getCondition().toString().compareTo(cucc2.getCondition().toString());
    }
    if (COVERAGE_COLUMN.equals(sortProperty)) {
      return Float.compare(cucc1.getCondition().getCoverage(), cucc2.getCondition().getCoverage());
    }
    if (COLUMN_RATIO.equals(sortProperty)) {
      return Float.compare(cucc1.getColumnRatio(), cucc2.getColumnRatio());
    }
    if (OCCURRENCE_RATIO.equals(sortProperty)) {
      return Float.compare(cucc1.getOccurrenceRatio(), cucc2.getOccurrenceRatio());
    }
    if (UNIQUENESS_RATIO.equals(sortProperty)) {
      return Float.compare(cucc1.getUniquenessRatio(), cucc2.getUniquenessRatio());
    }

    return 0;
  }


}

