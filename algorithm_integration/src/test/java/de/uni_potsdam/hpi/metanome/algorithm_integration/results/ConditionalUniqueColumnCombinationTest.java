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

/**
 *
 */
package de.uni_potsdam.hpi.metanome.algorithm_integration.results;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCondition;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;
import de.uni_potsdam.hpi.metanome.test_helper.EqualsAndHashCodeTester;
import de.uni_potsdam.hpi.metanome.test_helper.GwtSerializationTester;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Test for {@link de.uni_potsdam.hpi.metanome.algorithm_integration.results.ConditionalUniqueColumnCombination}
 *
 * @author Jens Hildebrandt
 */
public class ConditionalUniqueColumnCombinationTest {

  /**
   * @throws Exception
   */
  @Before
  public void setUp() throws Exception {
  }

  /**
   * @throws Exception
   */
  @After
  public void tearDown() throws Exception {
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.algorithm_integration.results.ConditionalUniqueColumnCombination#sendResultTo(de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver)}
   * <p/> The {@link de.uni_potsdam.hpi.metanome.algorithm_integration.results.ConditionalUniqueColumnCombination}
   * should be sendable to the {@link de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver}.
   */
  @Test
  public void testSendResultTo() throws CouldNotReceiveResultException {
    // Setup
    OmniscientResultReceiver resultReceiver = mock(OmniscientResultReceiver.class);
    ConditionalUniqueColumnCombination cucc = new ConditionalUniqueColumnCombination();

    // Execute functionality
    cucc.sendResultTo(resultReceiver);

    // Check result
    verify(resultReceiver).receiveResult(cucc);
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.algorithm_integration.results.ConditionalUniqueColumnCombination#toString()}
   * <p/> A {@link de.uni_potsdam.hpi.metanome.algorithm_integration.results.ConditionalUniqueColumnCombination}
   * should return the contained {@link de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination}
   * and the {@link de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCondition} string
   * representation.
   */
  @Test
  public void testToString() {
    // Setup
    ColumnIdentifier expectedColumn1 = new ColumnIdentifier("table1", "column1");
    ColumnIdentifier expectedColumn2 = new ColumnIdentifier("table2", "column2");
    List<ColumnCondition> conditionList = new LinkedList<>();
    conditionList.add(new ColumnCondition(new ColumnIdentifier("table1", "column1"), "condition1",
                                          "condition2"));
    conditionList
        .add(new ColumnCondition(new ColumnIdentifier("table1", "column2"), "condition3"));

    ConditionalUniqueColumnCombination
        actualConditionalColumnCombination =
        new ConditionalUniqueColumnCombination(
            new ColumnCombination(expectedColumn1, expectedColumn2),
            conditionList.toArray(new ColumnCondition[conditionList.size()]));
    // Expected values
    String
        expectedStringRepresentation =
        new ColumnCombination(expectedColumn1, expectedColumn2).toString() + "["
        + "table1.column1: [condition1, condition2]" + ", " + "table1.column2: [condition3]"
        + "]";

    // Execute functionality
    // Check result
    assertEquals(expectedStringRepresentation, actualConditionalColumnCombination.toString());
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.algorithm_integration.results.ConditionalUniqueColumnCombination#equals(Object)}
   * and {@link de.uni_potsdam.hpi.metanome.algorithm_integration.results.ConditionalUniqueColumnCombination#hashCode()}
   * <p/> {@link de.uni_potsdam.hpi.metanome.algorithm_integration.results.ConditionalUniqueColumnCombination}s
   * containing the same {@link de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier}s
   * and the same {@link de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCondition}s in
   * different order should be equal.
   */
  @Test
  public void testEqualsHashCode() {
    // Setup
    ColumnIdentifier column11 = new ColumnIdentifier("table1", "column1");
    ColumnIdentifier column12 = new ColumnIdentifier("table2", "column2");
    ColumnIdentifier column13 = new ColumnIdentifier("table3", "column3");
    ColumnIdentifier column21 = new ColumnIdentifier("table1", "column1");
    ColumnIdentifier column22 = new ColumnIdentifier("table2", "column2");
    ColumnIdentifier column23 = new ColumnIdentifier("table3", "column3");

    List<ColumnCondition> conditions1 = new LinkedList<>();
    conditions1.add(new ColumnCondition(column11, "a"));
    conditions1.add(new ColumnCondition(column13, "a", "b"));

    List<ColumnCondition> conditions1Eq = new LinkedList<>();
    conditions1Eq.add(new ColumnCondition(column11, "a"));
    conditions1Eq.add(new ColumnCondition((column23), "b", "a"));

    List<ColumnCondition> conditions2 = new LinkedList<>();
    conditions2.add(new ColumnCondition(column21, "a"));
    conditions2.add(new ColumnCondition(column23, "a"));

    ConditionalUniqueColumnCombination
        expectedColumnCombination1 =
        new ConditionalUniqueColumnCombination(new ColumnCombination(column12, column13, column11),
                                               conditions1.toArray(
                                                   new ColumnCondition[conditions1.size()]));

    ConditionalUniqueColumnCombination
        expectedColumnCombination2 =
        new ConditionalUniqueColumnCombination(new ColumnCombination(column21, column22, column23),
                                               conditions1.toArray(
                                                   new ColumnCondition[conditions1.size()]));
    ConditionalUniqueColumnCombination
        expectedColumnCombination2Eq =
        new ConditionalUniqueColumnCombination(new ColumnCombination(column11, column13, column12),
                                               conditions1Eq.toArray(
                                                   new ColumnCondition[conditions1Eq.size()]));
    ConditionalUniqueColumnCombination
        expectedColumnCombinationNotEquals =
        new ConditionalUniqueColumnCombination(new ColumnCombination(column21, column23),
                                               conditions2.toArray(
                                                   new ColumnCondition[conditions2.size()]));
    ConditionalUniqueColumnCombination
        expectedColumnCombinationNotEquals2 =
        new ConditionalUniqueColumnCombination(new ColumnCombination(column12, column13, column11),
                                               conditions2.toArray(
                                                   new ColumnCondition[conditions2.size()]));

    // Execute functionality
    // Check result

    EqualsAndHashCodeTester<ConditionalUniqueColumnCombination>
        tester =
        new EqualsAndHashCodeTester<>();
    tester
        .performBasicEqualsAndHashCodeChecks(expectedColumnCombination1, expectedColumnCombination2,
                                             expectedColumnCombinationNotEquals,
                                             expectedColumnCombinationNotEquals2);
    tester.performBasicEqualsAndHashCodeChecks(expectedColumnCombination1,
                                               expectedColumnCombination2Eq);
  }

  /**
   * Tests that the instances of {@link de.uni_potsdam.hpi.metanome.algorithm_integration.results.ConditionalUniqueColumnCombination}
   * are serializable in GWT.
   */
  @Test
  public void testGwtSerialization() {
    GwtSerializationTester.checkGwtSerializability(
        new ConditionalUniqueColumnCombination(mock(ColumnCombination.class),
                                               mock(ColumnCondition.class)));
  }

}
