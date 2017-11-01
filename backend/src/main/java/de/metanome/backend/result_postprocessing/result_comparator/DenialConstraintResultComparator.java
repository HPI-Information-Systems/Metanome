/**
 * Copyright 2017 by Metanome Project
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

import de.metanome.backend.result_postprocessing.results.DenialConstraintResult;

public class DenialConstraintResultComparator extends ResultComparator<DenialConstraintResult> {

  public static final String PREDICATES = "predicates";
  public static final String SIZE = "size";
  
  public DenialConstraintResultComparator(String sortProperty, boolean isAscending) {
    super(sortProperty, isAscending);
  }

  @Override
  protected int compare(DenialConstraintResult o1, DenialConstraintResult o2, String sortProperty) {
    switch(sortProperty) {
      case PREDICATES:
        return o1.getResult().toString().compareTo(o2.getResult().toString());
      case SIZE:
        return Integer.compare(o1.getResult().getPredicateCount(), o2.getResult().getPredicateCount());
    }
    
    return 0;
  }

}
