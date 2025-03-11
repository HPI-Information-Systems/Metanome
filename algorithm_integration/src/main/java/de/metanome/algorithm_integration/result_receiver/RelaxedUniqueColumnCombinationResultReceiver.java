/**
 * Copyright 2014-2025 by Metanome Project
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
package de.metanome.algorithm_integration.result_receiver;

import de.metanome.algorithm_integration.algorithm_types.RelaxedUniqueColumnCombinationAlgorithm;
import de.metanome.algorithm_integration.results.RelaxedUniqueColumnCombination;

/**
 * Receives the results of a {@link RelaxedUniqueColumnCombinationAlgorithm}.
 */
public interface RelaxedUniqueColumnCombinationResultReceiver {

    /**
     * Receives a {@link RelaxedUniqueColumnCombination}
     * from a {@link RelaxedUniqueColumnCombinationAlgorithm}.
     *
     * @param relaxedUniqueColumnCombination a found {@link de.metanome.algorithm_integration.results.UniqueColumnCombination}
     * @throws CouldNotReceiveResultException if the unique column combination could not be received
     * @throws ColumnNameMismatchException if the column names of the result does not match the column names of the input
     */
    void receiveResult(RelaxedUniqueColumnCombination relaxedUniqueColumnCombination)
            throws CouldNotReceiveResultException, ColumnNameMismatchException;

    /**
     * Check if the table/column names of the given result are equal to those in the input.
     *
     * @param result the result
     * @return true, if the names are accepted, false otherwise
     */
    Boolean acceptedResult(RelaxedUniqueColumnCombination result);
}