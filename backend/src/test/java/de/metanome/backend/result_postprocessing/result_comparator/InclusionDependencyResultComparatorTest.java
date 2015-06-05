/*
 * Copyright 2015 by the Metanome project
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

import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.ColumnPermutation;
import de.metanome.backend.result_postprocessing.results.InclusionDependencyResult;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InclusionDependencyResultComparatorTest {

  InclusionDependencyResult ind1 = new InclusionDependencyResult();
  InclusionDependencyResult ind2 = new InclusionDependencyResult();

  @Before
  public void setUp() {
    ColumnPermutation expectedDependant1 = new ColumnPermutation(
        new ColumnIdentifier("table1", "column1"));
    ColumnPermutation expectedReferenced1 = new ColumnPermutation(
        new ColumnIdentifier("table1", "column2"));
    ind1.setDependant(expectedDependant1);
    ind1.setReferenced(expectedReferenced1);

    ColumnPermutation expectedDependant2 = new ColumnPermutation(
        new ColumnIdentifier("table1", "column2"));
    ColumnPermutation expectedReferenced2 = new ColumnPermutation(
        new ColumnIdentifier("table1", "column1"));
    ind2.setDependant(expectedDependant2);
    ind2.setReferenced(expectedReferenced2);
  }

  @Test
  public void compareDependantAsc() {
    InclusionDependencyResultComparator
        resultComparator =
        new InclusionDependencyResultComparator(
            InclusionDependencyResultComparator.DEPENDANT_COLUMN, true);
    assertEquals(-1, resultComparator.compare(ind1, ind2));
  }

  @Test
  public void compareDependantDesc() {
    InclusionDependencyResultComparator
        resultComparator =
        new InclusionDependencyResultComparator(
            InclusionDependencyResultComparator.DEPENDANT_COLUMN, false);
    assertEquals(1, resultComparator.compare(ind1, ind2));
  }

  @Test
  public void compareReferenceAsc() {
    InclusionDependencyResultComparator
        resultComparator =
        new InclusionDependencyResultComparator(
            InclusionDependencyResultComparator.REFERENCED_COLUMN, true);
    assertEquals(1, resultComparator.compare(ind1, ind2));
  }

  @Test
  public void compareReferenceDesc() {
    InclusionDependencyResultComparator
        resultComparator =
        new InclusionDependencyResultComparator(
            InclusionDependencyResultComparator.REFERENCED_COLUMN, false);
    assertEquals(-1, resultComparator.compare(ind1, ind2));
  }

}
