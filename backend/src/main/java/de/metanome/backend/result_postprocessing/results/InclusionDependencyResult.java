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


import de.metanome.algorithm_integration.ColumnPermutation;

public class InclusionDependencyResult {

  protected ColumnPermutation dependant;
  protected ColumnPermutation referenced;

  // id of the input, which holds the dependant/referenced columns
  protected String dependantTableName;
  protected String referencedTableName;

  // number of columns on lhs
  private float dependantSizeRatio;
  private float referencedSizeRatio;

  //average number of appearances of lhs columns in other INDs
  private float dependantColumnOccurrence;
  private float referencedColumnOccurrence;

  //number of almost unique columns in lhs
  private float dependantConstancyRatio;
  private float referencedConstancyRatio;


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

  public float getDependantSizeRatio() {
    return dependantSizeRatio;
  }

  public void setDependantSizeRatio(float dependantSizeRatio) {
    this.dependantSizeRatio = dependantSizeRatio;
  }

  public float getReferencedSizeRatio() {
    return referencedSizeRatio;
  }

  public void setReferencedSizeRatio(float referencedSizeRatio) {
    this.referencedSizeRatio = referencedSizeRatio;
  }

  public float getDependantColumnOccurrence() {
    return dependantColumnOccurrence;
  }

  public void setDependantColumnOccurrence(float dependantColumnOccurrence) {
    this.dependantColumnOccurrence = dependantColumnOccurrence;
  }

  public float getReferencedColumnOccurrence() {
    return referencedColumnOccurrence;
  }

  public void setReferencedColumnOccurrence(float referencedColumnOccurrence) {
    this.referencedColumnOccurrence = referencedColumnOccurrence;
  }

  public float getDependantUniquenessRatio() {
    return dependantConstancyRatio;
  }

  public void setDependantUniqueRatio(float dependantConstancyRatio) {
    this.dependantConstancyRatio = dependantConstancyRatio;
  }

  public float getReferencedUniquenessRatio() {
    return referencedConstancyRatio;
  }

  public void setReferencedUniqueRatio(float referencedConstancyRatio) {
    this.referencedConstancyRatio = referencedConstancyRatio;
  }
}
