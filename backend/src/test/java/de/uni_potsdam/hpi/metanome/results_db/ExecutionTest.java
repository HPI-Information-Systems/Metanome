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

package de.uni_potsdam.hpi.metanome.results_db;

import de.uni_potsdam.hpi.metanome.test_helper.EqualsAndHashCodeTester;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Test method for {@link de.uni_potsdam.hpi.metanome.results_db.Execution}
 *
 * @author Jakob Zwiener
 */
public class ExecutionTest {

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.results_db.Execution#store()} and {@link
   * de.uni_potsdam.hpi.metanome.results_db.Execution#retrieve(Algorithm, java.sql.Timestamp)} <p/>
   * Executions should be storable and retrievable by id.
   */
  @Test
  public void testPersistence() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Store prerequisite objects in the database.
    Algorithm algorithm = new Algorithm("some file path");
    algorithm.store();

    // Expected values
    Timestamp begin = new Timestamp(new Date().getTime());
    Execution expectedExecution = new Execution(algorithm, begin);

    // Execute functionality
    assertSame(expectedExecution, expectedExecution.store());
    Execution actualExecution = Execution.retrieve(algorithm, begin);

    // Check result
    assertEquals(expectedExecution, actualExecution);

    // Cleanup
    HibernateUtil.clear();
  }

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
    assertTrue(new Date().getTime() - execution.getBegin().getTime() < 2000);
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.results_db.Execution#retrieveAll()}
   */
  @Test
  public void testRetrieveAll() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    Algorithm expectedAlgorithm = new Algorithm("some file name 1")
        .store();
    Execution expectedExecution1 = new Execution(expectedAlgorithm)
        .store();
    Execution expectedExecution2 = new Execution(expectedAlgorithm)
        .store();

    // Execute functionality
    List<Execution> actualExecutions = Execution.retrieveAll();

    // Check result
    assertThat(actualExecutions, IsIterableContainingInAnyOrder
        .containsInAnyOrder(expectedExecution1, expectedExecution2));

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.results_db.Execution#store()} and {@link
   * de.uni_potsdam.hpi.metanome.results_db.Execution#retrieve(Algorithm, java.sql.Timestamp)} <p/>
   * After roundtripping an execution all its {@link de.uni_potsdam.hpi.metanome.results_db.Input}s
   * should be retrievable from it.
   */
  @Test
  public void testPersistenceMultipleInputs() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Store prerequisite objects in the database
    Algorithm algorithm = new Algorithm("some path")
        .store();
    Input input1 = new Input()
        .store();
    Input input2 = new Input()
        .store();
    Collection<Input> inputs = new ArrayList<>();
    inputs.add(input1);
    inputs.add(input2);

    // Expected values
    Timestamp begin = new Timestamp(new Date().getTime());
    Execution expectedExecution = new Execution(algorithm, begin)
        .setInputs(inputs);

    // Execute functionality
    expectedExecution.store();
    Execution actualExecution = Execution.retrieve(algorithm, begin);
    Collection<Input> actualInputs = actualExecution.getInputs();

    // Check result
    assertEquals(expectedExecution, actualExecution);
    assertEquals(inputs, actualInputs);

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.results_db.Execution#store()} and {@link
   * de.uni_potsdam.hpi.metanome.results_db.Execution#retrieve(Algorithm, java.sql.Timestamp)} <p/>
   * Test the database roundtrip of an Execution with multiple {@link
   * de.uni_potsdam.hpi.metanome.results_db.Input}s and {@link de.uni_potsdam.hpi.metanome.results_db.Result}s.
   */
  @Test
  public void testPersistenceWithAlgorithmAndResultAndInputs() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Store prerequisite objects in the database
    Algorithm algorithm = new Algorithm("some algorithm jar path")
        .store();

    // Expected values
    Timestamp begin = new Timestamp(new Date().getTime());
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
    expectedExecution.store();
    expectedResult1.store();
    expectedResult2.store();
    Execution actualExecution = Execution.retrieve(algorithm, begin);

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
        .containsInAnyOrder(expectedTableInput, expectedFileInput, expectedFileInput));

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.results_db.Execution#addResult(Result)} <p/>
   * Results should be added and a bidirectional association should be created. The results should
   * be retrievable from the database.
   */
  @Test
  public void testPersistenceGetResults() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Store prerequisite objects in the database
    Algorithm algorithm = new Algorithm("some algorithm file path")
        .store();

    // Expected values
    Timestamp begin = new Timestamp(new Date().getTime());
    Execution expectedExecution = new Execution(algorithm, begin);
    Result expectedResult1 = new Result("some result file path");
    Result expectedResult2 = new Result("some other result file path");

    // Execute functionality
    expectedExecution.addResult(expectedResult1);
    expectedExecution.addResult(expectedResult2);

    expectedExecution.store();
    expectedResult1.store();
    expectedResult2.store();
    Execution actualExecution = Execution.retrieve(algorithm, begin);

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
   * Test method for {@link Execution#equals(Object)} and {@link Execution#hashCode()}
   */
  @Test
  public void testEqualsAndHashCode() {
    // Setup
    Algorithm algorithm = new Algorithm("some algorithm file name");
    Timestamp begin = new Timestamp(new Date().getTime());
    Execution execution = new Execution(algorithm, begin);
    Execution equalExecution = new Execution(algorithm, begin);
    Execution
        notEqualExecution =
        new Execution(new Algorithm("some other file name"), new Timestamp(197));

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
    Execution execution = new Execution(mock(Algorithm.class), mock(Timestamp.class));
    // Expected values
    Input input1 = new Input()
        .setId(42);
    Input input2 = new Input()
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

}
