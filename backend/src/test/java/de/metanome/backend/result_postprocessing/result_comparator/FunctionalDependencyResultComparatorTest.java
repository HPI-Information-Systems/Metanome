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

import de.metanome.algorithm_integration.ColumnCombination;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.results.FunctionalDependency;
import de.metanome.backend.result_postprocessing.results.FunctionalDependencyResult;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FunctionalDependencyResultComparatorTest {

  FunctionalDependencyResult fd1;
  FunctionalDependencyResult fd2;

  @Before
  public void setUp() {
    ColumnCombination determinant1 = new ColumnCombination(new ColumnIdentifier("table1", "column5"));
    ColumnIdentifier dependant1 = new ColumnIdentifier("table1", "column2");
    FunctionalDependency result1 = new FunctionalDependency(determinant1, dependant1);
    fd1 = new FunctionalDependencyResult(result1);
    fd1.setExtendedDependant(new ColumnCombination(new ColumnIdentifier("table1", "column5"), new ColumnIdentifier("table1", "column7")));
    fd1.setDependantColumnRatio(1.2f);
    fd1.setDeterminantColumnRatio(3.4f);
    fd1.setGeneralCoverage(3.3f);
    fd1.setDependantOccurrenceRatio(3.3f);
    fd1.setDeterminantOccurrenceRatio(5.3f);
    fd1.setDependantUniquenessRatio(3.3f);
    fd1.setDeterminantUniquenessRatio(5.3f);
    fd1.setPollution(2.3f);
    fd1.setPollutionColumn("E");
    fd1.setInformationGainCells(4.3f);
    fd1.setInformationGainBytes(5.3f);

    ColumnCombination determinant2 = new ColumnCombination(new ColumnIdentifier("table1", "column4"));
    ColumnIdentifier dependant2 = new ColumnIdentifier("table1", "column3");
    FunctionalDependency result2 = new FunctionalDependency(determinant2, dependant2);
    fd2 = new FunctionalDependencyResult(result2);
    fd2.setExtendedDependant(new ColumnCombination(new ColumnIdentifier("table1", "column2"), new ColumnIdentifier("table1", "column4")));
    fd2.setDependantColumnRatio(4.2f);
    fd2.setDeterminantColumnRatio(1.2f);
    fd2.setGeneralCoverage(3.3f);
    fd2.setDependantOccurrenceRatio(6.2f);
    fd2.setDeterminantOccurrenceRatio(3.3f);
    fd2.setDependantUniquenessRatio(6.2f);
    fd2.setDeterminantUniquenessRatio(3.3f);
    fd2.setPollution(1.2f);
    fd2.setPollutionColumn("D");
    fd2.setInformationGainCells(3.2f);
    fd2.setInformationGainBytes(2.2f);
  }

  @Test
  public void compareDependantAsc() {
    FunctionalDependencyResultComparator resultComparator =
        new FunctionalDependencyResultComparator(
            FunctionalDependencyResultComparator.DEPENDANT_COLUMN, true);
    assertEquals(-1, resultComparator.compare(fd1, fd2));
  }

  @Test
  public void compareDependantDesc() {
    FunctionalDependencyResultComparator resultComparator =
        new FunctionalDependencyResultComparator(
            FunctionalDependencyResultComparator.DEPENDANT_COLUMN, false);
    assertEquals(1, resultComparator.compare(fd1, fd2));
  }

  @Test
  public void compareReferenceAsc() {
    FunctionalDependencyResultComparator resultComparator =
        new FunctionalDependencyResultComparator(
            FunctionalDependencyResultComparator.DETERMINANT_COLUMN, true);
    assertEquals(1, resultComparator.compare(fd1, fd2));
  }

  @Test
  public void compareReferenceDesc() {
    FunctionalDependencyResultComparator resultComparator =
        new FunctionalDependencyResultComparator(
            FunctionalDependencyResultComparator.DETERMINANT_COLUMN, false);
    assertEquals(-1, resultComparator.compare(fd1, fd2));
  }

  @Test
  public void compareExtendedDependantAsc() {
    FunctionalDependencyResultComparator resultComparator =
        new FunctionalDependencyResultComparator(
            FunctionalDependencyResultComparator.EXTENDED_DEPENDANT_COLUMN, true);
    assertTrue(resultComparator.compare(fd1, fd2) > 0);
  }

  @Test
  public void compareExtendedDependantDesc() {
    FunctionalDependencyResultComparator resultComparator =
        new FunctionalDependencyResultComparator(
            FunctionalDependencyResultComparator.EXTENDED_DEPENDANT_COLUMN, false);
    assertTrue(resultComparator.compare(fd1, fd2) < 0);
  }

  @Test
  public void compareDeterminantColumnRatioAsc() {
    FunctionalDependencyResultComparator resultComparator =
        new FunctionalDependencyResultComparator(
            FunctionalDependencyResultComparator.DETERMINANT_COLUMN_RATIO, true);
    assertTrue(resultComparator.compare(fd1, fd2) > 0);
  }

  @Test
  public void compareDeterminantColumnRatioDesc() {
    FunctionalDependencyResultComparator resultComparator =
        new FunctionalDependencyResultComparator(
            FunctionalDependencyResultComparator.DETERMINANT_COLUMN_RATIO, false);
    assertTrue(resultComparator.compare(fd1, fd2) < 0);
  }

  @Test
  public void compareDependantColumnRatioAsc() {
    FunctionalDependencyResultComparator resultComparator =
        new FunctionalDependencyResultComparator(
            FunctionalDependencyResultComparator.DEPENDANT_COLUMN_RATIO, true);
    assertTrue(resultComparator.compare(fd1, fd2) < 0);
  }

  @Test
  public void compareDepedantColumnRatioDesc() {
    FunctionalDependencyResultComparator resultComparator =
        new FunctionalDependencyResultComparator(
            FunctionalDependencyResultComparator.DEPENDANT_COLUMN_RATIO, false);
    assertTrue(resultComparator.compare(fd1, fd2) > 0);
  }

  @Test
  public void compareGeneralCoverageAsc() {
    FunctionalDependencyResultComparator resultComparator =
        new FunctionalDependencyResultComparator(
            FunctionalDependencyResultComparator.GENERAL_COVERAGE, true);
    assertTrue(resultComparator.compare(fd1, fd2) == 0);
  }

  @Test
  public void compareGeneralCoverageDesc() {
    FunctionalDependencyResultComparator resultComparator =
        new FunctionalDependencyResultComparator(
            FunctionalDependencyResultComparator.GENERAL_COVERAGE, false);
    assertTrue(resultComparator.compare(fd1, fd2) == 0);
  }

  @Test
  public void compareDependantOccurrenceRatioAsc() {
    FunctionalDependencyResultComparator resultComparator =
        new FunctionalDependencyResultComparator(
            FunctionalDependencyResultComparator.DEPENDANT_OCCURRENCE_RATIO, true);
    assertTrue(resultComparator.compare(fd1, fd2) < 0);
  }

  @Test
  public void compareDependantOccurrenceRatioDesc() {
    FunctionalDependencyResultComparator resultComparator =
        new FunctionalDependencyResultComparator(
            FunctionalDependencyResultComparator.DEPENDANT_OCCURRENCE_RATIO, false);
    assertTrue(resultComparator.compare(fd1, fd2) > 0);
  }

  @Test
  public void compareDeterminantOccurrenceRatioAsc() {
    FunctionalDependencyResultComparator resultComparator =
        new FunctionalDependencyResultComparator(
            FunctionalDependencyResultComparator.DETERMINANT_OCCURRENCE_RATIO, true);
    assertTrue(resultComparator.compare(fd1, fd2) > 0);
  }

  @Test
  public void compareDeterminantOccurrenceRatioDesc() {
    FunctionalDependencyResultComparator resultComparator =
        new FunctionalDependencyResultComparator(
            FunctionalDependencyResultComparator.DETERMINANT_OCCURRENCE_RATIO, false);
    assertTrue(resultComparator.compare(fd1, fd2) < 0);
  }

  @Test
  public void compareDependantUniquenessRatioAsc() {
    FunctionalDependencyResultComparator resultComparator =
        new FunctionalDependencyResultComparator(
            FunctionalDependencyResultComparator.DEPENDANT_UNIQUENESS_RATIO, true);
    assertTrue(resultComparator.compare(fd1, fd2) < 0);
  }

  @Test
  public void compareDependantUniquenessRatioDesc() {
    FunctionalDependencyResultComparator resultComparator =
        new FunctionalDependencyResultComparator(
            FunctionalDependencyResultComparator.DEPENDANT_UNIQUENESS_RATIO, false);
    assertTrue(resultComparator.compare(fd1, fd2) > 0);
  }

  @Test
  public void compareDeterminantUniquenessRatioAsc() {
    FunctionalDependencyResultComparator resultComparator =
        new FunctionalDependencyResultComparator(
            FunctionalDependencyResultComparator.DETERMINANT_UNIQUENESS_RATIO, true);
    assertTrue(resultComparator.compare(fd1, fd2) > 0);
  }

  @Test
  public void compareDeterminantUniquenessRatioDesc() {
    FunctionalDependencyResultComparator resultComparator =
        new FunctionalDependencyResultComparator(
            FunctionalDependencyResultComparator.DETERMINANT_UNIQUENESS_RATIO, false);
    assertTrue(resultComparator.compare(fd1, fd2) < 0);
  }

  @Test
  public void comparePollutionAsc() {
    FunctionalDependencyResultComparator resultComparator =
        new FunctionalDependencyResultComparator(
            FunctionalDependencyResultComparator.POLLUTION, true);
    assertTrue(resultComparator.compare(fd1, fd2) > 0);
  }

  @Test
  public void comparePollutionDesc() {
    FunctionalDependencyResultComparator resultComparator =
        new FunctionalDependencyResultComparator(
            FunctionalDependencyResultComparator.POLLUTION, false);
    assertTrue(resultComparator.compare(fd1, fd2) < 0);
  }

  @Test
  public void comparePollutionColumnAsc() {
    FunctionalDependencyResultComparator resultComparator =
        new FunctionalDependencyResultComparator(
            FunctionalDependencyResultComparator.POLLUTION_COLUMN, true);
    assertTrue(resultComparator.compare(fd1, fd2) > 0);
  }

  @Test
  public void comparePollutionColumnDesc() {
    FunctionalDependencyResultComparator resultComparator =
        new FunctionalDependencyResultComparator(
            FunctionalDependencyResultComparator.POLLUTION_COLUMN, false);
    assertTrue(resultComparator.compare(fd1, fd2) < 0);
  }

  @Test
  public void compareInformationGainCellAsc() {
    FunctionalDependencyResultComparator resultComparator =
        new FunctionalDependencyResultComparator(
            FunctionalDependencyResultComparator.INFORMATION_GAIN_CELL, true);
    assertTrue(resultComparator.compare(fd1, fd2) > 0);
  }

  @Test
  public void compareInformationGainCellDesc() {
    FunctionalDependencyResultComparator resultComparator =
        new FunctionalDependencyResultComparator(
            FunctionalDependencyResultComparator.INFORMATION_GAIN_CELL, false);
    assertTrue(resultComparator.compare(fd1, fd2) < 0);
  }

  @Test
  public void compareInformationGainByteAsc() {
    FunctionalDependencyResultComparator resultComparator =
        new FunctionalDependencyResultComparator(
            FunctionalDependencyResultComparator.INFORMATION_GAIN_BYTE, true);
    assertTrue(resultComparator.compare(fd1, fd2) > 0);
  }

  @Test
  public void compareInformationGainByteDesc() {
    FunctionalDependencyResultComparator resultComparator =
        new FunctionalDependencyResultComparator(
            FunctionalDependencyResultComparator.INFORMATION_GAIN_BYTE, false);
    assertTrue(resultComparator.compare(fd1, fd2) < 0);
  }


}
