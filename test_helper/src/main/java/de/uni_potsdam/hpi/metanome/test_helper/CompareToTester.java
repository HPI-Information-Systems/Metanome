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

package de.uni_potsdam.hpi.metanome.test_helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test helper for compareTo.
 *
 * @author Jakob Zwiener
 */
public class CompareToTester<T extends Comparable> {

  private Comparable<T> reference;

  /**
   * Constructs a CompareToTester with the reference comparable.
   *
   * @param reference the reference comparable
   */
  public CompareToTester(Comparable<T> reference) {
    this.reference = reference;
  }

  /**
   * The given comparables will be asserted to be smaller.
   *
   * @param smaller smaller comparables
   */
  public void performCompareToTestSmaller(T... smaller) {
    for (T smallerComparable : smaller) {
      assertTrue(reference.compareTo(smallerComparable) > 0);
    }

  }

  /**
   * The given comparables will be asserted not to be smaller.
   *
   * @param notSmaller not smaller comparables
   */
  public void performCompareToTestNotSmaller(T... notSmaller) {
    for (T notSmallerComparable : notSmaller) {
      assertFalse(reference.compareTo(notSmallerComparable) > 0);
    }
  }

  /**
   * The given comparables will be asserted to be equal.
   *
   * @param equal equal comparables
   */
  public void performCompareToTestEqual(T... equal) {
    for (T equalComparable : equal) {
      assertEquals(0, reference.compareTo(equalComparable));
    }
  }

  /**
   * The given comparables will be asserted not to be equal.
   *
   * @param notEqual not equal comparables
   */
  public void performComparetoTestNotEqual(T... notEqual) {
    for (T notEqualComparable : notEqual) {
      assertNotEquals(0, reference.compareTo(notEqualComparable));
    }
  }

  /**
   * The given comparables will be asserted to be greater.
   *
   * @param greater greater comparables
   */
  public void performCompareToTestGreater(T... greater) {
    for (T greaterComparable : greater) {
      assertTrue(reference.compareTo(greaterComparable) < 0);
    }
  }

  /**
   * The given comparables will be asserted not to be greater.
   *
   * @param notGreater not greater comparables
   */
  public void performCompareToTestNotGreater(T... notGreater) {
    for (T notGreaterComparable : notGreater) {
      assertFalse(reference.compareTo(notGreaterComparable) < 0);
    }
  }

}
