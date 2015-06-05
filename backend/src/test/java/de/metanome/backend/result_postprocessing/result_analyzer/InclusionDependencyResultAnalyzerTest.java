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

package de.metanome.backend.result_postprocessing.result_analyzer;

import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.ColumnPermutation;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.algorithm_integration.results.InclusionDependency;
import de.metanome.backend.result_postprocessing.results.InclusionDependencyResult;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class InclusionDependencyResultAnalyzerTest {

  @Test
  public void testConvertResults() throws InputGenerationException, InputIterationException {
    // Set up
    InclusionDependencyResultAnalyzer resultAnalyzer = new InclusionDependencyResultAnalyzer(new ArrayList<RelationalInputGenerator>(), false);

    ColumnPermutation expectedDependant = new ColumnPermutation(
        new ColumnIdentifier("table1", "column1"));
    ColumnPermutation expectedReferenced = new ColumnPermutation(
        new ColumnIdentifier("table1", "column2"));

    InclusionDependencyResult expectedResult = new InclusionDependencyResult();
    expectedResult.setDependant(expectedDependant);
    expectedResult.setReferenced(expectedReferenced);
    InclusionDependency expectedInd = new InclusionDependency(expectedDependant, expectedReferenced);

    List<InclusionDependency> inclusionDependencies = new ArrayList<>();
    inclusionDependencies.add(expectedInd);

    // Execute functionality
    List<InclusionDependencyResult> actualResults = resultAnalyzer.convertResults(inclusionDependencies);

    // Check
    assertEquals(expectedResult, actualResults.get(0));
  }

}
