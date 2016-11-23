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


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Represents the abstract value of a basic statistic result.
 * @param <T> The type of the value.
 */
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type")
   @JsonSubTypes({
     @JsonSubTypes.Type(value = BasicStatisticValueDouble.class, name = "BasicStatisticValueDouble"),
     @JsonSubTypes.Type(value = BasicStatisticValueFloat.class, name = "BasicStatisticValueFloat"),
     @JsonSubTypes.Type(value = BasicStatisticValueInteger.class, name = "BasicStatisticValueInteger"),
     @JsonSubTypes.Type(value = BasicStatisticValueString.class, name = "BasicStatisticValueString"),
     @JsonSubTypes.Type(value = BasicStatisticValueLong.class, name = "BasicStatisticValueLong"),
     @JsonSubTypes.Type(value = BasicStatisticValueStringList.class, name = "BasicStatisticValueStringList"),
     @JsonSubTypes.Type(value = BasicStatisticValueIntegerList.class, name = "BasicStatisticValueIntegerList"),
   })
public abstract class BasicStatisticValue<T> implements Comparable<Object> {

  protected T value;

  // Exists for serialization
  public BasicStatisticValue() {}

  public BasicStatisticValue(T value) {
    this.value = value;
  }

  public T getValue() {
    return value;
  }

  public void setValue(T value) {
    this.value = value;
  }

  @Override
  public abstract String toString();

  @Override
  public abstract int hashCode();

  @Override
  public abstract boolean equals(Object obj);

  @Override
  public abstract int compareTo(Object o);

}
