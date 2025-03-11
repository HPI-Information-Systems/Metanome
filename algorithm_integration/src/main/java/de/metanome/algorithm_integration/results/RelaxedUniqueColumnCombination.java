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
package de.metanome.algorithm_integration.results;

import de.metanome.algorithm_integration.ColumnCombination;
import de.metanome.algorithm_integration.ColumnIdentifier;

import java.util.Objects;

public class RelaxedUniqueColumnCombination extends UniqueColumnCombination{
    public static final String TABLEAU_SEPARATOR = "#";

    private static final long serialVersionUID = 7828486818686878686L;

    protected Double measure = 1d;


    /**
     * Exists for serialization.
     */
    protected RelaxedUniqueColumnCombination() {
        super();
        measure = 1.0d;
    }

    public RelaxedUniqueColumnCombination(Double measure, ColumnIdentifier... columnIdentifier) {
        super(columnIdentifier);
        this.measure = measure;
    }
    public RelaxedUniqueColumnCombination(ColumnCombination columnCombination, Double measure) {
        super(columnCombination);
        this.measure = measure;
    }

    @Override
    public String toString() {
        return super.toString() + TABLEAU_SEPARATOR + measure;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        RelaxedUniqueColumnCombination other = (RelaxedUniqueColumnCombination) obj;
        if (!Objects.equals(columnCombination, other.columnCombination)) return false;
        return Objects.equals(measure, other.measure);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((columnCombination == null) ? 0 : columnCombination.hashCode());
        result = prime * result
                + ((measure == null) ? 0 : measure.hashCode());
        return result;
    }

    public Double getMeasure() {
        return measure;
    }

    public void setMeasure(Double measure) {
        this.measure = measure;
    }
}
