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

package de.metanome.frontend.client;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingFileInput;
import de.metanome.backend.results_db.Algorithm;
import de.metanome.frontend.client.BasePage.Tabs;
import de.metanome.frontend.client.algorithms.AlgorithmsPage;
import de.metanome.frontend.client.datasources.DataSourcePage;
import de.metanome.frontend.client.executions.ExecutionsPage;
import de.metanome.frontend.client.results.ResultsPage;
import de.metanome.frontend.client.runs.RunConfigurationPage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Tests related to the overall page.
 */
public class GwtTestBasePage extends GWTTestCase {

  LinkedList<Algorithm> algorithms = new LinkedList<>();

  /**
   * Test method for {@link de.metanome.frontend.client.BasePage#BasePage()}
   */
  public void testNewBasePage() {
    // Set up
    TestHelper.resetDatabaseSync();

    // Execute
    BasePage testPage = new BasePage();

    // Check
    assertEquals(6, testPage.getWidgetCount());

    Widget wrapper = testPage.getWidget(Tabs.RESULTS.ordinal());
    assertTrue(wrapper instanceof TabWrapper);
    assertTrue(((TabWrapper) wrapper).contentPanel instanceof ResultsPage);

    Widget panel = testPage.getWidget(Tabs.ALGORITHMS.ordinal());
    assertTrue(panel instanceof ScrollPanel);
    wrapper = ((ScrollPanel) panel).getWidget();
    assertTrue(wrapper instanceof TabWrapper);
    assertTrue(((TabWrapper) wrapper).contentPanel instanceof AlgorithmsPage);

    panel = testPage.getWidget(Tabs.EXECUTIONS.ordinal());
    assertTrue(panel instanceof ScrollPanel);
    wrapper = ((ScrollPanel) panel).getWidget();
    assertTrue(wrapper instanceof TabWrapper);
    assertTrue(((TabWrapper) wrapper).contentPanel instanceof ExecutionsPage);

    panel = testPage.getWidget(Tabs.DATA_SOURCES.ordinal());
    assertTrue(panel instanceof ScrollPanel);
    wrapper = ((ScrollPanel) panel).getWidget();
    assertTrue(wrapper instanceof TabWrapper);
    assertTrue(((TabWrapper) wrapper).contentPanel instanceof DataSourcePage);

    panel = testPage.getWidget(Tabs.RUN_CONFIGURATION.ordinal());
    assertTrue(panel instanceof ScrollPanel);
    wrapper = ((ScrollPanel) panel).getWidget();
    assertTrue(wrapper instanceof TabWrapper);
    assertTrue(((TabWrapper) wrapper).contentPanel instanceof RunConfigurationPage);

    // Cleanup
    TestHelper.resetDatabaseSync();
  }

  /**
   * Test method for {@link de.metanome.frontend.client.BasePage#addAlgorithmsToRunConfigurations(java.util.List)}
   */
  public void testAddAlgorithmsToRunConfigurations() {
    // Set up
    TestHelper.resetDatabaseSync();

    BasePage page = new BasePage();
    int itemCount = page.runConfigurationsPage.getJarChooser().getListItemCount();
    algorithms.add(new Algorithm("Algorithm 1"));
    algorithms.add(new Algorithm("Algorithm 2"));

    // Execute
    page.addAlgorithmsToRunConfigurations(algorithms);

    // Check
    assertEquals(itemCount + 2, page.runConfigurationsPage.getJarChooser().getListItemCount());

    // Cleanup
    TestHelper.resetDatabaseSync();
  }

  /**
   * Test method for {@link de.metanome.frontend.client.BasePage#switchToRunConfiguration(String,
   * de.metanome.algorithm_integration.configuration.ConfigurationSettingDataSource)} </p> Test
   * control flow from Algorithms to Run configuration
   */
  public void testSwitchToRunConfigurationFromAlgorithm() {
    // Set up
    TestHelper.resetDatabaseSync();

    final BasePage page = new BasePage();

    final String algorithmName = "some_name";
    Algorithm algorithm1 = new Algorithm("example_ind_algorithm.jar");
    algorithm1.setName(algorithmName);
    algorithm1.setFileInput(true);
    Algorithm algorithm2 = new Algorithm("some_other_file");
    algorithm2.setName("some_other_name");

    List<Algorithm> list = new ArrayList<>();
    list.add(algorithm1);
    list.add(algorithm2);

    page.addAlgorithmsToRunConfigurations(list);

    // Execute
    page.switchToRunConfiguration(algorithmName, null);

    // Check
    RunConfigurationPage runPage = getRunConfigurationPage(page);

    assertEquals(Tabs.RUN_CONFIGURATION.ordinal(), page.getSelectedIndex());
    assertEquals(algorithmName, runPage.getCurrentlySelectedAlgorithm());
    // assertNotNull(runPage.parameterTable);
    // assertEquals(4, runPage.getWidgetCount());

    // Cleanup
    TestHelper.resetDatabaseSync();
  }


  /**
   * Test method for {@link de.metanome.frontend.client.BasePage#switchToRunConfiguration(String,
   * de.metanome.algorithm_integration.configuration.ConfigurationSettingDataSource)} </p> Test
   * control flow from Data sources to Run configuration
   */
  public void testSwitchToRunConfigurationFromDataSource() throws AlgorithmConfigurationException {
    final BasePage page = new BasePage();
    ConfigurationSettingFileInput dataSource = new ConfigurationSettingFileInput();

    String dataSourceName = "inputA.csv";
    dataSource.setFileName(dataSourceName);
    final ConfigurationSettingFileInput finalDataSource = dataSource;

    Algorithm algorithm = new Algorithm("some_file");
    algorithm.setName("algorithm");

    List<Algorithm> list = new ArrayList<>();
    list.add(algorithm);

    page.addAlgorithmsToRunConfigurations(list);

    // Execute
    page.switchToRunConfiguration(null, finalDataSource);

    // Check
    RunConfigurationPage runConfigPage = getRunConfigurationPage(page);

    assertEquals(Tabs.RUN_CONFIGURATION.ordinal(), page.getSelectedIndex());
    assertEquals(finalDataSource.getValueAsString(),
                 runConfigPage.primaryDataSource.getValueAsString());
  }

  private RunConfigurationPage getRunConfigurationPage(final BasePage page) {
    return (RunConfigurationPage) ((TabWrapper) ((ScrollPanel) page
        .getWidget(page.getSelectedIndex())).getWidget()).contentPanel;
  }

  @Override
  public String getModuleName() {
    return "de.metanome.frontend.MetanomeTest";
  }

}
