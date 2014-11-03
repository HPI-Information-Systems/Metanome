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

package de.metanome.frontend.client.runs;

import com.google.gwt.junit.client.GWTTestCase;

import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingDatabaseConnection;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingFileInput;
import de.metanome.algorithm_integration.configuration.DbSystem;
import de.metanome.backend.results_db.Algorithm;
import de.metanome.backend.results_db.AlgorithmContentEquals;
import de.metanome.frontend.client.BasePage;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.TestHelper;

import java.util.LinkedList;
import java.util.List;

/**
 * Tests for {@link de.metanome.frontend.client.runs.AlgorithmChooser}
 *
 * @author Jakob Zwiener
 */
public class GwtTestAlgorithmChooser extends GWTTestCase {

  /**
   * Test that one can add an algorithm after the JarChooser's initial construction
   */
  public void testAddAlgorithm() {
    // Setup
    LinkedList<Algorithm> algorithms = new LinkedList<>();
    Algorithm expectedAlgorithm1 = new Algorithm("file name 1");
    expectedAlgorithm1.setName("name 1").setInd(true);
    algorithms.add(expectedAlgorithm1);
    Algorithm expectedAlgorithm2 = new Algorithm("file name 2");
    expectedAlgorithm2.setName("name 2").setFd(true);

    AlgorithmChooser jarChooser = new AlgorithmChooser(algorithms, new TabWrapper());

    // Execute functionality
    jarChooser.addAlgorithm(expectedAlgorithm2);

    // Check result
    assertTrue(AlgorithmContentEquals.contentEquals(expectedAlgorithm1, jarChooser.algorithmMap
        .get(expectedAlgorithm1.getName())));
    assertTrue(AlgorithmContentEquals.contentEquals(expectedAlgorithm2, jarChooser.algorithmMap
        .get(expectedAlgorithm2.getName())));

    assertEquals(expectedAlgorithm1.getName(), jarChooser.categoryMap
        .get(AlgorithmChooser.AlgorithmCategory.Inclusion_Dependencies.name()).get(0));
    assertEquals(expectedAlgorithm2.getName(), jarChooser.categoryMap
        .get(AlgorithmChooser.AlgorithmCategory.Functional_Dependencies.name()).get(0));
    assertEquals(2, jarChooser.categoryMap
        .get(AlgorithmChooser.AlgorithmCategory.All.name()).size());

    // Execute duplicate insert
    int previousCount = jarChooser.getListItemCount();
    jarChooser.addAlgorithm(expectedAlgorithm1);

    // Check that the algorithm was not added again
    assertEquals(previousCount, jarChooser.getListItemCount());
  }

  /**
   * Ensure that when an algorithm is chosen and submitted, the method adding a ParameterTable is
   * called.
   */
  public void testSubmit() {
    // Set up
    TestHelper.resetDatabaseSync();

    LinkedList<Algorithm> algorithms = new LinkedList<>();
    Algorithm expectedAlgorithm1 = new Algorithm("example_ucc_algorithm.jar");
    expectedAlgorithm1.setName("name 1");
    algorithms.add(expectedAlgorithm1);
    Algorithm expectedAlgorithm2 = new Algorithm("file name 2");
    expectedAlgorithm2.setName("name 2");

    TabWrapper tabWrapper = new TabWrapper();
    RunConfigurationPage page = new RunConfigurationPage(new BasePage()) {
      @Override
      public void addParameterTable(List<ConfigurationRequirement> paramList) {
        super.addParameterTable(paramList);
        finishTest();
      }
    };
    AlgorithmChooser jarChooser = new AlgorithmChooser(algorithms, tabWrapper);
    page.addNorth(jarChooser, 4);

    // Execute functionality
    jarChooser.algorithmListBox.setItemSelected(1, true);
    jarChooser.submit();

    // Set a delay period
    delayTestFinish(500);

    // Cleanup
    TestHelper.resetDatabaseSync();
  }

  /**
   * Ensure that all algorithms in the list given at construction are displayed
   */
  public void testConstructor() {
    //Setup
    LinkedList<Algorithm> algorithms = new LinkedList<>();
    algorithms.add(new Algorithm("Algorithm 1"));
    algorithms.add(new Algorithm("Algorithm 2"));

    //Execute
    AlgorithmChooser jarChooser = new AlgorithmChooser(algorithms, new TabWrapper());

    //Test
    assertEquals(3, jarChooser.getWidgetCount());
    assertEquals(algorithms.size() + 1, jarChooser.getListItemCount());
    assertEquals(AlgorithmChooser.AlgorithmCategory.values().length, jarChooser.categoryListBox.getItemCount());
  }

  /**
   * Test that the algorithms are listed in alphabetical order of their names
   */
  public void testOrdering() {
    // Setup
    LinkedList<Algorithm> algorithms = new LinkedList<>();
    Algorithm algo1 = new Algorithm("");
    algo1.setName("C");
    algorithms.add(algo1);
    Algorithm algo2 = new Algorithm("");
    algo2.setName("B");
    algorithms.add(algo2);
    Algorithm algo3 = new Algorithm("");
    algo3.setName("A");

    // Create dropdown
    AlgorithmChooser jarChooser = new AlgorithmChooser(algorithms, new TabWrapper());

    // Check
    assertTrue(jarChooser.algorithmListBox.getItemText(1).compareTo(jarChooser.algorithmListBox.getItemText(2)) < 0);

    // Add another algorithm
    jarChooser.addAlgorithm(algo3);
    jarChooser.updateAlgorithmListBox();

    // Check
    assertTrue(jarChooser.algorithmListBox.getItemText(1).compareTo(jarChooser.algorithmListBox.getItemText(2)) < 0);
    assertTrue(jarChooser.algorithmListBox.getItemText(2).compareTo(jarChooser.algorithmListBox.getItemText(3)) < 0);
  }

