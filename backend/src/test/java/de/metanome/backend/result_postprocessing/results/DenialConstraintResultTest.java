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
package de.metanome.backend.result_postprocessing.results;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.results.DenialConstraint;

public class DenialConstraintResultTest {
  @Test
  public void testInitialization() throws InputGenerationException, InputIterationException {
    // Expected Values
    DenialConstraint expectedResult = new DenialConstraint();

    // Execute functionality
    DenialConstraintResult rankingResult = new DenialConstraintResult(expectedResult);

    // Check
    assertEquals(expectedResult, rankingResult.getResult());
  }

  @Test
  public void testEquals() throws InputGenerationException, InputIterationException {
    // Set up
    DenialConstraint expectedResult = new DenialConstraint();
    DenialConstraint expectedResult2 = new DenialConstraint();

    // Execute functionality
    DenialConstraintResult rankingResult1 = new DenialConstraintResult(expectedResult);
    DenialConstraintResult rankingResult2 = new DenialConstraintResult(expectedResult2);

    // Check
    assertEquals(rankingResult1, rankingResult1);
    assertEquals(rankingResult1, rankingResult2);
  }
}
