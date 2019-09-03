/**
 * Copyright 2015-2017 by Metanome Project
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
import de.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.metanome.algorithm_integration.results.BasicStatistic;
import de.metanome.algorithm_integration.results.DenialConstraint;
import de.metanome.algorithm_integration.results.InclusionDependency;
import de.metanome.algorithm_integration.results.UniqueColumnCombination;
import de.metanome.backend.results_db.ResultType;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;


public class ResultCounterTest {

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
    DenialConstraint dc = mock(DenialConstraint.class);

    // Execute functionality
    resultCounter.receiveResult(basicStatistic);

    // Check result
    assertTrue(resultCounter.getResults().get(ResultType.BASIC_STAT) == 1);
    assertNull(resultCounter.getResults().get(ResultType.IND));
    assertNull(resultCounter.getResults().get(ResultType.DC));

    // Execute functionality
    resultCounter.receiveResult(basicStatistic);
    resultCounter.receiveResult(inclusionDependency);
    resultCounter.receiveResult(dc);

    // Check result
    assertTrue(resultCounter.getResults().get(ResultType.BASIC_STAT) == 2);
    assertTrue(resultCounter.getResults().get(ResultType.IND) == 1);
    assertTrue(resultCounter.getResults().get(ResultType.DC) == 1);
  }

  /**
   * Test method for {@link de.metanome.backend.result_receiver.ResultCounter#close()}
   */
  @Test
  public void testClose() throws IOException, CouldNotReceiveResultException {
    // Set up
    ResultCounter resultCounter = new ResultCounter("identifier");
    resultCounter.setResultTestDir();
    resultCounter.receiveResult(mock(UniqueColumnCombination.class));

    // Execute functionality
    resultCounter.close();

    // Check result
    File
      actualFile =
      new File(resultCounter.getOutputFilePathPrefix() + ResultType.UCC.getEnding());
    assertTrue(actualFile.exists());

    String fileContent = Files.toString(actualFile, Charsets.UTF_8);

    assertTrue(fileContent.contains("Unique Column Combination: 1"));

    // Cleanup
    FileUtils.deleteDirectory(new File(ResultPrinter.RESULT_TEST_DIR).getParentFile());
  }
}
