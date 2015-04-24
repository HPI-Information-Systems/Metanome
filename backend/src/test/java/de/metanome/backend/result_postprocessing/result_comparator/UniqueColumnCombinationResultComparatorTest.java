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
import de.metanome.algorithm_integration.results.UniqueColumnCombination;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UniqueColumnCombinationResultComparatorTest {

  ColumnIdentifier column1 = new ColumnIdentifier("table1", "column1");
  ColumnIdentifier column2 = new ColumnIdentifier("table1", "column2");
  ColumnIdentifier column3 = new ColumnIdentifier("table1", "column3");
  ColumnIdentifier column4 = new ColumnIdentifier("table1", "column4");

  UniqueColumnCombination ucc1 = new UniqueColumnCombination(column1, column2, column2);
  UniqueColumnCombination ucc2 = new UniqueColumnCombination(column3, column4, column2);

  @Test
  public void compare1() {
    UniqueColumnCombinationResultComparator resultComparator =
        new UniqueColumnCombinationResultComparator(UniqueColumnCombinationResultComparator.COLUMN_COMBINATION_COLUMN, true);
    assertEquals(-1, resultComparator.compare(ucc1, ucc2));
  }

  @Test
  public void compare2() {
    UniqueColumnCombinationResultComparator resultComparator =
        new UniqueColumnCombinationResultComparator(UniqueColumnCombinationResultComparator.COLUMN_COMBINATION_COLUMN, false);
    assertEquals(1, resultComparator.compare(ucc1, ucc2));
  }

}
