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

import de.metanome.backend.result_postprocessing.results.RelaxedUniqueColumnCombinationResult;

/**
 * Defines a relaxed unique column combination comparator based on a predefined sort property
 * and sort direction order.
 */
public class RelaxedUniqueColumnCombinationResultComparator
        extends ResultComparator<RelaxedUniqueColumnCombinationResult> {

    public static final String COLUMN_COMBINATION_COLUMN = "column_combination";
    public static final String MEASURE_COLUMN = "measure";

    /**
     * Creates a relaxed unique column combination result comparator for given property and
     * direction
     *
     * @param sortProperty Sort property
     * @param isAscending  Sort direction
     */
    public RelaxedUniqueColumnCombinationResultComparator(String sortProperty,
                                                          boolean isAscending) {
        super(sortProperty, isAscending);
    }

    /**
     * Compares two given relaxed unique column combination results depending on given sort
     * property
     *
     * @param pucc1        relaxed unique column combination result
     * @param pucc2        other relaxed unique column combination result
     * @param sortProperty Sort property
     * @return Returns 1 if pucc1 is greater than pucc2, 0 if both are equal, -1 otherwise
     */
    @Override
    protected int compare(RelaxedUniqueColumnCombinationResult pucc1,
                          RelaxedUniqueColumnCombinationResult pucc2, String sortProperty) {
        if (COLUMN_COMBINATION_COLUMN.equals(sortProperty)) {
            return pucc1.getColumnCombination().toString()
                    .compareTo(pucc2.getColumnCombination().toString());
        }
        if (MEASURE_COLUMN.equals(sortProperty)) {
            return pucc1.getMeasure().compareTo(pucc2.getMeasure());
        }

        return 0;
    }


}

