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

import de.metanome.backend.result_postprocessing.result_comparator.MultivaluedDependencyResultComparator;
import de.metanome.backend.result_postprocessing.result_comparator.ResultComparator;
import de.metanome.backend.result_postprocessing.results.MultivaluedDependencyResult;

/**
 * Stores Functional Dependency results of one execution.
 */
public class MultivaluedDependencyResultStore extends ResultsStore<MultivaluedDependencyResult> {

  /**
   * Defines an Functional Dependency result comparator
   *
   * @param sortProperty Sort property
   * @param ascending    Sort direction
   * @return Returns a new Functional Dependency result comparator
   */
  @Override
  protected ResultComparator<MultivaluedDependencyResult> getResultComparator(String sortProperty,
                                                                             boolean ascending) {
    return new MultivaluedDependencyResultComparator(sortProperty, ascending);
  }

}
