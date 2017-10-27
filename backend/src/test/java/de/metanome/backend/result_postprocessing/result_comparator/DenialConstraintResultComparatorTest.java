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
package de.metanome.backend.result_postprocessing.result_comparator;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.Before;
import org.junit.Test;
import de.metanome.algorithm_integration.Predicate;
import de.metanome.algorithm_integration.results.DenialConstraint;
import de.metanome.backend.result_postprocessing.results.DenialConstraintResult;

public class DenialConstraintResultComparatorTest {
  DenialConstraintResult dc1;
  DenialConstraintResult dc2;

  @Before
  public void setUp() {
    Predicate p1 = mock(Predicate.class);
    when(p1.toString()).thenReturn("p1");
    Predicate p2 = mock(Predicate.class);
    when(p1.toString()).thenReturn("p2");
    DenialConstraint result1 = new DenialConstraint(p1, p2);
    dc1 = new DenialConstraintResult(result1);

    DenialConstraint result2 = new DenialConstraint(p2);
    dc2 = new DenialConstraintResult(result2);
  }

  @Test
  public void compare1() {
    DenialConstraintResultComparator resultComparator = new DenialConstraintResultComparator(
        DenialConstraintResultComparator.PREDICATES, true);
    assertTrue(resultComparator.compare(dc1, dc2) > 0);
  }

  @Test
  public void compare2() {
    DenialConstraintResultComparator resultComparator = new DenialConstraintResultComparator(
        DenialConstraintResultComparator.PREDICATES, false);
    assertTrue(resultComparator.compare(dc1, dc2) < 0);
  }

  @Test
  public void compare3() {
    DenialConstraintResultComparator resultComparator = new DenialConstraintResultComparator(
        DenialConstraintResultComparator.SIZE, true);
    assertTrue(resultComparator.compare(dc1, dc2) > 0);
  }

  @Test
  public void compare4() {
    DenialConstraintResultComparator resultComparator = new DenialConstraintResultComparator(
        DenialConstraintResultComparator.SIZE, false);
    assertTrue(resultComparator.compare(dc1, dc2) < 0);
  }


}
