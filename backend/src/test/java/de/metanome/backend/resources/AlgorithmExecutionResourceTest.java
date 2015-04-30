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

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;

public class AlgorithmExecutionResourceTest {

  AlgorithmExecutionResource executionService = new AlgorithmExecutionResource();

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

}
