/*
 * Copyright 2015 by the Metanome project
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

import com.google.common.annotations.GwtIncompatible;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;

import de.metanome.algorithm_integration.ColumnCombination;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.results.FunctionalDependency;

import java.util.BitSet;

/**
 * Represents an functional dependency result with different ranking values.
 */
@JsonTypeName("FunctionalDependencyResult")
public class FunctionalDependencyResult implements RankingResult {

  // Original result
  protected FunctionalDependency result;

  // Table names of the determinant/dependent columns
  protected String determinantTableName;
  protected String dependantTableName;

  // Extended dependant column
  protected ColumnCombination extendedDependant;

  // How many columns of the whole result are involved
  // in the dependant/determinant side of the result?
  private float dependantColumnRatio = 0.0f;
  private float determinantColumnRatio = 0.0f;

  // How many columns of the dependant and determinant
  // table are involved in the result?
  private float generalCoverage = 0.0f;

  // Determinant/dependent columns as BitSet (needed for
  // calculating the extended dependant column)
  @JsonIgnore
  @GwtIncompatible("Gwt does not support java.util.BitSet")
  protected BitSet determinantAsBitSet;
  @JsonIgnore
  @GwtIncompatible("Gwt does not support java.util.BitSet")
  protected BitSet dependantAsBitSet;

  // Needed for serialization
  public FunctionalDependencyResult() {
  }

  public FunctionalDependencyResult(FunctionalDependency result) {
    this.result = result;
    if (result.getDependant() != null) {
      this.dependantTableName = result.getDependant().getTableIdentifier();
    } else {
      this.dependantTableName = "";
    }
    if (result.getDeterminant().getColumnIdentifiers().size() > 0) {
      this.determinantTableName =
          result.getDeterminant().getColumnIdentifiers().iterator().next().getTableIdentifier();
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

  @JsonIgnore
  @GwtIncompatible("Gwt does not support java.util.BitSet")
  public BitSet getDeterminantAsBitSet() {
    return determinantAsBitSet;
  }

  @JsonIgnore
  @GwtIncompatible("Gwt does not support java.util.BitSet")
  public void setDeterminantAsBitSet(BitSet determinantAsBitSet) {
    this.determinantAsBitSet = determinantAsBitSet;
  }

  @JsonIgnore
  @GwtIncompatible("Gwt does not support java.util.BitSet")
  public BitSet getDependantAsBitSet() {
    return dependantAsBitSet;
  }

  @JsonIgnore
  @GwtIncompatible("Gwt does not support java.util.BitSet")
  public void setDependantAsBitSet(BitSet dependantAsBitSet) {
    this.dependantAsBitSet = dependantAsBitSet;
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

}
