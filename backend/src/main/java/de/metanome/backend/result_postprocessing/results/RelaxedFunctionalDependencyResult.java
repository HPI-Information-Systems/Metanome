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
package de.metanome.backend.result_postprocessing.results;

import com.fasterxml.jackson.annotation.JsonTypeName;
import de.metanome.algorithm_integration.ColumnCombination;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.results.RelaxedFunctionalDependency;
import de.metanome.backend.result_postprocessing.helper.StringHelper;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a relaxed functional dependency result with different ranking values.
 */
@JsonTypeName("RelaxedFunctionalDependencyResult")
public class RelaxedFunctionalDependencyResult implements RankingResult {

    // Original result
    protected RelaxedFunctionalDependency result;

    // Table names of the determinant/dependent columns
    protected String determinantTableName = "";
    protected String dependantTableName = "";

    protected Double measure = 1d;

    // Needed for serialization
    public RelaxedFunctionalDependencyResult() {
    }

    public RelaxedFunctionalDependencyResult(RelaxedFunctionalDependency result) {
        this.result = result;
        if (result.getDependant() != null) {
            this.dependantTableName = StringHelper
                    .removeFileEnding(result.getDependant().getTableIdentifier());
        } else {
            this.dependantTableName = "";
        }
        if (!result.getDeterminant().getColumnIdentifiers().isEmpty()) {
            this.determinantTableName = StringHelper.removeFileEnding(
                    result.getDeterminant().getColumnIdentifiers().iterator().next().getTableIdentifier());
        } else {
            this.determinantTableName = "";
        }
        this.measure = result.getMeasure();
    }

    public RelaxedFunctionalDependency getResult() {
        return this.result;
    }

    public void setResult(RelaxedFunctionalDependency result) {
        this.result = result;
    }

    public ColumnCombination getDeterminant() {
        return this.result.getDeterminant();
    }

    public ColumnIdentifier getDependant() {
        return this.result.getDependant();
    }

    public String getDeterminantTableName() {
        return determinantTableName;
    }

    public void setDeterminantTableName(String determinantTableName) {
        this.determinantTableName = determinantTableName;
    }

    public String getDependantTableName() {
        return dependantTableName;
    }

    public void setDependantTableName(String dependantTableName) {
        this.dependantTableName = dependantTableName;
    }

    public Double getMeasure() {
        return measure;
    }

    public void setMeasure(Double measure) {
        this.measure = measure;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        RelaxedFunctionalDependencyResult other = (RelaxedFunctionalDependencyResult) obj;
        return this.result.equals(other.getResult());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 11).
                append(this.result).
                append(this.determinantTableName).
                append(this.dependantTableName).
                append(this.measure).
                toHashCode();
    }

}
