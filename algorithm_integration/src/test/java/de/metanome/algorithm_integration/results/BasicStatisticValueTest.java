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

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class BasicStatisticValueTest {


  /**
   * Test method for {@link BasicStatisticValue#toString()} <p/> A {@link BasicStatisticValue} should return a
   * human readable string representation.
   */
  @Test
  public void testToString() {
    // Setup
    BasicStatisticValue<String> value1 = new BasicStatisticValue<>("value");
    BasicStatisticValue<Integer> value2 = new BasicStatisticValue<>(5);
    BasicStatisticValue<Double> value3 = new BasicStatisticValue<>(4.2);

    // Expected values
    String expectedString1 = "value";
    String expectedString2 = "5";
    String expectedString3 = "4.2";

    // Execute functionality
    // Check result
    assertEquals(expectedString1, value1.toString());
    assertEquals(expectedString2, value2.toString());
    assertEquals(expectedString3, value3.toString());
  }

  /**
   * Test method for {@link BasicStatisticValue#equals(Object)} and {@link BasicStatisticValue#hashCode()}
   */
  @Test
  public void testEqualsHashCode() {
    // Setup
    BasicStatisticValue<String> value1 = new BasicStatisticValue<>("value");
    BasicStatisticValue<Integer> value2 = new BasicStatisticValue<>(5);
    BasicStatisticValue<Double> value3 = new BasicStatisticValue<>(4.2);
    BasicStatisticValue<String> value4 = new BasicStatisticValue<>("value");

    // Execute functionality
    // Check result
    assertEquals(value1, value4);
    assertNotEquals(value2, value1);
    assertNotEquals(value2, value3);

    assertEquals(value1.hashCode(), value4.hashCode());
    assertNotEquals(value2.hashCode(), value1.hashCode());
    assertNotEquals(value2.hashCode(), value3.hashCode());
  }

}
