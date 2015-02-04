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

package de.metanome.frontend.server;

import de.metanome.backend.algorithm_loading.AlgorithmFinder;
import de.metanome.backend.algorithm_loading.InputDataFinder;
import de.metanome.backend.resources.AlgorithmResource;
import de.metanome.backend.resources.FileInputResource;
import de.metanome.backend.resources.InputResource;
import de.metanome.backend.results_db.Algorithm;
import de.metanome.backend.results_db.AlgorithmContentEquals;
import de.metanome.backend.results_db.EntityStorageException;
import de.metanome.backend.results_db.FileInput;
import de.metanome.backend.results_db.HibernateUtil;
import de.metanome.backend.results_db.Input;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContextEvent;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link de.metanome.frontend.server.DatabaseInitializer}
 *
 * @author Jakob Zwiener
 */
public class DatabaseInitializerTest {

  private FileInputResource fileInputResource = new FileInputResource();

  AlgorithmResource algorithmResource = new AlgorithmResource();
  InputResource inputResource = new InputResource();

  /**
   * Test method for {@link de.metanome.frontend.server.DatabaseInitializer#contextInitialized(javax.servlet.ServletContextEvent)}
   * <p/> All algorithm jars should be represented by algorithms in the database with correct file
   * names as key. Algorithms should have the correct types assigned.
   */
  @Test
  public void testContextInitializedAlgorithms()
      throws IOException, ClassNotFoundException, EntityStorageException {
    // Setup
    HibernateUtil.clear();

    AlgorithmFinder jarFinder = new AlgorithmFinder();
    DatabaseInitializer initializer = new DatabaseInitializer();

    // Expected values
    String[] algorithmFileNames = jarFinder.getAvailableAlgorithmFileNames(null);
    Map<String, Algorithm> expectedAlgorithms = new HashMap<>();
    for (int i = 0; i < algorithmFileNames.length; i++) {
      expectedAlgorithms.put(algorithmFileNames[i],
                             buildExpectedAlgorithm(jarFinder, algorithmFileNames[i]));
    }

    // Execute functionality
    initializer.contextInitialized(mock(ServletContextEvent.class));
    List<Algorithm> actualAlgorithms = algorithmResource.getAll();

    // Check result
    for (Algorithm algorithm : actualAlgorithms) {
      assertTrue(expectedAlgorithms.containsKey(algorithm.getFileName()));
    }

    // Check algorithm fields
    for (Algorithm actualAlgorithm : actualAlgorithms) {
      // Get the matching algorithm based on the file name (equals method).
      Algorithm expectedAlgorithm = expectedAlgorithms.get(actualAlgorithm.getFileName());
      assertTrue(AlgorithmContentEquals.contentEquals(expectedAlgorithm, actualAlgorithm));
    }

    // Cleanup
    HibernateUtil.clear();
  }

  protected Algorithm buildExpectedAlgorithm(AlgorithmFinder jarFinder, String algorithmFileName)
      throws IOException, ClassNotFoundException {
    Set<Class<?>> algorithmInterfaces = jarFinder.getAlgorithmInterfaces(algorithmFileName);

    Algorithm algorithm = new Algorithm(algorithmFileName, algorithmInterfaces);
    algorithm.setName(algorithmFileName.replaceAll(".jar", ""));

    return algorithm;
  }

  /**
   * Test method for {@link DatabaseInitializer#contextInitialized(javax.servlet.ServletContextEvent)}
   * <p/> If the algorithm table is already populated it should not be initialized.
   */
  @Test
  public void testContextInitializedAlgorithmsNotEmpty() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    DatabaseInitializer initializer = new DatabaseInitializer();
    // Expected values
    Algorithm expectedAlgorithm = new Algorithm("example_ind_algorithm.jar");
    HibernateUtil.store(expectedAlgorithm);

    // Execute functionality
    initializer.contextInitialized(mock(ServletContextEvent.class));
    List<Algorithm> actualAlgorithms = algorithmResource.getAll();

    // Check result
    assertThat(actualAlgorithms,
               IsIterableContainingInAnyOrder.containsInAnyOrder(expectedAlgorithm));

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.frontend.server.DatabaseInitializer#contextInitialized(javax.servlet.ServletContextEvent)}
   * <p/> The database should be initialized with the found input files. The input file entries
   * should have the correct file name.
   */
  @Test
  public void testContextInitializedInputs()
      throws UnsupportedEncodingException, EntityStorageException {
    // Setup
    HibernateUtil.clear();

    InputDataFinder dataFinder = new InputDataFinder();
    DatabaseInitializer initializer = new DatabaseInitializer();

    // Expected values
    File[] inputDataFiles = dataFinder.getAvailableCsvs();
    String[] expectedFileNames = new String[inputDataFiles.length];
    for (int i = 0; i < inputDataFiles.length; i++) {
      expectedFileNames[i] = inputDataFiles[i].getPath();
    }

    // Execute functionality
    initializer.contextInitialized(mock(ServletContextEvent.class));
    List<Input> actualInputs = inputResource.getAll();

    // Check result
    // Extract actual input file names
    List<String> actualFileNames = new LinkedList<>();
    for (Input actualInput : actualInputs) {
      actualFileNames.add(((FileInput) actualInput).getFileName());
    }

    // Check input's file names
    assertThat(actualFileNames,
               IsIterableContainingInAnyOrder.containsInAnyOrder(expectedFileNames));

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.metanome.frontend.server.DatabaseInitializer#contextInitialized(javax.servlet.ServletContextEvent)}
   * <p/> If the inputs table is already populated it should not be initialized.
   */
  @Test
  public void testContextInitializedInputsNotEmpty() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    DatabaseInitializer initializer = new DatabaseInitializer();
    // Expected values
    FileInput expectedFileInput = fileInputResource.store(new FileInput());

    // Execute functionality
    initializer.contextInitialized(mock(ServletContextEvent.class));
    List<Input> actualInputs = inputResource.getAll();

    // Check result
    assertThat(actualInputs,
               IsIterableContainingInAnyOrder.containsInAnyOrder((Input) expectedFileInput));

    // Cleanup
    HibernateUtil.clear();
  }
}
