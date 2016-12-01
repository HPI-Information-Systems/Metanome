/**
 * Copyright 2015-2016 by Metanome Project
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
import de.metanome.algorithm_integration.results.InclusionDependency;
import de.metanome.backend.result_postprocessing.results.InclusionDependencyResult;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InclusionDependencyResultComparatorTest {

  InclusionDependencyResult ind1;
  InclusionDependencyResult ind2;

  @Before
  public void setUp() {
    ColumnPermutation expectedDependant1 = new ColumnPermutation(
        new ColumnIdentifier("table1", "column1"));
    ColumnPermutation expectedReferenced1 = new ColumnPermutation(
        new ColumnIdentifier("table1", "column2"));
    InclusionDependency result1 = new InclusionDependency(expectedDependant1, expectedReferenced1);
    ind1 = new InclusionDependencyResult(result1);
    ind1.setDependantColumnRatio(0.5f);
    ind1.setReferencedColumnRatio(0.3f);
    ind1.setDependantOccurrenceRatio(0.6f);
    ind1.setReferencedOccurrenceRatio(0.7f);
    ind1.setDependantUniquenessRatio(0.4f);
    ind1.setReferencedUniquenessRatio(0.1f);
    ind1.setGeneralCoverage(1.3f);

    ColumnPermutation expectedDependant2 = new ColumnPermutation(
        new ColumnIdentifier("table1", "column2"));
    ColumnPermutation expectedReferenced2 = new ColumnPermutation(
        new ColumnIdentifier("table1", "column1"));
    InclusionDependency result2 = new InclusionDependency(expectedDependant2, expectedReferenced2);
    ind2 = new InclusionDependencyResult(result2);
    ind2.setDependantColumnRatio(0.6f);
    ind2.setReferencedColumnRatio(0.2f);
    ind2.setDependantOccurrenceRatio(0.9f);
    ind2.setReferencedOccurrenceRatio(0.6f);
    ind2.setDependantUniquenessRatio(0.7f);
    ind2.setReferencedUniquenessRatio(0.3f);
    ind2.setGeneralCoverage(1.3f);
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

  @Test
  public void compareDependantColumnRatioAsc() {
    InclusionDependencyResultComparator
        resultComparator =
        new InclusionDependencyResultComparator(
            InclusionDependencyResultComparator.DEPENDANT_COLUMN_RATIO, true);
    assertEquals(-1, resultComparator.compare(ind1, ind2));
  }

  @Test
  public void compareDependantColumnRatioDesc() {
    InclusionDependencyResultComparator
        resultComparator =
        new InclusionDependencyResultComparator(
            InclusionDependencyResultComparator.DEPENDANT_COLUMN_RATIO, false);
    assertEquals(1, resultComparator.compare(ind1, ind2));
  }

  @Test
  public void compareReferenceColumnRatioAsc() {
    InclusionDependencyResultComparator
        resultComparator =
        new InclusionDependencyResultComparator(
            InclusionDependencyResultComparator.REFERENCED_COLUMN_RATIO, true);
    assertEquals(1, resultComparator.compare(ind1, ind2));
  }

  @Test
  public void compareReferenceColumnRatioDesc() {
    InclusionDependencyResultComparator
        resultComparator =
        new InclusionDependencyResultComparator(
            InclusionDependencyResultComparator.REFERENCED_COLUMN_RATIO, false);
    assertEquals(-1, resultComparator.compare(ind1, ind2));
  }


  @Test
  public void compareDependantOccurrenceRatioAsc() {
    InclusionDependencyResultComparator
        resultComparator =
        new InclusionDependencyResultComparator(
            InclusionDependencyResultComparator.DEPENDANT_OCCURRENCE_RATIO, true);
    assertEquals(-1, resultComparator.compare(ind1, ind2));
  }

  @Test
  public void compareDependantOccurrenceRatioDesc() {
    InclusionDependencyResultComparator
        resultComparator =
        new InclusionDependencyResultComparator(
            InclusionDependencyResultComparator.DEPENDANT_OCCURRENCE_RATIO, false);
    assertEquals(1, resultComparator.compare(ind1, ind2));
  }

  @Test
  public void compareReferenceOccurrenceRatioAsc() {
    InclusionDependencyResultComparator
        resultComparator =
        new InclusionDependencyResultComparator(
            InclusionDependencyResultComparator.REFERENCED_OCCURRENCE_RATIO, true);
    assertEquals(1, resultComparator.compare(ind1, ind2));
  }

  @Test
  public void compareReferenceOccurrenceRatioDesc() {
    InclusionDependencyResultComparator
        resultComparator =
        new InclusionDependencyResultComparator(
            InclusionDependencyResultComparator.REFERENCED_OCCURRENCE_RATIO, false);
    assertEquals(-1, resultComparator.compare(ind1, ind2));
  }


  @Test
  public void compareDependantUniquenessRatioAsc() {
    InclusionDependencyResultComparator
        resultComparator =
        new InclusionDependencyResultComparator(
            InclusionDependencyResultComparator.DEPENDANT_UNIQUENESS_RATIO, true);
    assertEquals(-1, resultComparator.compare(ind1, ind2));
  }

  @Test
  public void compareDependantUniquenessRatioDesc() {
    InclusionDependencyResultComparator
        resultComparator =
        new InclusionDependencyResultComparator(
            InclusionDependencyResultComparator.DEPENDANT_UNIQUENESS_RATIO, false);
    assertEquals(1, resultComparator.compare(ind1, ind2));
  }

  @Test
  public void compareReferenceUniquenessRatioAsc() {
    InclusionDependencyResultComparator
        resultComparator =
        new InclusionDependencyResultComparator(
            InclusionDependencyResultComparator.REFERENCED_UNIQUENESS_RATIO, true);
    assertEquals(-1, resultComparator.compare(ind1, ind2));
  }

  @Test
  public void compareReferenceUniquenessRatioDesc() {
    InclusionDependencyResultComparator
        resultComparator =
        new InclusionDependencyResultComparator(
            InclusionDependencyResultComparator.REFERENCED_UNIQUENESS_RATIO, false);
    assertEquals(1, resultComparator.compare(ind1, ind2));
  }

  @Test
  public void compareCoverageAsc() {
    InclusionDependencyResultComparator
        resultComparator =
        new InclusionDependencyResultComparator(
            InclusionDependencyResultComparator.COVERAGE, true);
    assertEquals(0, resultComparator.compare(ind1, ind2));
  }

  @Test
  public void compareCoverageDesc() {
    InclusionDependencyResultComparator
        resultComparator =
        new InclusionDependencyResultComparator(
            InclusionDependencyResultComparator.COVERAGE, false);
    assertEquals(0, resultComparator.compare(ind1, ind2));
  }

}
