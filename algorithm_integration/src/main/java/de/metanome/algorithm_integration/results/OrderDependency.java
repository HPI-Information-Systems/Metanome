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


import com.fasterxml.jackson.annotation.JsonTypeName;

import de.metanome.algorithm_integration.ColumnPermutation;
import de.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;

import javax.xml.bind.annotation.XmlTransient;


/**
 * Represents an order dependency.
 *
 * @author Philipp Langer
 */
@JsonTypeName("OrderDependency")
public class OrderDependency implements Result {

  public static enum ComparisonOperator {
    SMALLER_EQUAL, STRICTLY_SMALLER
  }
  public static enum OrderType {
    LEXICOGRAPHICAL, POINTWISE
  }

  public static final String OD_SEPARATOR = "~~>";

  protected ComparisonOperator comparisonOperator;
  protected ColumnPermutation lhs;
  protected OrderType orderType;
  protected ColumnPermutation rhs;

  /**
   * Exists for serialization.
   */
  protected OrderDependency() {
    this.lhs = new ColumnPermutation();
    this.rhs = new ColumnPermutation();
    this.orderType = OrderType.LEXICOGRAPHICAL;
    this.comparisonOperator = ComparisonOperator.SMALLER_EQUAL;
  }

  public OrderDependency(final ColumnPermutation lhs, final ColumnPermutation rhs,
      final OrderType orderType, final ComparisonOperator comparisonOperator) {
    this.lhs = lhs;
    this.rhs = rhs;
    this.orderType = orderType;
    this.comparisonOperator = comparisonOperator;
  }

  public void setLhs(final ColumnPermutation lhs) {
    this.lhs = lhs;
  }

  public void setOrderType(final OrderType orderType) {
    this.orderType = orderType;
  }

  public void setRhs(final ColumnPermutation rhs) {
    this.rhs = rhs;
  }

  public void setComparisonOperator(final ComparisonOperator comparisonOperator) {
    this.comparisonOperator = comparisonOperator;
  }

  public ComparisonOperator getComparisonOperator() {
    return comparisonOperator;
  }

  public ColumnPermutation getLhs() {
    return lhs;
  }

  public OrderType getOrderType() {
    return orderType;
  }

  public ColumnPermutation getRhs() {
    return rhs;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final OrderDependency other = (OrderDependency) obj;
    if (comparisonOperator != other.comparisonOperator) {
      return false;
    }
    if (lhs == null) {
      if (other.lhs != null) {
        return false;
      }
    } else if (!lhs.equals(other.lhs)) {
      return false;
    }
    if (orderType != other.orderType) {
      return false;
    }
    if (rhs == null) {
      if (other.rhs != null) {
        return false;
      }
    } else if (!rhs.equals(other.rhs)) {
      return false;
    }
    return true;
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
  @XmlTransient
  public void sendResultTo(final OmniscientResultReceiver resultReceiver)
      throws CouldNotReceiveResultException {
    resultReceiver.receiveResult(this);
  }

  @Override
  public String toString() {
    String orderTypeStringified = "";
    String comparisonOperatorStringified = "";

    switch (orderType) {
      case LEXICOGRAPHICAL:
        orderTypeStringified = "lex";
        break;
      case POINTWISE:
        orderTypeStringified = "pnt";
        break;
      default:
    }

    switch (comparisonOperator) {
      case SMALLER_EQUAL:
        comparisonOperatorStringified = "<=";
        break;
      case STRICTLY_SMALLER:
        comparisonOperatorStringified = "< ";
        break;
      default:
    }

    return lhs + OrderDependency.OD_SEPARATOR + "[" + comparisonOperatorStringified + ","
    + orderTypeStringified + "]" + rhs;
  }

}
