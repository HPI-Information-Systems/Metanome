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

import java.util.Comparator;

/**
 * Defines an abstract comparator based on a predefined sort property and sort direction order.
 */
public abstract class ResultComparator<ResultType> implements Comparator<ResultType> {

  // Sort property
  private String sortProperty;
  // Sort direction
  private boolean isAscending;

  /**
   * Creates a result comparator for given property and direction
   *
   * @param sortProperty Sort property
   * @param isAscending  Sort direction
   */
  public ResultComparator(String sortProperty, boolean isAscending) {
    super();
    this.sortProperty = sortProperty;
    this.isAscending = isAscending;
  }

  /**
   * Compares two given objects depending on stored sort property and sort order
   *
   * @param o1 Left object
   * @param o2 Right object
   * @return Returns 1 if o1 is greater than o2, 0 if both are equal, -1 otherwise
   */
  @Override
  public int compare(ResultType o1, ResultType o2) {
    int sortOrder = this.isAscending ? 1 : -1;
    return sortOrder * compare(o1, o2, this.sortProperty);
  }

  /**
   * Compares two given objects depending on given sort property
   *
   * @param o1           Left object
   * @param o2           Right object
   * @param sortProperty Sort property
   * @return Returns 1 if o1 is greater than o2, 0 if both are equal, -1 otherwise
   */
  protected abstract int compare(ResultType o1, ResultType o2, String sortProperty);
}
