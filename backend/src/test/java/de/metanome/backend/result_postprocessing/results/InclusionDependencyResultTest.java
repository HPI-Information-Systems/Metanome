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

import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.ColumnPermutation;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.results.InclusionDependency;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class InclusionDependencyResultTest {

  @Test
  public void testInitialization() throws InputGenerationException, InputIterationException {
    // Set up
    ColumnPermutation dependant = new ColumnPermutation(
      new ColumnIdentifier("table2", "column1"));
    ColumnPermutation referenced = new ColumnPermutation(
      new ColumnIdentifier("table1", "column2"));

    // Expected Values
    InclusionDependency expectedResult = new InclusionDependency(dependant, referenced);

    // Execute functionality
    InclusionDependencyResult rankingResult = new InclusionDependencyResult(expectedResult);

    // Check
    assertEquals(expectedResult, rankingResult.getResult());
    assertEquals("table2", rankingResult.getDependantTableName());
    assertEquals("table1", rankingResult.getReferencedTableName());
  }

  @Test
  public void testEquals() throws InputGenerationException, InputIterationException {
    // Set up
    ColumnPermutation dependant1 = new ColumnPermutation(
      new ColumnIdentifier("table2", "column1"));
    ColumnPermutation dependant2 = new ColumnPermutation(
      new ColumnIdentifier("table2", "column5"));
    ColumnPermutation referenced = new ColumnPermutation(
      new ColumnIdentifier("table1", "column2"));

    // Expected Values
    InclusionDependency result1 = new InclusionDependency(dependant1, referenced);
    InclusionDependency result2 = new InclusionDependency(dependant2, referenced);

    // Execute functionality
    InclusionDependencyResult rankingResult1 = new InclusionDependencyResult(result1);
    InclusionDependencyResult rankingResult2 = new InclusionDependencyResult(result2);

    // Check
    assertEquals(rankingResult1, rankingResult1);
    assertNotEquals(rankingResult1, rankingResult2);
  }

}
