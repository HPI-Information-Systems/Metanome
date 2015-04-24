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

package de.metanome.backend.result_postprocessing.result_store;

import de.metanome.algorithm_integration.results.BasicStatistic;
import de.metanome.backend.result_postprocessing.result_comparator.BasicStatisticResultComparator;
import de.metanome.backend.result_postprocessing.result_comparator.ResultComparator;

/**
 * Stores basic statistic results of one execution.
 */
public class BasicStatisticResultStore extends ResultsStore<BasicStatistic> {

  /**
   * Defines a basic statistic result comparator
   *
   * @param sortProperty Sort property
   * @param ascending    Sort direction
   * @return Returns a new basic statistic result comparator
   */
  @Override
  protected ResultComparator<BasicStatistic> getResultComparator(String sortProperty,
                                                                       boolean ascending) {
    return new BasicStatisticResultComparator(sortProperty, ascending);
  }

}
