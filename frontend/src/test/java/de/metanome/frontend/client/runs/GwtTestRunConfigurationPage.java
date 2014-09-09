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

import de.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.metanome.algorithm_integration.configuration.ConfigurationSpecificationCsvFile;
import de.metanome.algorithm_integration.configuration.ConfigurationSpecificationString;
import de.metanome.backend.results_db.Algorithm;
import de.metanome.frontend.client.BasePage;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.TestHelper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Tests for the algorithm specific pages (tabs)
 */
public class GwtTestRunConfigurationPage extends GWTTestCase {

  private BasePage page;

  /**
   * Test method for {@link de.metanome.frontend.client.runs.RunConfigurationPage#RunConfigurationPage(de.metanome.frontend.client.BasePage)}
   */
  public void testConstruction() {
    // Set up
    TestHelper.resetDatabaseSync();

    // Execute
    RunConfigurationPage runConfigPage = new RunConfigurationPage(page);

    // Check - should contain the jarChooser and a Label for pre-selected data source (possibly empty)
    assertEquals(2, runConfigPage.getWidgetCount());
    assertNotNull(runConfigPage.getJarChooser());

    // Cleanup
    TestHelper.resetDatabaseSync();
  }

  /**
   * Test method for {@link de.metanome.frontend.client.runs.RunConfigurationPage#addParameterTable(java.util.List)}
   */
  public void testAddParameterTable() {
    // Set up
    TestHelper.resetDatabaseSync();

    RunConfigurationPage runConfigPage = new RunConfigurationPage(page);
    List<ConfigurationSpecification> paramList = new ArrayList<>();
    int widgetCount = runConfigPage.getWidgetCount();

    // Execute
    runConfigPage.addParameterTable(paramList);

    // Check
    assertEquals(widgetCount + 1, runConfigPage.getWidgetCount());

    // Cleanup
    TestHelper.resetDatabaseSync();
  }

  /**
   * Test method for {@link de.metanome.frontend.client.runs.RunConfigurationPage#addAlgorithms(java.util.List)}
   */
  public void testAddAlgorithms() {
    // Set up
    TestHelper.resetDatabaseSync();

    RunConfigurationPage runConfigPage = new RunConfigurationPage(page);
    int noOfAlgorithms = runConfigPage.getJarChooser().getListItemCount();
    LinkedList<Algorithm> algorithms = new LinkedList<>();
    algorithms.add(new Algorithm("Algorithm 1"));
    algorithms.add(new Algorithm("Algorithm 2"));

    // Execute
    runConfigPage.addAlgorithms(algorithms);

    // Check
    assertEquals(noOfAlgorithms + algorithms.size(),
                 runConfigPage.getJarChooser().getListItemCount());

    // Cleanup
    TestHelper.resetDatabaseSync();
  }

  /**
   * Test method for {@link de.metanome.frontend.client.runs.RunConfigurationPage#removeAlgorithm(String)}
   */
  public void testRemoveAlgorithm() {
    // Set up
    TestHelper.resetDatabaseSync();

    RunConfigurationPage runConfigPage = new RunConfigurationPage(page);
    int noOfAlgorithms = runConfigPage.getJarChooser().getListItemCount();
    LinkedList<Algorithm> algorithms = new LinkedList<>();
    algorithms.add(new Algorithm("Algorithm 1"));
    algorithms.add(new Algorithm("Algorithm 2"));
    runConfigPage.addAlgorithms(algorithms);

    // Execute
    runConfigPage.removeAlgorithm("Algorithm 1");

    // Check
    assertEquals(noOfAlgorithms + algorithms.size() - 1,
                 runConfigPage.getJarChooser().getListItemCount());

    // Cleanup
    TestHelper.resetDatabaseSync();
  }

  /**
   * Test method for {@link de.metanome.frontend.client.runs.RunConfigurationPage#selectAlgorithm(String)}
   */
  public void testSelectAlgorithm() {
    // Set up
    TestHelper.resetDatabaseSync();

    RunConfigurationPage runConfigPage = new RunConfigurationPage(page);
    runConfigPage.setMessageReceiver(new TabWrapper());
    String algoName = setUpJarChooser(runConfigPage);

    // Execute
    runConfigPage.selectAlgorithm(algoName);

    // Check
    assertEquals(algoName, runConfigPage.getCurrentlySelectedAlgorithm());

    // Cleanup
    TestHelper.resetDatabaseSync();
  }

  /**
   * Test method for {@link de.metanome.frontend.client.runs.AlgorithmChooser#forwardParameters(java.util.List)}
   */
  public void testForwardParameters() {
    // Set up
    TestHelper.resetDatabaseSync();

    RunConfigurationPage runConfigPage = new RunConfigurationPage(page);
    List<ConfigurationSpecification> paramList = new ArrayList<>();
    paramList.add(new ConfigurationSpecificationString("someString"));
    paramList.add(new ConfigurationSpecificationCsvFile("theDataSource"));

    // Execute
    runConfigPage.algorithmChooser.forwardParameters(paramList);

    // Check
    assertNotNull(runConfigPage.parameterTable);

    // Cleanup
    TestHelper.resetDatabaseSync();
  }

  protected String setUpJarChooser(RunConfigurationPage runConfigPage) {
    String algoName = "somethingRandom";
    LinkedList<Algorithm> algorithms = new LinkedList<>();
    Algorithm a1 = new Algorithm("file/name/1");
    a1.setName("Algorithm 1");
    algorithms.add(a1);
    Algorithm a2 = new Algorithm("file/name/2");
    a2.setName(algoName);
    algorithms.add(a2);
    Algorithm a3 = new Algorithm("file/name/3");
    a3.setName("A..3");
    algorithms.add(a3);

    runConfigPage.addAlgorithms(algorithms);
    assertEquals("--", runConfigPage.getCurrentlySelectedAlgorithm());
    return algoName;
  }

  @Override
  public String getModuleName() {
    return "de.uni_potsdam.hpi.metanome.frontend.MetanomeTest";
  }

}
