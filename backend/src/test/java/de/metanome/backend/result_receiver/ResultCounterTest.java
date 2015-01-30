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

package de.metanome.backend.result_receiver;

import de.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.metanome.algorithm_integration.results.BasicStatistic;
import de.metanome.algorithm_integration.results.InclusionDependency;

import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;


public class ResultCounterTest{

  /**
   * Test method for {@link de.metanome.backend.result_receiver.ResultCounter#getResults}
   */
  @Test
  public void testGetResults() throws CouldNotReceiveResultException, FileNotFoundException {
    // Set up
    ResultCounter resultCounter = new ResultCounter("");

    // Expected
    BasicStatistic basicStatistic = mock(BasicStatistic.class);
    InclusionDependency inclusionDependency = mock(InclusionDependency.class);

    // Execute functionality
    resultCounter.receiveResult(basicStatistic);

    // Check result
    assertTrue(resultCounter.getStatsCount() == 1);
    assertTrue(resultCounter.getIndCount() == 0);


    // Execute functionality
    resultCounter.receiveResult(basicStatistic);
    resultCounter.receiveResult(inclusionDependency);

    // Check result
    assertTrue(resultCounter.getStatsCount() == 2);
    assertTrue(resultCounter.getIndCount() == 1);
  }
}
