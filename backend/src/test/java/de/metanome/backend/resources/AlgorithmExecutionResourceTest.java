/*
 * Copyright 2014 by the Metanome project
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

package de.metanome.backend.resources;

import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.results.Result;
import de.metanome.algorithm_integration.results.UniqueColumnCombination;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class AlgorithmExecutionResourceTest {

  AlgorithmExecutionResource executionService = new AlgorithmExecutionResource();

  @Test
  public void testFetchNewResults() throws Exception {
    // Setup
    // Expected values
    String expectedExecutionIdentifier = "executionIdentifier";
    executionService.buildExecutor(expectedExecutionIdentifier);
    UniqueColumnCombination expectedUcc = new UniqueColumnCombination(new ColumnIdentifier("table", "column"));

    // Execute functionality
    AlgorithmExecutionCache.getResultsCache(expectedExecutionIdentifier).receiveResult(
        expectedUcc);
    List<Result>
        actualResult = executionService.fetchNewResults(expectedExecutionIdentifier);

    // Check result
    assertFalse(actualResult.isEmpty());
    assertTrue(actualResult.contains(expectedUcc));
  }

  /**
   * Test method for {@link AlgorithmExecutionResource#fetchProgress(String)}
   *
   * When fetching the current progress for an execution the correct progress should be returned.
   */
  @Test
  public void testFetchProgress() throws FileNotFoundException, UnsupportedEncodingException {
    // Setup
    // Expected values
    String expectedExecutionIdentifier = "executionIdentifier";
    executionService.buildExecutor(expectedExecutionIdentifier);
    float expectedProgress = 0.42f;

    // Execute functionality
    AlgorithmExecutionCache.getProgressCache(expectedExecutionIdentifier)
        .updateProgress(expectedProgress);
    float actualProgress = executionService.fetchProgress(expectedExecutionIdentifier);

    // Check result
    assertEquals(expectedProgress, actualProgress);
  }

}
