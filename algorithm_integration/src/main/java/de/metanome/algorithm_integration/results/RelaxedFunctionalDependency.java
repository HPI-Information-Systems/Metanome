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
import de.metanome.algorithm_integration.ColumnCombination;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.result_receiver.ColumnNameMismatchException;
import de.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;

import javax.xml.bind.annotation.XmlTransient;
import java.util.Map;
import java.util.Objects;

@JsonTypeName("RelaxedFunctionalDependency")
public class RelaxedFunctionalDependency extends FunctionalDependency {

    public static final String TABLEAU_SEPARATOR = "#";
    private static final long serialVersionUID = 7625466610666776666L;
    protected Double measure;

    public RelaxedFunctionalDependency() {
        super();
        this.measure = 1d;
    }

    public RelaxedFunctionalDependency(ColumnCombination determinant,
                                       ColumnIdentifier dependant,
                                       Double measure) {
        super(determinant, dependant);
        this.measure = measure;
    }

    public Double getMeasure() {
        return measure;
    }

    public void setMeasure(Double measure) {
        this.measure = measure;
    }

    @Override
    @XmlTransient
    public void sendResultTo(OmniscientResultReceiver resultReceiver)
            throws CouldNotReceiveResultException, ColumnNameMismatchException {
        resultReceiver.receiveResult(this);
    }

    @Override
    public String toString() {
        return determinant.toString() + FD_SEPARATOR + dependant.toString() + TABLEAU_SEPARATOR + measure;
    }

    /**
     * Encodes the relaxed functional dependency as string with the given mappings.
     * @param tableMapping the table mapping
     * @param columnMapping the column mapping
     * @return the string
     */
    public String toString(Map<String, String> tableMapping, Map<String, String> columnMapping) {
        return determinant.toString(tableMapping, columnMapping) + FD_SEPARATOR +
                dependant.toString(tableMapping, columnMapping) + TABLEAU_SEPARATOR + measure;
    }

    /**
     * Creates a relaxed functional dependency from the given string using the given mapping.
     * @param tableMapping the table mapping
     * @param columnMapping the column mapping
     * @param str the string
     * @return a functional dependency
     */
    public static RelaxedFunctionalDependency fromString(Map<String, String> tableMapping, Map<String, String> columnMapping, String str)
            throws NullPointerException, IndexOutOfBoundsException {
        String[] parts = str.split(FD_SEPARATOR);
        ColumnCombination determinant = ColumnCombination.fromString(tableMapping, columnMapping, parts[0]);
        parts = parts[1].split(TABLEAU_SEPARATOR);
        ColumnIdentifier dependant = ColumnIdentifier.fromString(tableMapping, columnMapping, parts[0]);
        Double measure = Double.valueOf(parts[1]);

        return new RelaxedFunctionalDependency(determinant, dependant, measure);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RelaxedFunctionalDependency that = (RelaxedFunctionalDependency) o;

        if (!Objects.equals(determinant, that.determinant)) return false;
        if (!Objects.equals(dependant, that.dependant)) return false;
        return Objects.equals(measure, that.measure);
    }

    @Override
    public int hashCode() {
        int result = determinant != null ? determinant.hashCode() : 0;
        result = 31 * result + (dependant != null ? dependant.hashCode() : 0);
        result = 31 * result + (measure != null ? measure.hashCode() : 0);
        return result;
    }
}