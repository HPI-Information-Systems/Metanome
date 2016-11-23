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
package de.metanome.algorithm_integration;

import de.metanome.test_helper.EqualsAndHashCodeTester;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link de.metanome.algorithm_integration.ColumnPermutation}
 */
public class ColumnPermutationTest {

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
   * A {@link de.metanome.algorithm_integration.ColumnPermutation} should store all its identifiers.
   * Equal {@link de.metanome.algorithm_integration.ColumnIdentifier}s may be present more than once
   * (which is not the case for {@link de.metanome.algorithm_integration.ColumnPermutation}s).
   * {@link ColumnIdentifier}s are stored in insertion order in the underlying list.
   */
  @Test
  public void testConstructor() {
    // Expected column identifiers
    final ColumnIdentifier expectedColumn1 = new ColumnIdentifier("table1", "column1");
    final ColumnIdentifier expectedColumn2 = new ColumnIdentifier("table2", "column2");
    // Create a column permutation from the expected column identifiers
    final ColumnPermutation columnPermutation =
      new ColumnPermutation(expectedColumn1, expectedColumn2, expectedColumn2);

    assertEquals(3, columnPermutation.columnIdentifiers.size());
    assertTrue(columnPermutation.columnIdentifiers.get(0).equals(expectedColumn1));
    assertTrue(columnPermutation.columnIdentifiers.get(1).equals(expectedColumn2));
    assertTrue(columnPermutation.columnIdentifiers.get(2).equals(expectedColumn2));
  }

  /**
   * Test method for {@link ColumnPermutation#equals(Object)} and {@link
   * ColumnPermutation#hashCode()} <p/> {@link ColumnPermutation}s containing the same {@link
   * ColumnIdentifier}s in different order are not considered equal.
   */
  @Test
  public void testEqualsHashCode() {

    final ColumnIdentifier column11 = new ColumnIdentifier("table1", "column1");
    final ColumnIdentifier column12 = new ColumnIdentifier("table2", "column2");
    final ColumnIdentifier column13 = new ColumnIdentifier("table3", "column3");
    final ColumnPermutation expectedColumnPermutation1 =
      new ColumnPermutation(column11, column12, column13);
    final ColumnIdentifier column21 = new ColumnIdentifier("table1", "column1");
    final ColumnIdentifier column22 = new ColumnIdentifier("table2", "column2");
    final ColumnIdentifier column23 = new ColumnIdentifier("table3", "column3");
    final ColumnPermutation expectedColumnPermutation2 =
      new ColumnPermutation(column21, column22, column23);
    final ColumnPermutation expectedColumnPermutationNotEquals =
      new ColumnPermutation(column21, column23, column22);

    final EqualsAndHashCodeTester<ColumnPermutation> tester = new EqualsAndHashCodeTester<>();
    tester.performBasicEqualsAndHashCodeChecks(expectedColumnPermutation1,
      expectedColumnPermutation2,
      expectedColumnPermutationNotEquals);

  }

  /**
   * Test method for {@link ColumnPermutation#getColumnIdentifiers()} {@link ColumnIdentifier}s are
   * returned in insertion order.
   */
  @Test
  public void testGetColumnIdentifiers() {
    final ColumnIdentifier expectedColumn1 = new ColumnIdentifier("table1", "column1");
    final ColumnIdentifier expectedColumn2 = new ColumnIdentifier("table2", "column2");
    final ColumnIdentifier expectedColumn3 = new ColumnIdentifier("table3", "column3");
    final ColumnPermutation columnPermutation =
      new ColumnPermutation(expectedColumn2, expectedColumn3, expectedColumn1);

    final List<ColumnIdentifier> actualColumnIdentifiers = columnPermutation.getColumnIdentifiers();

    assertTrue(3 == actualColumnIdentifiers.size());
    assertTrue(actualColumnIdentifiers.get(0).equals(expectedColumn2));
    assertTrue(actualColumnIdentifiers.get(1).equals(expectedColumn3));
    assertTrue(actualColumnIdentifiers.get(2).equals(expectedColumn1));
  }

  /**
   * Test method for {@link ColumnPermutation#toString()} <p/> A {@link ColumnPermutation} should
   * return {@link ColumnIdentifier}s in insertion order as string representation. E.g. "[column1,
   * column2]".
   */
  @Test
  public void testToString() {
    // Setup
    final ColumnIdentifier expectedColumn1 = new ColumnIdentifier("table56", "column1");
    final ColumnIdentifier expectedColumn2 = new ColumnIdentifier("table2", "column2");
    final ColumnPermutation columnPermutation =
      new ColumnPermutation(expectedColumn1, expectedColumn2);
    // Expected values
    final String expectedStringRepresentation =
      "[" + expectedColumn1.toString() + ", " + expectedColumn2.toString() + "]";

    // Execute functionality
    final String actualStringRepresentation = columnPermutation.toString();

    // Check result
    assertEquals(expectedStringRepresentation, actualStringRepresentation);
  }

  /**
   * Test method for {@link de.metanome.algorithm_integration.ColumnPermutation#toString()} <p/> A {@link ColumnPermutation} should
   * return the column identifiers as string representation.
   */
  @Test
  public void testToFromStringWithMapping() {
    // Setup
    Map<String, String> tableMappingTo = new HashMap<>();
    tableMappingTo.put("table.56", "1");
    tableMappingTo.put("table2", "2");
    Map<String, String> columnMappingTo = new HashMap<>();
    columnMappingTo.put("1.column1", "1");
    columnMappingTo.put("2.column2", "2");
    Map<String, String> tableMappingFrom = new HashMap<>();
    tableMappingFrom.put("1", "table.56");
    tableMappingFrom.put("2", "table2");
    Map<String, String> columnMappingFrom = new HashMap<>();
    columnMappingFrom.put("1", "1.column1");
    columnMappingFrom.put("2", "2.column2");

    // Expected values
    ColumnPermutation expectedColumnPermutation =
      new ColumnPermutation(new ColumnIdentifier("table.56", "column1"), new ColumnIdentifier("table2", "column2"));
    String expectedString = "1,2";

    // Execute functionality
    String actualString = expectedColumnPermutation.toString(tableMappingTo, columnMappingTo);
    ColumnPermutation actualColumnPermutation = ColumnPermutation.fromString(tableMappingFrom, columnMappingFrom, actualString);

    // Check result
    assertEquals(expectedString, actualString);
    assertEquals(expectedColumnPermutation, actualColumnPermutation);
  }
}
