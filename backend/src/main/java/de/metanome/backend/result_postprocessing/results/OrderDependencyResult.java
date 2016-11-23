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
import de.metanome.algorithm_integration.results.OrderDependency;
import de.metanome.backend.result_postprocessing.helper.StringHelper;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents an order dependency result with different ranking values.
 */
@JsonTypeName("OrderDependencyResult")
public class OrderDependencyResult implements RankingResult {

  // Original result
  protected OrderDependency result;

  // Table names of the dependant/referenced columns
  protected String lhsTableName = "";
  protected String rhsTableName = "";

  // How many columns of the table are involved
  // in the lhs/rhs of the result?
  protected float lhsColumnRatio = 0.0f;
  protected float rhsColumnRatio = 0.0f;

  // How many columns of the lhs and rhs
  // table are involved in the result?
  private float generalCoverage = 0.0f;

  // On how many results are the columns of the
  // lhs/rhs side involved?
  private float lhsOccurrenceRatio = 0.0f;
  private float rhsOccurrenceRatio = 0.0f;

  // How many of the columns of the lhs/rhs
  // side are (almost) unique?
  private float lhsUniquenessRatio = 0.0f;
  private float rhsUniquenessRatio = 0.0f;


  // Needed for serialization
  public OrderDependencyResult() {
  }

  public OrderDependencyResult(OrderDependency result) {
    this.result = result;

    if (result.getLhs().getColumnIdentifiers().size() > 0) {
      this.lhsTableName = StringHelper
        .removeFileEnding(result.getLhs().getColumnIdentifiers().get(0).getTableIdentifier());
    } else {
      this.lhsTableName = "";
    }
    if (result.getRhs().getColumnIdentifiers().size() > 0) {
      this.rhsTableName = StringHelper.removeFileEnding(
        result.getRhs().getColumnIdentifiers().iterator().next().getTableIdentifier());
    } else {
      this.rhsTableName = "";
    }
  }

  public OrderDependency getResult() {
    return this.result;
  }

  public void setResult(OrderDependency result) {
    this.result = result;
  }

  public OrderDependency.ComparisonOperator getComparisonOperator() {
    return this.result.getComparisonOperator();
  }

  public ColumnPermutation getLhs() {
    return this.result.getLhs();
  }

  public OrderDependency.OrderType getOrderType() {
    return this.result.getOrderType();
  }

  public ColumnPermutation getRhs() {
    return this.result.getRhs();
  }

  public String getLhsTableName() {
    return lhsTableName;
  }

  public void setLhsTableName(String lhsTableName) {
    this.lhsTableName = lhsTableName;
  }

  public String getRhsTableName() {
    return rhsTableName;
  }

  public void setRhsTableName(String rhsTableName) {
    this.rhsTableName = rhsTableName;
  }

  public float getLhsColumnRatio() {
    return lhsColumnRatio;
  }

  public void setLhsColumnRatio(float lhsColumnRatio) {
    this.lhsColumnRatio = lhsColumnRatio;
  }

  public float getRhsColumnRatio() {
    return rhsColumnRatio;
  }

  public void setRhsColumnRatio(float rhsColumnRatio) {
    this.rhsColumnRatio = rhsColumnRatio;
  }

  public float getGeneralCoverage() {
    return generalCoverage;
  }

  public void setGeneralCoverage(float generalCoverage) {
    this.generalCoverage = generalCoverage;
  }

  public float getLhsOccurrenceRatio() {
    return lhsOccurrenceRatio;
  }

  public void setLhsOccurrenceRatio(float lhsOccurrenceRatio) {
    this.lhsOccurrenceRatio = lhsOccurrenceRatio;
  }

  public float getRhsOccurrenceRatio() {
    return rhsOccurrenceRatio;
  }

  public void setRhsOccurrenceRatio(float rhsOccurrenceRatio) {
    this.rhsOccurrenceRatio = rhsOccurrenceRatio;
  }

  public float getLhsUniquenessRatio() {
    return lhsUniquenessRatio;
  }

  public void setLhsUniquenessRatio(float lhsUniquenessRatio) {
    this.lhsUniquenessRatio = lhsUniquenessRatio;
  }

  public float getRhsUniquenessRatio() {
    return rhsUniquenessRatio;
  }

  public void setRhsUniquenessRatio(float rhsUniquenessRatio) {
    this.rhsUniquenessRatio = rhsUniquenessRatio;
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
    OrderDependencyResult other = (OrderDependencyResult) obj;
    return this.result.equals(other.getResult());
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 27).
      append(this.result).
      append(this.lhsTableName).
      append(this.rhsTableName).
      toHashCode();
  }

}
