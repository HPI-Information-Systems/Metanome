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
package de.metanome.backend.result_postprocessing.results;

import de.metanome.algorithm_integration.ColumnCombination;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.results.FunctionalDependency;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class FunctionalDependencyResultTest {

  @Test
  public void testInitialization() throws InputGenerationException, InputIterationException {
    // Set up
    ColumnCombination determinant = new ColumnCombination(new ColumnIdentifier("table2", "column5"));
    ColumnIdentifier dependant = new ColumnIdentifier("table1", "column2");

    // Expected Values
    FunctionalDependency expectedResult = new FunctionalDependency(
      determinant, dependant
    );

    // Execute functionality
    FunctionalDependencyResult rankingResult = new FunctionalDependencyResult(expectedResult);

    // Check
    assertEquals(expectedResult, rankingResult.getResult());
    assertEquals("table2", rankingResult.getDeterminantTableName());
    assertEquals("table1", rankingResult.getDependantTableName());
  }

  @Test
  public void testEquals() throws InputGenerationException, InputIterationException {
    // Set up
    ColumnCombination determinant1 = new ColumnCombination(new ColumnIdentifier("table1", "column5"));
    ColumnIdentifier dependant1 = new ColumnIdentifier("table1", "column2");
    ColumnCombination determinant2 = new ColumnCombination(new ColumnIdentifier("table2", "column3"));
    ColumnIdentifier dependant2 = new ColumnIdentifier("table1", "column7");

    // Expected Values
    FunctionalDependency result1 = new FunctionalDependency(
      determinant1, dependant1
    );
    FunctionalDependency result2 = new FunctionalDependency(
      determinant2, dependant2
    );

    // Execute functionality
    FunctionalDependencyResult rankingResult1 = new FunctionalDependencyResult(result1);
    FunctionalDependencyResult rankingResult2 = new FunctionalDependencyResult(result2);

    // Check
    assertEquals(rankingResult1, rankingResult1);
    assertNotEquals(rankingResult1, rankingResult2);
  }

}
