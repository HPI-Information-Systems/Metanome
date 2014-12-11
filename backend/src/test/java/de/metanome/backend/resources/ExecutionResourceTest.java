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


import de.metanome.backend.algorithm_loading.AlgorithmLoadingException;
import de.metanome.backend.results_db.Algorithm;
import de.metanome.backend.results_db.EntityStorageException;
import de.metanome.backend.results_db.Execution;
import de.metanome.backend.results_db.FileInput;
import de.metanome.backend.results_db.HibernateUtil;
import de.metanome.backend.results_db.Input;
import de.metanome.backend.results_db.Result;
import de.metanome.backend.results_db.TableInput;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ExecutionResourceTest {

  AlgorithmResource algorithmResource = new AlgorithmResource();
  ExecutionResource executionResource = new ExecutionResource();
  ResultResource resultResource = new ResultResource();

  /**
   * Test method for {@link de.metanome.backend.resources.ExecutionResource#store(de.metanome.backend.results_db.Execution)}
   * and {@link de.metanome.backend.resources.ExecutionResource#get(long)}
   *
   * Executions should be storable and retrievable by id.
   */
  @Test
  public void testPersistence() throws EntityStorageException, AlgorithmLoadingException {
    // Setup
    HibernateUtil.clear();

    // Store prerequisite objects in the database.
    Algorithm algorithm = new Algorithm("example_ind_algorithm.jar");
    algorithmResource.store(algorithm);

    // Expected values
    long begin = new Date().getTime();
    Execution expectedExecution = new Execution(algorithm, begin);

    // Execute functionality
    Execution storedExecution = executionResource.store(expectedExecution);
    assertSame(expectedExecution, storedExecution);
    Execution actualExecution = executionResource.get(storedExecution.getId());

    // Check result
    assertEquals(expectedExecution, actualExecution);

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link ExecutionResource#getAll()}
   */
  @Test
  public void testGetAll() throws EntityStorageException, AlgorithmLoadingException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    Algorithm expectedAlgorithm = new Algorithm("example_ind_algorithm.jar");
    algorithmResource.store(expectedAlgorithm);

    Execution expectedExecution1 = new Execution(expectedAlgorithm);
    executionResource.store(expectedExecution1);
    Execution expectedExecution2 = new Execution(expectedAlgorithm);
    executionResource.store(expectedExecution2);

    // Execute functionality
    List<Execution> actualExecutions = executionResource.getAll();

    // Check result
    assertThat(actualExecutions, IsIterableContainingInAnyOrder
        .containsInAnyOrder(expectedExecution1, expectedExecution2));

    // Cleanup
    HibernateUtil.clear();
  }


  /**
   * Test method for {@link de.metanome.backend.resources.ExecutionResource#store(de.metanome.backend.results_db.Execution)}
   * and {@link de.metanome.backend.resources.ExecutionResource#get(long)}
   *
   * After roundtripping an execution all its {@link de.metanome.backend.results_db.Input}s should be
   * retrievable from it.
   */
  @Test
  public void testPersistenceMultipleInputs()
      throws EntityStorageException, AlgorithmLoadingException {
    // Setup
    HibernateUtil.clear();

    // Store prerequisite objects in the database
    Algorithm algorithm = new Algorithm("example_ind_algorithm.jar");
    algorithmResource.store(algorithm);

    Input input1 = new Input()
        .store();
    Input input2 = new Input()
        .store();
    Collection<Input> inputs = new ArrayList<>();
    inputs.add(input1);
    inputs.add(input2);

    // Expected values
    long begin = new Date().getTime();
    Execution expectedExecution = new Execution(algorithm, begin)
        .setInputs(inputs);

    // Execute functionality
    expectedExecution = executionResource.store(expectedExecution);
    Execution actualExecution = executionResource.get(expectedExecution.getId());
    Collection<Input> actualInputs = actualExecution.getInputs();

    // Check result
    assertEquals(expectedExecution, actualExecution);
    assertEquals(inputs, actualInputs);

    // Cleanup
    HibernateUtil.clear();
  }



  /**
   * Test method for {@link de.metanome.backend.resources.ExecutionResource#store(de.metanome.backend.results_db.Execution)}
   * and {@link de.metanome.backend.resources.ExecutionResource#get(long)}
   *
   * Test the database roundtrip of an Execution with multiple {@link de.metanome.backend.results_db.Input}s
   * and {@link de.metanome.backend.results_db.Result}s.
   */
  @Test
  public void testPersistenceWithAlgorithmAndResultAndInputs()
      throws EntityStorageException, AlgorithmLoadingException {
    // Setup
    HibernateUtil.clear();

    // Store prerequisite objects in the database
    Algorithm algorithm = new Algorithm("example_ind_algorithm.jar");
    algorithmResource.store(algorithm);

    // Expected values
    long begin = new Date().getTime();
    Execution expectedExecution = new Execution(algorithm, begin);
    // Adding results and inputs
    // Results
    Result expectedResult1 = new Result("some result file path");
    expectedExecution.addResult(expectedResult1);
    Result expectedResult2 = new Result("some other result file path");
    expectedExecution.addResult(expectedResult2);
    // Inputs
    FileInput expectedFileInput = new FileInput();
    expectedExecution.addInput(expectedFileInput);
    // Inputs can be added twice
    expectedExecution.addInput(expectedFileInput);
    TableInput expectedTableInput = new TableInput();
    expectedExecution.addInput(expectedTableInput);

    // Execute functionality
    expectedFileInput.store();
    expectedTableInput.store();
    expectedExecution = executionResource.store(expectedExecution);
    resultResource.store(expectedResult1);
    resultResource.store(expectedResult2);

    Execution actualExecution = executionResource.get(expectedExecution.getId());

    Input[] expectedInputs = {expectedTableInput, expectedFileInput, expectedFileInput};

    Set<Result> actualResults = actualExecution.getResults();
    Collection<Input> actualInputs = actualExecution.getInputs();

    // Check result
    assertEquals(expectedExecution, actualExecution);
    // Verify result set
    assertEquals(2, actualResults.size());
    assertTrue(actualResults.contains(expectedResult1));
    assertTrue(actualResults.contains(expectedResult2));
    // Verify input list
    assertEquals(3, actualInputs.size());
    assertThat(actualInputs, IsIterableContainingInAnyOrder
        .containsInAnyOrder(expectedInputs));

    // Cleanup
    HibernateUtil.clear();
  }


  /**
   * Test method for {@link de.metanome.backend.results_db.Execution#addResult(Result)} <p/> Results
   * should be added and a bidirectional association should be created. The results should be
   * retrievable from the database.
   */
  @Test
  public void testPersistenceGetResults()
      throws EntityStorageException, AlgorithmLoadingException, InterruptedException {
    // Setup
    HibernateUtil.clear();

    // Store prerequisite objects in the database
    Algorithm algorithm = new Algorithm("example_ind_algorithm.jar");
    algorithmResource.store(algorithm);

    // Expected values
    long begin = new Date().getTime();
    Execution expectedExecution = new Execution(algorithm, begin);
    Result expectedResult1 = new Result("some result file path");
    Result expectedResult2 = new Result("some other result file path");

    // Execute functionality
    expectedExecution.addResult(expectedResult1);
    expectedExecution.addResult(expectedResult2);

    expectedExecution = executionResource.store(expectedExecution);

    resultResource.store(expectedResult1);
    resultResource.store(expectedResult2);

    Execution actualExecution = executionResource.get(expectedExecution.getId());
    Set<Result> actualResults = actualExecution.getResults();

    // Check result
    assertEquals(expectedExecution, actualExecution);
    // All results should be properly retrieved
    assertEquals(2, actualResults.size());
    assertTrue(actualResults.contains(expectedResult1));
    assertTrue(actualResults.contains(expectedResult2));

    // All results should be properly attached to the execution
    for (Result actualResult : actualResults) {
      assertSame(actualExecution, actualResult.getExecution());
    }

    // Cleanup
    HibernateUtil.clear();
  }

}
