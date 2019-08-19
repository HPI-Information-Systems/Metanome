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
import de.metanome.algorithm_integration.results.ConditionalInclusionDependency;
import de.metanome.backend.result_postprocessing.helper.StringHelper;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents an conditional inclusion dependency result with different ranking values.
 */
@JsonTypeName("ConditionalInclusionDependencyResult")
public class ConditionalInclusionDependencyResult implements RankingResult {

  // Original result
  protected ConditionalInclusionDependency result;

  // Table names of the determinant/dependent columns
  protected String dependantTableName = "";
  protected String referencedTableName = "";
  protected String patternTableau = "";
  
  // Needed for serialization
  public ConditionalInclusionDependencyResult() {
  }

  public ConditionalInclusionDependencyResult(ConditionalInclusionDependency result) {
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
    this.patternTableau = result.getPatternTableau();
  }
  
  @Override
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
    ConditionalInclusionDependencyResult other = (ConditionalInclusionDependencyResult) obj;
    return this.result.equals(other.result);
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(11, 31).
      append(this.result).
      append(this.dependantTableName).
      append(this.referencedTableName).
      append(this.patternTableau).
      toHashCode();
  }
  
  public ConditionalInclusionDependency getResult() {
    return this.result;
  }

  public void setResult(ConditionalInclusionDependency result) {
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
  
  public String getPatternTableau() {
    return patternTableau;
  }

  public void setPatternTableau(String patternTableau) {
    this.patternTableau = patternTableau;
  }
}