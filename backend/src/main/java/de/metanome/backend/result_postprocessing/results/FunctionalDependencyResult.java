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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import de.metanome.algorithm_integration.ColumnCombination;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.results.FunctionalDependency;
import de.metanome.backend.result_postprocessing.helper.StringHelper;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.BitSet;

/**
 * Represents an functional dependency result with different ranking values.
 */
@JsonTypeName("FunctionalDependencyResult")
public class FunctionalDependencyResult implements RankingResult {

  // Original result
  protected FunctionalDependency result;

  // Table names of the determinant/dependent columns
  protected String determinantTableName = "";
  protected String dependantTableName = "";

  // Extended dependant column
  protected ColumnCombination extendedDependant = new ColumnCombination();

  // How many columns of the whole result are involved
  // in the dependant/determinant side of the result?
  private float dependantColumnRatio = 0.0f;
  private float determinantColumnRatio = 0.0f;

  // How many columns of the dependant and determinant
  // table are involved in the result?
  private float generalCoverage = 0.0f;

  // On how many results are the columns of the
  // dependant/determinant side involved?
  private float dependantOccurrenceRatio = 0.0f;
  private float determinantOccurrenceRatio = 0.0f;

  // How many of the columns of the dependant/determinant
  // side are (almost) unique?
  private float dependantUniquenessRatio = 0.0f;
  private float determinantUniquenessRatio = 0.0f;

  // Describes the minimal ratio of tuples needed to change
  // for increasing the dependant of the functional dependency
  private float pollution = 0.0f;
  // Describes the column which would be added to the dependant
  // if the "polluted" tuples would be changed
  private String pollutionColumn = "";

  // Approximate number of bytes, which can be saved,
  // when using the functional dependency for normalization
  private float informationGainBytes = 0.0f;
  // Approximate number of cells, which can be saved,
  // when using the functional dependency for normalization
  private float informationGainCells = 0.0f;

  // Determinant/dependent columns as BitSet (needed for
  // calculating the extended dependant column)
  @JsonIgnore
  protected BitSet determinantAsBitSet;
  @JsonIgnore
  protected BitSet dependantAsBitSet;
  @JsonIgnore
  protected BitSet extendedDependantAsBitSet;

  // Needed for serialization
  public FunctionalDependencyResult() {
  }

  public FunctionalDependencyResult(FunctionalDependency result) {
    this.result = result;
    if (result.getDependant() != null) {
      this.dependantTableName = StringHelper
        .removeFileEnding(result.getDependant().getTableIdentifier());
    } else {
      this.dependantTableName = "";
    }
    if (result.getDeterminant().getColumnIdentifiers().size() > 0) {
      this.determinantTableName = StringHelper.removeFileEnding(
        result.getDeterminant().getColumnIdentifiers().iterator().next().getTableIdentifier());
    } else {
      this.determinantTableName = "";
    }
  }

  public FunctionalDependency getResult() {
    return this.result;
  }

  public void setResult(FunctionalDependency result) {
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

  public ColumnCombination getExtendedDependant() {
    return extendedDependant;
  }

  public void setExtendedDependant(ColumnCombination extendedDependant) {
    this.extendedDependant = extendedDependant;
  }

  public float getDependantColumnRatio() {
    return dependantColumnRatio;
  }

  public void setDependantColumnRatio(float dependantColumnRatio) {
    this.dependantColumnRatio = dependantColumnRatio;
  }

  public float getDeterminantColumnRatio() {
    return determinantColumnRatio;
  }

  public void setDeterminantColumnRatio(float determinantColumnRatio) {
    this.determinantColumnRatio = determinantColumnRatio;
  }

  public float getGeneralCoverage() {
    return generalCoverage;
  }

  public void setGeneralCoverage(float generalCoverage) {
    this.generalCoverage = generalCoverage;
  }

  public float getDeterminantOccurrenceRatio() {
    return determinantOccurrenceRatio;
  }

  public void setDeterminantOccurrenceRatio(float determinantOccurrenceRatio) {
    this.determinantOccurrenceRatio = determinantOccurrenceRatio;
  }

  public float getDependantOccurrenceRatio() {
    return dependantOccurrenceRatio;
  }

  public void setDependantOccurrenceRatio(float dependantOccurrenceRatio) {
    this.dependantOccurrenceRatio = dependantOccurrenceRatio;
  }

  public float getDependantUniquenessRatio() {
    return dependantUniquenessRatio;
  }

  public void setDependantUniquenessRatio(float dependantUniquenessRatio) {
    this.dependantUniquenessRatio = dependantUniquenessRatio;
  }

  public float getDeterminantUniquenessRatio() {
    return determinantUniquenessRatio;
  }

  public void setDeterminantUniquenessRatio(float determinantUniquenessRatio) {
    this.determinantUniquenessRatio = determinantUniquenessRatio;
  }

  public float getPollution() {
    return pollution;
  }

  public void setPollution(float pollution) {
    this.pollution = pollution;
  }

  public String getPollutionColumn() {
    return pollutionColumn;
  }

  public void setPollutionColumn(String pollutionColumn) {
    this.pollutionColumn = pollutionColumn;
  }

  public float getInformationGainBytes() {
    return informationGainBytes;
  }

  public void setInformationGainBytes(float informationGainBytes) {
    this.informationGainBytes = informationGainBytes;
  }

  public float getInformationGainCells() {
    return informationGainCells;
  }

  public void setInformationGainCells(float informationGainCells) {
    this.informationGainCells = informationGainCells;
  }

  @JsonIgnore
  public BitSet getDeterminantAsBitSet() {
    return determinantAsBitSet;
  }

  @JsonIgnore
  public void setDeterminantAsBitSet(BitSet determinantAsBitSet) {
    this.determinantAsBitSet = determinantAsBitSet;
  }

  @JsonIgnore
  public BitSet getDependantAsBitSet() {
    return dependantAsBitSet;
  }

  @JsonIgnore
  public void setDependantAsBitSet(BitSet dependantAsBitSet) {
    this.dependantAsBitSet = dependantAsBitSet;
  }

  @JsonIgnore
  public BitSet getExtendedDependantAsBitSet() {
    return extendedDependantAsBitSet;
  }

  @JsonIgnore
  public void setExtendedDependantAsBitSet(BitSet extendedDependantAsBitSet) {
    this.extendedDependantAsBitSet = extendedDependantAsBitSet;
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
    FunctionalDependencyResult other = (FunctionalDependencyResult) obj;
    return this.result.equals(other.getResult());
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 11).
      append(this.result).
      append(this.determinantTableName).
      append(this.dependantTableName).
      toHashCode();
  }

}
