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

import de.metanome.algorithm_integration.ColumnCombination;
import de.metanome.algorithm_integration.ColumnCondition;
import de.metanome.algorithm_integration.results.ConditionalUniqueColumnCombination;

/**
 * Represents a conditional unique column combination result with different ranking values.
 */
@JsonTypeName("ConditionalUniqueColumnCombinationResult")
public class ConditionalUniqueColumnCombinationResult implements RankingResult {

  protected ConditionalUniqueColumnCombination result;
  protected String tableName;

  // Needed for serialization
  public ConditionalUniqueColumnCombinationResult() {}

  public ConditionalUniqueColumnCombinationResult(ConditionalUniqueColumnCombination result) {
    this.result = result;
    if (result.getColumnCombination().getColumnIdentifiers().size() > 0) {
      this.tableName =
          result.getColumnCombination().getColumnIdentifiers().iterator().next()
              .getTableIdentifier();
    } else {
      this.tableName = "";
    }
  }

  public ConditionalUniqueColumnCombination getResult() {
    return this.result;
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

}
