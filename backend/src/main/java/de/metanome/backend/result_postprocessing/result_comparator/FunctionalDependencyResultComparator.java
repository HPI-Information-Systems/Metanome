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

import de.metanome.algorithm_integration.results.FunctionalDependency;

/**
 * Defines an Functional Dependency comparator based on a predefined sort property and sort
 * direction order.
 */
public class FunctionalDependencyResultComparator extends ResultComparator<FunctionalDependency> {

  /**
   * Creates a Functional Dependency result comparator for given property and direction
   *
   * @param sortProperty Sort property
   * @param isAscending  Sort direction
   */
  public FunctionalDependencyResultComparator(String sortProperty, boolean isAscending) {
    super(sortProperty, isAscending);
  }

  /**
   * Compares two given Functional Dependency results depending on given sort property
   *
   * @param fd1          Left Functional Dependency result
   * @param fd2          Right Functional Dependency result
   * @param sortProperty Sort property
   * @return Returns 1 if fd1 is greater than fd2, 0 if both are equal, -1 otherwise
   */
  @Override
  protected int compare(FunctionalDependency fd1, FunctionalDependency fd2, String sortProperty) {
    if ("dependantAsString".equals(sortProperty)) {
      return fd1.getDependant().toString().compareTo(fd2.getDependant().toString());
    }
    if ("determinantAsString".equals(sortProperty)) {
      return fd1.getDeterminant().toString().compareTo(fd2.getDeterminant().toString());
    }

    return 0;
  }


}
