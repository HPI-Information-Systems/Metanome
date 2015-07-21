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

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import de.metanome.algorithm_integration.ColumnCombination;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.metanome.algorithm_integration.results.BasicStatistic;
import de.metanome.algorithm_integration.results.FunctionalDependency;
import de.metanome.algorithm_integration.results.InclusionDependency;
import de.metanome.algorithm_integration.results.JsonConverter;
import de.metanome.algorithm_integration.results.Result;
import de.metanome.algorithm_integration.results.UniqueColumnCombination;
import de.metanome.backend.results_db.ResultType;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
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
   * Test method for {@link ResultCache#fetchNewResults()} <p/> Should return all results once.
   * After receiving the list it should be cleared and only filled by new results.
   */
  @Test
  public void testGetNewResults() {
    // Execute functionality
    resultCache.receiveResult(expectedStatistic);
    resultCache.receiveResult(expectedFd);

    List<Result> actualResults = resultCache.fetchNewResults();

    // Check result
    assertEquals(2, actualResults.size());
    // Results should be in correct order.
    assertSame(expectedStatistic, actualResults.get(0));
    assertSame(expectedFd, actualResults.get(1));
    // No new results should be fetched.
    assertEquals(0, resultCache.fetchNewResults().size());

    // Add new results
    resultCache.receiveResult(expectedInd);
    resultCache.receiveResult(expectedUcc);

    // New results should be in list
    actualResults = resultCache.fetchNewResults();
    assertEquals(2, actualResults.size());
    // Results should be in correct order.
    assertSame(expectedInd, actualResults.get(0));
    assertSame(expectedUcc, actualResults.get(1));

    // No new results should be fetched.
    assertEquals(0, resultCache.fetchNewResults().size());
  }

  /**
   * Test method for {@link de.metanome.backend.result_receiver.ResultCache#close()}
   */
  @Test
  public void testClose() throws IOException, CouldNotReceiveResultException {
    // Set up
    FunctionalDependency expectedFd = new FunctionalDependency(
        new ColumnCombination(
            new ColumnIdentifier("table1", "column2")),
        new ColumnIdentifier("table1", "column23")
    );

    ResultCache resultCache = new ResultCache("identifier");
    resultCache.setResultTestDir();
    resultCache.receiveResult(expectedFd);

    // Execute functionality
    resultCache.close();

    // Check result
    File actualFile = new File(resultCache.getOutputFilePathPrefix() + ResultType.FD.getEnding());
    assertTrue(actualFile.exists());

    String fileContent = Files.toString(actualFile, Charsets.UTF_8);

    JsonConverter<FunctionalDependency> jsonConverter = new JsonConverter<>();
    assertTrue(fileContent.contains(jsonConverter.toJsonString(expectedFd)));

    // Cleanup
    FileUtils.deleteDirectory(new File(ResultPrinter.RESULT_TEST_DIR).getParentFile());
  }

}
