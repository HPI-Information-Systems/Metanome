/*
 * Copyright 2014 by the Metanome project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package de.metanome.algorithm_integration.results;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.ColumnPermutation;
import de.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;
import de.metanome.algorithm_integration.results.OrderDependency.ComparisonOperator;
import de.metanome.algorithm_integration.results.OrderDependency.OrderType;
import de.metanome.test_helper.EqualsAndHashCodeTester;
import de.metanome.test_helper.GwtSerializationTester;

/**
 * Test for {@link OrderDependency}
 *
 * @author Philipp Langer
 */
public class OrderDependencyTest {

  @Before
  public void setUp() throws Exception {}

  @After
  public void tearDown() throws Exception {}

  /**
   * Test method for {@link OrderDependency#OrderDependency()}
   * <p/>
   * A {@link OrderDependency} should store the left-hand {@link ColumnPermutation} and the
   * right-hand {@link ColumnPermutation}, as well as the underlying sort order and comparison
   * operator used.
   */
  @Test
  public void testConstructor() {
    // Setup
    // Expected values
    final ColumnPermutation expectedLhs =
        new ColumnPermutation(new ColumnIdentifier("table1", "column1"), new ColumnIdentifier(
            "table1", "column2"));
    final ColumnPermutation expectedRhs =
        new ColumnPermutation(new ColumnIdentifier("table1", "column7"));

    // Execute functionality
    final OrderDependency orderDependency =
        new OrderDependency(expectedLhs, expectedRhs, OrderType.LEXICOGRAPHICAL,
            ComparisonOperator.SMALLER_EQUAL);

    // Check result
    assertEquals(expectedLhs, orderDependency.getLhs());
    assertEquals(expectedRhs, orderDependency.getRhs());
    assertEquals(OrderType.LEXICOGRAPHICAL, orderDependency.getOrderType());
    assertEquals(ComparisonOperator.SMALLER_EQUAL, orderDependency.getComparisonOperator());
  }

  /**
   * Test method for {@link OrderDependency#equals(Object)} and {@link OrderDependency#hashCode()}
   * <p/>
   * {@link OrderDependency}s with equal left- and right-hand sides as well as order type and
   * comparison operator should be equal.
   */
  @Test
  public void testEqualsHashCode() {
    // Setup
    final OrderDependency expectedOd =
        new OrderDependency(new ColumnPermutation(new ColumnIdentifier("table1", "column2")),
            new ColumnPermutation(new ColumnIdentifier("table1", "column47")),
            OrderType.LEXICOGRAPHICAL, ComparisonOperator.SMALLER_EQUAL);
    final OrderDependency expectedEqualOd =
        new OrderDependency(new ColumnPermutation(new ColumnIdentifier("table1", "column2")),
            new ColumnPermutation(new ColumnIdentifier("table1", "column47")),
            OrderType.LEXICOGRAPHICAL, ComparisonOperator.SMALLER_EQUAL);
    final OrderDependency expectedNotEqualLhsOd =
        new OrderDependency(new ColumnPermutation(new ColumnIdentifier("table1", "column3")),
            new ColumnPermutation(new ColumnIdentifier("table1", "column47")),
            OrderType.LEXICOGRAPHICAL, ComparisonOperator.SMALLER_EQUAL);
    final OrderDependency expectedNotEqualRhsOd =
        new OrderDependency(new ColumnPermutation(new ColumnIdentifier("table1", "column2")),
            new ColumnPermutation(new ColumnIdentifier("table1", "column3")),
            OrderType.LEXICOGRAPHICAL, ComparisonOperator.SMALLER_EQUAL);

    // Execute functionality
    // Check result
    final EqualsAndHashCodeTester<OrderDependency> tester = new EqualsAndHashCodeTester<>();
    tester.performBasicEqualsAndHashCodeChecks(expectedOd, expectedEqualOd, expectedNotEqualLhsOd,
        expectedNotEqualRhsOd);
  }

  /**
   * Tests that the instances of {@link OrderDependency} are serializable in GWT.
   */
  @Test
  public void testGwtSerialization() {
    GwtSerializationTester.checkGwtSerializability(new OrderDependency(
        mock(ColumnPermutation.class), mock(ColumnPermutation.class), OrderType.LEXICOGRAPHICAL,
        ComparisonOperator.SMALLER_EQUAL));
  }

  /**
   * Test method for {@link OrderDependency#sendResultTo(OmniscientResultReceiver)}
   * <p/>
   * The {@link OrderDependency} should be sendable to the {@link OmniscientResultReceiver}.
   */
  @Test
  public void testSendResultTo() throws CouldNotReceiveResultException {
    // Setup
    final OmniscientResultReceiver resultReceiver = mock(OmniscientResultReceiver.class);
    final OrderDependency od =
        new OrderDependency(mock(ColumnPermutation.class), mock(ColumnPermutation.class),
            OrderType.LEXICOGRAPHICAL, ComparisonOperator.SMALLER_EQUAL);

    // Execute functionality
    od.sendResultTo(resultReceiver);

    // Check result
    verify(resultReceiver).receiveResult(od);
  }

  /**
   * Test method for {@link OrderDependency#toString()}
   * <p/>
   * A {@link OrderDependency} should return a human readable string representation.
   */
  @Test
  public void testToString() {
    // Setup
    final ColumnPermutation expectedLhs =
        new ColumnPermutation(new ColumnIdentifier("table1", "column1"), new ColumnIdentifier(
            "table1", "column2"));
    final ColumnPermutation expectedRhs =
        new ColumnPermutation(new ColumnIdentifier("table1", "column3"), new ColumnIdentifier(
            "table1", "column4"));
    final OrderDependency orderDependency =
        new OrderDependency(expectedLhs, expectedRhs, OrderType.LEXICOGRAPHICAL,
            ComparisonOperator.SMALLER_EQUAL);
    // Expected values
    final String expectedStringRepresentation =
        expectedLhs + OrderDependency.OD_SEPARATOR + "[" + "<=" + "," + "lex" + "]" + expectedRhs;

    // Execute functionality
    // Check result
    assertEquals(expectedStringRepresentation, orderDependency.toString());
  }
}
