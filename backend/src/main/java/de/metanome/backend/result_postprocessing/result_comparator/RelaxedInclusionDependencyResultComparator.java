/**
 * Copyright 2015-2025 by Metanome Project
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

import de.metanome.backend.result_postprocessing.results.RelaxedInclusionDependencyResult;

/**
 * Defines a relaxed inclusion dependency comparator based on a predefined sort property and sort
 * direction order.
 */
public class RelaxedInclusionDependencyResultComparator extends ResultComparator<RelaxedInclusionDependencyResult> {

    public static final String DEPENDANT_COLUMN = "dependant";
    public static final String REFERENCED_COLUMN = "referenced";
    public static final String MEASURE_COLUMN = "measure";

    /**
     * Creates a relaxed inclusion dependency result comparator for given property and direction
     *
     * @param sortProperty Sort property
     * @param isAscending  Sort direction
     */
    public RelaxedInclusionDependencyResultComparator(String sortProperty, boolean isAscending) {
        super(sortProperty, isAscending);
    }

    /**
     * Compares two given relaxed inclusion dependency results depending on given sort property
     *
     * @param pid1          relaxed inclusion dependency result
     * @param pid2          other relaxed inclusion dependency result
     * @param sortProperty Sort property
     * @return Returns 1 if cid1 is greater than cid2, 0 if both are equal, -1 otherwise
     */
    @Override
    protected int compare(RelaxedInclusionDependencyResult pid1, RelaxedInclusionDependencyResult pid2, String sortProperty) {
        if (DEPENDANT_COLUMN.equals(sortProperty)) {
            return pid1.getDependant().toString().compareTo(pid2.getDependant().toString());
        }
        if (REFERENCED_COLUMN.equals(sortProperty)) {
            return pid1.getReferenced().toString().compareTo(pid2.getReferenced().toString());
        }
        if (MEASURE_COLUMN.equals(sortProperty)) {
            return pid1.getMeasure().compareTo(pid2.getMeasure());
        }
        return 0;
    }
}
