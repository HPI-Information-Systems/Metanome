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

import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.BasicStatisticsAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.FunctionalDependencyAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.InclusionDependencyAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.ProgressEstimatingAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.UniqueColumnCombinationsAlgorithm;
import de.uni_potsdam.hpi.metanome.test_helper.EqualsAndHashCodeTester;
import de.uni_potsdam.hpi.metanome.test_helper.GwtSerializationTester;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link de.uni_potsdam.hpi.metanome.results_db.Algorithm}
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
    Set<Class<?>> algorithmInterfaces = new HashSet<>();
    algorithmInterfaces.add(UniqueColumnCombinationsAlgorithm.class);
    algorithmInterfaces.add(InclusionDependencyAlgorithm.class);
    algorithmInterfaces.add(ProgressEstimatingAlgorithm.class);
    algorithmInterfaces.add(FunctionalDependencyAlgorithm.class);
    algorithmInterfaces.add(BasicStatisticsAlgorithm.class);
    // Expected values
    String expectedFileName = "some file name";

    // Execute functionality
    Algorithm actualAlgorithm = new Algorithm(expectedFileName, algorithmInterfaces);

    // Check result
    assertEquals(expectedFileName, actualAlgorithm.getFileName());
    assertTrue(actualAlgorithm.isInd());
    assertTrue(actualAlgorithm.isFd());
    assertTrue(actualAlgorithm.isUcc());
    assertTrue(actualAlgorithm.isBasicStat());
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.results_db.Algorithm#store()} and {@link
   * Algorithm#retrieve(String)} <p/> Algorithms should be storable and retrievable by id. The store
   * method should return the stored algorithm instance.
   */
  @Test
  public void testPersistence() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    String expectedFileName = "someFileName";
    Algorithm expectedAlgorithm = new Algorithm(expectedFileName);

    // Execute functionality
    assertSame(expectedAlgorithm, expectedAlgorithm.store());
    Algorithm actualAlgorithm = Algorithm.retrieve(expectedFileName);

    // Check result
    assertEquals(expectedAlgorithm, actualAlgorithm);

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link Algorithm#retrieveAll()}
   */
  @Test
  public void testRetrieveAll() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    Algorithm expectedAlgorithm1 = new Algorithm("some file name 1").store();
    Algorithm expectedAlgorithm2 = new Algorithm("some file name 2").store();

    // Execute functionality
    List<Algorithm> actualAlgorithms = Algorithm.retrieveAll();

    // Check result
    assertThat(actualAlgorithms, IsIterableContainingInAnyOrder
        .containsInAnyOrder(expectedAlgorithm1, expectedAlgorithm2));

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link Algorithm#retrieveAll()} <p/> When the table has never been accessed the
   * list of algorithms should still be retrievable and empty.
   */
  @Test
  public void testRetrieveAllTableEmpty() {
    // Setup
    HibernateUtil.clear();

    // Execute functionality
    Collection<Algorithm> actualAlgorithms = Algorithm.retrieveAll();

    // Check result
    assertTrue(actualAlgorithms.isEmpty());

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.results_db.Algorithm#retrieveAll(Class[])}
   * <p/> Only algorithms that implement the given interfaces (hence are of certain type) should be
   * returned when querying.
   */
  @Test
  public void testRetrieveWithInterfaces() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    Algorithm
        expectedIndAlgorithm =
        new Algorithm("some ind algorithm file path").setInd(true).store();

    Algorithm expectedFdAlgorithm = new Algorithm("some fd algorithm file path")
        .setFd(true)
        .store();

    Algorithm expectedUccAlgorithm = new Algorithm("some ucc algorithm file path")
        .setUcc(true)
        .store();

    Algorithm expectedBasicStatAlgorithm = new Algorithm("some basic stat algorithm file path")
        .setBasicStat(true)
        .store();

    Algorithm expectedHolisticAlgorithm = new Algorithm("some holistic algorithm file path")
        .setFd(true)
        .setUcc(true)
        .store();

    Algorithm otherAlgorithm = new Algorithm("some other path");
    otherAlgorithm.store();

    // Execute functionality
    List<Algorithm> actualIndAlgorithms = Algorithm.retrieveAll(InclusionDependencyAlgorithm.class);
    List<Algorithm> actualFdAlgorithms = Algorithm.retrieveAll(FunctionalDependencyAlgorithm.class);
    List<Algorithm>
        actualUccAlgorithms =
        Algorithm.retrieveAll(UniqueColumnCombinationsAlgorithm.class);
    List<Algorithm>
        actualBasicStatAlgorithms =
        Algorithm.retrieveAll(BasicStatisticsAlgorithm.class);
    List<Algorithm>
        actualHolisticAlgorithms =
        Algorithm.retrieveAll(FunctionalDependencyAlgorithm.class,
                              UniqueColumnCombinationsAlgorithm.class);

    // Check result
    // Should retrieve the algorithms of the particular type only
    assertThat(actualIndAlgorithms,
               IsIterableContainingInAnyOrder.containsInAnyOrder(expectedIndAlgorithm));
    assertThat(actualFdAlgorithms, IsIterableContainingInAnyOrder
        .containsInAnyOrder(expectedFdAlgorithm, expectedHolisticAlgorithm));
    assertThat(actualUccAlgorithms, IsIterableContainingInAnyOrder
        .containsInAnyOrder(expectedUccAlgorithm, expectedHolisticAlgorithm));
    assertThat(actualBasicStatAlgorithms,
               IsIterableContainingInAnyOrder.containsInAnyOrder(expectedBasicStatAlgorithm));
    assertThat(actualHolisticAlgorithms,
               IsIterableContainingInAnyOrder.containsInAnyOrder(expectedHolisticAlgorithm));

    // Cleanup
    HibernateUtil.clear();
  }


  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.results_db.Algorithm#equals(Object)} and
   * {@link de.uni_potsdam.hpi.metanome.results_db.Algorithm#hashCode()}
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
   * Tests that the instances of {@link de.uni_potsdam.hpi.metanome.results_db.Algorithm} are
   * serializable in GWT.
   */
  @Test
  public void testGwtSerialization() {
    GwtSerializationTester.checkGwtSerializability(new Algorithm("some file path"));
  }
}
