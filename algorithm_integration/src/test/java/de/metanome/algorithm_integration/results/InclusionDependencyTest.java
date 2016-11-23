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

import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.ColumnPermutation;
import de.metanome.algorithm_integration.result_receiver.ColumnNameMismatchException;
import de.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Test for {@link InclusionDependency}
 *
 * @author Jakob Zwiener
 */
public class InclusionDependencyTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  /**
   * Test method for {@link InclusionDependency#sendResultTo(OmniscientResultReceiver)} <p/> The
   * {@link InclusionDependency} should be sendable to the {@link OmniscientResultReceiver}.
   */
  @Test
  public void testSendResultTo() throws CouldNotReceiveResultException, ColumnNameMismatchException {
    // Setup
    OmniscientResultReceiver resultReceiver = mock(OmniscientResultReceiver.class);
    InclusionDependency
      ind =
      new InclusionDependency(mock(ColumnPermutation.class), mock(ColumnPermutation.class));

    // Execute functionality
    ind.sendResultTo(resultReceiver);

    // Check result
    verify(resultReceiver).receiveResult(ind);
  }

  /**
   * Test method for {@link InclusionDependency#InclusionDependency(ColumnPermutation,
   * ColumnPermutation)} <p/> A {@link InclusionDependency} should store referenced and dependant.
   */
  @Test
  public void testConstructor() {
    // Setup
    // Expected values
    ColumnPermutation expectedDependant = new ColumnPermutation(
      new ColumnIdentifier("table2", "column2"),
      new ColumnIdentifier("table2", "column27"));
    ColumnPermutation expectedReferenced = new ColumnPermutation(
      new ColumnIdentifier("table1", "column1"),
      new ColumnIdentifier("table1", "column4"));
    // Execute functionality
    InclusionDependency ind = new InclusionDependency(expectedDependant, expectedReferenced);

    // Check result
    assertEquals(expectedDependant, ind.getDependant());
    assertEquals(expectedReferenced, ind.getReferenced());
  }

  /**
   * Test method for {@link InclusionDependency#toString()} <p/> A {@link InclusionDependency}
   * should return a human readable string representation.
   */
  @Test
  public void testToString() {
    // Setup
    ColumnPermutation expectedDependant = new ColumnPermutation(
      new ColumnIdentifier("table2", "column2"),
      new ColumnIdentifier("table2", "column27"));
    ColumnPermutation expectedReferenced = new ColumnPermutation(
      new ColumnIdentifier("table1", "column1"),
      new ColumnIdentifier("table1", "column4"));
    InclusionDependency ind = new InclusionDependency(expectedDependant, expectedReferenced);
    // Expected values
    String
      expectedStringRepresentation =
      expectedDependant + InclusionDependency.IND_SEPARATOR + expectedReferenced;

    // Execute functionality
    // Check result
    assertEquals(expectedStringRepresentation, ind.toString());
  }

  /**
   * Test method for {@link de.metanome.algorithm_integration.results.InclusionDependency#toString()} <p/> A {@link InclusionDependency}
   * should return a human readable string representation.
   */
  @Test
  public void testToFromStringWithMapping() {
    // Setup
    Map<String, String> tableMappingTo = new HashMap<>();
    tableMappingTo.put("table1", "1");
    tableMappingTo.put("table2", "2");
    Map<String, String> columnMappingTo = new HashMap<>();
    columnMappingTo.put("1.column1", "1");
    columnMappingTo.put("2.column2", "2");
    columnMappingTo.put("1.column7", "3");
    columnMappingTo.put("2.column4", "4");
    Map<String, String> tableMappingFrom = new HashMap<>();
    tableMappingFrom.put("1", "table1");
    tableMappingFrom.put("2", "table2");
    Map<String, String> columnMappingFrom = new HashMap<>();
    columnMappingFrom.put("1", "1.column1");
    columnMappingFrom.put("2", "2.column2");
    columnMappingFrom.put("3", "1.column7");
    columnMappingFrom.put("4", "2.column4");

    ColumnPermutation dependant = new ColumnPermutation(new ColumnIdentifier("table2", "column2"),
      new ColumnIdentifier("table2", "column4"));
    ColumnPermutation referenced = new ColumnPermutation(new ColumnIdentifier("table1", "column1"),
      new ColumnIdentifier("table1", "column7"));

    // Expected values
    InclusionDependency expectedIND = new InclusionDependency(dependant, referenced);
    String expectedString = "2,4[=1,3";

    // Execute functionality
    String actualString = expectedIND.toString(tableMappingTo, columnMappingTo);
    InclusionDependency actualIND = InclusionDependency.fromString(tableMappingFrom, columnMappingFrom, expectedString);

    // Check result
    assertEquals(expectedString, actualString);
    assertEquals(expectedIND, actualIND);
  }

  /**
   * Test method for {@link InclusionDependency#equals(Object)} and {@link
   * InclusionDependency#hashCode()} <p/> {@link InclusionDependency}s containing equals dependant
   * and referenced should be equal and have same hash codes.
   */
  @Test
  public void testEqualsHashCode() {
    // Setup
    InclusionDependency expectedInd = new InclusionDependency(
      new ColumnPermutation(
        new ColumnIdentifier("table1", "column2")),
      new ColumnPermutation(
        new ColumnIdentifier("table2", "column47"))
    );
    InclusionDependency expectedEqualInd = new InclusionDependency(
      new ColumnPermutation(
        new ColumnIdentifier("table1", "column2")),
      new ColumnPermutation(
        new ColumnIdentifier("table2", "column47"))
    );
    InclusionDependency expectedNotEqualDependantInd = new InclusionDependency(
      new ColumnPermutation(
        new ColumnIdentifier("table1", "column4")),
      new ColumnPermutation(
        new ColumnIdentifier("table2", "column47"))
    );
    InclusionDependency expectedNotEqualReferencedInd = new InclusionDependency(
      new ColumnPermutation(
        new ColumnIdentifier("table1", "column2")),
      new ColumnPermutation(
        new ColumnIdentifier("table5", "column47"))
    );

    // Execute functionality
    // Check result
    assertEquals(expectedInd, expectedInd);
    assertEquals(expectedInd.hashCode(), expectedInd.hashCode());
    assertNotSame(expectedInd, expectedEqualInd);
    assertEquals(expectedInd, expectedEqualInd);
    assertEquals(expectedInd.hashCode(), expectedEqualInd.hashCode());
    assertNotEquals(expectedInd, expectedNotEqualDependantInd);
    assertNotEquals(expectedInd.hashCode(), expectedNotEqualDependantInd.hashCode());
    assertNotEquals(expectedInd, expectedNotEqualReferencedInd);
    assertNotEquals(expectedInd.hashCode(), expectedNotEqualReferencedInd.hashCode());
  }


}
