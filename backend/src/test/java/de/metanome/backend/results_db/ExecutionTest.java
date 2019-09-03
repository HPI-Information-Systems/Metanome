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
package de.metanome.backend.results_db;

import de.metanome.backend.constants.Constants;
import de.metanome.backend.resources.AlgorithmResource;
import de.metanome.backend.resources.ExecutionResource;
import de.metanome.test_helper.EqualsAndHashCodeTester;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Test method for {@link de.metanome.backend.results_db.Execution}
 *
 * @author Jakob Zwiener
 */
public class ExecutionTest {

  /**
   * Test method for {@link Execution#Execution(Algorithm)} <p/> When constructing an {@link
   * Execution} without a {@link Timestamp} the current time should be recorded.
   */
  @Test
  public void testConstructorDateNow() {
    // Execute functionality
    Execution execution = new Execution(mock(Algorithm.class));

    // Check result
    // The execution should have a timestamp for begin that is not older than 2 seconds.
    assertTrue(new Date().getTime() - execution.getBegin() < 2000);
  }

  /**
   * Test method for {@link Execution#equals(Object)} and {@link Execution#hashCode()}
   */
  @Test
  public void testEqualsAndHashCode() {
    // Setup
    Algorithm algorithm = new Algorithm("some algorithm file name");
    long begin = new Date().getTime();
    Execution execution = new Execution(algorithm, begin);
    Execution equalExecution = new Execution(algorithm, begin);
    Execution
      notEqualExecution =
      new Execution(new Algorithm("some other file name"), 197);

    // Execute functionality
    // Check result
    new EqualsAndHashCodeTester<Execution>()
      .performBasicEqualsAndHashCodeChecks(execution, equalExecution, notEqualExecution);
  }

  /**
   * Test method for {@link Execution#addInput(Input)} <p/> After adding an input to the execution
   * the list of inputs should contain the added input.
   */
  @Test
  public void testAddInput() {
    // Setup
    Execution execution = new Execution(mock(Algorithm.class), 123456789);
    // Expected values
    Input input1 = new Input("input")
      .setId(42);
    Input input2 = new Input("input")
      .setId(23);

    // Execute functionality
    // Check result
    Collection<Input> actualInputs = execution.getInputs();
    assertTrue(actualInputs.isEmpty());

    execution.addInput(input1);
    actualInputs = execution.getInputs();
    assertTrue(actualInputs.contains(input1));

    execution.addInput(input2);
    actualInputs = execution.getInputs();
    assertEquals(2, actualInputs.size());
    assertTrue(actualInputs.contains(input1));
    assertTrue(actualInputs.contains(input2));
  }

  /**
   * Test method for {@link Execution#addResult(Result)}
   * <p/>
   * After adding a result to the execution the list of results should contain the added result.
   */
  @Test
  public void testAddResult() {
    // Setup
    Execution execution = new Execution(mock(Algorithm.class), 123456789);
    // Expected values
    Result result1 = new Result().setFileName("result1");
    Result result2 = new Result().setFileName("result2");

    // Execute functionality
    // Check result
    Set<Result> actualResults = execution.getResults();
    assertTrue(actualResults.isEmpty());

    execution.addResult(result1);
    actualResults = execution.getResults();
    assertTrue(actualResults.contains(result1));

    execution.addResult(result2);
    actualResults = execution.getResults();
    assertEquals(2, actualResults.size());
    assertTrue(actualResults.contains(result1));
    assertTrue(actualResults.contains(result2));
  }

  /**
   * Test method for {@link de.metanome.backend.results_db.Execution#getId()}
   */
  @Test
  public void testGetId() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    ExecutionResource resource = new ExecutionResource();
    HibernateUtil.store(new Execution());
    HibernateUtil.store(new Execution());

    // Execute functionality
    List<Execution> actualExecutions = resource.getAll();

    long actualId1 = actualExecutions.get(0).getId();
    long actualId2 = actualExecutions.get(1).getId();

    // Check result
    assertEquals(Math.abs(actualId1 - actualId2), 1);

    // Cleanup
    HibernateUtil.clear();
  }

  @Test(expected = EntityStorageException.class)
  public void testUniqueAlgorithmAndBegin() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    long begin1 = new Date().getTime();
    Algorithm algorithm = new Algorithm("example_ind_algorithm.jar");
    AlgorithmResource algorithmResource = new AlgorithmResource();
    algorithmResource.store(algorithm);
    long begin2 = new Date().getTime();

    // Check precondition
    assertTrue(begin1 != begin2);

    Execution execution1 = new Execution(algorithm, begin1);
    try {
      HibernateUtil.store(execution1);
    } catch (EntityStorageException e) {
      fail();
    }

    Execution execution2 = new Execution(algorithm, begin2);
    try {
      HibernateUtil.store(execution2);
    } catch (EntityStorageException e) {
      fail();
    }

    // Check
    Execution execution3 = new Execution(algorithm, begin1);
    HibernateUtil.store(execution3);

    // Clean up
    HibernateUtil.clear();
  }

  @Test
  @SuppressWarnings(Constants.SUPPRESS_WARNINGS_UNCHECKED)
  public void testCascadingSaveOfResults() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    long begin = new Date().getTime();
    Algorithm algorithm = new Algorithm("example_ind_algorithm.jar");
    HibernateUtil.store(algorithm);

    Execution execution = new Execution(algorithm, begin);
    Result result = new Result("some_file");

    execution.addResult(result);

    // Execute Functionality
    HibernateUtil.store(execution);

    // Check
    List<Execution> executions = (List<Execution>) HibernateUtil.queryCriteria(Execution.class);
    List<Result> results = (List<Result>) HibernateUtil.queryCriteria(Result.class);

    assertFalse(executions.isEmpty());
    assertFalse(results.isEmpty());

    // Clean up
    HibernateUtil.clear();
  }

  @Test
  @SuppressWarnings(Constants.SUPPRESS_WARNINGS_UNCHECKED)
  public void testCascadingDeleteOfResults() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    long begin = new Date().getTime();
    Algorithm algorithm = new Algorithm("example_ind_algorithm.jar");
    HibernateUtil.store(algorithm);

    Execution execution = new Execution(algorithm, begin);
    Result result = new Result("some_file");

    execution.addResult(result);

    HibernateUtil.store(execution);

    // Check precondition
    List<Execution> executions = (List<Execution>) HibernateUtil.queryCriteria(Execution.class);
    List<Result> results = (List<Result>) HibernateUtil.queryCriteria(Result.class);
    assertFalse(executions.isEmpty());
    assertFalse(results.isEmpty());

    // Execute Functionality
    HibernateUtil.delete(execution);

    // Check
    executions = (List<Execution>) HibernateUtil.queryCriteria(Execution.class);
    results = (List<Result>) HibernateUtil.queryCriteria(Result.class);
    assertTrue(executions.isEmpty());
    assertTrue(results.isEmpty());

    // Clean up
    HibernateUtil.clear();
  }

}
