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
import de.metanome.algorithm_integration.results.OrderDependency;

/**
 * Represents an order dependency result with different ranking values.
 */
@JsonTypeName("OrderDependencyResult")
public class OrderDependencyResult implements RankingResult {

  protected OrderDependency.ComparisonOperator comparisonOperator;
  protected ColumnPermutation lhs;
  protected OrderDependency.OrderType orderType;
  protected ColumnPermutation rhs;

  public OrderDependency.ComparisonOperator getComparisonOperator() {
    return comparisonOperator;
  }

  public void setComparisonOperator(OrderDependency.ComparisonOperator comparisonOperator) {
    this.comparisonOperator = comparisonOperator;
  }

  public ColumnPermutation getLhs() {
    return lhs;
  }

  public void setLhs(ColumnPermutation lhs) {
    this.lhs = lhs;
  }

  public OrderDependency.OrderType getOrderType() {
    return orderType;
  }

  public void setOrderType(OrderDependency.OrderType orderType) {
    this.orderType = orderType;
  }

  public ColumnPermutation getRhs() {
    return rhs;
  }

  public void setRhs(ColumnPermutation rhs) {
    this.rhs = rhs;
  }
}
