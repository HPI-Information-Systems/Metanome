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
import de.metanome.backend.results_db.HibernateUtil;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class AlgorithmResourceTest {

  AlgorithmResource resource = new AlgorithmResource();

  /**
   * Test method for {@link de.metanome.backend.resources.AlgorithmResource#getAll()}
   */
  @Test
  public void testGetAll() throws Exception {
    // Setup
    HibernateUtil.clear();

    // Expected values
    Algorithm expectedAlgorithm1 = new Algorithm("example_ind_algorithm.jar");
    Algorithm expectedAlgorithm2 = new Algorithm("example_fd_algorithm.jar");

    HibernateUtil.store(expectedAlgorithm1);
    HibernateUtil.store(expectedAlgorithm2);

    // Execute functionality
    List<Algorithm> actualAlgorithms = resource.getAll();

    // Check result
    assertThat(actualAlgorithms, IsIterableContainingInAnyOrder
      .containsInAnyOrder(expectedAlgorithm1, expectedAlgorithm2));

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.resources.AlgorithmResource#getAll()}
   * <p/>
   * When the database has never been accessed, the list of algorithms should still be retrievable
   * and empty.
   */
  @Test
  public void testGetAllEmpty() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Execute functionality
    Collection<Algorithm> actualAlgorithms = resource.getAll();

    // Check result
    assertTrue(actualAlgorithms.isEmpty());

    // Cleanup
    HibernateUtil.clear();
  }

  @Test
  public void testListInclusionDependencyAlgorithms() throws Exception {
    // Setup
    HibernateUtil.clear();

    // Expected values
    Algorithm expectedAlgorithm = new Algorithm("example_ind_algorithm.jar")
      .setName("ind")
      .setInd(true);
    HibernateUtil.store(expectedAlgorithm);

    // Execute functionality
    List<Algorithm> actualAlgorithms = resource.listInclusionDependencyAlgorithms();

    // Check result
    assertThat(actualAlgorithms,
      IsIterableContainingInAnyOrder.containsInAnyOrder(expectedAlgorithm));

    // Cleanup
    HibernateUtil.clear();
  }

  @Test
  public void testListUniqueColumnCombinationsAlgorithms() throws Exception {
    // Setup
    HibernateUtil.clear();

    // Expected values
    Algorithm expectedAlgorithm = new Algorithm("example_ucc_algorithm.jar")
      .setName("Ucc")
      .setUcc(true);
    HibernateUtil.store(expectedAlgorithm);

    // Execute functionality
    List<Algorithm> actualAlgorithms = resource.listUniqueColumnCombinationsAlgorithms();

    // Check result
    assertThat(actualAlgorithms,
      IsIterableContainingInAnyOrder.containsInAnyOrder(expectedAlgorithm));

    // Cleanup
    HibernateUtil.clear();
  }

  @Test
  public void testListConditionalUniqueColumnCombinationsAlgorithms() throws Exception {
    // Setup
    HibernateUtil.clear();

    // Expected values
    Algorithm expectedAlgorithm = new Algorithm("example_cucc_algorithm.jar")
      .setName("cucc")
      .setCucc(true);
    HibernateUtil.store(expectedAlgorithm);

    // Execute functionality
    List<Algorithm> actualAlgorithms = resource.listConditionalUniqueColumnCombinationsAlgorithms();

    // Check result
    assertThat(actualAlgorithms,
      IsIterableContainingInAnyOrder.containsInAnyOrder(expectedAlgorithm));

    // Cleanup
    HibernateUtil.clear();
  }

  @Test
  public void testListFunctionalDependencyAlgorithms() throws Exception {
    // Setup
    HibernateUtil.clear();

    // Expected values
    Algorithm expectedAlgorithm = new Algorithm("example_fd_algorithm.jar")
      .setName("fd")
      .setFd(true);
    HibernateUtil.store(expectedAlgorithm);

    // Execute functionality
    List<Algorithm> actualAlgorithms = resource.listFunctionalDependencyAlgorithms();

    // Check result
    assertThat(actualAlgorithms,
      IsIterableContainingInAnyOrder.containsInAnyOrder(expectedAlgorithm));

    // Cleanup
    HibernateUtil.clear();
  }

  @Test
  public void testListOrderDependencyAlgorithms() throws Exception {
    // Setup
    HibernateUtil.clear();

    // Expected values
    Algorithm expectedAlgorithm = new Algorithm("example_od_algorithm.jar")
      .setName("od")
      .setOd(true);
    HibernateUtil.store(expectedAlgorithm);

    // Execute functionality
    List<Algorithm> actualAlgorithms = resource.listOrderDependencyAlgorithms();

    // Check result
    assertThat(actualAlgorithms,
      IsIterableContainingInAnyOrder.containsInAnyOrder(expectedAlgorithm));

    // Cleanup
    HibernateUtil.clear();
  }

  @Test
  public void testListBasicStatisticsAlgorithms() throws Exception {
    // Setup
    HibernateUtil.clear();

    // Expected values
    Algorithm expectedAlgorithm = new Algorithm("example_basic_algorithm.jar")
      .setName("basic")
      .setBasicStat(true);
    HibernateUtil.store(expectedAlgorithm);

    // Execute functionality
    List<Algorithm> actualAlgorithms = resource.listBasicStatisticsAlgorithms();

    // Check result
    assertThat(actualAlgorithms,
      IsIterableContainingInAnyOrder.containsInAnyOrder(expectedAlgorithm));

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.resources.AlgorithmResource#delete(long)}
   */
  @Test
  public void testDelete() throws Exception {
    // Setup
    HibernateUtil.clear();

    // Expected values
    Algorithm expectedAlgorithm = new Algorithm("example_ind_algorithm.jar");
    expectedAlgorithm = resource.store(expectedAlgorithm);
    // Check precondition
    assertFalse(resource.getAll().isEmpty());

    // Execute functionality
    resource.delete(expectedAlgorithm.getId());

    // Check result
    assertTrue(resource.getAll().isEmpty());

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link AlgorithmResource#listAvailableAlgorithmFiles()}
   * <p/>
   * The list of available algorithms should not be emtpy.
   */
  @Test
  public void testListAvailableAlgorithmFiles() throws Exception {
    // Setup
    HibernateUtil.clear();

    // Expected values
    // Execute functionality
    List<String> files = resource.listAvailableAlgorithmFiles();

    // Check result
    assertFalse(files.isEmpty());

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.resources.AlgorithmResource#store(de.metanome.backend.results_db.Algorithm)}
   * and {@link de.metanome.backend.resources.AlgorithmResource#get(long)}
   * <p/>
   * Algorithms should be storable and retrievable by id. The store method should return the stored
   * algorithm instance.
   */
  @Test
  public void testPersistence() throws EntityStorageException, AlgorithmLoadingException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    Algorithm expectedAlgorithm = new Algorithm("example_ind_algorithm.jar");

    // Execute functionality
    Algorithm storedAlgorithm = resource.store(expectedAlgorithm);
    assertSame(expectedAlgorithm, storedAlgorithm);
    Algorithm actualAlgorithm = resource.get(storedAlgorithm.getId());

    // Check result
    assertEquals(storedAlgorithm, actualAlgorithm);

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.resources.AlgorithmResource#update(de.metanome.backend.results_db.Algorithm)}
   * Algorithm should be storable and updatable.
   */
  @Test
  public void testUpdate() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    Algorithm algorithm = new Algorithm("example_ind_algorithm.jar");

    // Execute functionality
    Algorithm actualAlgorithm = resource.store(algorithm);

    // Check result
    assertEquals(algorithm, actualAlgorithm);

    // Execute functionality
    algorithm.setFileName("example_ucc_algorithm.jar").setAuthor("author");
    resource.update(algorithm);

    // Check result
    actualAlgorithm = resource.get(algorithm.getId());
    assertEquals(algorithm, actualAlgorithm);

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.resources.AlgorithmResource#update(de.metanome.backend.results_db.Algorithm)}
   * Updating algorithms with a not loadable jar file should not be possible
   */
  @Test(expected = WebException.class)
  public void testUpdateFailure() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    Algorithm algorithm = new Algorithm("example_ind_algorithm.jar");

    // Execute functionality
    Algorithm actualAlgorithm = resource.store(algorithm);

    // Check result
    assertEquals(algorithm, actualAlgorithm);

    // Execute functionality
    algorithm.setFileName("example_wrong_bootstrap_algorithm.jar").setAuthor("author");

    // Check result
    resource.update(algorithm);

    // Cleanup
    HibernateUtil.clear();
  }

}
