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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.MatchingCombination;
import de.metanome.algorithm_integration.MatchingIdentifier;
import de.metanome.algorithm_integration.result_receiver.ColumnNameMismatchException;
import de.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

/**
 * Test for {@link MatchingDependency}
 *
 * @author Philipp Schirmer
 */
public class MatchingDependencyTest {

  /**
   * Test method for {@link MatchingDependency#sendResultTo(OmniscientResultReceiver)} <p/> The
   * {@link MatchingDependency} should be sendable to the {@link OmniscientResultReceiver}.
   */
  @Test
  public void testSendResultTo() throws CouldNotReceiveResultException, ColumnNameMismatchException {
    // Setup
    OmniscientResultReceiver resultReceiver = mock(OmniscientResultReceiver.class);
    MatchingDependency
      md =
      new MatchingDependency(mock(MatchingCombination.class), mock(MatchingIdentifier.class), 10L);

    // Execute functionality
    md.sendResultTo(resultReceiver);

    // Check result
    verify(resultReceiver).receiveResult(md);
  }

  /**
   * Test method for {@link MatchingDependency#MatchingDependency(MatchingCombination,
   * MatchingIdentifier, long)} <p/> A {@link MatchingDependency} should store the determinant {@link
   * MatchingCombination} and the dependant {@link MatchingIdentifier}.
   */
  @Test
  public void testConstructor() {
    // Setup
    // Expected values
    MatchingCombination
      expectedDeterminant =
      new MatchingCombination(new MatchingIdentifier(
          new ColumnIdentifier("table1", "column1"),
          new ColumnIdentifier("table2", "column2"),
          "sim", 0.5),
          new MatchingIdentifier(
              new ColumnIdentifier("table1", "column3"),
              new ColumnIdentifier("table2", "column4"),
              "sim", 0.5));
    MatchingIdentifier expectedDependant = new MatchingIdentifier(
        new ColumnIdentifier("table1", "column7"),
        new ColumnIdentifier("table2", "column8"),
        "sim", 0.5);
    // Execute functionality
    MatchingDependency
      matchingDependency =
      new MatchingDependency(expectedDeterminant, expectedDependant, 10);

    // Check result
    assertEquals(expectedDeterminant, matchingDependency.getDeterminant());
    assertEquals(expectedDependant, matchingDependency.getDependant());
  }

  /**
   * Test method for {@link MatchingDependency#toString()} <p/> A {@link MatchingDependency}
   * should return a human readable string representation.
   */
  @Test
  public void testToString() {
    // Setup
    MatchingCombination
        expectedDeterminant =
        new MatchingCombination(new MatchingIdentifier(
            new ColumnIdentifier("table1", "column1"),
            new ColumnIdentifier("table2", "column2"),
            "sim", 0.5),
            new MatchingIdentifier(
                new ColumnIdentifier("table1", "column3"),
                new ColumnIdentifier("table2", "column4"),
                "sim", 0.5));
    MatchingIdentifier expectedDependant = new MatchingIdentifier(
        new ColumnIdentifier("table1", "column7"),
        new ColumnIdentifier("table2", "column8"),
        "sim", 0.5);
    // Execute functionality
    MatchingDependency
        matchingDependency =
        new MatchingDependency(expectedDeterminant, expectedDependant, 10);
    // Expected values
    String
      expectedStringRepresentation =
      expectedDeterminant + MatchingDependency.MD_SEPARATOR + expectedDependant + MatchingDependency.SUPPORT_SEPARATOR + 10;

    // Execute functionality
    // Check result
    assertEquals(expectedStringRepresentation, matchingDependency.toString());
  }

