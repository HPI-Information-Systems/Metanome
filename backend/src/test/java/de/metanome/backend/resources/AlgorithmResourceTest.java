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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class AlgorithmResourceTest {

  /**
   * Test method for {@link AlgorithmResource#listAllAlgorithms()}
   * @throws Exception
   */
  @Test
  public void testListAllAlgorithms() throws Exception {
    // Setup
    HibernateUtil.clear();

    // Expected values
    Algorithm expectedAlgorithm1 = new Algorithm("example_ind_algorithm.jar");
    Algorithm expectedAlgorithm2 = new Algorithm("example_fd_algorithm.jar");

    HibernateUtil.store(expectedAlgorithm1);
    HibernateUtil.store(expectedAlgorithm2);

    // Execute functionality
    List<Algorithm> actualAlgorithms = AlgorithmResource.listAllAlgorithms();

    // Check result
    assertThat(actualAlgorithms, IsIterableContainingInAnyOrder
        .containsInAnyOrder(expectedAlgorithm1, expectedAlgorithm2));

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link AlgorithmResource#listAllAlgorithms()} ()}
   *
   * When the database has never been accessed, the list of algorithms should still be
   * retrievable and empty.
   */
  @Test
  public void testListAllAlgorithmEmpty() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Execute functionality
    Collection<Algorithm> actualAlgorithms = AlgorithmResource.listAllAlgorithms();

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
    List<Algorithm> actualAlgorithms = AlgorithmResource.listInclusionDependencyAlgorithms();

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
        .setName("ucc")
        .setUcc(true);
    HibernateUtil.store(expectedAlgorithm);

    // Execute functionality
    List<Algorithm> actualAlgorithms = AlgorithmResource.listUniqueColumnCombinationsAlgorithms();

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
    List<Algorithm> actualAlgorithms = AlgorithmResource.listConditionalUniqueColumnCombinationsAlgorithms();

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
    List<Algorithm> actualAlgorithms = AlgorithmResource.listFunctionalDependencyAlgorithms();

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
    List<Algorithm> actualAlgorithms = AlgorithmResource.listOrderDependencyAlgorithms();

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
    List<Algorithm> actualAlgorithms = AlgorithmResource.listBasicStatisticsAlgorithms();

    // Check result
    assertThat(actualAlgorithms,
               IsIterableContainingInAnyOrder.containsInAnyOrder(expectedAlgorithm));

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.resources.AlgorithmResource#deleteAlgorithm(String)}
   * @throws Exception
   */
  @Test
  public void testDeleteAlgorithm() throws Exception {
    // Setup
    HibernateUtil.clear();

    // Expected values
    String expectedFileName = "someFileName";
    Algorithm expectedAlgorithm = new Algorithm(expectedFileName);
    HibernateUtil.store(expectedAlgorithm);
    // Check precondition
    assertFalse(AlgorithmResource.listAllAlgorithms().isEmpty());

    // Execute functionality
    AlgorithmResource.deleteAlgorithm(expectedFileName);

    // Check result
    assertTrue(AlgorithmResource.listAllAlgorithms().isEmpty());

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link AlgorithmResource#listAvailableAlgorithmFiles()}
   *
   * The list of available algorithms should not be emtpy.
   * @throws Exception
   */
  @Test
  public void testListAvailableAlgorithmFiles() throws Exception {
    // Setup
    HibernateUtil.clear();

    // Expected values
    // Execute functionality
    List<String> files = AlgorithmResource.listAvailableAlgorithmFiles();

    // Check result
    assertFalse(files.isEmpty());

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.backend.resources.AlgorithmResource#addAlgorithm(de.metanome.backend.results_db.Algorithm)}
   * and {@link de.metanome.backend.resources.AlgorithmResource#retrieve(String)}
   *
   * Algorithms should be storable and retrievable by id. The store method should return the stored
   * algorithm instance.
   */
  @Test
  public void testPersistence() throws EntityStorageException, AlgorithmLoadingException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    String expectedFileName = "example_ind_algorithm.jar";
    Algorithm expectedAlgorithm = new Algorithm(expectedFileName);

    // Execute functionality
    assertSame(expectedAlgorithm, AlgorithmResource.addAlgorithm(expectedAlgorithm));
    Algorithm actualAlgorithm = AlgorithmResource.retrieve(expectedFileName);

    // Check result
    assertEquals(expectedAlgorithm, actualAlgorithm);

    // Cleanup
    HibernateUtil.clear();
  }

}
