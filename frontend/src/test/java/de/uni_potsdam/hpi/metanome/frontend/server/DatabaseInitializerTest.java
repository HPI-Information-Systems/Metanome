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

package de.uni_potsdam.hpi.metanome.frontend.server;

import de.uni_potsdam.hpi.metanome.algorithm_loading.AlgorithmFinder;
import de.uni_potsdam.hpi.metanome.algorithm_loading.InputDataFinder;
import de.uni_potsdam.hpi.metanome.results_db.Algorithm;
import de.uni_potsdam.hpi.metanome.results_db.AlgorithmContentEquals;
import de.uni_potsdam.hpi.metanome.results_db.EntityStorageException;
import de.uni_potsdam.hpi.metanome.results_db.FileInput;
import de.uni_potsdam.hpi.metanome.results_db.HibernateUtil;
import de.uni_potsdam.hpi.metanome.results_db.Input;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContextEvent;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link DatabaseInitializer}
 *
 * @author Jakob Zwiener
 */
public class DatabaseInitializerTest {

  /**
   * Test method for {@link DatabaseInitializer#contextInitialized(javax.servlet.ServletContextEvent)}
   * <p/> All algorithm jars should be represented by algorithms in the database with correct file
   * names as key. Algorithms should have the correct types assigned.
   */
  @Test
  public void testContextInitializedAlgorithms() throws IOException, ClassNotFoundException {
    // Setup
    HibernateUtil.clear();

    AlgorithmFinder jarFinder = new AlgorithmFinder();
    DatabaseInitializer initializer = new DatabaseInitializer();

    // Expected values
    String[] algorithmFileNames = jarFinder.getAvailableAlgorithmFileNames(null);
    Algorithm[] expectedAlgorithms = new Algorithm[algorithmFileNames.length];
    for (int i = 0; i < algorithmFileNames.length; i++) {
      expectedAlgorithms[i] = buildExpectedAlgorithm(jarFinder, algorithmFileNames[i]);
    }

    // Execute functionality
    initializer.contextInitialized(mock(ServletContextEvent.class));
    List<Algorithm> actualAlgorithms = Algorithm.retrieveAll();

    // Check result
    assertThat(actualAlgorithms,
               IsIterableContainingInAnyOrder.containsInAnyOrder(expectedAlgorithms));
    // Check algorithm fields
    for (Algorithm expectedAlgorithm : expectedAlgorithms) {
      // Get the matching algorithm based on the file name (equals method).
      Algorithm actualAlgorithm = actualAlgorithms.get(actualAlgorithms.indexOf(expectedAlgorithm));
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
    Algorithm expectedAlgorithm = new Algorithm("some file name")
        .store();

    // Execute functionality
    initializer.contextInitialized(mock(ServletContextEvent.class));
    List<Algorithm> actualAlgorithms = Algorithm.retrieveAll();

    // Check result
    assertThat(actualAlgorithms,
               IsIterableContainingInAnyOrder.containsInAnyOrder(expectedAlgorithm));

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.frontend.server.DatabaseInitializer#contextInitialized(javax.servlet.ServletContextEvent)}
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
      expectedFileNames[i] = inputDataFiles[i].getName();
    }

    // Execute functionality
    initializer.contextInitialized(mock(ServletContextEvent.class));
    List<Input> actualInputs = Input.retrieveAll();

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
   * Test method for {@link de.uni_potsdam.hpi.metanome.frontend.server.DatabaseInitializer#contextInitialized(javax.servlet.ServletContextEvent)}
   * <p/> If the inputs table is already populated it should not be initialized.
   */
  @Test
  public void testContextInitializedInputsNotEmpty() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    DatabaseInitializer initializer = new DatabaseInitializer();
    // Expected values
    FileInput expectedFileInput = new FileInput()
        .store();

    // Execute functionality
    initializer.contextInitialized(mock(ServletContextEvent.class));
    List<Input> actualInputs = Input.retrieveAll();

    // Check result
    assertThat(actualInputs,
               IsIterableContainingInAnyOrder.containsInAnyOrder((Input) expectedFileInput));

    // Cleanup
    HibernateUtil.clear();
  }
}
