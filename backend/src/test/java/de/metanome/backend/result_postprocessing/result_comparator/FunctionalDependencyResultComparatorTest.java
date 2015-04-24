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

import de.metanome.algorithm_integration.ColumnCombination;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.results.FunctionalDependency;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FunctionalDependencyResultComparatorTest {


  ColumnCombination determinant1 = new ColumnCombination(new ColumnIdentifier("table1", "column5"));
  ColumnIdentifier dependant1 = new ColumnIdentifier("table1", "column2");
  FunctionalDependency fd1 = new FunctionalDependency(determinant1, dependant1);

  ColumnCombination determinant2 = new ColumnCombination(new ColumnIdentifier("table1", "column4"));
  ColumnIdentifier dependant2 = new ColumnIdentifier("table1", "column3");
  FunctionalDependency fd2 = new FunctionalDependency(determinant2, dependant2);

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


}
