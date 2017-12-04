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

public class PredicateVariable implements Predicate {
  private ColumnIdentifier column1;
  private int index1;

  private Operator op;

  private ColumnIdentifier column2;
  private int index2;


  @JsonCreator
  public PredicateVariable(@JsonProperty("column1") ColumnIdentifier column1,
      @JsonProperty("index1") int index1, @JsonProperty("op") Operator op,
      @JsonProperty("column2") ColumnIdentifier column2, @JsonProperty("index2") int index2) {
    super();
    this.column1 = column1;
    this.index1 = index1;
    this.op = op;
    this.column2 = column2;
    this.index2 = index2;
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

  public ColumnIdentifier getColumn2() {
    return column2;
  }

  public int getIndex2() {
    return index2;
  }

  @Override
  public Collection<ColumnIdentifier> getColumnIdentifiers() {
    return Arrays.asList(column1, column2);
  }

  @Override
  public String toString() {
    return "t" + index1 + "." + column1 + op.getShortString() + "t" + index2 + "." + column2;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((column1 == null) ? 0 : column1.hashCode());
    result = prime * result + ((column2 == null) ? 0 : column2.hashCode());
    result = prime * result + index1;
    result = prime * result + index2;
    result = prime * result + ((op == null) ? 0 : op.hashCode());
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
    PredicateVariable other = (PredicateVariable) obj;
    if (column1 == null) {
      if (other.column1 != null)
        return false;
    } else if (!column1.equals(other.column1))
      return false;
    if (column2 == null) {
      if (other.column2 != null)
        return false;
    } else if (!column2.equals(other.column2))
      return false;
    if (index1 != other.index1)
      return false;
    if (index2 != other.index2)
      return false;
    if (op != other.op)
      return false;
    return true;
  }

}
