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
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;
import de.uni_potsdam.hpi.metanome.test_helper.GwtSerializationTester;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Test for {@link UniqueColumnCombination}
 *
 * @author Jakob Zwiener
 */
public class UniqueColumnCombinationTest {

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception {
  }

  /**
   * Test method for {@link UniqueColumnCombination#sendResultTo(OmniscientResultReceiver)} <p/> The
   * {@link UniqueColumnCombination} should be sendable to the {@link OmniscientResultReceiver}.
   */
  @Test
  public void testSendResultTo() throws CouldNotReceiveResultException {
    // Setup
    OmniscientResultReceiver resultReceiver = mock(OmniscientResultReceiver.class);
    UniqueColumnCombination ucc = new UniqueColumnCombination();

    // Execute functionality
    ucc.sendResultTo(resultReceiver);

    // Check result
    verify(resultReceiver).receiveResult(ucc);
  }

  /**
   * Test method for {@link UniqueColumnCombination#UniqueColumnCombination(ColumnIdentifier...)}
   * <p/> A {@link UniqueColumnCombination} should store all it's identifiers. Identifiers can only
   * appear once in the {@link UniqueColumnCombination}.
   */
  @Test
  public void testConstructorColumnIdentifiers() {
    // Setup
    // Expected values
    ColumnIdentifier expectedColumn1 = new ColumnIdentifier("table1", "column1");
    ColumnIdentifier expectedColumn2 = new ColumnIdentifier("table2", "column2");

    // Execute functionality
    UniqueColumnCombination
        actualUniqueColumnCombination =
        new UniqueColumnCombination(expectedColumn1, expectedColumn2, expectedColumn2);

    // Check result
    assertEquals(new ColumnCombination(expectedColumn1, expectedColumn2),
                 actualUniqueColumnCombination.getColumnCombination());
  }

  /**
   * Test method for {@link UniqueColumnCombination#UniqueColumnCombination(ColumnCombination)} <p/>
   * A {@link UniqueColumnCombination} should store a given {@link ColumnCombination} on
   * construction.
   */
  @Test
  public void testConstructorColumnCombination() {
    // Setup
    // Expected values
    ColumnIdentifier expectedColumn1 = new ColumnIdentifier("table1", "column1");
    ColumnIdentifier expectedColumn2 = new ColumnIdentifier("table2", "column2");
    ColumnCombination
        expectedColumnCombination =
        new ColumnCombination(expectedColumn1, expectedColumn2, expectedColumn2);

    // Execute functionality
    UniqueColumnCombination
        actualUniqueColumnCombination =
        new UniqueColumnCombination(expectedColumnCombination);

    // Check result
    assertEquals(expectedColumnCombination, actualUniqueColumnCombination.getColumnCombination());
  }

  /**
   * Test method for {@link UniqueColumnCombination#toString()} <p/> A {@link
   * UniqueColumnCombination} should return the contained {@link ColumnCombination}s string
   * representation.
   */
  @Test
  public void testToString() {
    // Setup
    ColumnIdentifier expectedColumn1 = new ColumnIdentifier("table1", "column1");
    ColumnIdentifier expectedColumn2 = new ColumnIdentifier("table2", "column2");
    UniqueColumnCombination
        uniqueColumnCombination =
        new UniqueColumnCombination(expectedColumn1, expectedColumn2);
    // Expected values
    String
        expectedStringRepresentation =
        new ColumnCombination(expectedColumn1, expectedColumn2).toString();

    // Execute functionality
    // Check result
    assertEquals(expectedStringRepresentation, uniqueColumnCombination.toString());
  }

  /**
   * Test method for {@link UniqueColumnCombination#equals(Object)} and {@link
   * UniqueColumnCombination#hashCode()} <p/> {@link UniqueColumnCombination}s containing the same
   * {@link ColumnIdentifier}s in different order should be equal.
   */
  @Test
  public void testEqualsHashCode() {
    // Setup
    ColumnIdentifier column11 = new ColumnIdentifier("table1", "column1");
    ColumnIdentifier column12 = new ColumnIdentifier("table2", "column2");
    ColumnIdentifier column13 = new ColumnIdentifier("table3", "column3");
    UniqueColumnCombination
        expectedColumnCombination1 =
        new UniqueColumnCombination(column12, column13, column11);
    ColumnIdentifier column21 = new ColumnIdentifier("table1", "column1");
    ColumnIdentifier column22 = new ColumnIdentifier("table2", "column2");
    ColumnIdentifier column23 = new ColumnIdentifier("table3", "column3");
    UniqueColumnCombination
        expectedColumnCombination2 =
        new UniqueColumnCombination(column21, column22, column23);
    UniqueColumnCombination
        expectedColumnCombinationNotEquals =
        new UniqueColumnCombination(column21, column23);

    // Execute functionality
    // Check result
    assertEquals(expectedColumnCombination1, expectedColumnCombination1);
    assertEquals(expectedColumnCombination1.hashCode(), expectedColumnCombination1.hashCode());
    assertNotSame(expectedColumnCombination1, expectedColumnCombination2);
    assertEquals(expectedColumnCombination1, expectedColumnCombination2);
    assertEquals(expectedColumnCombination1.hashCode(), expectedColumnCombination2.hashCode());
    assertNotEquals(expectedColumnCombination1, expectedColumnCombinationNotEquals);
    assertNotEquals(expectedColumnCombination1.hashCode(),
                    expectedColumnCombinationNotEquals.hashCode());
  }

  /**
   * Tests that the instances of {@link UniqueColumnCombination} are serializable in GWT.
   */
  @Test
  public void testGwtSerialization() {
    GwtSerializationTester
        .checkGwtSerializability(new UniqueColumnCombination(mock(ColumnIdentifier.class)));
  }

}
