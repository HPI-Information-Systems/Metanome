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

import de.metanome.algorithm_integration.algorithm_types.BasicStatisticsAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.ConditionalUniqueColumnCombinationAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.DatabaseConnectionParameterAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.FileInputParameterAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.FunctionalDependencyAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.InclusionDependencyAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.OrderDependencyAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.ProgressEstimatingAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.RelationalInputParameterAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.TableInputParameterAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.UniqueColumnCombinationsAlgorithm;
import de.metanome.test_helper.EqualsAndHashCodeTester;
import de.metanome.test_helper.GwtSerializationTester;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
    Set<Class<?>> algorithmInterfaces = new HashSet<>();
    algorithmInterfaces.add(UniqueColumnCombinationsAlgorithm.class);
    algorithmInterfaces.add(ConditionalUniqueColumnCombinationAlgorithm.class);
    algorithmInterfaces.add(OrderDependencyAlgorithm.class);
    algorithmInterfaces.add(InclusionDependencyAlgorithm.class);
    algorithmInterfaces.add(ProgressEstimatingAlgorithm.class);
    algorithmInterfaces.add(FunctionalDependencyAlgorithm.class);
    algorithmInterfaces.add(BasicStatisticsAlgorithm.class);
    algorithmInterfaces.add(RelationalInputParameterAlgorithm.class);
    algorithmInterfaces.add(FileInputParameterAlgorithm.class);
    algorithmInterfaces.add(TableInputParameterAlgorithm.class);
    algorithmInterfaces.add(DatabaseConnectionParameterAlgorithm.class);

    // Expected values
    String expectedFileName = "some file name";

    // Execute functionality
    Algorithm actualAlgorithm = new Algorithm(expectedFileName, algorithmInterfaces);

    // Check result
    assertEquals(expectedFileName, actualAlgorithm.getFileName());
    assertTrue(actualAlgorithm.isInd());
    assertTrue(actualAlgorithm.isFd());
    assertTrue(actualAlgorithm.isUcc());
    assertTrue(actualAlgorithm.isCucc());
    assertTrue(actualAlgorithm.isOd());
    assertTrue(actualAlgorithm.isBasicStat());
    assertTrue(actualAlgorithm.isDatabaseConnection());
    assertTrue(actualAlgorithm.isFileInput());
    assertTrue(actualAlgorithm.isRelationalInput());
    assertTrue(actualAlgorithm.isTableInput());
  }

  /**
   * Test method for {@link de.metanome.backend.results_db.Algorithm#Algorithm(String, String,
   * String, String, java.util.Set)}
   *
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
    assertFalse(actualAlgorithm.isInd());
    assertFalse(actualAlgorithm.isFd());
    assertTrue(actualAlgorithm.isUcc());
    assertFalse(actualAlgorithm.isCucc());
    assertFalse(actualAlgorithm.isOd());
    assertFalse(actualAlgorithm.isBasicStat());
    assertEquals(expectedName, actualAlgorithm.getName());
    assertEquals(expectedAuthor, actualAlgorithm.getAuthor());
    assertEquals(expectedDescription, actualAlgorithm.getDescription());
    assertFalse(actualAlgorithm.isDatabaseConnection());
    assertTrue(actualAlgorithm.isFileInput());
    assertFalse(actualAlgorithm.isRelationalInput());
    assertFalse(actualAlgorithm.isTableInput());
  }

  @Test
  public void testUniqueAlgorithmName() {
    // Setup
    HibernateUtil.clear();

    Algorithm algorithm1 = new Algorithm("someFile1");
    algorithm1.setName("name");
    try {
      HibernateUtil.store(algorithm1);
    } catch (EntityStorageException e) {
      fail();
    }

    Algorithm algorithm2 = new Algorithm("someFile2");
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
   * Tests that the instances of {@link de.metanome.backend.results_db.Algorithm} are serializable
   * in GWT.
   */
  @Test
  public void testGwtSerialization() {
    GwtSerializationTester.checkGwtSerializability(new Algorithm("some file path"));
  }
}
