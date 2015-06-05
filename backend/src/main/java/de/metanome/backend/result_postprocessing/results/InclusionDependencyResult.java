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


import com.fasterxml.jackson.annotation.JsonTypeName;

import de.metanome.algorithm_integration.ColumnPermutation;

/**
 * Represents an inclusion dependency result with different ranking values.
 */
@JsonTypeName("InclusionDependencyResult")
public class InclusionDependencyResult implements RankingResult {

  // Columns of the dependant and referenced side
  protected ColumnPermutation dependant;
  protected ColumnPermutation referenced;

  // Table names of the dependant/referenced columns
  protected String dependantTableName;
  protected String referencedTableName;

  // How many columns of the table are involved
  // in the dependant/referenced side of the result?
  private float dependantColumnRatio;
  private float referencedColumnRatio;

  // On how many results are the columns of the
  // dependant/referenced side involved?
  private float dependantOccurrenceRatio;
  private float referencedOccurrenceRatio;

  // How many of the columns of the dependant/referenced
  // side are (almost) unique?
  private float dependantUniquenessRatio;
  private float referencedUniquenessRatio;


  public ColumnPermutation getDependant() {
    return dependant;
  }

  public void setDependant(ColumnPermutation dependant) {
    this.dependant = dependant;
  }

  public ColumnPermutation getReferenced() {
    return referenced;
  }

  public void setReferenced(ColumnPermutation referenced) {
    this.referenced = referenced;
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
    if (dependant == null) {
      if (other.dependant != null) {
        return false;
      }
    } else if (!dependant.equals(other.dependant)) {
      return false;
    }
    if (referenced == null) {
      if (other.referenced != null) {
        return false;
      }
    } else if (!referenced.equals(other.referenced)) {
      return false;
    }
    return true;
  }
}