  /**
   * Test method for {@link MatchingDependency#toString()} <p/> A {@link MatchingDependency}
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
    columnMappingTo.put("1.column3", "3");
    columnMappingTo.put("2.column4", "4");
    columnMappingTo.put("1.column7", "5");
    columnMappingTo.put("2.column8", "6");
    Map<String, String> tableMappingFrom = new HashMap<>();
    tableMappingFrom.put("1", "table1");
    tableMappingFrom.put("2", "table2");
    Map<String, String> columnMappingFrom = new HashMap<>();
    columnMappingFrom.put("1", "1.column1");
    columnMappingFrom.put("2", "2.column2");
    columnMappingFrom.put("3", "1.column3");
    columnMappingFrom.put("4", "2.column4");
    columnMappingFrom.put("5", "1.column7");
    columnMappingFrom.put("6", "2.column8");

    MatchingCombination
        expectedDeterminant =
        new MatchingCombination(new MatchingIdentifier(
            new ColumnIdentifier("table1", "column1"),
            new ColumnIdentifier("table2", "column2"),
            "sim", 0.5),
            new MatchingIdentifier(
                new ColumnIdentifier("table1", "column3"),
                new ColumnIdentifier("table2", "column4"),
                "sim", 0.5));
    MatchingIdentifier expectedDependant = new MatchingIdentifier(
        new ColumnIdentifier("table1", "column7"),
        new ColumnIdentifier("table2", "column8"),
        "sim", 0.5);
    MatchingDependency
        expectedMD =
        new MatchingDependency(expectedDeterminant, expectedDependant, 10);

    // Execute functionality
    String actualString = expectedMD.toString(tableMappingTo, columnMappingTo);
    MatchingDependency actualMD = MatchingDependency.fromString(tableMappingFrom, columnMappingFrom, actualString);

    // Check result
    assertEquals(expectedMD, actualMD);
  }

  /**
   * Test method for {@link MatchingDependency#equals(Object)} and {@link
   * MatchingDependency#hashCode()} <p/> {@link MatchingDependency}s with equal determinant and
   * dependants should be equal.
   */
  @Test
  public void testEqualsHashCode() {
    // Setup
    MatchingDependency expectedMd = new MatchingDependency(
      new MatchingCombination(
        new MatchingIdentifier(new ColumnIdentifier("table1", "column2"), new ColumnIdentifier("table1", "column2"), "sim", 0.5)),
        new MatchingIdentifier(new ColumnIdentifier("table1", "column2"), new ColumnIdentifier("table1", "column2"), "sim", 0.5),
        10
    );
    MatchingDependency expectedEqualMd = new MatchingDependency(
        new MatchingCombination(
            new MatchingIdentifier(new ColumnIdentifier("table1", "column2"), new ColumnIdentifier("table1", "column2"), "sim", 0.5)),
        new MatchingIdentifier(new ColumnIdentifier("table1", "column2"), new ColumnIdentifier("table1", "column2"), "sim", 0.5),
        10
    );
    MatchingDependency expectedNotEqualDeterminantMd  = new MatchingDependency(
        new MatchingCombination(
            new MatchingIdentifier(new ColumnIdentifier("table1", "column1"), new ColumnIdentifier("table1", "column2"), "sim", 0.5)),
        new MatchingIdentifier(new ColumnIdentifier("table1", "column2"), new ColumnIdentifier("table1", "column2"), "sim", 0.5),
        10
    );
    MatchingDependency expectedNotEqualDependantMd = new MatchingDependency(
        new MatchingCombination(
            new MatchingIdentifier(new ColumnIdentifier("table1", "column2"), new ColumnIdentifier("table1", "column2"), "sim", 0.5)),
        new MatchingIdentifier(new ColumnIdentifier("table1", "column3"), new ColumnIdentifier("table1", "column2"), "sim", 0.5),
        10
    );
    MatchingDependency expectedNotEqualSupportMd = new MatchingDependency(
        new MatchingCombination(
            new MatchingIdentifier(new ColumnIdentifier("table1", "column2"), new ColumnIdentifier("table1", "column2"), "sim", 0.5)),
        new MatchingIdentifier(new ColumnIdentifier("table1", "column2"), new ColumnIdentifier("table1", "column2"), "sim", 0.5),
        9
    );
    MatchingDependency expectedNotEqualThresholdMd = new MatchingDependency(
        new MatchingCombination(
            new MatchingIdentifier(new ColumnIdentifier("table1", "column2"), new ColumnIdentifier("table1", "column2"), "sim", 0.3)),
        new MatchingIdentifier(new ColumnIdentifier("table1", "column2"), new ColumnIdentifier("table1", "column2"), "sim", 0.5),
        10
    );
    MatchingDependency expectedNotEqualSimMd = new MatchingDependency(
        new MatchingCombination(
            new MatchingIdentifier(new ColumnIdentifier("table1", "column2"), new ColumnIdentifier("table1", "column2"), "foo", 0.5)),
        new MatchingIdentifier(new ColumnIdentifier("table1", "column2"), new ColumnIdentifier("table1", "column2"), "sim", 0.5),
        10
    );

    // Execute functionality
    // Check result
    assertEquals(expectedMd, expectedMd);
    assertEquals(expectedMd.hashCode(), expectedMd.hashCode());
    assertNotSame(expectedMd, expectedEqualMd);
    assertEquals(expectedMd, expectedEqualMd);
    assertEquals(expectedMd.hashCode(), expectedEqualMd.hashCode());
    assertNotEquals(expectedMd, expectedNotEqualDeterminantMd);
    assertNotEquals(expectedMd.hashCode(), expectedNotEqualDeterminantMd.hashCode());
    assertNotEquals(expectedMd, expectedNotEqualDependantMd);
    assertNotEquals(expectedMd.hashCode(), expectedNotEqualDependantMd.hashCode());
    assertNotEquals(expectedMd, expectedNotEqualSupportMd);
    assertNotEquals(expectedMd.hashCode(), expectedNotEqualSupportMd.hashCode());
    assertNotEquals(expectedMd, expectedNotEqualThresholdMd);
    assertNotEquals(expectedMd.hashCode(), expectedNotEqualThresholdMd.hashCode());
    assertNotEquals(expectedMd, expectedNotEqualSimMd);
    assertNotEquals(expectedMd.hashCode(), expectedNotEqualSimMd.hashCode());
  }


}
