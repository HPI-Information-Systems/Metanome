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
package de.metanome.backend.result_postprocessing.results;


import com.fasterxml.jackson.annotation.JsonTypeName;
import de.metanome.algorithm_integration.ColumnPermutation;
import de.metanome.algorithm_integration.results.InclusionDependency;
import de.metanome.backend.result_postprocessing.helper.StringHelper;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents an inclusion dependency result with different ranking values.
 */
@JsonTypeName("InclusionDependencyResult")
public class InclusionDependencyResult implements RankingResult {

  // Original result
  protected InclusionDependency result;

  // Table names of the dependant/referenced columns
  protected String dependantTableName = "";
  protected String referencedTableName = "";

  // How many columns of the table are involved
  // in the dependant/referenced side of the result?
  private float dependantColumnRatio = 0.0f;
  private float referencedColumnRatio = 0.0f;

  // On how many results are the columns of the
  // dependant/referenced side involved?
  private float dependantOccurrenceRatio = 0.0f;
  private float referencedOccurrenceRatio = 0.0f;

  // How many columns of the dependant and referenced
  // table are involved in the result?
  private float generalCoverage = 0.0f;

  // How many of the columns of the dependant/referenced
  // side are (almost) unique?
  private float dependantUniquenessRatio = 0.0f;
  private float referencedUniquenessRatio = 0.0f;

  // Needed for serialization
  public InclusionDependencyResult() {
  }

  public InclusionDependencyResult(InclusionDependency result) {
    this.result = result;
    if (result.getDependant().getColumnIdentifiers().size() > 0) {
      this.dependantTableName = StringHelper.removeFileEnding(
        result.getDependant().getColumnIdentifiers().get(0).getTableIdentifier());
    } else {
      this.dependantTableName = "";
    }
    if (result.getReferenced().getColumnIdentifiers().size() > 0) {
      this.referencedTableName = StringHelper.removeFileEnding(
        result.getReferenced().getColumnIdentifiers().get(0).getTableIdentifier());
    } else {
      this.referencedTableName = "";
    }
  }

  public InclusionDependency getResult() {
    return this.result;
  }

  public void setResult(InclusionDependency result) {
    this.result = result;
  }

  public ColumnPermutation getDependant() {
    return this.result.getDependant();
  }

  public ColumnPermutation getReferenced() {
    return this.result.getReferenced();
  }

  public String getDependantTableName() {
    return dependantTableName;
  }

  public void setDependantTableName(String dependantTableName) {
    this.dependantTableName = dependantTableName;
  }

  public String getReferencedTableName() {
    return referencedTableName;
  }

  public void setReferencedTableName(String referencedTableName) {
    this.referencedTableName = referencedTableName;
  }

  public float getDependantColumnRatio() {
    return dependantColumnRatio;
  }

  public void setDependantColumnRatio(float dependantColumnRatio) {
    this.dependantColumnRatio = dependantColumnRatio;
  }

  public float getReferencedColumnRatio() {
    return referencedColumnRatio;
  }

  public void setReferencedColumnRatio(float referencedColumnRatio) {
    this.referencedColumnRatio = referencedColumnRatio;
  }

  public float getDependantOccurrenceRatio() {
    return dependantOccurrenceRatio;
  }

  public void setDependantOccurrenceRatio(float dependantOccurrenceRatio) {
    this.dependantOccurrenceRatio = dependantOccurrenceRatio;
  }

  public float getReferencedOccurrenceRatio() {
    return referencedOccurrenceRatio;
  }

  public void setReferencedOccurrenceRatio(float referencedOccurrenceRatio) {
    this.referencedOccurrenceRatio = referencedOccurrenceRatio;
  }

  public float getDependantUniquenessRatio() {
    return dependantUniquenessRatio;
  }

  public void setDependantUniquenessRatio(float dependantUniquenessRatio) {
    this.dependantUniquenessRatio = dependantUniquenessRatio;
  }

  public float getReferencedUniquenessRatio() {
    return referencedUniquenessRatio;
  }

  public void setReferencedUniquenessRatio(float referencedUniquenessRatio) {
    this.referencedUniquenessRatio = referencedUniquenessRatio;
  }

  public float getGeneralCoverage() {
    return generalCoverage;
  }

  public void setGeneralCoverage(float generalCoverage) {
    this.generalCoverage = generalCoverage;
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
    InclusionDependencyResult other = (InclusionDependencyResult) obj;
    return this.result.equals(other.result);
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(11, 31).
      append(this.result).
      append(this.dependantTableName).
      append(this.referencedTableName).
      toHashCode();
  }
}
