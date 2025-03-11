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

import de.metanome.backend.result_postprocessing.results.RelaxedFunctionalDependencyResult;

/**
 * Defines a relaxed functional dependency comparator based on a predefined sort property and sort
 * direction order.
 */
public class RelaxedFunctionalDependencyResultComparator
        extends ResultComparator<RelaxedFunctionalDependencyResult> {

    public static final String DEPENDANT_COLUMN = "dependant";
    public static final String DETERMINANT_COLUMN = "determinant";
    public static final String MEASURE_COLUMN = "measure";

    /**
     * Creates a relaxed functional dependency result comparator for given property and direction
     *
     * @param sortProperty Sort property
     * @param isAscending  Sort direction
     */
    public RelaxedFunctionalDependencyResultComparator(String sortProperty, boolean isAscending) {
        super(sortProperty, isAscending);
    }

    /**
     * Compares two given relaxed functional dependency results depending on given sort property
     *
     * @param pfd1         relaxed functional dependency result
     * @param pfd2         other relaxed functional dependency result
     * @param sortProperty Sort property
     * @return Returns 1 if pfd1 is greater than pfd2, 0 if both are equal, -1 otherwise
     */
    @Override
    protected int compare(RelaxedFunctionalDependencyResult pfd1, RelaxedFunctionalDependencyResult pfd2,
                          String sortProperty) {
        if (DEPENDANT_COLUMN.equals(sortProperty)) {
            return pfd1.getDependant().toString().compareTo(pfd2.getDependant().toString());
        }
        if (DETERMINANT_COLUMN.equals(sortProperty)) {
            return pfd1.getDeterminant().toString().compareTo(pfd2.getDeterminant().toString());
        }
        if (MEASURE_COLUMN.equals(sortProperty)) {
            return pfd1.getMeasure().compareTo(pfd2.getMeasure());
        }

        return 0;
    }


}
