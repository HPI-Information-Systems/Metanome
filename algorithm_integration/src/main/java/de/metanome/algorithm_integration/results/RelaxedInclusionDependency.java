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

import com.fasterxml.jackson.annotation.JsonTypeName;
import de.metanome.algorithm_integration.ColumnPermutation;
import java.util.Map;
import java.util.Objects;

@JsonTypeName("RelaxedInclusionDependency")
public class RelaxedInclusionDependency extends InclusionDependency implements Result {
    public static final String TABLEAU_SEPARATOR = "#";

    private static final long serialVersionUID = 7828486818686878686L;

    protected Double measure = 1d;

    /**
     * Exists for serialization.
     */
    protected RelaxedInclusionDependency() {
        super();
        measure = 1.0d;
    }

    public RelaxedInclusionDependency(ColumnPermutation dependant, ColumnPermutation referenced, Double measure) {
        super(dependant, referenced);
        this.measure = measure;
    }

    @Override
    public String toString() {
        return super.toString() + TABLEAU_SEPARATOR + measure;
    }

    /**
     * Encodes the relaxed inclusion dependency as string with the given mappings.
     * @param tableMapping the table mapping
     * @param columnMapping the column mapping
     * @return the string
     */
    public String toString(Map<String, String> tableMapping, Map<String, String> columnMapping) {
        return super.toString(tableMapping, columnMapping) + TABLEAU_SEPARATOR + measure;
    }

    /**
     * Creates a relaxed inclusion dependency from the given string using the given mapping.
     * @param tableMapping the table mapping
     * @param columnMapping the column mapping
     * @param str the string
     * @return a relaxed inclusion dependency
     */
    public static RelaxedInclusionDependency fromString(Map<String, String> tableMapping, Map<String, String> columnMapping, String str)
            throws NullPointerException, IndexOutOfBoundsException {
        String[] parts = str.split(IND_SEPARATOR_ESC);
        ColumnPermutation dependant = ColumnPermutation.fromString(tableMapping, columnMapping, parts[0]);
        parts = parts[1].split(TABLEAU_SEPARATOR);
        ColumnPermutation referenced = ColumnPermutation.fromString(tableMapping, columnMapping, parts[0]);
        Double measure = Double.valueOf(parts[1]);

        return new RelaxedInclusionDependency(dependant, referenced, measure);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        RelaxedInclusionDependency other = (RelaxedInclusionDependency) obj;
        if (!Objects.equals(dependant, other.dependant)) return false;
        if (!Objects.equals(referenced, other.referenced)) return false;
        return Objects.equals(measure, other.measure);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((dependant == null) ? 0 : dependant.hashCode());
        result = prime * result
                + ((referenced == null) ? 0 : referenced.hashCode());
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