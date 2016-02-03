/*
 * Copyright 2016 by the Metanome project
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

package de.metanome.algorithm_integration.results;


/**
 * Represents the value of basic statistic.
 * @param <T> The type of the value.
 */
public class BasicStatisticValue<T> implements Comparable {

  private T value;

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
  public String toString() {
    return value.toString();
  }

  @Override
  public int hashCode() {
    return value.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof BasicStatisticValue) {
      return value.equals(((BasicStatisticValue) obj).getValue());
    }

    return false;
  }

  @Override
  public int compareTo(Object o) {
    if (o instanceof BasicStatisticValue) {
      BasicStatisticValue other = (BasicStatisticValue) o;

      if (other.getValue() instanceof String) {
        if (this.value instanceof String) {
          return ((String) other.getValue()).compareTo((String) this.value);
        } else {
          return -1;
        }
      }

      if (other.getValue() instanceof Integer) {
        if (this.value instanceof Integer) {
          return ((Integer) other.getValue()).compareTo((Integer) this.value);
        } else {
          return -1;
        }
      }

      if (other.getValue() instanceof Float) {
        if (this.value instanceof Float) {
          return ((Float) other.getValue()).compareTo((Float) this.value);
        } else {
          return -1;
        }
      }

      if (other.getValue() instanceof Double) {
        if (this.value instanceof Double) {
          return ((Double) other.getValue()).compareTo((Double) this.value);
        } else {
          return -1;
        }
      }

      if (other.getValue() instanceof Long) {
        if (this.value instanceof Long) {
          return ((Long) other.getValue()).compareTo((Long) this.value);
        } else {
          return -1;
        }
      }
    }
    return -1;
  }
}