  /**
   * Test method for {@link de.metanome.frontend.client.runs.AlgorithmChooser#removeAlgorithm(String)}
   */
  public void testRemoveAlgorithm() {
    LinkedList<Algorithm> algorithms = new LinkedList<>();
    algorithms.add(new Algorithm("Algorithm 1"));
    algorithms.add(new Algorithm("Algorithm 2"));

    AlgorithmChooser jarChooser = new AlgorithmChooser(algorithms, new TabWrapper());

    assertEquals(3, jarChooser.algorithmListBox.getItemCount());

    // Execute
    jarChooser.removeAlgorithm("Algorithm 2");

    assertEquals(2, jarChooser.algorithmListBox.getItemCount());
    assertEquals("--", jarChooser.algorithmListBox.getItemText(0));
    assertEquals("Algorithm 1", jarChooser.algorithmListBox.getItemText(1));
  }

  /**
   * Test method for {@link de.metanome.frontend.client.runs.AlgorithmChooser#filterForPrimaryDataSource(de.metanome.algorithm_integration.configuration.ConfigurationSettingDataSource)}
   */
  public void testFilterForPrimaryDataSourceFileInput() {
    // Setup
    LinkedList<Algorithm> algorithms = new LinkedList<>();

    Algorithm algo1 = new Algorithm("file1.jar");
    algo1.setName("algo1");
    algo1.setInd(true);
    algo1.setFileInput(true);
    algorithms.add(algo1);

    Algorithm algo2 = new Algorithm("file2.jar");
    algo2.setName("algo2");
    algo2.setFd(true);
    algo2.setTableInput(true);
    algorithms.add(algo2);

    Algorithm algo3 = new Algorithm("file3.jar");
    algo3.setName("algo3");
    algo3.setUcc(true);
    algo3.setDatabaseConnection(true);
    algorithms.add(algo3);

    Algorithm algo4 = new Algorithm("file4.jar");
    algo4.setName("algo4");
    algo4.setUcc(true);
    algo4.setRelationalInput(true);
    algorithms.add(algo4);

    AlgorithmChooser jarChooser = new AlgorithmChooser(algorithms, new TabWrapper());

    assertEquals(5, jarChooser.algorithmListBox.getItemCount());

    // Execute
    jarChooser.filterForPrimaryDataSource(new ConfigurationSettingFileInput());

    assertEquals(3, jarChooser.algorithmListBox.getItemCount());
    assertEquals("--", jarChooser.algorithmListBox.getItemText(0));
    assertEquals("algo1", jarChooser.algorithmListBox.getItemText(1));
    assertEquals("algo4", jarChooser.algorithmListBox.getItemText(2));
  }

  /**
   * Test method for {@link de.metanome.frontend.client.runs.AlgorithmChooser#filterForPrimaryDataSource(de.metanome.algorithm_integration.configuration.ConfigurationSettingDataSource)}
   */
  public void testFilterForPrimaryDataSourceDatabaseConnection() {
    // Setup
    LinkedList<Algorithm> algorithms = new LinkedList<>();

    Algorithm algo1 = new Algorithm("file1.jar");
    algo1.setName("algo1");
    algo1.setInd(true);
    algo1.setFileInput(true);
    algorithms.add(algo1);

    Algorithm algo2 = new Algorithm("file2.jar");
    algo2.setName("algo2");
    algo2.setFd(true);
    algo2.setTableInput(true);
    algorithms.add(algo2);

    Algorithm algo3 = new Algorithm("file3.jar");
    algo3.setName("algo3");
    algo3.setUcc(true);
    algo3.setFileInput(true);
    algo3.setDatabaseConnection(true);
    algorithms.add(algo3);

    Algorithm algo4 = new Algorithm("file4.jar");
    algo4.setName("algo4");
    algo4.setUcc(true);
    algo4.setRelationalInput(true);
    algorithms.add(algo4);

    AlgorithmChooser jarChooser = new AlgorithmChooser(algorithms, new TabWrapper());

    assertEquals(5, jarChooser.algorithmListBox.getItemCount());

    // Execute
    jarChooser.filterForPrimaryDataSource(new ConfigurationSettingDatabaseConnection("url", "user", "pwd",
                                                                                     DbSystem.DB2));

    assertEquals(2, jarChooser.algorithmListBox.getItemCount());
    assertEquals("--", jarChooser.algorithmListBox.getItemText(0));
    assertEquals("algo3", jarChooser.algorithmListBox.getItemText(1));
    assertEquals(0, jarChooser.categoryListBox.getSelectedIndex());
  }

  /**
   * Test method for {@link AlgorithmChooser#resetListBoxes()}
   */
  public void testResetListBox() {
    LinkedList<Algorithm> algorithms = new LinkedList<>();
    algorithms.add(new Algorithm("Algorithm 1"));
    algorithms.add(new Algorithm("Algorithm 2"));

    AlgorithmChooser jarChooser = new AlgorithmChooser(algorithms, new TabWrapper());
    jarChooser.categoryListBox.setSelectedIndex(3);

    assertEquals(3, jarChooser.algorithmListBox.getItemCount());
    jarChooser.algorithmListBox.removeItem(1);
    assertEquals(2, jarChooser.algorithmListBox.getItemCount());

    // Execute
    jarChooser.resetListBoxes();

    assertEquals(3, jarChooser.algorithmListBox.getItemCount());
    assertEquals(0, jarChooser.categoryListBox.getSelectedIndex());
  }


  @Override
  public String getModuleName() {
    return "de.metanome.frontend.MetanomeTest";
  }
}
