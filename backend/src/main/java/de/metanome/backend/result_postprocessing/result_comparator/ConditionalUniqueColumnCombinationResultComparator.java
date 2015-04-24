/*
 * Copyright 2015 by the Metanome project
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

import de.metanome.algorithm_integration.results.ConditionalUniqueColumnCombination;

/**
 * Defines a conditional unique column combination comparator based on a predefined sort property
 * and sort direction order.
 */
public class ConditionalUniqueColumnCombinationResultComparator
    extends ResultComparator<ConditionalUniqueColumnCombination> {

  public static final String COLUMN_COMBINATION_COLUMN = "column_combination";
  public static final String CONDITION_COLUMN = "condition";

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
  protected int compare(ConditionalUniqueColumnCombination cucc1,
                        ConditionalUniqueColumnCombination cucc2, String sortProperty) {
    if (COLUMN_COMBINATION_COLUMN.equals(sortProperty)) {
      return cucc1.getColumnCombination().toString()
          .compareTo(cucc2.getColumnCombination().toString());
    }
    if (CONDITION_COLUMN.equals(sortProperty)) {
      return cucc1.getCondition().toString().compareTo(cucc2.getCondition().toString());
    }

    return 0;
  }


}

