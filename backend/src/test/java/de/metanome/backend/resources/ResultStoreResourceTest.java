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
package de.metanome.backend.resources;

import de.metanome.backend.results_db.*;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertTrue;

public class ResultStoreResourceTest {

  ResultStoreResource resource = new ResultStoreResource();
  ExecutionResource executionResource = new ExecutionResource();
  FileInputResource fileInputResource = new FileInputResource();

  @Test
  public void testGetResultsForFileInput() throws Exception {
    // Setup
    HibernateUtil.clear();

    FileInput input = new FileInput("file");
    input = fileInputResource.store(input);

    Result expectedResult1 = new Result("result_file_1");
    expectedResult1.setType(ResultType.CUCC);
    Result expectedResult2 = new Result("result_file_2");
    expectedResult2.setType(ResultType.CUCC);
    Result expectedResult3 = new Result("result_file_3");
    expectedResult3.setType(ResultType.UCC);

    Execution execution = new Execution(null);
    execution.addInput(input);
    execution.addResult(expectedResult1);
    execution.addResult(expectedResult2);
    execution.addResult(expectedResult3);
    HibernateUtil.store(execution);

    // Execute functionality
    Set<Result> actualResults = resource.getResults(input);

    // Check result
    assertTrue(actualResults.size() == 2);
    assertTrue(actualResults.contains(expectedResult3));
    assertTrue(actualResults.contains(expectedResult2));

    // Cleanup
    HibernateUtil.clear();
  }

}
