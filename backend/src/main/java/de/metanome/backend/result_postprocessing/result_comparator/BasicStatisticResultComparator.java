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

import de.metanome.backend.result_postprocessing.results.BasicStatisticResult;

/**
 * Defines a basic statistic comparator based on a predefined sort property and sort direction
 * order.
 */
public class BasicStatisticResultComparator
  extends ResultComparator<BasicStatisticResult> {

  public static final String COLUMN_COMBINATION_COLUMN = "column_combination";
  public static final String COLUMN_RATIO = "column_ratio";
  public static final String OCCURRENCE_RATIO = "occurrence_ratio";
  public static final String UNIQUENESS_RATIO = "uniqueness_ratio";

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
  protected int compare(BasicStatisticResult b1,
                        BasicStatisticResult b2, String sortProperty) {
    if (COLUMN_COMBINATION_COLUMN.equals(sortProperty)) {
      return b1.getColumnCombination().toString()
        .compareTo(b2.getColumnCombination().toString());
    }
    if (COLUMN_RATIO.equals(sortProperty)) {
      return Float.compare(b1.getColumnRatio(), b2.getColumnRatio());
    }
    if (OCCURRENCE_RATIO.equals(sortProperty)) {
      return Float.compare(b1.getOccurrenceRatio(), b2.getOccurrenceRatio());
    }
    if (UNIQUENESS_RATIO.equals(sortProperty)) {
      return Float.compare(b1.getUniquenessRatio(), b2.getUniquenessRatio());
    }

    if (b1.getStatisticMap().containsKey(sortProperty) && b2.getStatisticMap().containsKey(sortProperty)) {
      return b1.getStatisticMap().get(sortProperty).compareTo(b2.getStatisticMap().get(sortProperty));
    } else if (!b1.getStatisticMap().containsKey(sortProperty) && b2.getStatisticMap().containsKey(sortProperty)) {
      return 1;
    } else if ((b1.getStatisticMap().containsKey(sortProperty) && !b2.getStatisticMap().containsKey(sortProperty))) {
      return -1;
    }

    return 0;
  }


}

