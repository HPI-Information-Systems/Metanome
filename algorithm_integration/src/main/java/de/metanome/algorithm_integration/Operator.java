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

public enum Operator {
  EQUAL, UNEQUAL, GREATER, LESS, GREATER_EQUAL, LESS_EQUAL;

  private static final double EPSILON = 0.00001d;

  private Operator inverse;
  private Operator symmetric;
  private Operator[] implications;
  private Operator[] transitives;
  private String shortString;

  public Operator getInverse() {
    return inverse;
  }

  public Operator getSymmetric() {
    return symmetric;
  }

  public Operator[] getImplications() {
    return implications;
  }

  public String getShortString() {
    return shortString;
  }

  public Operator[] getTransitives() {
    return transitives;
  }

  public boolean isTransitiveWith(Operator op) {
    for (Operator i : transitives) {
      if (i == op)
        return true;
    }
    return false;
  }

  public <T> boolean eval(Comparable<T> value1, T value2) {
    if (this == EQUAL) {
      return value1.equals(value2);
    } else if (this == UNEQUAL) {
      return !value1.equals(value2);
    } else {
      int c = value1.compareTo(value2);
      switch (this) {
        case GREATER_EQUAL:
          return c >= 0;
        case LESS:
          return c < 0;
        case LESS_EQUAL:
          return c <= 0;
        case GREATER:
          return c > 0;
        default:
          break;
      }
    }

    return false;
  }

  public boolean eval(int value1, int value2) {
    switch (this) {
      case EQUAL:
        return value1 == value2;
      case GREATER:
        return value1 > value2;
      case GREATER_EQUAL:
        return value1 >= value2;
      case LESS:
        return value1 < value2;
      case LESS_EQUAL:
        return value1 <= value2;
      case UNEQUAL:
        return value1 != value2;
    }
    return false;
  }

  public boolean eval(double value1, double value2) {
    switch (this) {
      case EQUAL:
        return Math.abs(value1 - value2) < EPSILON;
      case UNEQUAL:
        return Math.abs(value1 - value2) >= EPSILON;
      case GREATER:
        return value1 > value2;
      case GREATER_EQUAL:
        return value1 >= value2;
      case LESS:
        return value1 < value2;
      case LESS_EQUAL:
        return value1 <= value2;
    }
    return false;
  }

  /**
   * a {op} b iff ! (a {op.inverse} b)
   */
  static {
    EQUAL.inverse = UNEQUAL;
    UNEQUAL.inverse = EQUAL;
    GREATER.inverse = LESS_EQUAL;
    LESS.inverse = GREATER_EQUAL;
    GREATER_EQUAL.inverse = LESS;
    LESS_EQUAL.inverse = GREATER;
  }

  /**
   * a {op} b iff b {op.symmetric} a
   */
  static {
    EQUAL.symmetric = EQUAL;
    UNEQUAL.symmetric = UNEQUAL;
    GREATER.symmetric = LESS;
    LESS.symmetric = GREATER;
    GREATER_EQUAL.symmetric = LESS_EQUAL;
    LESS_EQUAL.symmetric = GREATER_EQUAL;
  }

  /**
   * if a {op} b, then a {op.implications} b
   */
  static {
    EQUAL.implications = new Operator[] {EQUAL, GREATER_EQUAL, LESS_EQUAL};
    UNEQUAL.implications = new Operator[] {UNEQUAL};
    GREATER.implications = new Operator[] {GREATER, GREATER_EQUAL, UNEQUAL};
    LESS.implications = new Operator[] {LESS, LESS_EQUAL, UNEQUAL};
    GREATER_EQUAL.implications = new Operator[] {GREATER_EQUAL};
    LESS_EQUAL.implications = new Operator[] {LESS_EQUAL};
  }

  /**
   * if a {op} b and b {op.transitives} c, then a {op} c
   */
  static {
    EQUAL.transitives = new Operator[] {EQUAL};
    UNEQUAL.transitives = new Operator[] {EQUAL};
    GREATER.transitives = new Operator[] {GREATER, GREATER_EQUAL, EQUAL};
    LESS.transitives = new Operator[] {LESS, LESS_EQUAL, EQUAL};
    GREATER_EQUAL.transitives = new Operator[] {GREATER, GREATER_EQUAL, EQUAL};
    LESS_EQUAL.transitives = new Operator[] {LESS, LESS_EQUAL, EQUAL};
  }

  /**
   * a short string that can be used in text output
   */
  static {
    EQUAL.shortString = "==";
    UNEQUAL.shortString = "<>";
    GREATER.shortString = ">";
    LESS.shortString = "<";
    GREATER_EQUAL.shortString = ">=";
    LESS_EQUAL.shortString = "<=";
  }

}
