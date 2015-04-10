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
import de.metanome.backend.results_db.ResultType;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.EnumMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AlgorithmExecutionResourceTest {

  AlgorithmExecutionResource executionService = new AlgorithmExecutionResource();

  @Test
  public void testFetchNewResults() throws Exception {
    // Setup
    String expectedExecutionIdentifier = "executionIdentifier";

    AlgorithmExecutionParams params = new AlgorithmExecutionParams();
    params.setExecutionIdentifier(expectedExecutionIdentifier)
        .setCacheResults(true);

    // Expected values
    executionService.buildExecutor(params);
    UniqueColumnCombination expectedUcc = new UniqueColumnCombination(new ColumnIdentifier("table", "column"));

    // Execute functionality
    AlgorithmExecutionCache.getResultCache(expectedExecutionIdentifier).receiveResult(
        expectedUcc);
    List<Result>
        actualResult = executionService.getCacheResults(expectedExecutionIdentifier);

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
    String expectedExecutionIdentifier = "executionIdentifier";

    AlgorithmExecutionParams params = new AlgorithmExecutionParams();
    params.setExecutionIdentifier(expectedExecutionIdentifier)
        .setCountResults(true);

    // Expected values
    executionService.buildExecutor(params);
    float expectedProgress = 0.42f;

    // Execute functionality
    AlgorithmExecutionCache.getProgressCache(expectedExecutionIdentifier)
        .updateProgress(expectedProgress);
    float actualProgress = executionService.fetchProgress(expectedExecutionIdentifier);

    // Check result
    assertEquals(expectedProgress, actualProgress, 0.0);
  }


  @Test(expected = WebException.class)
  public void testFetchResultsNotPossible() throws FileNotFoundException, UnsupportedEncodingException {
    // Setup
    String expectedExecutionIdentifier = "executionIdentifier";

    AlgorithmExecutionParams params = new AlgorithmExecutionParams();
    params.setExecutionIdentifier(expectedExecutionIdentifier)
        .setCountResults(true);

    // Expected values
    executionService.buildExecutor(params);

    // Check
    List<Result>
        actualResult = executionService.getCacheResults(expectedExecutionIdentifier);
  }

  @Test
  public void testGetCounterResults() throws Exception {
    // Setup
    String expectedExecutionIdentifier = "executionIdentifier";

    AlgorithmExecutionParams params = new AlgorithmExecutionParams();
    params.setExecutionIdentifier(expectedExecutionIdentifier)
        .setCountResults(true);

    // Expected values
    executionService.buildExecutor(params);
    UniqueColumnCombination expectedUcc = new UniqueColumnCombination(new ColumnIdentifier("table", "column"));

    // Execute functionality
    AlgorithmExecutionCache.getResultCounter(expectedExecutionIdentifier).receiveResult(
        expectedUcc);
    EnumMap<ResultType, Integer>
        results = executionService.getCounterResults(expectedExecutionIdentifier);

    // Check result
    assertFalse(results.isEmpty());
    assertTrue(results.get(ResultType.UCC) == 1);
  }

  @Test
  public void testGetPrinterResults() throws Exception {
    // Setup
    String expectedExecutionIdentifier = "executionIdentifier";

    AlgorithmExecutionParams params = new AlgorithmExecutionParams();
    params.setExecutionIdentifier(expectedExecutionIdentifier)
        .setWriteResults(true);

    // Expected values
    executionService.buildExecutor(params);
    UniqueColumnCombination expectedUcc = new UniqueColumnCombination(new ColumnIdentifier("table", "column"));
    AlgorithmExecutionCache.getResultPrinter(expectedExecutionIdentifier).setResultTestDir();

    // Execute functionality
    AlgorithmExecutionCache.getResultPrinter(expectedExecutionIdentifier).receiveResult(
        expectedUcc);
    List<Result> results = executionService.getPrinterResults(expectedExecutionIdentifier);

    // Check result
    assertFalse(results.isEmpty());
    assertTrue(results.contains(expectedUcc));
  }

}
