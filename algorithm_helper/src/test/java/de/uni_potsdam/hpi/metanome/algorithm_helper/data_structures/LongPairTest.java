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

import de.uni_potsdam.hpi.metanome.test_helper.CompareToTester;
import de.uni_potsdam.hpi.metanome.test_helper.EqualsAndHashCodeTester;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures.LongPair}
 *
 * @author Jakob Zwiener
 */
public class LongPairTest {

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures.LongPair#equals(Object)}
   * and {@link de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures.LongPair#hashCode()}
   */
  @Test
  public void testEqualsHashCode() {
    // Setup
    // Expected values
    LongPair longPair = new LongPair(1, 2);
    LongPair equalLongPair = new LongPair(1, 2);
    LongPair notEqualLongPair1 = new LongPair(2, 2);
    LongPair notEqualLongPair2 = new LongPair(1, 3);

    // Execute functionality
    // Check result
    new EqualsAndHashCodeTester<LongPair>()
        .performBasicEqualsAndHashCodeChecks(longPair, equalLongPair, notEqualLongPair1,
                                             notEqualLongPair2);
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures.LongPair#compareTo(LongPair)}
   */
  @Test
  public void testCompareTo() {
    // Setup
    // Expected values
    CompareToTester<LongPair> compareToTester = new CompareToTester<>(new LongPair(4, 10));

    // Execute functionality
    // Check result
    compareToTester.performCompareToTestEqual(new LongPair(4, 10));
    compareToTester.performComparetoTestNotEqual(new LongPair(42, 23));
    compareToTester.performCompareToTestGreater(new LongPair(5, 9), new LongPair(4, 11));
    compareToTester.performCompareToTestNotGreater(new LongPair(3, 11));
    compareToTester.performCompareToTestSmaller(new LongPair(3, 10), new LongPair(4, 9));
    compareToTester.performCompareToTestNotSmaller(new LongPair(5, 2));
  }

  /**
   * Test method for {@link LongPair#getFirst()} and {@link LongPair#setFirst(long)}
   */
  @Test
  public void testGetSetFirst() {
    // Setup
    LongPair longPair = new LongPair(2, 3);

    // Execute functionality
    // Check result
    assertEquals(2, longPair.getFirst());
    longPair.setFirst(42);
    assertEquals(42, longPair.getFirst());
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures.LongPair#getSecond()}
   * and {@link de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures.LongPair#setSecond(long)}
   */
  @Test
  public void testGetSetSecond() {
    // Setup
    LongPair longPair = new LongPair(5, 6);

    // Execute functionality
    // Check result
    assertEquals(6, longPair.getSecond());
    longPair.setSecond(23);
    assertEquals(23, longPair.getSecond());
  }
}
