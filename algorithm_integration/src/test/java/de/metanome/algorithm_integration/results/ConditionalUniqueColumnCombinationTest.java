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
/**
 *
 */
package de.metanome.algorithm_integration.results;

import de.metanome.algorithm_integration.*;
import de.metanome.algorithm_integration.result_receiver.ColumnNameMismatchException;
import de.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Test for {@link de.metanome.algorithm_integration.results.ConditionalUniqueColumnCombination}
 *
 * @author Jens Ehrlich
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
   * Test method for {@link de.metanome.algorithm_integration.results.ConditionalUniqueColumnCombination#sendResultTo(de.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver)}
   * <p/> The {@link de.metanome.algorithm_integration.results.ConditionalUniqueColumnCombination}
   * should be sendable to the {@link de.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver}.
   */
  @Test
  public void testSendResultTo() throws CouldNotReceiveResultException, ColumnNameMismatchException {
    // Setup
    OmniscientResultReceiver resultReceiver = mock(OmniscientResultReceiver.class);
    ConditionalUniqueColumnCombination cucc = new ConditionalUniqueColumnCombination();

    // Execute functionality
    cucc.sendResultTo(resultReceiver);

    // Check result
    verify(resultReceiver).receiveResult(cucc);
  }

  /**
   * Test method for {@link de.metanome.algorithm_integration.results.ConditionalUniqueColumnCombination#toString()}
   * <p/> A {@link de.metanome.algorithm_integration.results.ConditionalUniqueColumnCombination}
   * should return the contained {@link de.metanome.algorithm_integration.ColumnCombination} and the
   * {@link de.metanome.algorithm_integration.ColumnConditionAnd} string representation.
   */
  @Test
  public void testToString() {
    // Setup
    ColumnIdentifier expectedColumn1 = new ColumnIdentifier("table1", "column1");
    ColumnIdentifier expectedColumn2 = new ColumnIdentifier("table2", "column2");
    ColumnConditionOr outerCondition = new ColumnConditionOr();
    outerCondition.add(
      new ColumnConditionAnd(new ColumnConditionValue(expectedColumn1, "condition1"),
        new ColumnConditionValue(expectedColumn2, "condition2")));
    outerCondition
      .add(new ColumnConditionValue(expectedColumn1, "condition3"));

    ConditionalUniqueColumnCombination
      actualConditionalColumnCombination =
      new ConditionalUniqueColumnCombination(
        new ColumnCombination(expectedColumn1, expectedColumn2),
        outerCondition);
    // Expected values
    String
      expectedStringRepresentation =
      "[table1.column1, table2.column2]" + ConditionalUniqueColumnCombination.LINESEPARATOR +
        "[table1.column1, table2.column2]" + ConditionalUniqueColumnCombination.LINESEPARATOR +
        "  condition3         -       " + ConditionalUniqueColumnCombination.LINESEPARATOR +
        "  condition1    condition2  " + ConditionalUniqueColumnCombination.LINESEPARATOR +
        "Coverage: 0.0" + ConditionalUniqueColumnCombination.LINESEPARATOR +
        "";

    // Execute functionality
    // System.out.println(actualConditionalColumnCombination.buildPatternTableau());
    // Check result
    assertEquals(expectedStringRepresentation,
      actualConditionalColumnCombination.buildPatternTableau());
  }


}
