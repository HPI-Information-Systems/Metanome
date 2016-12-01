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
package de.metanome.backend.result_postprocessing.result_store;

import de.metanome.backend.result_postprocessing.result_comparator.OrderDependencyResultComparator;
import de.metanome.backend.result_postprocessing.result_comparator.ResultComparator;
import de.metanome.backend.result_postprocessing.results.OrderDependencyResult;

/**
 * Stores order dependency results of one execution.
 */
public class OrderDependencyResultStore extends ResultsStore<OrderDependencyResult> {

  /**
   * Defines an order dependency result comparator
   *
   * @param sortProperty Sort property
   * @param ascending    Sort direction
   * @return Returns a new order dependency result comparator
   */
  @Override
  protected ResultComparator<OrderDependencyResult> getResultComparator(String sortProperty,
                                                                        boolean ascending) {
    return new OrderDependencyResultComparator(sortProperty, ascending);
  }

}
