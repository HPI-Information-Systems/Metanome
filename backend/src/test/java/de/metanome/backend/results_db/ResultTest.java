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

package de.metanome.backend.results_db;

import de.metanome.backend.algorithm_loading.AlgorithmLoadingException;
import de.metanome.backend.resources.AlgorithmResource;
import de.metanome.backend.resources.ExecutionResource;
import de.metanome.test_helper.EqualsAndHashCodeTester;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Tests for {@link de.metanome.backend.results_db.Result}
 *
 * @author Jakob Zwiener
 */
public class ResultTest {

  /**
   * Test method for {@link Result#store()} and {@link Result#retrieve(String)} <p/> Tests
   * persistence of a Result without attached {@link Execution}.
   */
  @Test
  public void testPersistence() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    String expectedFilePath = "some file name";
    Result expectedResult = new Result(expectedFilePath);

    // Execute functionality
    assertSame(expectedResult, expectedResult.store());
    Result actualResult = Result.retrieve(expectedFilePath);

    // Check result
    assertEquals(expectedResult, actualResult);

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link Result#store()} and {@link Result#retrieve(String)} <p/> Tests
   * persistence of a Result with an attached {@link Execution}.
   */
  @Test
  public void testPersistenceWithExecution()
      throws EntityStorageException, AlgorithmLoadingException {
    // Setup
    HibernateUtil.clear();

    // Store prerequisite objects in the database
    Algorithm algorithm = new Algorithm("example_ind_algorithm.jar");
    AlgorithmResource algorithmResource = new AlgorithmResource();
    algorithmResource.store(algorithm);

    Execution execution = new Execution(algorithm, new Date().getTime());
    ExecutionResource executionResource = new ExecutionResource();
    executionResource.store(execution);

    // Expected values
    String filePath = "some file name";
    Result expectedResult = new Result(filePath);
    expectedResult.setExecution(execution);

    // Execute functionality
    expectedResult.store();
    Result actualResult = Result.retrieve(filePath);

    // Check result
    assertEquals(expectedResult, actualResult);

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link Result#equals(Object)} and {@link de.metanome.backend.results_db.Result#hashCode()}
   */
  @Test
  public void testEqualsAndHashCode() {
    // Setup
    String expectedFilePath = "some file path";
    Result result = new Result(expectedFilePath);
    Result equalResult = new Result(expectedFilePath);
    Result notEqualResult = new Result("some other path");

    // Execute functionality
    // Check result
    new EqualsAndHashCodeTester<Result>()
        .performBasicEqualsAndHashCodeChecks(result, equalResult, notEqualResult);
  }

}
