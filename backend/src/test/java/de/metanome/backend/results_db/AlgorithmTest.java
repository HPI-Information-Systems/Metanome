/**
 * Copyright 2014-2017 by Metanome Project
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

import de.metanome.algorithm_integration.algorithm_types.*;
import de.metanome.backend.constants.Constants;
import de.metanome.backend.resources.AlgorithmResource;
import de.metanome.test_helper.EqualsAndHashCodeTester;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Test for {@link de.metanome.backend.results_db.Algorithm}
 *
 * @author Jakob Zwiener
 */
public class AlgorithmTest {

  /**
   * Test method for {@link Algorithm#Algorithm(String, java.util.Set)} <p/> The algorithm should
   * have the appropriate algorithm types set, based on the implemented interfaces.
   */
  @Test
  public void testConstructorWithInterfaces() {
    // Setup
    Set<Class<?>> algorithmInterfaces = AlgorithmType.asStream()
            .map( type -> type.getAlgorithmClass())
            .collect(Collectors.toSet());

    // Expected values
    String expectedFileName = "some file name";

    // Execute functionality
    Algorithm actualAlgorithm = new Algorithm(expectedFileName, algorithmInterfaces);

    // Check result
    assertEquals(expectedFileName, actualAlgorithm.getFileName());
    AlgorithmType.asStream()
            .forEach(type -> assertTrue(actualAlgorithm.hasAlgorithmType(type)));
  }

  /**
   * Test method for {@link de.metanome.backend.results_db.Algorithm#Algorithm(String, String,
   * String, String, java.util.Set)}
   * <p/>
   * The algorithm should have the appropriate algorithm types and other values set.
   */
  @Test
  public void testConstructorFull() {
    // Setup
    Set<Class<?>> algorithmInterfaces = new HashSet<>();
    algorithmInterfaces.add(UniqueColumnCombinationsAlgorithm.class);
    algorithmInterfaces.add(FileInputParameterAlgorithm.class);
    // Expected values
    String expectedFileName = "some file name";
    String expectedName = "some name";
    String expectedAuthor = "some author";
    String expectedDescription = "some description";

    // Execute functionality
    Algorithm
      actualAlgorithm =
      new Algorithm(expectedFileName, expectedName, expectedAuthor, expectedDescription,
        algorithmInterfaces);

    // Check result
    assertEquals(expectedFileName, actualAlgorithm.getFileName());
    assertEquals(expectedName, actualAlgorithm.getName());
    assertEquals(expectedAuthor, actualAlgorithm.getAuthor());
    assertEquals(expectedDescription, actualAlgorithm.getDescription());
    
    for (AlgorithmType type : AlgorithmType.values()) {
        if (type.equals(AlgorithmType.FILE_INPUT) || type.equals(AlgorithmType.UCC)) {
            assertTrue(actualAlgorithm.hasAlgorithmType(type));
        } else {
            assertFalse(actualAlgorithm.hasAlgorithmType(type));
        }
    }
  }

  /**
   * Test method for {@link de.metanome.backend.results_db.Algorithm#getId()}
   */
  @Test
  public void testGetId() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    AlgorithmResource resource = new AlgorithmResource();
    resource.store(new Algorithm("example_ind_algorithm.jar"));
    resource.store(new Algorithm("example_ucc_algorithm.jar"));

    // Execute functionality
    List<Algorithm> actualAlgorithms = resource.getAll();

    long actualId1 = actualAlgorithms.get(0).getId();
    long actualId2 = actualAlgorithms.get(1).getId();

    // Check result
    assertEquals(Math.abs(actualId1 - actualId2), 1);

    // Cleanup
    HibernateUtil.clear();
  }

  @Test
  public void testUniqueAlgorithmName() {
    // Setup
    HibernateUtil.clear();

    Algorithm algorithm1 = new Algorithm("example_ind_algorithm.jar");
    algorithm1.setName("name");
    try {
      HibernateUtil.store(algorithm1);
    } catch (EntityStorageException e) {
      fail();
    }

    Algorithm algorithm2 = new Algorithm("example_ucc_algorithm.jar");
    algorithm2.setName("name");

    // Check
    try {
      HibernateUtil.store(algorithm2);
    } catch (EntityStorageException e) {
      // should throw an exception
    }

    // Clean up
    HibernateUtil.clear();
  }

  @Test
  public void testUniqueAlgorithmFileName() {
    // Setup
    HibernateUtil.clear();

    Algorithm algorithm1 = new Algorithm("example_ind_algorithm.jar");
    try {
      HibernateUtil.store(algorithm1);
    } catch (EntityStorageException e) {
      fail();
    }

    Algorithm algorithm2 = new Algorithm("example_ind_algorithm.jar");

    // Check
    try {
      HibernateUtil.store(algorithm2);
    } catch (ConstraintViolationException | EntityStorageException e) {
      // should throw an exception
    }

    // Clean up
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.results_db.Algorithm#equals(Object)} and {@link
   * de.metanome.backend.results_db.Algorithm#hashCode()}
   */
  @Test
  public void testEqualsHashCode() {
    // Setup
    // Expected values
    String expectedFileName1 = "someFileName1";
    String expectedFileName2 = "someFileName2";
    Algorithm algorithm = new Algorithm(expectedFileName1);
    Algorithm algorithmEqual = new Algorithm(expectedFileName1);
    Algorithm algorithmNotEqual = new Algorithm(expectedFileName2);

    // Execute functionality
    // Check result
    new EqualsAndHashCodeTester<Algorithm>()
      .performBasicEqualsAndHashCodeChecks(algorithm, algorithmEqual, algorithmNotEqual);
  }

  /**
   * Test method for {@link de.metanome.backend.results_db.Algorithm#equals(Object)} and {@link
   * de.metanome.backend.results_db.Algorithm#hashCode()}
   */
  @Test
  @SuppressWarnings(Constants.SUPPRESS_WARNINGS_UNCHECKED)
  public void testDeleteExecutionsCascading() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    Algorithm algorithm = new Algorithm("example_ind_algorithm.jar");
    Execution execution = new Execution(algorithm);

    HibernateUtil.store(algorithm);
    HibernateUtil.store(execution);

    // Execute functionality
    HibernateUtil.delete(algorithm);

    // Check result
    List<Algorithm> acutalAlgorithms = (List<Algorithm>) HibernateUtil.queryCriteria(Algorithm.class);
    List<Execution> acutalExecutions = (List<Execution>) HibernateUtil.queryCriteria(Execution.class);

    assertTrue(acutalAlgorithms.isEmpty());
    assertTrue(acutalExecutions.isEmpty());

    // Clean up
    HibernateUtil.clear();
  }


}
