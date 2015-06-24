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

import de.metanome.backend.result_postprocessing.results.FunctionalDependencyResult;

/**
 * Defines an functional dependency comparator based on a predefined sort property and sort
 * direction order.
 */
public class FunctionalDependencyResultComparator
    extends ResultComparator<FunctionalDependencyResult> {

  public static final String DEPENDANT_COLUMN = "dependant";
  public static final String DETERMINANT_COLUMN = "determinant";
  public static final String EXTENDED_DEPENDANT_COLUMN = "extended_dependant";

  /**
   * Creates a functional dependency result comparator for given property and direction
   *
   * @param sortProperty Sort property
   * @param isAscending  Sort direction
   */
  public FunctionalDependencyResultComparator(String sortProperty, boolean isAscending) {
    super(sortProperty, isAscending);
  }

  /**
   * Compares two given functional dependency results depending on given sort property
   *
   * @param fd1          functional dependency result
   * @param fd2          other functional dependency result
   * @param sortProperty Sort property
   * @return Returns 1 if fd1 is greater than fd2, 0 if both are equal, -1 otherwise
   */
  @Override
  protected int compare(FunctionalDependencyResult fd1, FunctionalDependencyResult fd2,
                        String sortProperty) {
    if (DEPENDANT_COLUMN.equals(sortProperty)) {
      return fd1.getDependant().toString().compareTo(fd2.getDependant().toString());
    }
    if (DETERMINANT_COLUMN.equals(sortProperty)) {
      return fd1.getDeterminant().toString().compareTo(fd2.getDeterminant().toString());
    }
    if (EXTENDED_DEPENDANT_COLUMN.equals(sortProperty)) {
      return fd1.getExtendedDependant().toString().compareTo(fd2.getExtendedDependant().toString());
    }

    return 0;
  }


}
