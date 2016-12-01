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
package de.metanome.algorithm_integration.results;

import de.metanome.algorithm_integration.results.basic_statistic_values.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class BasicStatisticValueTest {


  /**
   * Test method for {@link de.metanome.algorithm_integration.results.basic_statistic_values.BasicStatisticValue#toString()} <p/> A {@link de.metanome.algorithm_integration.results.basic_statistic_values.BasicStatisticValue} should return a
   * human readable string representation.
   */
  @Test
  public void testToString() {
    // Setup
    BasicStatisticValueString value1 = new BasicStatisticValueString("value");
    BasicStatisticValueInteger value2 = new BasicStatisticValueInteger(5);
    BasicStatisticValueDouble value3 = new BasicStatisticValueDouble(4.2);

    List<String> list = new ArrayList<>();
    list.add("min");
    list.add("max");
    list.add("avg");
    BasicStatisticValueStringList value4 = new BasicStatisticValueStringList(list);

    // Expected values
    String expectedString1 = "value";
    String expectedString2 = "5";
    String expectedString3 = "4.2";
    String expectedString4 = "min,max,avg";

    // Execute functionality
    // Check result
    assertEquals(expectedString1, value1.toString());
    assertEquals(expectedString2, value2.toString());
    assertEquals(expectedString3, value3.toString());
    assertEquals(expectedString4, value4.toString());
  }

  /**
   * Test method for {@link BasicStatisticValue#equals(Object)} and {@link BasicStatisticValue#hashCode()}
   */
  @Test
  public void testEqualsHashCode() {
    // Setup
    List<Integer> integerList = new ArrayList<>();
    integerList.add(4);
    integerList.add(3);

    List<String> stringList = new ArrayList<>();
    stringList.add("min");
    stringList.add("max");
    stringList.add("avg");

    BasicStatisticValueString value1 = new BasicStatisticValueString("value");
    BasicStatisticValueInteger value2 = new BasicStatisticValueInteger(5);
    BasicStatisticValueDouble value3 = new BasicStatisticValueDouble(4.2);
    BasicStatisticValueString value4 = new BasicStatisticValueString("value");
    BasicStatisticValueFloat value5 = new BasicStatisticValueFloat(3.2f);
    BasicStatisticValueLong value6 = new BasicStatisticValueLong(2L);
    BasicStatisticValueIntegerList value7 = new BasicStatisticValueIntegerList(integerList);
    BasicStatisticValueStringList value8 = new BasicStatisticValueStringList(stringList);
    BasicStatisticValueStringList value9 = new BasicStatisticValueStringList(stringList);
    BasicStatisticValueFloat value10 = new BasicStatisticValueFloat(3.2f);

    // Execute functionality
    // Check result
    assertEquals(value1, value4);
    assertEquals(value5, value10);
    assertEquals(value8, value9);
    assertNotEquals(value2, value1);
    assertNotEquals(value2, value3);
    assertNotEquals(value5, value6);
    assertNotEquals(value7, value8);

    assertEquals(value1.hashCode(), value4.hashCode());
    assertEquals(value5.hashCode(), value10.hashCode());
    assertEquals(value8.hashCode(), value9.hashCode());
    assertNotEquals(value2.hashCode(), value1.hashCode());
    assertNotEquals(value2.hashCode(), value3.hashCode());
    assertNotEquals(value5.hashCode(), value3.hashCode());
    assertNotEquals(value7.hashCode(), value6.hashCode());
    assertNotEquals(value8.hashCode(), value10.hashCode());
  }

  /**
   * Test method for {@link BasicStatisticValue#compareTo(Object)}
   */
  @Test
  public void testCompareto() {
    // Setup
    List<String> stringList = new ArrayList<>();
    stringList.add("min");
    stringList.add("max");
    stringList.add("avg");

    BasicStatisticValueString value1 = new BasicStatisticValueString("value");
    BasicStatisticValueInteger value2 = new BasicStatisticValueInteger(5);
    BasicStatisticValueDouble value3 = new BasicStatisticValueDouble(4.2);
    BasicStatisticValueString value4 = new BasicStatisticValueString("value");
    BasicStatisticValueFloat value5 = new BasicStatisticValueFloat(3.2f);
    BasicStatisticValueLong value6 = new BasicStatisticValueLong(2L);
    BasicStatisticValueLong value7 = new BasicStatisticValueLong(4L);
    BasicStatisticValueStringList value8 = new BasicStatisticValueStringList(stringList);
    BasicStatisticValueStringList value9 = new BasicStatisticValueStringList(stringList);
    BasicStatisticValueFloat value10 = new BasicStatisticValueFloat(3.2f);
    BasicStatisticValueInteger value11 = new BasicStatisticValueInteger(4);

    // Execute functionality
    // Check result
    assertTrue(value1.compareTo(value4) == 0);
    assertTrue(value5.compareTo(value10) == 0);
    assertTrue(value8.compareTo(value9) == 0);

    assertTrue(value2.compareTo(value11) < 0);
    assertTrue(value2.compareTo(value9) < 0);
    assertTrue(value3.compareTo(value5) < 0);

    assertTrue(value6.compareTo(value7) > 0);
  }

}
