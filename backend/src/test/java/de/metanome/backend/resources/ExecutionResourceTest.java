/**
 * Copyright 2014-2016 by Metanome Project
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
import de.metanome.backend.results_db.*;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

public class ExecutionResourceTest {

  AlgorithmResource algorithmResource = new AlgorithmResource();
  ExecutionResource executionResource = new ExecutionResource();

  /**
   * Test method for {@link de.metanome.backend.resources.ExecutionResource#get(long)}
   * <p/>
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
    HibernateUtil.store(expectedExecution);
    Execution actualExecution = executionResource.get(expectedExecution.getId());

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
    Algorithm expectedAlgorithm1 = new Algorithm("example_ind_algorithm.jar");
    algorithmResource.store(expectedAlgorithm1);
    Algorithm expectedAlgorithm2 = new Algorithm("example_ucc_algorithm.jar");
    algorithmResource.store(expectedAlgorithm2);

    Execution expectedExecution1 = new Execution(expectedAlgorithm1);
    HibernateUtil.store(expectedExecution1);
    Execution expectedExecution2 = new Execution(expectedAlgorithm2);
    HibernateUtil.store(expectedExecution2);

    // Execute functionality
    List<Execution> actualExecutions = executionResource.getAll();

    // Check result
    assertThat(actualExecutions, IsIterableContainingInAnyOrder
      .containsInAnyOrder(expectedExecution1, expectedExecution2));

    // Cleanup
    HibernateUtil.clear();
  }


  /**
   * Test method for {@link de.metanome.backend.resources.ExecutionResource#get(long)}
   * <p/>
   * After roundtripping an execution all its {@link de.metanome.backend.results_db.Input}s should
   * be retrievable from it.
   */
  @Test
  public void testPersistenceMultipleInputs()
    throws EntityStorageException, AlgorithmLoadingException {
    // Setup
    HibernateUtil.clear();

    // Store prerequisite objects in the database
    Algorithm algorithm = new Algorithm("example_ind_algorithm.jar");
    algorithmResource.store(algorithm);

    Input input1 = new Input("input");
    HibernateUtil.store(input1);
    Input input2 = new Input("input");
    HibernateUtil.store(input2);

    List<Input> inputs = new ArrayList<>();
    inputs.add(input1);
    inputs.add(input2);

    // Expected values
    long begin = new Date().getTime();
    Execution expectedExecution = new Execution(algorithm, begin)
      .setInputs(inputs);

    // Execute functionality
    HibernateUtil.store(expectedExecution);
    Execution actualExecution = executionResource.get(expectedExecution.getId());
    Collection<Input> actualInputs = actualExecution.getInputs();

    // Check result
    assertEquals(expectedExecution, actualExecution);
    assertEquals(inputs, actualInputs);

    // Cleanup
    HibernateUtil.clear();
  }


  /**
   * Test method for {@link de.metanome.backend.resources.ExecutionResource#get(long)}
   * <p/>
   * Test the database roundtrip of an Execution with multiple {@link
   * de.metanome.backend.results_db.Input}s and {@link de.metanome.backend.results_db.Result}s.
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
    FileInput expectedFileInput = new FileInput("fileInput");
    expectedExecution.addInput(expectedFileInput);
    TableInput expectedTableInput = new TableInput("tableInput");
    expectedExecution.addInput(expectedTableInput);

    // Execute functionality
    HibernateUtil.store(expectedFileInput);
    HibernateUtil.store(expectedTableInput);
    HibernateUtil.store(expectedExecution);

    Execution actualExecution = executionResource.get(expectedExecution.getId());

    Input[] expectedInputs = {expectedTableInput, expectedFileInput};

    Set<Result> actualResults = actualExecution.getResults();
    Collection<Input> actualInputs = actualExecution.getInputs();

    // Check result
    assertEquals(expectedExecution, actualExecution);
    // Verify result set
    assertEquals(2, actualResults.size());
    assertTrue(actualResults.contains(expectedResult1));
    assertTrue(actualResults.contains(expectedResult2));
    // Verify input list
    assertEquals(2, actualInputs.size());
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

    HibernateUtil.store(expectedExecution);

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

  /**
   * Test method for {@link de.metanome.backend.resources.ExecutionResource#delete(long)} Executions
   * should be storable and updatable.
   */
  @Test
  public void testDelete() throws EntityStorageException, IOException {
    // Setup
    HibernateUtil.clear();

    String fileName = "result_ind";

    File f = new File(fileName);
    f.createNewFile();

    Algorithm algorithm = new Algorithm("example_ind_algorithm.jar");
    Result result = new Result(fileName);

    algorithmResource.store(algorithm);
    HibernateUtil.store(result);

    Execution execution = new Execution(algorithm);
    execution.addResult(result);
    HibernateUtil.store(execution);

    // Check result
    assertEquals(execution, execution);

    // Check precondition
    assertFalse(executionResource.getAll().isEmpty());
    assertTrue(f.exists());

    // Execute functionality
    executionResource.delete(execution.getId());

    // Check result
    assertTrue(executionResource.getAll().isEmpty());
    assertFalse(f.exists());

    // Cleanup
    HibernateUtil.clear();
  }

}
