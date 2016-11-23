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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Tests for {@link de.metanome.algorithm_integration.ColumnCombination}
 */
public class ColumnCombinationTest {

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
   * A {@link de.metanome.algorithm_integration.ColumnCombination} should store all it's
   * identifiers. Identifiers can only appear once in the {@link de.metanome.algorithm_integration.ColumnCombination}.
   */
  @Test
  public void testConstructor() {
    // Setup
    // Expected values
    ColumnIdentifier expectedColumn1 = new ColumnIdentifier("table1", "column1");
    ColumnIdentifier expectedColumn2 = new ColumnIdentifier("table2", "column2");
    // Execute functionality
    ColumnCombination
      columnCombination =
      new ColumnCombination(expectedColumn1, expectedColumn2, expectedColumn2);

    // Check result
    assertEquals(2, columnCombination.columnIdentifiers.size());
    assertTrue(columnCombination.columnIdentifiers.contains(expectedColumn1));
    assertTrue(columnCombination.columnIdentifiers.contains(expectedColumn2));
  }

  /**
   * Test method for {@link ColumnCombination#getColumnIdentifiers()} <p/> A {@link
   * ColumnCombination} should return its columnIdentifiers as a set.
   */
  @Test
  public void testGetColumnIdentifiers() {
    // Setup
    // Expected values
    ColumnIdentifier expectedColumn1 = new ColumnIdentifier("table1", "column1");
    ColumnIdentifier expectedColumn2 = new ColumnIdentifier("table2", "column2");
    ColumnIdentifier expectedColumn3 = new ColumnIdentifier("table3", "column3");
    ColumnCombination
      columnCombination =
      new ColumnCombination(expectedColumn2, expectedColumn3, expectedColumn1);

    // Execute functionality
    Set<ColumnIdentifier> actualColumnIdentifiers = columnCombination.getColumnIdentifiers();

    // Check result
    assertTrue(3 == actualColumnIdentifiers.size());
    assertTrue(actualColumnIdentifiers.contains(expectedColumn1));
    assertTrue(actualColumnIdentifiers.contains(expectedColumn2));
    assertTrue(actualColumnIdentifiers.contains(expectedColumn3));
  }

  /**
   * Test method for {@link ColumnCombination#toString()} <p/> A {@link ColumnCombination} should
   * return the ordered column identifiers as string representation. E.g. "[column1, column2]".
   */
  @Test
  public void testToString() {
    // Setup
    ColumnIdentifier expectedColumn1 = new ColumnIdentifier("table56", "column1");
    ColumnIdentifier expectedColumn2 = new ColumnIdentifier("table2", "column2");
    ColumnCombination columnCombination = new ColumnCombination(expectedColumn1, expectedColumn2);
    // Expected values
    String
      expectedStringRepresentation =
      "[" + expectedColumn2.toString() + ", " + expectedColumn1.toString() + "]";

    // Execute functionality
    String actualStringRepresentation = columnCombination.toString();

    // Check result
    assertEquals(expectedStringRepresentation, actualStringRepresentation);
  }

  /**
   * Test method for {@link ColumnCombination#toString()} <p/> A {@link ColumnCombination} should
   * return the ordered column identifiers as string representation. E.g. "[column1, column2]".
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
    ColumnCombination expectedColumnCombination =
      new ColumnCombination(new ColumnIdentifier("table.56", "column1"), new ColumnIdentifier("table2", "column2"));
    String expectedString = "1,2";

    // Execute functionality
    String actualString = expectedColumnCombination.toString(tableMappingTo, columnMappingTo);
    ColumnCombination actualColumnCombination = ColumnCombination.fromString(tableMappingFrom, columnMappingFrom, actualString);

    // Check result
    assertEquals(expectedString, actualString);
    assertEquals(expectedColumnCombination, actualColumnCombination);
  }

  /**
   * Test method for {@link ColumnCombination#equals(Object)} and {@link
   * ColumnCombination#hashCode()} <p/> {@link ColumnCombination}s containing the same {@link
   * ColumnIdentifier}s in different order should be equal.
   */
  @Test
  public void testEqualsHashCode() {
    // Setup
    ColumnIdentifier column11 = new ColumnIdentifier("table1", "column1");
    ColumnIdentifier column12 = new ColumnIdentifier("table2", "column2");
    ColumnIdentifier column13 = new ColumnIdentifier("table3", "column3");
    ColumnCombination
      expectedColumnCombination1 =
      new ColumnCombination(column12, column13, column11);
    ColumnIdentifier column21 = new ColumnIdentifier("table1", "column1");
    ColumnIdentifier column22 = new ColumnIdentifier("table2", "column2");
    ColumnIdentifier column23 = new ColumnIdentifier("table3", "column3");
    ColumnCombination
      expectedColumnCombination2 =
      new ColumnCombination(column21, column22, column23);
    ColumnCombination
      expectedColumnCombinationNotEquals =
      new ColumnCombination(column21, column23);

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

  @Test
  public void testCompareTo() {
    // Setup
    ColumnIdentifier column11 = new ColumnIdentifier("table1", "column1");
    ColumnIdentifier column12 = new ColumnIdentifier("table2", "column2");
    ColumnIdentifier column13 = new ColumnIdentifier("table3", "column4");
    ColumnCombination
      columnCombination1 =
      new ColumnCombination(column12, column13, column11);
    ColumnIdentifier column21 = new ColumnIdentifier("table1", "column1");
    ColumnIdentifier column22 = new ColumnIdentifier("table2", "column2");
    ColumnIdentifier column23 = new ColumnIdentifier("table3", "column2");
    ColumnCombination
      columnCombination2 =
      new ColumnCombination(column21, column22, column23);

    // Execute functionality
    // Check result
    assertTrue(columnCombination1.compareTo(columnCombination2) > 0);
    assertTrue(columnCombination1.compareTo(columnCombination1) == 0);
    assertTrue(columnCombination2.compareTo(columnCombination2) == 0);
    assertTrue(columnCombination2.compareTo(columnCombination1) < 0);
  }


}
