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

import de.metanome.backend.result_postprocessing.results.UniqueColumnCombinationResult;

/**
 * Defines an unique column combination comparator based on a predefined sort property and sort
 * direction order.
 */
public class UniqueColumnCombinationResultComparator
  extends ResultComparator<UniqueColumnCombinationResult> {

  public static final String COLUMN_COMBINATION_COLUMN = "column_combination";
  public static final String COLUMN_RATIO = "column_ratio";
  public static final String OCCURRENCE_RATIO = "occurrence_ratio";
  public static final String UNIQUENESS_RATIO = "uniqueness_ratio";
  public static final String RANDOMNESS = "randomness";

  /**
   * Creates an unique column combination result comparator for given property and direction
   *
   * @param sortProperty Sort property
   * @param isAscending  Sort direction
   */
  public UniqueColumnCombinationResultComparator(String sortProperty,
                                                 boolean isAscending) {
    super(sortProperty, isAscending);
  }

  /**
   * Compares two given unique column combination results depending on given sort property
   *
   * @param ucc1         unique column combination result
   * @param ucc2         other unique column combination result
   * @param sortProperty Sort property
   * @return Returns 1 if ucc1 is greater than ucc2, 0 if both are equal, -1 otherwise
   */
  @Override
  protected int compare(UniqueColumnCombinationResult ucc1,
                        UniqueColumnCombinationResult ucc2, String sortProperty) {
    if (COLUMN_COMBINATION_COLUMN.equals(sortProperty)) {
      return ucc1.getColumnCombination().toString()
        .compareTo(ucc2.getColumnCombination().toString());
    }
    if (COLUMN_RATIO.equals(sortProperty)) {
      return Float.compare(ucc1.getColumnRatio(), ucc2.getColumnRatio());
    }
    if (OCCURRENCE_RATIO.equals(sortProperty)) {
      return Float.compare(ucc1.getOccurrenceRatio(), ucc2.getOccurrenceRatio());
    }
    if (UNIQUENESS_RATIO.equals(sortProperty)) {
      return Float.compare(ucc1.getUniquenessRatio(), ucc2.getUniquenessRatio());
    }
    if (RANDOMNESS.equals(sortProperty)) {
      return Float.compare(ucc1.getRandomness(), ucc2.getRandomness());
    }

    return 0;
  }


}

