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

package de.metanome.backend.result_receiver;

import de.metanome.algorithm_integration.results.BasicStatistic;
import de.metanome.algorithm_integration.results.FunctionalDependency;
import de.metanome.algorithm_integration.results.InclusionDependency;
import de.metanome.algorithm_integration.results.Result;
import de.metanome.algorithm_integration.results.UniqueColumnCombination;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

/**
 * Test for {@link ResultCache}
 *
 * @author Jakob Zwiener
 */
public class ResultCacheTest {

  protected ResultCache resultCache;
  protected BasicStatistic expectedStatistic;
  protected FunctionalDependency expectedFd;
  protected InclusionDependency expectedInd;
  protected UniqueColumnCombination expectedUcc;

  @Before
  public void setUp() throws Exception {
    resultCache = new ResultCache("");
    expectedStatistic = mock(BasicStatistic.class);
    expectedFd = mock(FunctionalDependency.class);
    expectedInd = mock(InclusionDependency.class);
    expectedUcc = mock(UniqueColumnCombination.class);
  }

  @After
  public void tearDown() throws Exception {
  }

  /**
   * Test method for {@link ResultCache#getResults()} <p/> Should return all results once. After
   * receiving the list it should be cleared and only filled by new results.
   */
  @Test
  public void testGetNewResults() {
    // Execute functionality
    resultCache.receiveResult(expectedStatistic);
    resultCache.receiveResult(expectedFd);

    List<Result> actualResults = resultCache.getResults();

    // Check result
    assertEquals(2, actualResults.size());
    // Results should be in correct order.
    assertSame(expectedStatistic, actualResults.get(0));
    assertSame(expectedFd, actualResults.get(1));
    // No new results should be fetched.
    assertEquals(0, resultCache.getResults().size());

    // Add new results
    resultCache.receiveResult(expectedInd);
    resultCache.receiveResult(expectedUcc);

    // New results should be in list
    actualResults = resultCache.getResults();
    assertEquals(2, actualResults.size());
    // Results should be in correct order.
    assertSame(expectedInd, actualResults.get(0));
    assertSame(expectedUcc, actualResults.get(1));

    // No new results should be fetched.
    assertEquals(0, resultCache.getResults().size());
  }

}
