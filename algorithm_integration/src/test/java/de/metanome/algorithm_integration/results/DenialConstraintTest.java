/**
 * Copyright 2017 by Metanome Project
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
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import de.metanome.algorithm_integration.Predicate;
import de.metanome.algorithm_integration.result_receiver.ColumnNameMismatchException;
import de.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;

/**
 * Test for {@link DenialConstraint}
 */
public class DenialConstraintTest {

  @Before
  public void setUp() throws Exception {}

  @After
  public void tearDown() throws Exception {}

  /**
   * Test method for {@link DenialConstraint#sendResultTo(OmniscientResultReceiver)}
   * <p/>
   * The {@link DenialConstraint} should be sendable to the {@link OmniscientResultReceiver}.
   */
  @Test
  public void testSendResultTo()
      throws CouldNotReceiveResultException, ColumnNameMismatchException {
    // Setup
    OmniscientResultReceiver resultReceiver = mock(OmniscientResultReceiver.class);
    DenialConstraint dc = new DenialConstraint();

    // Execute functionality
    dc.sendResultTo(resultReceiver);

    // Check result
    verify(resultReceiver).receiveResult(dc);
  }

  /**
   * Test method for {@link DenialConstraint#DenialConstraint(Predicate)}
   * <p/>
   * A {@link DenialConstraint} should store the predicedst.
   */
  @Test
  public void testConstructor() {
    // Setup
    // Expected values
    Predicate p = mock(Predicate.class);
    // Execute functionality
    DenialConstraint dc = new DenialConstraint(p, p);

    // Check result
    assertTrue(dc.getPredicates().contains(p));
  }

  /**
   * Test method for {@link DenialConstraint#toString()}
   * <p/>
   * A {@link DenialConstraint} should return a human readable string representation.
   */
  @Test
  public void testToString() {
    Predicate p1 = mock(Predicate.class);
    when(p1.toString()).thenReturn("p1");
    Predicate p2 = mock(Predicate.class);
    when(p2.toString()).thenReturn("p2");

    // Setup
    DenialConstraint dc1 = new DenialConstraint(p1, p2);
    DenialConstraint dc2 = new DenialConstraint(p2, p1);
    // Expected values
    String expectedStringRepresentation =
        DenialConstraint.NOT + "(p1" + DenialConstraint.AND + "p2)";

    // Execute functionality
    // Check result
    assertEquals(expectedStringRepresentation, dc1.toString());
    assertEquals(expectedStringRepresentation, dc2.toString());
  }


  /**
   * Test method for {@link DenialConstraint#equals(Object)} and {@link DenialConstraint#hashCode()}
   * <p/>
   * {@link DenialConstraint}s with equal determinant and dependants should be equal.
   */
  @Test
  public void testEqualsHashCode() {
    Predicate p1 = mock(Predicate.class);
    Predicate p2 = mock(Predicate.class);
    Predicate p3 = mock(Predicate.class);

    // Setup
    DenialConstraint expectedDC = new DenialConstraint(p1, p2);
    DenialConstraint expectedEqualDC = new DenialConstraint(p2, p1);
    DenialConstraint expectedNotEqualDC = new DenialConstraint(p1, p2, p3);

    // Execute functionality
    // Check result
    assertEquals(expectedDC, expectedDC);
    assertEquals(expectedDC.hashCode(), expectedDC.hashCode());
    assertEquals(expectedDC, expectedEqualDC);
    assertEquals(expectedDC.hashCode(), expectedEqualDC.hashCode());
    assertNotEquals(expectedDC, expectedNotEqualDC);
    assertNotEquals(expectedDC.hashCode(), expectedNotEqualDC.hashCode());
  }


}
