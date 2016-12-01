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
import de.metanome.algorithm_integration.ColumnCombination;
import de.metanome.algorithm_integration.ColumnCondition;
import de.metanome.algorithm_integration.results.ConditionalUniqueColumnCombination;
import de.metanome.backend.result_postprocessing.helper.StringHelper;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a conditional unique column combination result with different ranking values.
 */
@JsonTypeName("ConditionalUniqueColumnCombinationResult")
public class ConditionalUniqueColumnCombinationResult implements RankingResult {

  // Original result
  protected ConditionalUniqueColumnCombination result;

  // The table name of the columns
  protected String tableName = "";

  // How many columns of the table are involved
  // in the column combination of the result?
  private float columnRatio = 0.0f;

  // On how many results are the columns of the
  // column combination involved?
  private float occurrenceRatio = 0.0f;

  // How many of the columns of the column combination
  // are (almost) unique?
  private float uniquenessRatio = 0.0f;

  // Needed for serialization
  public ConditionalUniqueColumnCombinationResult() {
  }

  public ConditionalUniqueColumnCombinationResult(ConditionalUniqueColumnCombination result) {
    this.result = result;
    if (result.getColumnCombination().getColumnIdentifiers().size() > 0) {
      this.tableName = StringHelper.removeFileEnding(
        result.getColumnCombination().getColumnIdentifiers().iterator().next()
          .getTableIdentifier());
    } else {
      this.tableName = "";
    }
  }

  public ConditionalUniqueColumnCombination getResult() {
    return this.result;
  }

  public void setResult(ConditionalUniqueColumnCombination result) {
    this.result = result;
  }

  public ColumnCombination getColumnCombination() {
    return this.result.getColumnCombination();
  }

  public ColumnCondition getCondition() {
    return this.result.getCondition();
  }

  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  public float getColumnRatio() {
    return columnRatio;
  }

  public void setColumnRatio(float columnRatio) {
    this.columnRatio = columnRatio;
  }

  public float getOccurrenceRatio() {
    return occurrenceRatio;
  }

  public void setOccurrenceRatio(float occurrenceRatio) {
    this.occurrenceRatio = occurrenceRatio;
  }

  public float getUniquenessRatio() {
    return uniquenessRatio;
  }

  public void setUniquenessRatio(float uniquenessRatio) {
    this.uniquenessRatio = uniquenessRatio;
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
    ConditionalUniqueColumnCombinationResult other = (ConditionalUniqueColumnCombinationResult) obj;
    return this.result.equals(other.getResult());
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 31).
      append(this.result).
      append(this.tableName).
      toHashCode();
  }

}
