/**
 * Copyright 2014-2016 by Metanome Project
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

import de.metanome.algorithm_integration.ColumnCombination;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.result_receiver.ColumnNameMismatchException;
import de.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;
import de.metanome.algorithm_integration.results.basic_statistic_values.BasicStatisticValueString;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Test for {@link BasicStatistic}
 *
 * @author Jakob Zwiener
 */
public class BasicStatisticTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  /**
   * Test method for {@link BasicStatistic#sendResultTo(OmniscientResultReceiver)} <p/> The {@link
   * BasicStatistic} should be sendable to the {@link OmniscientResultReceiver}.
   */
  @Test
  public void testSendResultTo() throws CouldNotReceiveResultException, ColumnNameMismatchException {
    // Setup
    OmniscientResultReceiver resultReceiver = mock(OmniscientResultReceiver.class);
    BasicStatistic
      statistic =
      new BasicStatistic(mock(ColumnIdentifier.class));

    // Execute functionality
    statistic.sendResultTo(resultReceiver);

    // Check result
    verify(resultReceiver).receiveResult(statistic);
  }

  /**
   * Test method for {@link BasicStatistic#BasicStatistic(ColumnIdentifier...)} <p/>
   * A {@link BasicStatistic} should store the statistic's name, the value and the associated {@link
   * ColumnCombination}.
   */
  @Test
  public void testConstructor() {
    // Setup
    // Expected values
    ColumnIdentifier expectedColumn = new ColumnIdentifier("table42", "column23");
    ColumnCombination expectedColumnCombination = new ColumnCombination(expectedColumn);
    // Execute functionality
    BasicStatistic
      statistic =
      new BasicStatistic(expectedColumn);

    // Check result
    assertEquals(expectedColumnCombination, statistic.getColumnCombination());
  }

  /**
   * Test method for {@link BasicStatistic#addStatistic(String, de.metanome.algorithm_integration.results.basic_statistic_values.BasicStatisticValue) <p/>
   * A {@link BasicStatistic} should store the statistic's name and the value in a map.
   */
  @Test
  public void testAddStatistic() {
    // Setup
    BasicStatistic statistic = new BasicStatistic(new ColumnIdentifier("table", "column"));

    // Expected values
    String expectedName = "statistic";
    BasicStatisticValueString expectedValue = new BasicStatisticValueString("value");

    // Execute functionality
    statistic.addStatistic(expectedName, expectedValue);

    // Check result
    assertTrue(statistic.statisticMap.containsKey(expectedName));
    assertTrue(statistic.statisticMap.get(expectedName) == expectedValue);
  }

  /**
   * Test method for {@link BasicStatistic#toString()} <p/> A {@link BasicStatistic} should return a
   * human readable string representation.
   */
  @Test
  public void testToString() {
    // Setup
    String expectedStatisticName1 = "Min";
    BasicStatisticValueString expectedStatisticValue1 = new BasicStatisticValueString("minValue");
    String expectedStatisticName2 = "Max";
    BasicStatisticValueString expectedStatisticValue2 = new BasicStatisticValueString("maxValue");
    ColumnIdentifier expectedColumn = new ColumnIdentifier("table42", "column23");
    ColumnCombination expectedColumnCombination = new ColumnCombination(expectedColumn);
    BasicStatistic statistic = new BasicStatistic(expectedColumn);
    statistic.addStatistic(expectedStatisticName1, expectedStatisticValue1);
    statistic.addStatistic(expectedStatisticName2, expectedStatisticValue2);

    // Expected values
    String expectedString1 = expectedColumnCombination + BasicStatistic.COLUMN_VALUE_SEPARATOR +
      expectedStatisticName2 + BasicStatistic.NAME_COLUMN_SEPARATOR  + expectedStatisticValue2 +
      BasicStatistic.STATISTIC_SEPARATOR +
      expectedStatisticName1 + BasicStatistic.NAME_COLUMN_SEPARATOR  + expectedStatisticValue1 + BasicStatistic.STATISTIC_SEPARATOR;

    String expectedString2 = expectedColumnCombination + BasicStatistic.COLUMN_VALUE_SEPARATOR +
      expectedStatisticName1 + BasicStatistic.NAME_COLUMN_SEPARATOR  + expectedStatisticValue1 +
      BasicStatistic.STATISTIC_SEPARATOR +
      expectedStatisticName2 + BasicStatistic.NAME_COLUMN_SEPARATOR  + expectedStatisticValue2 + BasicStatistic.STATISTIC_SEPARATOR;

    // Execute functionality
    // Check result
    assertTrue(expectedString1.equals(statistic.toString()) || expectedString2.equals(statistic.toString()));
  }

  /**
   * Test method for {@link BasicStatistic#equals(Object)} and {@link BasicStatistic#hashCode()}
   */
  @Test
  public void testEqualsHashCode() {
    // Setup
    BasicStatistic expectedStatistic = new BasicStatistic(
      new ColumnIdentifier("table2", "column47"));
    expectedStatistic.addStatistic("Min", new BasicStatisticValueString("MinValue"));
    BasicStatistic expectedEqualStatistic = new BasicStatistic(
      new ColumnIdentifier("table2", "column47"));
    expectedEqualStatistic.addStatistic("Min", new BasicStatisticValueString("MinValue"));
    BasicStatistic expectedNotEqualNameStatistic = new BasicStatistic(
      new ColumnIdentifier("table2", "column47"));
    expectedNotEqualNameStatistic.addStatistic("Max", new BasicStatisticValueString("MinValue"));
    BasicStatistic expectedNotEqualValueStatistic = new BasicStatistic(
      new ColumnIdentifier("table2", "column47"));
    expectedNotEqualValueStatistic.addStatistic("Min", new BasicStatisticValueString("MaxValue"));
    BasicStatistic expectedNotEqualColumnStatistic = new BasicStatistic(
      new ColumnIdentifier("table2", "column42"));
    expectedNotEqualColumnStatistic.addStatistic("Min", new BasicStatisticValueString("MinValue"));

    // Execute functionality
    // Check result
    assertEquals(expectedStatistic, expectedStatistic);
    assertEquals(expectedStatistic.hashCode(), expectedStatistic.hashCode());
    assertNotSame(expectedStatistic, expectedEqualStatistic);
    assertEquals(expectedStatistic, expectedEqualStatistic);
    assertEquals(expectedStatistic.hashCode(), expectedEqualStatistic.hashCode());
    assertNotEquals(expectedStatistic, expectedNotEqualNameStatistic);
    assertNotEquals(expectedStatistic.hashCode(), expectedNotEqualNameStatistic.hashCode());
    assertNotEquals(expectedStatistic, expectedNotEqualValueStatistic);
    assertNotEquals(expectedStatistic.hashCode(), expectedNotEqualValueStatistic.hashCode());
    assertNotEquals(expectedStatistic, expectedNotEqualColumnStatistic);
    assertNotEquals(expectedStatistic.hashCode(), expectedNotEqualColumnStatistic.hashCode());
  }


}
