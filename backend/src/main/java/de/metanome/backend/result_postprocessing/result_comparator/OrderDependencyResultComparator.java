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

import de.metanome.algorithm_integration.results.OrderDependency;

/**
 * Defines an order dependency comparator based on a predefined sort property and sort direction
 * order.
 */
public class OrderDependencyResultComparator
    extends ResultComparator<OrderDependency> {

  public static final String LHS_COLUMN = "lhs";
  public static final String RHS_COLUMN = "rhs";

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
  protected int compare(OrderDependency od1,
                        OrderDependency od2, String sortProperty) {
    if (LHS_COLUMN.equals(sortProperty)) {
      return od1.getLhs().toString()
          .compareTo(od2.getLhs().toString());
    }
    if (RHS_COLUMN.equals(sortProperty)) {
      return od1.getRhs().toString().compareTo(od2.getRhs().toString());
    }

    return 0;
  }

}

