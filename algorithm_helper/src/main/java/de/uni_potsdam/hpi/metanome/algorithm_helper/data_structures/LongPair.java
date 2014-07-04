/*
 * Copyright 2014 by the Metanome project
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

package de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures;

/**
 * A pair of long values.
 *
 * @author Jakob Zwiener
 */
public class LongPair implements Comparable<LongPair> {

  private long first;
  private long second;

  public LongPair(long first, long second) {
    this.first = first;
    this.second = second;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (first ^ (first >>> 32));
    result = prime * result + (int) (second ^ (second >>> 32));
    return result;
  }

  @Override
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
    LongPair other = (LongPair) obj;
    if (first != other.first) {
      return false;
    }
    return second == other.second;
  }

  public long getFirst() {
    return this.first;
  }

  public void setFirst(long first) {
    this.first = first;
  }

  public long getSecond() {
    return this.second;
  }

  public void setSecond(long second) {
    this.second = second;
  }

  @Override
  public int compareTo(LongPair other) {
    if (other == null) {
      return 1;
    }

    if (other.first == this.first) {
      return (int) (this.second - other.second);
    }
    return (int) (this.first - other.first);
  }


}
