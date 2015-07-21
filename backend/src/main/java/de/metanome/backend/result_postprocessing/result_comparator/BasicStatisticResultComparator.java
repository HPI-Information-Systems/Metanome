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

import de.metanome.algorithm_integration.results.BasicStatistic;

/**
 * Defines a basic statistic comparator based on a predefined sort property and sort direction
 * order.
 */
public class BasicStatisticResultComparator
    extends ResultComparator<BasicStatistic> {

  public static final String COLUMN_COMBINATION_COLUMN = "column_combination";
  public static final String VALUE_COLUMN = "value";
  public static final String NAME_COLUMN = "name";

  /**
   * Creates a basic statistic result comparator for given property and direction
   *
   * @param sortProperty Sort property
   * @param isAscending  Sort direction
   */
  public BasicStatisticResultComparator(String sortProperty,
                                        boolean isAscending) {
    super(sortProperty, isAscending);
  }

  /**
   * Compares two given basic statistic results depending on given sort property
   *
   * @param b1           basic statistic result
   * @param b2           other basic statistic result
   * @param sortProperty Sort property
   * @return Returns 1 if b1 is greater than b2, 0 if both are equal, -1 otherwise
   */
  @Override
  protected int compare(BasicStatistic b1,
                        BasicStatistic b2, String sortProperty) {
    if (COLUMN_COMBINATION_COLUMN.equals(sortProperty)) {
      return b1.getColumnCombination().toString()
          .compareTo(b2.getColumnCombination().toString());
    }
    if (VALUE_COLUMN.equals(sortProperty)) {
      return b1.getStatisticValue().toString().compareTo(b2.getStatisticValue().toString());
    }
    if (NAME_COLUMN.equals(sortProperty)) {
      return b1.getStatisticName().compareTo(b2.getStatisticName());
    }

    return 0;
  }


}

