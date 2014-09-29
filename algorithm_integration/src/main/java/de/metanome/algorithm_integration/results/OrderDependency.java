/*
 * Copyright 2014 by the Metanome project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package de.metanome.algorithm_integration.results;

import de.metanome.algorithm_integration.ColumnPermutation;
import de.metanome.algorithm_integration.ComparisonOperator;
import de.metanome.algorithm_integration.OrderType;
import de.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;

/**
 * Represents an order dependency.
 *
 * @author Philipp Langer
 */
public class OrderDependency implements Result {

  private static final long serialVersionUID = 8780488672271071564L;
  public static final String OD_SEPARATOR = "~~>";
  protected ColumnPermutation lhs;
  protected ColumnPermutation rhs;
  protected OrderType orderType;
  protected ComparisonOperator comparisonOperator;

  /**
   * Exists for GWT serialization.
   */
  protected OrderDependency() {
    this.lhs = new ColumnPermutation();
    this.rhs = new ColumnPermutation();
    this.orderType = OrderType.LEXICOGRAPHICAL;
    this.comparisonOperator = ComparisonOperator.SMALLER_EQUAL;
  }
  
  public OrderDependency(ColumnPermutation lhs, ColumnPermutation rhs, OrderType orderType,
      ComparisonOperator comparisonOperator) {
    this.lhs = lhs;
    this.rhs = rhs;
    this.orderType = orderType;
    this.comparisonOperator = comparisonOperator;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((comparisonOperator == null) ? 0 : comparisonOperator.hashCode());
    result = prime * result + ((lhs == null) ? 0 : lhs.hashCode());
    result = prime * result + ((orderType == null) ? 0 : orderType.hashCode());
    result = prime * result + ((rhs == null) ? 0 : rhs.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    OrderDependency other = (OrderDependency) obj;
    if (comparisonOperator != other.comparisonOperator)
      return false;
    if (lhs == null) {
      if (other.lhs != null)
        return false;
    } else if (!lhs.equals(other.lhs))
      return false;
    if (orderType != other.orderType)
      return false;
    if (rhs == null) {
      if (other.rhs != null)
        return false;
    } else if (!rhs.equals(other.rhs))
      return false;
    return true;
  }

  public ColumnPermutation getLhs() {
    return lhs;
  }

  public void setLhs(ColumnPermutation lhs) {
    this.lhs = lhs;
  }

  public ColumnPermutation getRhs() {
    return rhs;
  }

  public void setRhs(ColumnPermutation rhs) {
    this.rhs = rhs;
  }

  public OrderType getOrderType() {
    return orderType;
  }

  public void setOrderType(OrderType orderType) {
    this.orderType = orderType;
  }

  public ComparisonOperator getComparisonOperator() {
    return comparisonOperator;
  }

  public void setComparisonOperator(ComparisonOperator comparisonOperator) {
    this.comparisonOperator = comparisonOperator;
  }

  @Override
  public void sendResultTo(OmniscientResultReceiver resultReceiver)
      throws CouldNotReceiveResultException {
    resultReceiver.receiveResult(this);
  }



}
