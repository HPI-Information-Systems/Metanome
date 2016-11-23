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

import de.metanome.backend.result_postprocessing.results.OrderDependencyResult;

/**
 * Defines an order dependency comparator based on a predefined sort property and sort direction
 * order.
 */
public class OrderDependencyResultComparator
  extends ResultComparator<OrderDependencyResult> {

  public static final String LHS_COLUMN = "lhs";
  public static final String RHS_COLUMN = "rhs";
  public static final String LHS_COLUMN_RATIO = "lhs_column_ratio";
  public static final String RHS_COLUMN_RATIO = "rhs_column_ratio";
  public static final String COVERAGE = "coverage";
  public static final String LHS_OCCURRENCE_RATIO = "lhs_occurrence_ratio";
  public static final String RHS_OCCURRENCE_RATIO = "rhs_occurrence_ratio";
  public static final String LHS_UNIQUENESS_RATIO = "lhs_uniqueness_ratio";
  public static final String RHS_UNIQUENESS_RATIO = "rhs_uniqueness_ratio";

  /**
   * Creates an order dependency result comparator for given property and direction
   *
   * @param sortProperty Sort property
   * @param isAscending  Sort direction
   */
  public OrderDependencyResultComparator(String sortProperty,
                                         boolean isAscending) {
    super(sortProperty, isAscending);
  }

  /**
   * Compares two given order dependency results depending on given sort property
   *
   * @param od1          order dependency result
   * @param od2          other order dependency result
   * @param sortProperty Sort property
   * @return Returns 1 if od1 is greater than od2, 0 if both are equal, -1 otherwise
   */
  @Override
  protected int compare(OrderDependencyResult od1,
                        OrderDependencyResult od2, String sortProperty) {
    if (LHS_COLUMN.equals(sortProperty)) {
      return od1.getLhs().toString()
        .compareTo(od2.getLhs().toString());
    }
    if (RHS_COLUMN.equals(sortProperty)) {
      return od1.getRhs().toString().compareTo(od2.getRhs().toString());
    }
    if (LHS_COLUMN_RATIO.equals(sortProperty)) {
      return Float.compare(od1.getLhsColumnRatio(), od2.getLhsColumnRatio());
    }
    if (RHS_COLUMN_RATIO.equals(sortProperty)) {
      return Float.compare(od1.getRhsColumnRatio(), od2.getRhsColumnRatio());
    }
    if (COVERAGE.equals(sortProperty)) {
      return Float.compare(od1.getGeneralCoverage(), od2.getGeneralCoverage());
    }
    if (LHS_OCCURRENCE_RATIO.equals(sortProperty)) {
      return Float.compare(od1.getLhsOccurrenceRatio(), od2.getLhsOccurrenceRatio());
    }
    if (RHS_OCCURRENCE_RATIO.equals(sortProperty)) {
      return Float.compare(od1.getRhsOccurrenceRatio(), od2.getRhsOccurrenceRatio());
    }
    if (LHS_UNIQUENESS_RATIO.equals(sortProperty)) {
      return Float.compare(od1.getLhsUniquenessRatio(), od2.getLhsUniquenessRatio());
    }
    if (RHS_UNIQUENESS_RATIO.equals(sortProperty)) {
      return Float.compare(od1.getRhsUniquenessRatio(), od2.getRhsUniquenessRatio());
    }

    return 0;
  }

}

