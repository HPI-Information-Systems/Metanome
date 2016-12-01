/**
 * Copyright 2016 by Metanome Project
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
package de.metanome.algorithm_integration.results.basic_statistic_values;


import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Represents a double value for a basic statistic.
 */
@JsonTypeName("BasicStatisticValueDouble")
public class BasicStatisticValueDouble extends BasicStatisticValue<Double> {

  public BasicStatisticValueDouble() {
    super();
  }

  public BasicStatisticValueDouble(Double value) {
    super(value);
  }

  @Override
  public String toString() {
    return this.value.toString();
  }

  @Override
  public int hashCode() {
    return this.value.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof BasicStatisticValueDouble && this.value.equals(((BasicStatisticValueDouble) obj).getValue());
  }

  @Override
  public int compareTo(Object o) {
    if (o instanceof BasicStatisticValueDouble) {
      BasicStatisticValueDouble other = (BasicStatisticValueDouble) o;
      return (other.getValue()).compareTo(this.value);
    }
    return -1;
  }

}
