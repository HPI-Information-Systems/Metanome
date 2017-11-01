/**
 * Copyright 2017 by Metanome Project
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
package de.metanome.algorithm_integration;

import java.util.Arrays;
import java.util.Collection;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

public class PredicateConstant<T extends Comparable<T>> implements Predicate {

  private ColumnIdentifier column1;
  private int index1;

  private Operator op;

  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,
      property = "@class")
  private T constant;

  @JsonCreator
  public PredicateConstant(@JsonProperty("column1") ColumnIdentifier column1,
      @JsonProperty("index1") int index1, @JsonProperty("op") Operator op,
      @JsonProperty("constant") T constant) {
    super();
    this.column1 = column1;
    this.index1 = index1;
    this.op = op;
    this.constant = constant;
  }

  public ColumnIdentifier getColumn1() {
    return column1;
  }

  public int getIndex1() {
    return index1;
  }

  public Operator getOp() {
    return op;
  }

  public T getConstant() {
    return constant;
  }

  @Override
  public Collection<ColumnIdentifier> getColumnIdentifiers() {
    return Arrays.asList(column1);
  }


  @Override
  public String toString() {
    return "t" + index1 + "." + column1 + op.getShortString() + constant;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((column1 == null) ? 0 : column1.hashCode());
    result = prime * result + ((constant == null) ? 0 : constant.hashCode());
    result = prime * result + index1;
    result = prime * result + ((op == null) ? 0 : op.hashCode());
    return result;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    PredicateConstant other = (PredicateConstant) obj;
    if (column1 == null) {
      if (other.column1 != null)
        return false;
    } else if (!column1.equals(other.column1))
      return false;
    if (constant == null) {
      if (other.constant != null)
        return false;
    } else if (!constant.equals(other.constant))
      return false;
    if (index1 != other.index1)
      return false;
    if (op != other.op)
      return false;
    return true;
  }
}
